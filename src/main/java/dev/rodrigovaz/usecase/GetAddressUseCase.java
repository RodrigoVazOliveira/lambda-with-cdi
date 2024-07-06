package dev.rodrigovaz.usecase;

import com.google.inject.Inject;
import dev.rodrigovaz.core.usercase.IGetAddressUseCase;
import dev.rodrigovaz.domain.AddressResponse;
import dev.rodrigovaz.integration.CepIntegrationFeign;
import feign.FeignException;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetAddressUseCase implements IGetAddressUseCase {
    private static final String FIELD_RESPONSE = "responseBody";
    private final static String FIELD_CEP = "cep";
    private final static String FIELD_STATUS_CODE = "statusCode";
    private static final String FIELD_ADDRESS_RESPONSE = "addressResponse";
    private final Logger logger = LoggerFactory.getLogger(GetAddressUseCase.class);
    private final CepIntegrationFeign cepIntegrationFeign;

    @Inject
    public GetAddressUseCase(CepIntegrationFeign cepIntegrationFeign) {
        this.cepIntegrationFeign = cepIntegrationFeign;
    }

    @Override
    public AddressResponse byCep(String cep) {
        ThreadContext.put(FIELD_CEP, cep);
        logger.info("start get address by cep");

        try {
            final AddressResponse addressResponse = cepIntegrationFeign.getAddressByCep(cep);
            ThreadContext.put(FIELD_ADDRESS_RESPONSE, addressResponse.toString());
            logger.info("completed get address with sucessfully");
            ThreadContext.remove(FIELD_CEP);
            ThreadContext.remove(FIELD_ADDRESS_RESPONSE);

            return addressResponse;
        } catch (FeignException feignException) {
            ThreadContext.put(FIELD_STATUS_CODE, String.valueOf(feignException.status()));
            ThreadContext.put(FIELD_RESPONSE, feignException.responseBody().toString());
            logger.error("there was a problem in integration with viacep");
            ThreadContext.remove(FIELD_STATUS_CODE);
            ThreadContext.remove(FIELD_RESPONSE);

            throw new RuntimeException(feignException.getMessage());
        }
    }
}
