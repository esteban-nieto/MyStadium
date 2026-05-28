package com.MyStadium.MapaDeCalor.infrastructure.config;

import com.MyStadium.MapaDeCalor.domain.gateway.BoletoGateway;
import com.MyStadium.MapaDeCalor.domain.gateway.ZonaGateway;
import com.MyStadium.MapaDeCalor.domain.usecase.MapaDeCalorUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {
    @Bean
    public MapaDeCalorUseCase mapaDeCalorUseCase(ZonaGateway zonaGateway, BoletoGateway boletoGateway) {
        return new MapaDeCalorUseCase(zonaGateway, boletoGateway);
    }
}
