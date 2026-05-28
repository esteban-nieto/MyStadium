package com.MyStadium.MapaDeCalor.infrastructure.adapter.output.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BoletoDataJpaRepository extends JpaRepository<BoletoData, String> {
    Optional<BoletoData> findByCodigoUnico(String codigoUnico);
    List<BoletoData> findByUsuarioId(String usuarioId);
}
