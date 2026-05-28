package com.MyStadium.Notifications.domain.gateway;

public interface EnvioPushGateway {
    boolean enviarPush(String usuarioId, String titulo, String cuerpo);
}
