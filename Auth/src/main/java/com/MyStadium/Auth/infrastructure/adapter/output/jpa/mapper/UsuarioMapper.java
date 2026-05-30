package com.MyStadium.Auth.infrastructure.adapter.output.jpa.mapper;

import com.MyStadium.Auth.domain.entity.Usuario;
import com.MyStadium.Auth.infrastructure.adapter.output.jpa.UsuarioData;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {
    public Usuario toModel(UsuarioData data) {
        if (data == null) return null;
        return Usuario.builder().id(data.getId()).correo(data.getCorreo())
                .contraseña(data.getContraseña()).rol(data.getRol()).build();
    }
    public UsuarioData toData(Usuario model) {
        if (model == null) return null;
        return UsuarioData.builder().id(model.getId()).correo(model.getCorreo())
                .contraseña(model.getContraseña()).rol(model.getRol()).build();
    }
}
