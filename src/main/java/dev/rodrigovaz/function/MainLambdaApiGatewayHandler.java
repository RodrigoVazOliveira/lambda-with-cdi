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
        final AddressResponse addressResponse = getAddressResponse(apiGatewayProxyRequestEvent);
        final APIGatewayProxyResponseEvent apiGatewayProxyResponseEvent = createResponse(addressResponse);
        logger.info("completed lambda");

        return apiGatewayProxyResponseEvent;
    }

    private APIGatewayProxyResponseEvent createResponse(AddressResponse addressResponse) {
        APIGatewayProxyResponseEvent apiGatewayProxyResponseEvent = new APIGatewayProxyResponseEvent();
        final String responseBody = convertAddressResponseToJson(addressResponse);
        apiGatewayProxyResponseEvent.setBody(responseBody);
        this.loggerUseCase.responseLogger(apiGatewayProxyResponseEvent);

        return apiGatewayProxyResponseEvent;
    }

    private AddressResponse getAddressResponse(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent) {
        this.loggerUseCase.requestLogger(apiGatewayProxyRequestEvent);
        final String requestBody = apiGatewayProxyRequestEvent.getBody();
        final AddressRequest addressRequest = getAddressRequest(requestBody);

        return this.getAddressUseCase.byCep(addressRequest.cep());
    }

    private String convertAddressResponseToJson(AddressResponse addressResponse) {
        try {
            return this.objectMapper.writeValueAsString(addressResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private AddressRequest getAddressRequest(String requestBody) {
        try {
            return objectMapper.readValue(requestBody, AddressRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
