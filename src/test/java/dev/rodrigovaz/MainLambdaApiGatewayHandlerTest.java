package dev.rodrigovaz;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.tests.annotations.Event;
import dev.rodrigovaz.function.MainLambdaApiGatewayHandler;
import org.junit.jupiter.params.ParameterizedTest;

class MainLambdaApiGatewayHandlerTest {

    @ParameterizedTest
    @Event(value = "apigw_event.json", type = APIGatewayProxyRequestEvent.class)
    void test(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent) {
        MainLambdaApiGatewayHandler mainLambdaApiGatewayHandler = new MainLambdaApiGatewayHandler();
        mainLambdaApiGatewayHandler.handleRequest(apiGatewayProxyRequestEvent, null);
    }
}