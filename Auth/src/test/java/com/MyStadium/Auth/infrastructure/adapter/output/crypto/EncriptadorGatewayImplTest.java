package com.MyStadium.Auth.infrastructure.adapter.output.crypto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EncriptadorGatewayImplTest {
    private final EncriptadorGatewayImpl encriptador = new EncriptadorGatewayImpl();

    @Test
    void encriptar_AgregaPrefijo() {
        assertEquals("ENCRYPTED_miContraseña", encriptador.encriptar("miContraseña"));
    }

    @Test
    void encriptar_EmptyString() {
        assertEquals("ENCRYPTED_", encriptador.encriptar(""));
    }

    @Test
    void coinciden_Correcto() {
        assertTrue(encriptador.coinciden("pass", "ENCRYPTED_pass"));
    }

    @Test
    void coinciden_Incorrecto() {
        assertFalse(encriptador.coinciden("pass", "ENCRYPTED_wrong"));
    }

    @Test
    void coinciden_NullEncriptado() {
        assertThrows(NullPointerException.class, () -> encriptador.coinciden("pass", null));
    }

    @Test
    void encriptar_Null() {
        assertEquals("ENCRYPTED_null", encriptador.encriptar(null));
    }

    @Test
    void coinciden_NullCruda() {
        assertFalse(encriptador.coinciden(null, "ENCRYPTED_pass"));
    }
}
