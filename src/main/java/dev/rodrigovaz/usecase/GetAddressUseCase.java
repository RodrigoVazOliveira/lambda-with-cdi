package dev.rodrigovaz.usecase;

import com.google.inject.Inject;
import dev.rodrigovaz.core.helper.ConstantApplication;
import dev.rodrigovaz.core.usercase.IGetAddressUseCase;
import dev.rodrigovaz.domain.AddressResponse;
import dev.rodrigovaz.domain.exception.GetAddressException;
import dev.rodrigovaz.integration.CepIntegrationFeign;
import feign.FeignException;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class GetAddressUseCase implements IGetAddressUseCase {
    private final Logger logger = LoggerFactory.getLogger(GetAddressUseCase.class);
    private final CepIntegrationFeign cepIntegrationFeign;

    @Inject
    public GetAddressUseCase(CepIntegrationFeign cepIntegrationFeign) {
        this.cepIntegrationFeign = cepIntegrationFeign;
    }

    @Override
    public AddressResponse byCep(String cep) {
        ThreadContext.put(ConstantApplication.FIELD_CEP, cep);
        logger.info("start get address by cep");

        try {
            final AddressResponse addressResponse = cepIntegrationFeign
                    .getAddressByCep(cep);
            ThreadContext.put(ConstantApplication.FIELD_ADDRESS_RESPONSE,
                    addressResponse.toString());
            logger.info("completed get address with successfully");
            removeFieldLog();

            return addressResponse;
        } catch (FeignException feignException) {
            createLoggerError(feignException);

            throw new GetAddressException(ConstantApplication.MESSAGE_ERROR_GET_ADDRESS,
                    feignException);
        }
    }

    private void removeFieldLog() {
        ThreadContext.remove(ConstantApplication.FIELD_CEP);
        ThreadContext.remove(ConstantApplication.FIELD_ADDRESS_RESPONSE);
    }

    private void createLoggerError(FeignException feignException) {
        ThreadContext.put(ConstantApplication.FIELD_STATUS_CODE,
                String.valueOf(feignException.status()));
        ThreadContext.put(ConstantApplication.FIELD_RESPONSE,
                feignException.responseBody().toString());
        logger.error("there was a problem in integration with viacep");
        ThreadContext.remove(ConstantApplication.FIELD_STATUS_CODE);
        ThreadContext.remove(ConstantApplication.FIELD_RESPONSE);
    }
}
