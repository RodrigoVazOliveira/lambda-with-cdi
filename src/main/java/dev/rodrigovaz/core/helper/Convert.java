package dev.rodrigovaz.core.helper;

import com.amazonaws.lambda.thirdparty.com.fasterxml.jackson.core.JsonProcessingException;
import com.amazonaws.lambda.thirdparty.com.fasterxml.jackson.databind.ObjectMapper;
import dev.rodrigovaz.domain.exception.ConvertErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Convert<T> {
    private static final Logger logger = LoggerFactory.getLogger(Convert.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private Convert() {
    }

    public static <T> T toObject(String json, Class<T> objectType) {
        try {
            return objectMapper.readValue(json, objectType);
        } catch (JsonProcessingException e) {
            logger.error("There a was problem convert json to object");
            throw new ConvertErrorException(
                    ConstantApplication.MESSAGE_ERROR_CONVERT_TO_OBJECT, e);
        }
    }

    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.error("There a was problem convert object to json");
            throw new ConvertErrorException(
                    ConstantApplication.MESSAGE_ERROR_CONVERT_TO_JSON, e);
        }
    }
}
