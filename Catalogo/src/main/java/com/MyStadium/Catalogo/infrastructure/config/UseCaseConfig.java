package com.MyStadium.Catalogo.infrastructure.config;

import com.MyStadium.Catalogo.domain.gateway.ConciertoGateway;
import com.MyStadium.Catalogo.domain.usecase.ConciertoUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {
    @Bean
    public ConciertoUseCase conciertoUseCase(ConciertoGateway conciertoGateway) {
        return new ConciertoUseCase(conciertoGateway);
    }
}
