package com.MyStadium.Notifications.infrastructure.adapter.output.simulated;

import com.MyStadium.Notifications.domain.entity.ReciboCompra;
import com.MyStadium.Notifications.domain.gateway.EnvioEmailGateway;
import org.springframework.stereotype.Component;

@Component
public class EnvioEmailGatewayImpl implements EnvioEmailGateway {
    @Override
    public boolean enviarVerificacionEmail(String email, String usuarioId) {
        System.out.println("Enviando email de verificación a: " + email);
        System.out.println("Usuario ID: " + usuarioId);
        System.out.println("Link de verificación: http://localhost:8083/api/verify?usuarioId=" + usuarioId);

        return true;
    }
    @Override
    public boolean enviarRecuperacionEmail(String email, String enlaceRecuperacion) {

        System.out.println("Enviando email de recuperación a: " + email);
        System.out.println("Link de recuperación: " + enlaceRecuperacion);

        return true;
    }
    @Override
    public boolean enviarReciboCompra(ReciboCompra recibo) {

        System.out.println("Enviando recibo de compra a: " + recibo.getEmailUsuario());
        System.out.println("Orden: " + recibo.getCodigoOrden());
        System.out.println("Concierto: " + recibo.getNombreConcierto());
        System.out.println("Asientos: " + String.join(", ", recibo.getAsientos()));
        System.out.println("Total pagado: $" + recibo.getTotalPagado());

        return true;
    }
}
