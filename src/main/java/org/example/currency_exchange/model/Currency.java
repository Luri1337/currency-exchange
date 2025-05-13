package org.example.currency_exchange.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Currency {
    private int id;
    @JsonProperty(value = "name")
    private String fullName;
    private String code;
    private String sign;

    public Currency(String fullName, String code, String sign) {
        this.fullName = fullName;
        this.code = code;
        this.sign = sign;
    }
}
