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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

public class ProductService implements RequestHandler<Map<String, Object>, APIGatewayProxyResponseEvent> {

    private final static Logger LOGGER = LoggerFactory.getLogger(ProductService.class);
    private final static ProductServiceDAL DAL = new ProductServiceDAL();
    private final static ObjectMapper MAPPER = new ObjectMapper();

    public APIGatewayProxyResponseEvent handleRequest(Map<String, Object> event, Context context) {
        logRequestEvent(event);
        return getProducts(event, context);
    }

    public APIGatewayProxyResponseEvent getProducts(Map<String, Object> event, Context context) {
        logRequestEvent(event);
        LoggingManager.log(event, "ProductService::getProducts");
        List<Product> products = DAL.getProducts(event);
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withBody(toJson(products));
        return response;
    }

    public APIGatewayProxyResponseEvent getProduct(Map<String, Object> event, Context context) {
        logRequestEvent(event);
        LoggingManager.log(event, "ProductService::getProduct");
        Map<String, String> params = (Map) event.get("pathParameters");
        Integer productId = Integer.valueOf(params.get("id"));

        Product product = DAL.getProduct(event, productId);

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withBody(toJson(product));
        return response;
    }

    public APIGatewayProxyResponseEvent updateProduct(Map<String, Object> event, Context context) {
        logRequestEvent(event);
        LoggingManager.log(event, "ProductService::updateProduct");
        Map<String, String> params = (Map) event.get("pathParameters");
        Integer productId = Integer.valueOf(params.get("id"));
        Product product = fromJson((String) event.get("body"));
        APIGatewayProxyResponseEvent response = null;
        if (product == null) {
            response = new APIGatewayProxyResponseEvent()
                    .withStatusCode(400);
        } else if (product.getId() != productId) {
            LoggingManager.log(event, "ProductService::updateProduct product.id does not match resource path id");
            response = new APIGatewayProxyResponseEvent()
                    .withStatusCode(400)
                    .withBody("product.id does not match resource path id");
        } else {
            product = DAL.updateProduct(event, product);
            response = new APIGatewayProxyResponseEvent()
                    .withStatusCode(200)
                    .withBody(toJson(product));
        }
        return response;
    }

    public APIGatewayProxyResponseEvent insertProduct(Map<String, Object> event, Context context) {
        logRequestEvent(event);
        LoggingManager.log(event, "ProductService::insertProduct");
        APIGatewayProxyResponseEvent response = null;
        Product product = fromJson((String) event.get("body"));
        if (product == null) {
            response = new APIGatewayProxyResponseEvent()
                    .withStatusCode(400);
        } else {
            product = DAL.insertProduct(event, product);
            response = new APIGatewayProxyResponseEvent()
                    .withStatusCode(200)
                    .withBody(toJson(product));
        }
        return response;
    }

    public APIGatewayProxyResponseEvent deleteProduct(Map<String, Object> event, Context context) {
        logRequestEvent(event);
        LoggingManager.log(event, "ProductService::deleteProduct");
        Map<String, String> params = (Map) event.get("pathParameters");
        Integer productId = Integer.valueOf(params.get("id"));
        APIGatewayProxyResponseEvent response = null;
        Product product = fromJson((String) event.get("body"));
        if (product == null) {
            response = new APIGatewayProxyResponseEvent()
                    .withStatusCode(400);
        } else if (product.getId() != productId) {
            LoggingManager.log(event, "ProductService::deleteProduct product.id does not match resource path id");
            response = new APIGatewayProxyResponseEvent()
                    .withStatusCode(400)
                    .withBody("product.id does not match resource path id");
        } else {
            DAL.deleteProduct(event, product);
            response = new APIGatewayProxyResponseEvent()
                    .withStatusCode(200);
        }
        return response;
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

    private static Product fromJson(String json) {
        Product product = null;
        try {
            product = MAPPER.readValue(json, Product.class);
        } catch (IOException e) {
            LOGGER.error(getFullStackTrace(e));
        }
        return product;
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