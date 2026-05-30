package com.MyStadium.MapaDeCalor.infrastructure.adapter.output.jpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BoletoDataJpaRepositoryTest {

    @Autowired private BoletoDataJpaRepository repository;

    @Test
    void saveAndFindByCodigoUnico() {
        BoletoData b = BoletoData.builder().codigoUnico("MST-TEST-001").usuarioId("u1").conciertoId("c1").build();
        BoletoData saved = repository.save(b);
        assertNotNull(saved.getId());
        Optional<BoletoData> found = repository.findByCodigoUnico("MST-TEST-001");
        assertTrue(found.isPresent());
    }

    @Test
    void findByCodigoUnicoNotFound() {
        assertFalse(repository.findByCodigoUnico("NONEXISTENT").isPresent());
    }

    @Test
    void findByUsuarioId() {
        repository.save(BoletoData.builder().codigoUnico("MST-U1-1").usuarioId("userX").conciertoId("c1").build());
        repository.save(BoletoData.builder().codigoUnico("MST-U1-2").usuarioId("userX").conciertoId("c1").build());
        assertEquals(2, repository.findByUsuarioId("userX").size());
    }

    @Test
    void findByUsuarioIdEmpty() {
        assertTrue(repository.findByUsuarioId("nouser").isEmpty());
    }
}
