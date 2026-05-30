package com.MyStadium.Auth.infrastructure.adapter.output.crypto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EncriptadorGatewayImplTest {
    private final EncriptadorGatewayImpl encriptador = new EncriptadorGatewayImpl();

    @Test
    void encriptar_DevuelveHashBCrypt() {
        String hash = encriptador.encriptar("password");
        assertTrue(hash.startsWith("$2a$") || hash.startsWith("$2b$"));
        assertTrue(hash.length() >= 50);
    }

    @Test
    void encriptar_EmptyString() {
        String hash = encriptador.encriptar("");
        assertTrue(hash.startsWith("$2a$") || hash.startsWith("$2b$"));
    }

    @Test
    void coinciden_Correcto() {
        String hash = encriptador.encriptar("password");
        assertTrue(encriptador.coinciden("password", hash));
    }

    @Test
    void coinciden_Incorrecto() {
        String hash = encriptador.encriptar("password");
        assertFalse(encriptador.coinciden("wrong", hash));
    }

    @Test
    void coinciden_NullEncriptado() {
        assertFalse(encriptador.coinciden("pass", null));
    }

    @Test
    void encriptar_Null() {
        String hash = encriptador.encriptar(null);
        assertTrue(hash.startsWith("$2a$") || hash.startsWith("$2b$"));
    }

    @Test
    void coinciden_NullCruda() {
        assertFalse(encriptador.coinciden(null, "$2a$10$hash"));
    }

    @Test
    void hashDistintoCadaVez() {
        String hash1 = encriptador.encriptar("password");
        String hash2 = encriptador.encriptar("password");
        assertNotEquals(hash1, hash2);
    }
}
