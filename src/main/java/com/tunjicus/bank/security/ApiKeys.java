package com.tunjicus.bank.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ConfigurationProperties("app.keys")
@Component
public class ApiKeys {
    private String newsApi;
}
