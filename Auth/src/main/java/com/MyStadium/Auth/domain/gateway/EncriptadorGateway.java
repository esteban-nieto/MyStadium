package com.MyStadium.Auth.domain.gateway;

public interface EncriptadorGateway {
    String encriptar(String contrasena);
    boolean coinciden(String contrasenaCruda, String contrasenaEncriptada);
}
