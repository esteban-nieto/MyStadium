package com.MyStadium.MapaDeCalor.infrastructure.adapter.output.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ZonaDataJpaRepository extends JpaRepository<ZonaData, String> {
    List<ZonaData> findByConciertoId(String conciertoId);
}
