package dev.rodrigovaz.configuration;

import com.amazonaws.lambda.thirdparty.com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import dev.rodrigovaz.usecase.ILoggerUseCase;
import dev.rodrigovaz.usecase.LoggerUseCase;

public class GuiceBeanConfiguration extends AbstractModule {
    @Override
    protected void configure() {
        //bind(ObjectMapper.class).to(ObjectMapper.class);
        bind(ILoggerUseCase.class).to(LoggerUseCase.class);
    }
}