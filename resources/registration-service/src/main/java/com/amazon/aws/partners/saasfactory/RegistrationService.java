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
import software.amazon.awssdk.core.exception.SdkServiceException;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.services.cloudformation.CloudFormationAsyncClient;
import software.amazon.awssdk.services.cloudformation.model.CreateStackResponse;
import software.amazon.awssdk.services.cloudformation.model.Parameter;
import software.amazon.awssdk.services.elasticloadbalancingv2.ElasticLoadBalancingV2Client;
import software.amazon.awssdk.services.elasticloadbalancingv2.model.DescribeRulesResponse;
import software.amazon.awssdk.services.ssm.SsmAsyncClient;
import software.amazon.awssdk.services.ssm.model.GetParametersResponse;
import software.amazon.awssdk.services.ssm.model.ParameterType;
import software.amazon.awssdk.services.ssm.model.PutParameterResponse;
import software.amazon.awssdk.utils.IoUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RegistrationService implements RequestHandler<Map<String, Object>, APIGatewayProxyResponseEvent> {

    private final static Logger LOGGER = LoggerFactory.getLogger(RegistrationService.class);
    private final static ObjectMapper MAPPER = new ObjectMapper();
    private final static String ONBOARDING_TEMPLATE = "onboard-tenant.template";
    private SsmAsyncClient ssm;
    private CloudFormationAsyncClient cfn;
    private ElasticLoadBalancingV2Client elbv2;
    private String apiGatewayEndpoint;
    private String workshopBucket;
    private String keyPairName;
    private String vpcId;
    private String applicationServerSecurityGroup;
    private String privateSubnetIds;
    private String codePipelineBucket;
    private String codeDeployApplication;
    private String deploymentGroup;
    private String updateCodeDeployLambdaArn;
    private String albListenerArn;

    public RegistrationService() {

        this.ssm = SsmAsyncClient.builder()
                .httpClientBuilder(NettyNioAsyncHttpClient.builder())
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();
        this.elbv2 = ElasticLoadBalancingV2Client.builder()
                .httpClientBuilder(UrlConnectionHttpClient.builder())
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();

        CompletableFuture<GetParametersResponse> asyncResponse = this.ssm.getParameters(request -> request
                .names("API_GW", "WORKSHOP_BUCKET", "KEY_PAIR", "VPC", "APP_SG","PRIVATE_SUBNETS",
                        "PIPELINE_BUCKET", "CODE_DEPLOY", "DEPLOYMENT_GROUP", "CODE_DEPLOY_LAMBDA")
        );
        asyncResponse.whenComplete((parametersResponse, exception) -> {
            if (parametersResponse != null) {
                for (software.amazon.awssdk.services.ssm.model.Parameter parameter : parametersResponse.parameters()) {
                    switch (parameter.name()) {
                        case "API_GW":
                            this.apiGatewayEndpoint = parameter.value();
                            LOGGER.info("Setting env api gateway = " + this.apiGatewayEndpoint);
                            break;
                        case "WORKSHOP_BUCKET":
                            this.workshopBucket = parameter.value();
                            LOGGER.info("Setting env workshop bucket = " + this.workshopBucket);
                            break;
                        case "KEY_PAIR":
                            this.keyPairName = parameter.value();
                            LOGGER.info("Setting env key pair name = " + this.keyPairName);
                            break;
                        case "VPC":
                            this.vpcId = parameter.value();
                            LOGGER.info("Setting env vpc = " + this.vpcId);
                            break;
                        case "APP_SG":
                            this.applicationServerSecurityGroup = parameter.value();
                            LOGGER.info("Setting env app server security group = " + this.applicationServerSecurityGroup);
                            break;
                        case "PRIVATE_SUBNETS":
                            this.privateSubnetIds = parameter.value();
                            LOGGER.info("Setting env private subnets = " + this.privateSubnetIds);
                            break;
                        case "PIPELINE_BUCKET":
                            this.codePipelineBucket = parameter.value();
                            LOGGER.info("Setting env codepipeline bucket = " + this.codePipelineBucket);
                            break;
                        case "CODE_DEPLOY":
                            this.codeDeployApplication = parameter.value();
                            LOGGER.info("Setting env codedeploy application = " + this.codeDeployApplication);
                            break;
                        case "DEPLOYMENT_GROUP":
                            this.deploymentGroup = parameter.value();
                            LOGGER.info("Setting env codedeploy deployment group = " + this.deploymentGroup);
                            break;
                        case "CODE_DEPLOY_LAMBDA":
                            this.updateCodeDeployLambdaArn = parameter.value();
                            LOGGER.info("Setting env update codedeploy lambda = " + this.updateCodeDeployLambdaArn);
                            break;
                    }
                }
            } else {
                LOGGER.error(exception.getMessage());
                throw new RuntimeException(exception);
            }
        });

        // Can only query for a max of 10 parameters at a time...
        CompletableFuture<GetParametersResponse> asyncResponse2 = this.ssm.getParameters(request -> request
                .names("ALB_LISTENER")
        );
        asyncResponse2.whenComplete((parametersResponse, exception) -> {
            if (parametersResponse != null) {
                for (software.amazon.awssdk.services.ssm.model.Parameter parameter : parametersResponse.parameters()) {
                    switch (parameter.name()) {
                        case "ALB_LISTENER":
                            this.albListenerArn = parameter.value();
                            LOGGER.info("Setting env alb listener = " + this.albListenerArn);
                            break;
                    }
                }
            } else {
                LOGGER.error(exception.getMessage());
                throw new RuntimeException(exception);
            }
        });

        this.cfn = CloudFormationAsyncClient.builder()
                .httpClientBuilder(NettyNioAsyncHttpClient.builder())
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();

        asyncResponse.join();
        asyncResponse2.join();
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(Map<String, Object> event, Context context) {
        return register(event, context);
    }

    /**
     * 1. Call the TenantService to create a new tenant record
     * 2. Call the TenantService to get an unclaimed RDS cluster from the hot pool
     * 3. Update the RDS cluster to add the new application user and password
     * 4. Save the database connection properties to parameter store so the app servers
     *    for this tenant can configure themselves at runtime
     * 5. Trigger CloudFormation to run the onboarding stack for this tenant
     * @param event
     * @param context
     * @return
     */
    public APIGatewayProxyResponseEvent register(Map<String, Object> event, Context context) {
        long startTimeMillis = System.currentTimeMillis();
        //logRequestEvent(event);
        LOGGER.info("RegistrationService::register");
        APIGatewayProxyResponseEvent response = null;

        Registration registration = registrationFromJson((String) event.get("body"));
        Tenant tenant = null;
        if (registration != null) {
            try {
                // Not thread safe - do not copy this

                // First, get an available RDS cluster
                String availableDatabaseResponse = nextAvailableDatabase();
                Map<String, String> availableDatabase = MAPPER.readValue(availableDatabaseResponse, Map.class);
                if (availableDatabase == null || availableDatabase.isEmpty()) {
                    throw new RuntimeException("Cannot register new tenant. Hot pool of RDS clusters has been depleted.");
                }
                String tenantDatabase = availableDatabase.get("Endpoint");
                LOGGER.info("RegistrationService::register next available database = " + tenantDatabase);

                // Second, create the new tenant
                tenant = createTenant(registration.getCompany(), registration.getPlan(), tenantDatabase);
                LOGGER.info("RegistrationService::register created tenant " + tenant.getId().toString());

                // Third, save this tenant's environment variables to parameter store
                storeParameters(tenant);

                // Fourth, now provision this tenant's silo infrastructure
                createStack(tenant);

                response = new APIGatewayProxyResponseEvent()
                        .withStatusCode(200)
                        .withBody(tenant.getId().toString());
            } catch (Exception e) {
                response = new APIGatewayProxyResponseEvent()
                        .withStatusCode(400)
                        .withBody(e.getMessage());
            }
        } else {
            response = new APIGatewayProxyResponseEvent()
                    .withStatusCode(400);
        }

        long totalTimeMillis = System.currentTimeMillis() - startTimeMillis;
        LOGGER.info("RegistrationService::register exec " + totalTimeMillis);
        return response;
    }

    private Tenant createTenant(String companyName, String plan, String database) throws Exception {
        long startTimeMillis = System.currentTimeMillis();
        LOGGER.info("RegistrationService::createTenant " + companyName);
        Tenant tenant = new Tenant(null, Boolean.TRUE, companyName, plan, null, database);
        
        URI invokeURL = URI.create(apiGatewayEndpoint + "/tenants");
        HttpURLConnection apiGateway = (HttpURLConnection) invokeURL.toURL().openConnection();
        apiGateway.setDoOutput(true);
        apiGateway.setRequestMethod("POST");
        apiGateway.setRequestProperty("Accept", "application/json");
        apiGateway.setRequestProperty("Content-Type", "application/json");

        LOGGER.info("RegistrationService::createTenant Invoking API Gateway at " + invokeURL.toString());
        OutputStream body = apiGateway.getOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(body, StandardCharsets.UTF_8);
        writer.write(toJson(tenant));
        writer.flush();
        writer.close();
        body.close();

        if (apiGateway.getResponseCode() >= 400) {
            throw new Exception(IoUtils.toUtf8String(apiGateway.getErrorStream()));
        }
        Tenant result = tenantFromJson(IoUtils.toUtf8String(apiGateway.getInputStream()));

        long totalTimeMillis = System.currentTimeMillis() - startTimeMillis;
        LOGGER.info("RegistrationService::createTenant exec " + totalTimeMillis);
        return result;
    }

    private String nextAvailableDatabase() throws Exception {
        long startTimeMillis = System.currentTimeMillis();
        LOGGER.info("RegistrationService::nextAvailableDatabase");
        URI invokeURL = URI.create(apiGatewayEndpoint + "/tenants/pool/database");
        HttpURLConnection apiGateway = (HttpURLConnection) invokeURL.toURL().openConnection();
        apiGateway.setRequestMethod("GET");
        apiGateway.setRequestProperty("Accept", "application/json");
        apiGateway.setRequestProperty("Content-Type", "application/json");

        LOGGER.info("RegistrationService::createTenant Invoking API Gateway at " + invokeURL.toString());

        if (apiGateway.getResponseCode() >= 400) {
            throw new Exception(IoUtils.toUtf8String(apiGateway.getErrorStream()));
        }
        String result = IoUtils.toUtf8String(apiGateway.getInputStream());
        LOGGER.info("RegistrationService::nextAvailableDatabse TenantService call \n" + result);

        long totalTimeMillis = System.currentTimeMillis() - startTimeMillis;
        LOGGER.info("RegistrationService::nextAvailableDatabase exec " + totalTimeMillis);
        return result;
    }

    protected void storeParameters(Tenant tenant) {
        long startTimeMillis = System.currentTimeMillis();
        LOGGER.info("RegistrationService::storeParameters");
        Map<String, String> params = Stream.of(
                new AbstractMap.SimpleEntry<>("DB_NAME", "saas_factory_srvls_wrkshp"),
                new AbstractMap.SimpleEntry<>("DB_USER", "application"),
                new AbstractMap.SimpleEntry<>("DB_PASS", generatePassword()),
                new AbstractMap.SimpleEntry<>("DB_HOST", tenant.getDatabase())
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        List<CompletableFuture<PutParameterResponse>> threads = new ArrayList<>();
        params.forEach((key, value) -> {
            String param = tenant.getId().toString() + "_" + key;
            LOGGER.info("RegistrationService::storeParameters PutParameter " + param);
            CompletableFuture<PutParameterResponse> asyncResponse = ssm.putParameter(request -> request
                    .name(param)
                    .value(value)
                    .type(key.startsWith("DB_PASS") ? ParameterType.SECURE_STRING : ParameterType.STRING)
                    .overwrite(Boolean.TRUE)
            );
            threads.add(asyncResponse);
        });
        CompletableFuture.allOf(threads.toArray(new CompletableFuture[threads.size()])).join();
        long totalTimeMillis = System.currentTimeMillis() - startTimeMillis;
        LOGGER.info("RegistrationService::storeParameters exec " + totalTimeMillis);
    }

    protected void createStack(Tenant tenant) {
        long startTimeMillis = System.currentTimeMillis();

        LOGGER.info("RegistrationService::createStack getting routing rule priority");
        int priority = 0;
        try {
            DescribeRulesResponse elbResponse = elbv2.describeRules(request -> request
                    .listenerArn(albListenerArn)
            );
            priority = elbResponse.rules().size() + 1;
        } catch (SdkServiceException e) {
            LOGGER.error(getFullStackTrace(e));
            throw new RuntimeException(e);
        }
        LOGGER.info("RegistrationService::createStack routing rule priority = " + priority);

        final Integer tenantAlbRulePriority = priority;
        String stackName = "Tenant-" + tenant.getId().toString().substring(0, 8);
        LOGGER.info("RegistrationService::createStack " + stackName);
        CompletableFuture<CreateStackResponse> asyncResponse = cfn.createStack(request -> request
                .stackName(stackName)
                .onFailure("DO_NOTHING")
                .capabilitiesWithStrings("CAPABILITY_NAMED_IAM")
                .templateURL("https://" + workshopBucket + ".s3-" + System.getenv("AWS_REGION") + ".amazonaws.com/" + ONBOARDING_TEMPLATE)
                .parameters(
                        Parameter.builder().parameterKey("TenantId").parameterValue(tenant.getId().toString()).build(),
                        Parameter.builder().parameterKey("TenantRouteALBPriority").parameterValue(tenantAlbRulePriority.toString()).build(),
                        Parameter.builder().parameterKey("KeyPair").parameterValue(keyPairName).build(),
                        Parameter.builder().parameterKey("VPC").parameterValue(vpcId).build(),
                        Parameter.builder().parameterKey("PrivateSubnets").parameterValue(privateSubnetIds).build(),
                        Parameter.builder().parameterKey("AppServerSecurityGroup").parameterValue(applicationServerSecurityGroup).build(),
                        Parameter.builder().parameterKey("CodePipelineBucket").parameterValue(codePipelineBucket).build(),
                        Parameter.builder().parameterKey("CodeDeployApplication").parameterValue(codeDeployApplication).build(),
                        Parameter.builder().parameterKey("DeploymentGroup").parameterValue(deploymentGroup).build(),
                        Parameter.builder().parameterKey("LambdaUpdateDeploymentGroupArn").parameterValue(updateCodeDeployLambdaArn).build(),
                        Parameter.builder().parameterKey("ALBListener").parameterValue(albListenerArn).build()
                )
        );
        asyncResponse.whenComplete((response, exception) -> {
            if (response != null) {
                LOGGER.info("RegistrationService::createStack stack id " + response.stackId());
            } else {
                LOGGER.error(exception.getMessage());
                throw new RuntimeException(exception);
            }
        });
        asyncResponse.join();
        long totalTimeMillis = System.currentTimeMillis() - startTimeMillis;
        LOGGER.info("RegistrationService::createStack CloudFormation CreateStack returned in " + totalTimeMillis);
    }

    protected static String generatePassword () {
        final char[] chars = new char[]{'!', '#', '$', '%', '&', '*', '+', '-', '.', ':', '=', '?', '^', '_', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        final int passwordLength = 12;
        Random random = new Random();
        StringBuilder password = new StringBuilder(passwordLength);
        for (int i = 0; i < passwordLength; i++) {
            password.append(chars[random.nextInt(chars.length)]);
        }
        return password.toString();
    }

    protected static Registration registrationFromJson(String json) {
        Registration registration = null;
        try {
            registration = MAPPER.readValue(json, Registration.class);
        } catch (IOException e) {
            LOGGER.error(getFullStackTrace(e));
        }
        return registration;
    }

    protected static Tenant tenantFromJson(String json) {
        Tenant tenant = null;
        try {
            tenant = MAPPER.readValue(json, Tenant.class);
        } catch (IOException e) {
            LOGGER.error(getFullStackTrace(e));
        }
        return tenant;
    }

    protected static String toJson(Object obj) {
        String json = null;
        try {
            json = MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            LOGGER.error(getFullStackTrace(e));
        }
        return json;
    }

    protected static void logRequestEvent(Map<String, Object> event) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            LOGGER.info(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(event));
        } catch (JsonProcessingException e) {
            LOGGER.error("Could not log request event " + e.getMessage());
        }
    }

    protected static String getFullStackTrace(Exception e) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        e.printStackTrace(pw);
        return sw.getBuffer().toString();
    }
}