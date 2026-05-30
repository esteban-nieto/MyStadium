package com.MyStadium.Auth.infrastructure.adapter.output.crypto;

import com.MyStadium.Auth.domain.gateway.EncriptadorGateway;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class EncriptadorGatewayImpl implements EncriptadorGateway {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override public String encriptar(String contraseña) {
        return encoder.encode(contraseña != null ? contraseña : "");
    }

    @Override public boolean coinciden(String contraseñaCruda, String contraseñaEncriptada) {
        if (contraseñaCruda == null || contraseñaEncriptada == null) return false;
        return encoder.matches(contraseñaCruda, contraseñaEncriptada);
    }
}
