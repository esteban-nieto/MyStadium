package com.MyStadium.Notifications.domain.gateway;

import com.MyStadium.Notifications.domain.entity.ReciboCompra;

public interface EnvioEmailGateway {
    boolean enviarVerificacionEmail(String email, String usuarioId);
    boolean enviarRecuperacionEmail(String email, String enlaceRecuperacion);
    boolean enviarReciboCompra(ReciboCompra recibo);
}
