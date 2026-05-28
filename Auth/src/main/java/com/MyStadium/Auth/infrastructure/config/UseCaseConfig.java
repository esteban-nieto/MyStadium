package com.MyStadium.Auth.infrastructure.config;

import com.MyStadium.Auth.domain.gateway.EncriptadorGateway;
import com.MyStadium.Auth.domain.gateway.UsuarioGateway;
import com.MyStadium.Auth.domain.usecase.UsuarioUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {
    @Bean
    public UsuarioUseCase usuarioUseCase(UsuarioGateway usuarioGateway, EncriptadorGateway encriptadorGateway) {
        return new UsuarioUseCase(usuarioGateway, encriptadorGateway);
    }
}
