package dev.rodrigovaz.core.usercase;

import dev.rodrigovaz.domain.AddressResponse;

public interface IGetAddressUseCase {
    AddressResponse byCep(String cep);
}