package com.MyStadium.Auth.infrastructure.adapter.output.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioDataJpaRepository extends JpaRepository<UsuarioData, String> {
    Optional<UsuarioData> findByCorreo(String correo);
}
