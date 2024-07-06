package dev.rodrigovaz.domain;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressResponse {
    @JsonProperty("cep")
    private final String cep;
    @JsonProperty("logradouro")
    private final String publicPlace;
    @JsonProperty("bairro")
    private final String district;
    @JsonProperty("localidade")
    private final String city;
    @JsonProperty("uf")
    private final String districtFederal;

    @JsonCreator
    public AddressResponse(@JsonProperty("cep") String cep,
                           @JsonProperty("logradouro") String publicPlace,
                           @JsonProperty("bairro") String district,
                           @JsonProperty("localidade") String city,
                           @JsonProperty("uf")String districtFederal) {
        this.cep = cep;
        this.publicPlace = publicPlace;
        this.district = district;
        this.city = city;
        this.districtFederal = districtFederal;
    }

    public String getCep() {
        return cep;
    }

    public String getPublicPlace() {
        return publicPlace;
    }

    public String getDistrict() {
        return district;
    }

    public String getCity() {
        return city;
    }

    public String getDistrictFederal() {
        return districtFederal;
    }

    @Override
    public String toString() {
        return "AddressResponse{" +
                "cep='" + cep + '\'' +
                ", publicPlace='" + publicPlace + '\'' +
                ", district='" + district + '\'' +
                ", city='" + city + '\'' +
                ", districtFederal='" + districtFederal + '\'' +
                '}';
    }
}
