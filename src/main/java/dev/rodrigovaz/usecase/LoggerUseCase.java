package dev.rodrigovaz.usecase;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import dev.rodrigovaz.core.helper.ConstantApplication;
import dev.rodrigovaz.core.helper.Convert;
import dev.rodrigovaz.core.usercase.ILoggerUseCase;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LoggerUseCase implements ILoggerUseCase {
    private final Logger logger = LoggerFactory.getLogger(LoggerUseCase.class);

    @Override
    public void requestLogger(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent) {
        this.outputLogger(ConstantApplication.FIELD_API_GATEWAY_PROXY_REQUEST,
                apiGatewayProxyRequestEvent,
                ConstantApplication.MESSAGE_REQUEST_LOGGER);
    }

    @Override
    public void responseLogger(
            APIGatewayProxyResponseEvent apiGatewayProxyResponseEvent) {
        this.outputLogger(ConstantApplication.FIELD_API_GATEWAY_PROXY_RESPONSE,
                apiGatewayProxyResponseEvent,
                ConstantApplication.MESSAGE_RESPONSE_LOGGER);
    }

    private String deserialize(Object object) {
        return Convert.toJson(object);
    }

    private void outputLogger(String fieldContext,
                              Object object,
                              String message) {
        final String json = this.deserialize(object);
        ThreadContext.put(fieldContext, json);
        logger.info(message);
        ThreadContext.remove(fieldContext);
    }
}
