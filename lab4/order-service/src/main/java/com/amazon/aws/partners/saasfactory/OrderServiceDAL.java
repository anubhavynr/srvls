package com.amazon.aws.partners.saasfactory;

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
        long startTimeMillis = System.currentTimeMillis();
        LoggingManager.log(event, "OrderServiceDAL::getOrder " + orderId);

        Map<String, AttributeValue> order = null;
        try {
            Map<String, AttributeValue> key = new HashMap<>();
            key.put("id", AttributeValue.builder().s(orderId).build());
            GetItemResponse response = ddb.getItem(request -> request.tableName(getTableName(event)).key(key));
            order = response.item();
        } catch (DynamoDbException e) {
            LoggingManager.log(event, "OrderServiceDAL::getOrder " + getFullStackTrace(e));
            throw new RuntimeException(e);
        }

        long totalTimeMillis = System.currentTimeMillis() - startTimeMillis;
        MetricsManager.recordMetric(event, "OrderServiceDAL", "getOrder", totalTimeMillis);

        return DynamoDbHelper.orderFromAttributeValueMap(order);
    }

    public Order insertOrder(Map<String, Object> event, Order order) {
        long startTimeMillis = System.currentTimeMillis();
        UUID orderId = UUID.randomUUID();
        LoggingManager.log(event, "OrderServiceDAL::insertOrder " + orderId);

        order.setId(orderId);
        try{
            Map<String, AttributeValue> item = DynamoDbHelper.toAttributeValueMap(order);
            PutItemResponse response = ddb.putItem(request -> request.tableName(getTableName(event)).item(item));
        } catch (DynamoDbException e) {
            LoggingManager.log(event, "OrderServiceDAL::insertOrder " + getFullStackTrace(e));
            throw new RuntimeException(e);
        }

        long totalTimeMillis = System.currentTimeMillis() - startTimeMillis;
        MetricsManager.recordMetric(event, "OrderServiceDAL", "insertOrder", totalTimeMillis);

        return order;
    }

    private static String getTableName(Map<String, Object> event) {
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