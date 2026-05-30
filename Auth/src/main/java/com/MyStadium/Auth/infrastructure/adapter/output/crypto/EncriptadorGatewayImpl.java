package com.MyStadium.Auth.infrastructure.adapter.output.crypto;

import com.MyStadium.Auth.domain.gateway.EncriptadorGateway;
import org.springframework.stereotype.Component;

@Component
public class EncriptadorGatewayImpl implements EncriptadorGateway {
    @Override public String encriptar(String contraseña) {
        return "ENCRYPTED_" + contraseña;
    }
    @Override public boolean coinciden(String contraseñaCruda, String contraseñaEncriptada) {
        return contraseñaEncriptada.equals(encriptar(contraseñaCruda));
    }
}
