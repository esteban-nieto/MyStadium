package com.MyStadium.Notifications.infrastructure.adapter.output.simulated;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EnvioPushGatewayImplTest {
    private final EnvioPushGatewayImpl gateway = new EnvioPushGatewayImpl();

    @Test
    void enviarPush_RetornaTrue() {
        assertTrue(gateway.enviarPush("u1", "Titulo", "Cuerpo"));
    }
}
