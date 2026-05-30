package com.MyStadium.Auth.domain.gateway;

public interface EncriptadorGateway {
    String encriptar(String contraseña);
    boolean coinciden(String contraseñaCruda, String contraseñaEncriptada);
}
