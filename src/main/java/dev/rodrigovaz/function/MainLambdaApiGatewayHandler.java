package dev.rodrigovaz.function;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.rodrigovaz.configuration.GuiceBeanConfiguration;
import dev.rodrigovaz.core.helper.Convert;
import dev.rodrigovaz.core.usercase.IGetAddressUseCase;
import dev.rodrigovaz.core.usercase.ILoggerUseCase;
import dev.rodrigovaz.domain.AddressRequest;
import dev.rodrigovaz.domain.AddressResponse;
import dev.rodrigovaz.domain.exception.MainException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.lambda.powertools.logging.CorrelationIdPathConstants;
import software.amazon.lambda.powertools.logging.Logging;

public class MainLambdaApiGatewayHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final Logger logger = LoggerFactory.getLogger(MainLambdaApiGatewayHandler.class);
    private final ILoggerUseCase loggerUseCase;
    private final IGetAddressUseCase getAddressUseCase;

    public MainLambdaApiGatewayHandler() {
        Injector injector = Guice.createInjector(new GuiceBeanConfiguration());
        this.loggerUseCase = injector.getInstance(ILoggerUseCase.class);
        this.getAddressUseCase = injector.getInstance(IGetAddressUseCase.class);
    }

    @Logging(correlationIdPath = CorrelationIdPathConstants.API_GATEWAY_REST, clearState = true)
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context) {
        logger.info("start lambda");
        final AddressResponse addressResponse = getAddressResponse(apiGatewayProxyRequestEvent);
        try {
            final APIGatewayProxyResponseEvent apiGatewayProxyResponseEvent = createResponse(addressResponse);
            logger.info("completed lambda");

            return apiGatewayProxyResponseEvent;
        } catch (MainException exception) {
            APIGatewayProxyResponseEvent apiGatewayProxyResponseEvent = new APIGatewayProxyResponseEvent();
            apiGatewayProxyResponseEvent.setStatusCode(500);
            apiGatewayProxyResponseEvent.setBody(Convert.toJson(exception));

            return apiGatewayProxyResponseEvent;
        }

    }

    private APIGatewayProxyResponseEvent createResponse(AddressResponse addressResponse) {
        return getResponseError(addressResponse);
    }

    private AddressResponse getAddressResponse(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent) {
        this.loggerUseCase.requestLogger(apiGatewayProxyRequestEvent);
        final String requestBody = apiGatewayProxyRequestEvent.getBody();
        final AddressRequest addressRequest = getAddressRequest(requestBody);

        return this.getAddressUseCase.byCep(addressRequest.cep());
    }

    private String convertAddressResponseToJson(AddressResponse addressResponse) {
        return Convert.toJson(addressResponse);
    }

    private AddressRequest getAddressRequest(String requestBody) {
        return Convert.toObject(requestBody, AddressRequest.class);
    }

    private APIGatewayProxyResponseEvent getResponseError(
            AddressResponse addressResponse) {
        APIGatewayProxyResponseEvent apiGatewayProxyResponseEvent = new APIGatewayProxyResponseEvent();
        final String responseBody = convertAddressResponseToJson(addressResponse);
        apiGatewayProxyResponseEvent.setBody(responseBody);
        this.loggerUseCase.responseLogger(apiGatewayProxyResponseEvent);

        return apiGatewayProxyResponseEvent;
    }
}
