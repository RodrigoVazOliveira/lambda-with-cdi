package dev.rodrigovaz.usecase;

import com.google.inject.Inject;
import dev.rodrigovaz.core.helper.ConstantApplication;
import dev.rodrigovaz.core.usercase.IGetAddressUseCase;
import dev.rodrigovaz.domain.AddressResponse;
import dev.rodrigovaz.domain.exception.GetAddressException;
import dev.rodrigovaz.integration.CepIntegrationFeign;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.lambda.powertools.logging.LoggingUtils;

public final class GetAddressUseCase implements IGetAddressUseCase {
    private final Logger logger = LoggerFactory.getLogger(GetAddressUseCase.class);
    private final CepIntegrationFeign cepIntegrationFeign;

    @Inject
    public GetAddressUseCase(CepIntegrationFeign cepIntegrationFeign) {
        this.cepIntegrationFeign = cepIntegrationFeign;
    }

    @Override
    public AddressResponse byCep(String cep) {
        LoggingUtils.appendKey(ConstantApplication.FIELD_CEP, cep);
        logger.info("start get address by cep");

        try {
            final AddressResponse addressResponse = cepIntegrationFeign
                    .getAddressByCep(cep);
            LoggingUtils.appendKey(ConstantApplication.FIELD_ADDRESS_RESPONSE,
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
        LoggingUtils.removeKey(ConstantApplication.FIELD_CEP);
        LoggingUtils.removeKey(ConstantApplication.FIELD_ADDRESS_RESPONSE);
    }

    private void createLoggerError(FeignException feignException) {
        LoggingUtils.appendKey(ConstantApplication.FIELD_STATUS_CODE,
                String.valueOf(feignException.status()));
        LoggingUtils.appendKey(ConstantApplication.FIELD_RESPONSE,
                feignException.responseBody().toString());
        logger.error("there was a problem in integration with viacep");
        LoggingUtils.removeKey(ConstantApplication.FIELD_STATUS_CODE);
        LoggingUtils.removeKey(ConstantApplication.FIELD_RESPONSE);
    }
}
