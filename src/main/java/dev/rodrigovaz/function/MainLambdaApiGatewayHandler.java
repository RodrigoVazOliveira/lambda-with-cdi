package dev.rodrigovaz.function;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.rodrigovaz.configuration.GuiceBeanConfiguration;
import dev.rodrigovaz.usecase.ILoggerUseCase;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainLambdaApiGatewayHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private final Logger logger = LoggerFactory.getLogger(MainLambdaApiGatewayHandler.class);
    private final ILoggerUseCase loggerUseCase;

    public MainLambdaApiGatewayHandler() {
        Injector injector = Guice.createInjector(new GuiceBeanConfiguration());
        this.loggerUseCase = injector.getInstance(ILoggerUseCase.class);
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context) {
        logger.info("start lambda");
        this.loggerUseCase.requestLogger(apiGatewayProxyRequestEvent);
        APIGatewayProxyResponseEvent apiGatewayProxyResponseEvent = new APIGatewayProxyResponseEvent();
        this.loggerUseCase.responseLogger(apiGatewayProxyResponseEvent);
        ThreadContext.put("x", apiGatewayProxyRequestEvent.toString());
        logger.info("completed lambda");
        return apiGatewayProxyResponseEvent;
    }
}
