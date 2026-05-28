package com.MyStadium.Notifications.infrastructure.adapter.output.simulated;

import com.MyStadium.Notifications.domain.entity.ReciboCompra;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class EnvioEmailGatewayImplTest {
    private final EnvioEmailGatewayImpl gateway = new EnvioEmailGatewayImpl();

    @Test
    void enviarVerificacionEmail_RetornaTrue() {
        assertTrue(gateway.enviarVerificacionEmail("test@test.com", "u1"));
    }

    @Test
    void enviarRecuperacionEmail_RetornaTrue() {
        assertTrue(gateway.enviarRecuperacionEmail("test@test.com", "http://reset"));
    }

    @Test
    void enviarReciboCompra_RetornaTrue() {
        ReciboCompra recibo = ReciboCompra.builder().emailUsuario("test@test.com")
                .codigoOrden("ORD-001").nombreConcierto("C").asientos(List.of("A1"))
                .totalPagado(100.0).fechaPago(LocalDateTime.now()).build();
        assertTrue(gateway.enviarReciboCompra(recibo));
    }
}
