package com.MyStadium.Notifications.infrastructure.adapter.output.simulated;

import com.MyStadium.Notifications.domain.gateway.EnvioPushGateway;
import org.springframework.stereotype.Component;

@Component
public class EnvioPushGatewayImpl implements EnvioPushGateway {
    @Override
    public boolean enviarPush(String usuarioId, String titulo, String cuerpo) {
        System.out.println("Enviando notificación push al usuario: " + usuarioId);
        System.out.println("Título: " + titulo);
        System.out.println("Mensaje: " + cuerpo);
        return true;
    }
}
