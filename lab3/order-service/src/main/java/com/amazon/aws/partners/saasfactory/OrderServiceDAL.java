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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OrderServiceDAL {

    private final static Logger LOGGER = LoggerFactory.getLogger(OrderServiceDAL.class);
    private DynamoDbClient ddb;

    public OrderServiceDAL() {
        this.ddb = DynamoDbClient.builder()
                .httpClientBuilder(UrlConnectionHttpClient.builder())
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();
    }

    public Order getOrder(Map<String, Object> event, UUID orderId) {
        return getOrder(event, orderId.toString());
    }

    public Order getOrder(Map<String, Object> event, String orderId) {
        LOGGER.info("OrderServiceDAL::getOrder " + orderId);

        Map<String, AttributeValue> order = null;
        try {
            Map<String, AttributeValue> key = new HashMap<>();
            key.put("id", AttributeValue.builder().s(orderId).build());
            GetItemResponse response = ddb.getItem(request -> request.tableName(tableName(event)).key(key));
            order = response.item();
        } catch (DynamoDbException e) {
            LOGGER.error("OrderServiceDAL::getOrder " + getFullStackTrace(e));
            throw new RuntimeException(e);
        }

        return DynamoDbHelper.orderFromAttributeValueMap(order);
    }

    public Order insertOrder(Map<String, Object> event, Order order) {
        UUID orderId = UUID.randomUUID();
        LOGGER.info("OrderServiceDAL::insertOrder " + orderId);

        order.setId(orderId);
        try{
            Map<String, AttributeValue> item = DynamoDbHelper.toAttributeValueMap(order);
            PutItemResponse response = ddb.putItem(request -> request.tableName(tableName(event)).item(item));
        } catch (DynamoDbException e) {
            LOGGER.error("OrderServiceDAL::insertOrder " + getFullStackTrace(e));
            throw new RuntimeException(e);
        }

        return order;
    }

    private static String tableName(Map<String, Object> event) {
        String tenantId = TokenManager.getTenantId(event);
        String tableName = "order_fulfillment_" + tenantId;
        return tableName;
    }

    private static String getFullStackTrace(Exception e) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        e.printStackTrace(pw);
        return sw.getBuffer().toString();
    }
}