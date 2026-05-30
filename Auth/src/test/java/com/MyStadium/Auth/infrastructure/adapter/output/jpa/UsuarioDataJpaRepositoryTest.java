package com.MyStadium.Auth.infrastructure.adapter.output.jpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UsuarioDataJpaRepositoryTest {
    @Autowired private UsuarioDataJpaRepository repository;

    @Test
    void saveAndFindByCorreo() {
        UsuarioData user = UsuarioData.builder().correo("test@test.com").contraseña("1234").rol("USER").build();
        UsuarioData saved = repository.save(user);
        assertNotNull(saved.getId());

        Optional<UsuarioData> found = repository.findByCorreo("test@test.com");
        assertTrue(found.isPresent());
        assertEquals("test@test.com", found.get().getCorreo());
    }

    @Test
    void findById_NotFound() {
        assertFalse(repository.findById("nonexistent").isPresent());
    }

    @Test
    void findByCorreo_NotFound() {
        assertFalse(repository.findByCorreo("notfound@test.com").isPresent());
    }
}
