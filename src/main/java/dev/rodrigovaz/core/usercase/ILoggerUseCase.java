package dev.rodrigovaz.core.usercase;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public interface ILoggerUseCase {
    void requestLogger(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent);
    void responseLogger(APIGatewayProxyResponseEvent apiGatewayProxyResponseEvent);
}