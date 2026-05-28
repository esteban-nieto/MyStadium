package com.MyStadium.Auth.infrastructure.adapter.output.jpa;

import com.MyStadium.Auth.domain.entity.Usuario;
import com.MyStadium.Auth.domain.gateway.UsuarioGateway;
import com.MyStadium.Auth.infrastructure.adapter.output.jpa.mapper.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UsuarioDataGatewayImpl implements UsuarioGateway {
    private final UsuarioDataJpaRepository repository;
    private final UsuarioMapper mapper;

    @Override public Usuario guardarUsuario(Usuario usuario) {
        return mapper.toModel(repository.save(mapper.toData(usuario)));
    }
    @Override public Usuario buscarUsuarioPorId(String id) {
        return repository.findById(id).map(mapper::toModel).orElse(null);
    }
    @Override public Usuario buscarPorCorreo(String correo) {
        return repository.findByCorreo(correo).map(mapper::toModel).orElse(null);
    }
    @Override public void eliminarUsuarioPorId(String id) {
        repository.deleteById(id);
    }
}
