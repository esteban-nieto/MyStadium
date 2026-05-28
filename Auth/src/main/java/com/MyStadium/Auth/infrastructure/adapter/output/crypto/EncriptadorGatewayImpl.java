package com.MyStadium.Auth.infrastructure.adapter.output.crypto;

import com.MyStadium.Auth.domain.gateway.EncriptadorGateway;
import org.springframework.stereotype.Component;

@Component
public class EncriptadorGatewayImpl implements EncriptadorGateway {
    @Override public String encriptar(String contrasena) {
        return "ENCRYPTED_" + contrasena;
    }
    @Override public boolean coinciden(String contrasenaCruda, String contrasenaEncriptada) {
        return contrasenaEncriptada.equals(encriptar(contrasenaCruda));
    }
}
