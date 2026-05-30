package com.MyStadium.MapaDeCalor.infrastructure.adapter.output.jpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ZonaDataJpaRepositoryTest {

    @Autowired private ZonaDataJpaRepository repository;

    @Test
    void saveAndFindByConciertoId() {
        ZonaData z = ZonaData.builder().conciertoId("c1").nombre("VIP").capacidad(100).ocupados(30).build();
        ZonaData saved = repository.save(z);
        assertNotNull(saved.getId());
        assertFalse(repository.findByConciertoId("c1").isEmpty());
    }

    @Test
    void findByConciertoIdEmpty() {
        assertTrue(repository.findByConciertoId("nonexistent").isEmpty());
    }

    @Test
    void saveAndFindById() {
        ZonaData z = repository.save(ZonaData.builder().conciertoId("c2").nombre("GENERAL").capacidad(500).build());
        Optional<ZonaData> found = repository.findById(z.getId());
        assertTrue(found.isPresent());
        assertEquals("GENERAL", found.get().getNombre());
    }

    @Test
    void findByIdNotFound() {
        assertFalse(repository.findById("nonexistent").isPresent());
    }
}
