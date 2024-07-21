package dev.rodrigovaz.core.helper;

public final class ConstantApplication {
    private ConstantApplication() {
    }

    public static final String FIELD_RESPONSE = "responseBody";
    public static final String FIELD_CEP = "cep";
    public static final String FIELD_STATUS_CODE = "statusCode";
    public static final String FIELD_ADDRESS_RESPONSE = "addressResponse";
    public static final String FIELD_API_GATEWAY_PROXY_REQUEST =
            "apiGatewayProxyRequestEvent";
    public static final String FIELD_API_GATEWAY_PROXY_RESPONSE = "apiGatewayProxyResponseEvent";
    public static final String MESSAGE_REQUEST_LOGGER = "request received";
    public static final String MESSAGE_RESPONSE_LOGGER = "response to be sent";

    /**
     * ************************************************************************
     * *
     * MESSAGE ERROR EXCEPTIONS                        =
     * *
     * *
     * *
     ***************************************************************************/
    public static final String MESSAGE_ERROR_GET_ADDRESS = "There a was problem from getting address";
    public static final String MESSAGE_ERROR_CONVERT_TO_OBJECT = "There a was " +
            "problem form convert json to object";
    public static final String MESSAGE_ERROR_CONVERT_TO_JSON = "There a was " +
            "problem to convert object to json";
}
