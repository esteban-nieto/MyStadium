package com.MyStadium.Auth.infrastructure.adapter.output.jpa.mapper;

import com.MyStadium.Auth.domain.entity.Usuario;
import com.MyStadium.Auth.infrastructure.adapter.output.jpa.UsuarioData;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioMapperTest {
    private final UsuarioMapper mapper = new UsuarioMapper();

    @Test
    void toModel_MapeaTodosLosCampos() {
        UsuarioData data = UsuarioData.builder().id("1").correo("a@b.com").contrasena("secret").rol("USER").build();
        Usuario model = mapper.toModel(data);
        assertEquals("1", model.getId());
        assertEquals("a@b.com", model.getCorreo());
        assertEquals("secret", model.getContrasena());
        assertEquals("USER", model.getRol());
    }

    @Test
    void toModel_Null() {
        assertNull(mapper.toModel(null));
    }

    @Test
    void toData_MapeaTodosLosCampos() {
        Usuario model = Usuario.builder().id("1").correo("a@b.com").contrasena("secret").rol("ADMIN").build();
        UsuarioData data = mapper.toData(model);
        assertEquals("1", data.getId());
        assertEquals("a@b.com", data.getCorreo());
        assertEquals("secret", data.getContrasena());
        assertEquals("ADMIN", data.getRol());
    }

    @Test
    void toData_Null() {
        assertNull(mapper.toData(null));
    }

    @Test
    void toModel_CamposNulos() {
        UsuarioData data = UsuarioData.builder().build();
        Usuario model = mapper.toModel(data);
        assertNull(model.getId());
        assertNull(model.getCorreo());
        assertNull(model.getContrasena());
        assertNull(model.getRol());
    }
}
