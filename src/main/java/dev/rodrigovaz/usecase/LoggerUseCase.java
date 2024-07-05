package dev.rodrigovaz.usecase;

import com.amazonaws.lambda.thirdparty.com.fasterxml.jackson.core.JsonProcessingException;
import com.amazonaws.lambda.thirdparty.com.fasterxml.jackson.databind.ObjectMapper;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.inject.Inject;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUseCase implements ILoggerUseCase {
    private final Logger logger = LoggerFactory.getLogger(LoggerUseCase.class);
    private static final String FIELD_API_GATEWAY_PROXY_REQUEST = "apiGatewayProxyRequestEvent";
    private static final String FIELD_API_GATEWAY_PROXY_RESPONSE = "apiGatewayProxyResponseEvent";
    private static final String MESSAGE_REQUEST_LOGGER = "request received";
    private static final String MESSAGE_RESPONSE_LOGGER = "response to be sent";
    private final ObjectMapper objectMapper;

    @Inject
    public LoggerUseCase(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void requestLogger(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent) {
        this.outputLogger(FIELD_API_GATEWAY_PROXY_REQUEST, apiGatewayProxyRequestEvent, MESSAGE_REQUEST_LOGGER);
    }

    @Override
    public void responseLogger(APIGatewayProxyResponseEvent apiGatewayProxyResponseEvent) {
        this.outputLogger(FIELD_API_GATEWAY_PROXY_RESPONSE, apiGatewayProxyResponseEvent, MESSAGE_RESPONSE_LOGGER);
    }

    private String deserialize(Object object) {
        try {
            return this.objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.error("Error deserialized object");

            throw new RuntimeException(e);
        }
    }

    private void outputLogger(String fieldContext, Object object, String message) {
        final String json = this.deserialize(object);
        ThreadContext.put(fieldContext, json);
        logger.info(message);
        ThreadContext.remove(fieldContext);
    }
}
