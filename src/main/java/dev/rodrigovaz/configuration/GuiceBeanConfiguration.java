package dev.rodrigovaz.configuration;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import dev.rodrigovaz.core.usercase.IGetAddressUseCase;
import dev.rodrigovaz.core.usercase.ILoggerUseCase;
import dev.rodrigovaz.integration.CepIntegrationFeign;
import dev.rodrigovaz.usecase.GetAddressUseCase;
import dev.rodrigovaz.usecase.LoggerUseCase;
import feign.Feign;
import feign.Logger;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;

public class GuiceBeanConfiguration extends AbstractModule {
    private final static String URL_BASE_CEP = "https://viacep.com.br";

    @Override
    protected void configure() {
        bind(ILoggerUseCase.class).to(LoggerUseCase.class);
        bind(IGetAddressUseCase.class).to(GetAddressUseCase.class);
    }

    @Provides
    public CepIntegrationFeign provideCepIntegrationFeign() {
        return Feign.builder()
                .decoder(new JacksonDecoder())
                .encoder(new JacksonEncoder())
                .logger(new Slf4jLogger(CepIntegrationFeign.class))
                .logLevel(Logger.Level.BASIC)
                .target(CepIntegrationFeign.class, URL_BASE_CEP);
    }
}