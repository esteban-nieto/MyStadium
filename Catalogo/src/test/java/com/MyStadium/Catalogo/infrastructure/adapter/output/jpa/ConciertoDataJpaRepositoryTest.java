package com.MyStadium.Catalogo.infrastructure.adapter.output.jpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ConciertoDataJpaRepositoryTest {

    @Autowired private ConciertoDataJpaRepository repository;

    @Test
    void saveAndFindById() {
        ConciertoData c = ConciertoData.builder().nombre("Test").artista("Artist").build();
        ConciertoData saved = repository.save(c);
        assertNotNull(saved.getId());
        Optional<ConciertoData> found = repository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Test", found.get().getNombre());
    }

    @Test
    void findByIdNotFound() {
        assertFalse(repository.findById("nonexistent").isPresent());
    }

    @Test
    void findAll() {
        repository.save(ConciertoData.builder().nombre("C1").artista("A1").build());
        repository.save(ConciertoData.builder().nombre("C2").artista("A2").build());
        assertTrue(repository.findAll().size() >= 2);
    }

    @Test
    void delete() {
        ConciertoData c = repository.save(ConciertoData.builder().nombre("ToDelete").artista("A").build());
        repository.deleteById(c.getId());
        assertFalse(repository.findById(c.getId()).isPresent());
    }
}
