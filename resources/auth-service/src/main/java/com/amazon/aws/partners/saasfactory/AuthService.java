/**
 * Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.amazon.aws.partners.saasfactory;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AuthService implements RequestHandler<Map<String, Object>, APIGatewayProxyResponseEvent> {

    private final static Logger LOGGER = LoggerFactory.getLogger(AuthService.class);
    private final static ObjectMapper MAPPER = new ObjectMapper();

    private CognitoIdentityProviderClient cognito;

    public AuthService() {
        this.cognito = CognitoIdentityProviderClient.builder()
                .httpClientBuilder(UrlConnectionHttpClient.builder())
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(Map<String, Object> event, Context context) {
        APIGatewayProxyResponseEvent response = null;
        try {
            Map<String, String> signin = MAPPER.readValue((String) event.get("body"), Map.class);
            String username = signin.get("username");
            String password = signin.get("password");

            String userPoolId = findUserPool(username);
            String appClientId = appClient(userPoolId);
            AdminInitiateAuthResponse authResponse = cognito.adminInitiateAuth(request -> request
                    .userPoolId(userPoolId)
                    .clientId(appClientId)
                    .authFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)
                    .authParameters(Stream.of(
                            new AbstractMap.SimpleEntry<String, String>("USERNAME", username),
                            new AbstractMap.SimpleEntry<String, String>("PASSWORD", password)
                            ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                    )
            );
            String challenge = authResponse.challengeNameAsString();
            if (challenge != null && !challenge.isEmpty()) {
                response = new APIGatewayProxyResponseEvent()
                        .withBody(toJson(challenge))
                        .withStatusCode(401);
            } else {
                AuthenticationResultType auth = authResponse.authenticationResult();
                CognitoAuthResult result = CognitoAuthResult.builder()
                        .accessToken(auth.accessToken())
                        .idToken(auth.idToken())
                        .expiresIn(auth.expiresIn())
                        .refreshToken(auth.refreshToken())
                        .tokenType(auth.tokenType())
                        .build();
                response = new APIGatewayProxyResponseEvent()
                        .withBody(toJson(result))
                        .withStatusCode(200);
            }
        } catch (IOException ioe) {
            LOGGER.error(getFullStackTrace(ioe));
            response = new APIGatewayProxyResponseEvent()
                    .withBody(toJson(ioe.getMessage()))
                    .withStatusCode(400);
        }
        return response;
    }

    protected String findUserPool(String username) {
        String userPoolId = null;
        ListUserPoolsResponse userPoolsResponse = cognito.listUserPools(request -> request.maxResults(60));
        List<UserPoolDescriptionType> userPools = userPoolsResponse.userPools();
        if (userPools != null) {
            for (UserPoolDescriptionType userPool : userPools) {
                ListUsersResponse usersResponse = cognito.listUsers(request -> request
                        .userPoolId(userPool.id())
                );
                List<UserType> users = usersResponse.users();
                if (users != null) {
                    for (UserType user : users) {
                        Map<String, String> attributes = user.attributes()
                                .stream()
                                .map(a -> new AbstractMap.SimpleEntry<String, String>(a.name(), a.value()))
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                        CognitoUser cognitoUser = CognitoUser.builder()
                                .username(user.username())
                                .status(user.userStatusAsString())
                                .attributes(attributes)
                                .build();
                        try {
                            LOGGER.info(MAPPER.writeValueAsString(cognitoUser));
                        } catch (Exception e) {
                        }
                        if (username.equals(user.username())) {
                            userPoolId = userPool.id();
                            break;
                        }
                    }
                }
            }
        }
        return userPoolId;
    }

    protected String appClient(String userPoolId) {
        String appClientId = null;
        ListUserPoolClientsResponse appClientsResponse = cognito.listUserPoolClients(request -> request.userPoolId(userPoolId));
        List<UserPoolClientDescription> appClients = appClientsResponse.userPoolClients();
        if (appClients != null && !appClients.isEmpty()) {
            appClientId = appClients.get(0).clientId();
        }
        return appClientId;
    }

    private static String toJson(Object obj) {
        String json = null;
        try {
            json = MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            LOGGER.error(getFullStackTrace(e));
        }
        return json;
    }

    private static void logRequestEvent(Map<String, Object> event) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            LOGGER.info(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(event));
        } catch (JsonProcessingException e) {
            LOGGER.error("Could not log request event " + e.getMessage());
        }
    }

    private static String getFullStackTrace(Exception e) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        e.printStackTrace(pw);
        return sw.getBuffer().toString();
    }
}