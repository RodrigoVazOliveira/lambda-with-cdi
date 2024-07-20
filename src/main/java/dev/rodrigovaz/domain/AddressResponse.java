package dev.rodrigovaz.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AddressResponse(@JsonProperty("cep") String cep,
                              @JsonProperty("logradouro") String publicPlace,
                              @JsonProperty("bairro") String district,
                              @JsonProperty("localidade") String city,
                              @JsonProperty("uf") String districtFederal) {
}
