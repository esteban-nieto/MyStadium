package com.MyStadium.Auth.domain.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    void builderAndGetters() {
        Usuario u = Usuario.builder().id("1").correo("a@b.com").contraseña("pass").rol("USER").build();
        assertEquals("1", u.getId());
        assertEquals("a@b.com", u.getCorreo());
        assertEquals("pass", u.getContraseña());
        assertEquals("USER", u.getRol());
    }

    @Test
    void setters() {
        Usuario u = new Usuario();
        u.setId("2");
        u.setCorreo("x@y.com");
        u.setContraseña("secret");
        u.setRol("ADMIN");
        assertEquals("2", u.getId());
        assertEquals("x@y.com", u.getCorreo());
        assertEquals("secret", u.getContraseña());
        assertEquals("ADMIN", u.getRol());
    }

    @Test
    void equalsAndHashCode() {
        Usuario u1 = Usuario.builder().id("1").correo("a@b.com").contraseña("p").rol("USER").build();
        Usuario u2 = Usuario.builder().id("1").correo("a@b.com").contraseña("p").rol("USER").build();
        assertEquals(u1, u2);
        assertEquals(u1.hashCode(), u2.hashCode());
    }

    @Test
    void noArgsConstructor() {
        Usuario u = new Usuario();
        assertNull(u.getId());
        assertNull(u.getCorreo());
        assertNull(u.getContraseña());
        assertNull(u.getRol());
    }

    @Test
    void toStringContainsFields() {
        Usuario u = Usuario.builder().id("1").correo("test@test.com").contraseña("p").rol("USER").build();
        String s = u.toString();
        assertTrue(s.contains("1") || s.contains("test@test.com"));
    }
}
