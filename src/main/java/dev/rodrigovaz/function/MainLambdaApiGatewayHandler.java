package dev.rodrigovaz.function;

import com.amazonaws.lambda.thirdparty.com.fasterxml.jackson.core.JsonProcessingException;
import com.amazonaws.lambda.thirdparty.com.fasterxml.jackson.databind.ObjectMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.rodrigovaz.configuration.GuiceBeanConfiguration;
import dev.rodrigovaz.core.usercase.IGetAddressUseCase;
import dev.rodrigovaz.core.usercase.ILoggerUseCase;
import dev.rodrigovaz.domain.AddressRequest;
import dev.rodrigovaz.domain.AddressResponse;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainLambdaApiGatewayHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private final Logger logger = LoggerFactory.getLogger(MainLambdaApiGatewayHandler.class);
    private final ILoggerUseCase loggerUseCase;
    private final IGetAddressUseCase getAddressUseCase;
    private final ObjectMapper objectMapper;

    public MainLambdaApiGatewayHandler() {
        Injector injector = Guice.createInjector(new GuiceBeanConfiguration());
        this.loggerUseCase = injector.getInstance(ILoggerUseCase.class);
        this.getAddressUseCase = injector.getInstance(IGetAddressUseCase.class);
        this.objectMapper = injector.getInstance(ObjectMapper.class);
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context) {
        logger.info("start lambda");
        this.loggerUseCase.requestLogger(apiGatewayProxyRequestEvent);
        APIGatewayProxyResponseEvent apiGatewayProxyResponseEvent = new APIGatewayProxyResponseEvent();
        this.loggerUseCase.responseLogger(apiGatewayProxyResponseEvent);
        final String requestBody = apiGatewayProxyRequestEvent.getBody();
        final AddressRequest addressRequest = getAddressRequest(requestBody);
        AddressResponse addressResponse = this.getAddressUseCase.byCep(addressRequest.cep());

        ThreadContext.put("addressResponse", addressResponse.toString());

        logger.info("completed lambda");
        return apiGatewayProxyResponseEvent;
    }

    private AddressRequest getAddressRequest(String requestBody) {
        try {
            return objectMapper.readValue(requestBody, AddressRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
