package dev.rodrigovaz.integration;

import dev.rodrigovaz.domain.AddressResponse;
import feign.Param;
import feign.RequestLine;

public interface CepIntegrationFeign {

    @RequestLine("GET /ws/{cepNumber}/json/")
    AddressResponse getAddressByCep(@Param("cepNumber") String numberCep);
}