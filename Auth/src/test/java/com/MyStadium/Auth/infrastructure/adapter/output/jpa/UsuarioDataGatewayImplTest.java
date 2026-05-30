package com.MyStadium.Auth.infrastructure.adapter.output.jpa;

import com.MyStadium.Auth.domain.entity.Usuario;
import com.MyStadium.Auth.infrastructure.adapter.output.jpa.mapper.UsuarioMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioDataGatewayImplTest {
    @Mock private UsuarioDataJpaRepository repository;
    @Mock private UsuarioMapper mapper;
    @InjectMocks private UsuarioDataGatewayImpl gateway;
    private Usuario usuario;
    private UsuarioData data;

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder().id("1").correo("a@b.com").contraseña("secret").build();
        data = UsuarioData.builder().id("1").correo("a@b.com").contraseña("secret").build();
    }

    @Test
    void guardarUsuario_DelegaEnRepository() {
        when(mapper.toData(usuario)).thenReturn(data);
        when(repository.save(data)).thenReturn(data);
        when(mapper.toModel(data)).thenReturn(usuario);
        Usuario result = gateway.guardarUsuario(usuario);
        assertEquals("a@b.com", result.getCorreo());
        verify(repository).save(data);
    }

    @Test
    void buscarUsuarioPorId_Encontrado() {
        when(repository.findById("1")).thenReturn(Optional.of(data));
        when(mapper.toModel(data)).thenReturn(usuario);
        Usuario result = gateway.buscarUsuarioPorId("1");
        assertEquals("a@b.com", result.getCorreo());
    }

    @Test
    void buscarUsuarioPorId_NoEncontrado() {
        when(repository.findById("99")).thenReturn(Optional.empty());
        assertNull(gateway.buscarUsuarioPorId("99"));
    }

    @Test
    void buscarPorCorreo_Encontrado() {
        when(repository.findByCorreo("a@b.com")).thenReturn(Optional.of(data));
        when(mapper.toModel(data)).thenReturn(usuario);
        Usuario result = gateway.buscarPorCorreo("a@b.com");
        assertEquals("a@b.com", result.getCorreo());
    }

    @Test
    void buscarPorCorreo_NoEncontrado() {
        when(repository.findByCorreo("x@y.com")).thenReturn(Optional.empty());
        assertNull(gateway.buscarPorCorreo("x@y.com"));
    }

    @Test
    void eliminarUsuarioPorId_DelegaEnRepository() {
        doNothing().when(repository).deleteById("1");
        gateway.eliminarUsuarioPorId("1");
        verify(repository).deleteById("1");
    }

    @Test
    void guardarUsuario_ConMapperNull() {
        when(mapper.toData(usuario)).thenReturn(null);
        when(repository.save(null)).thenThrow(new RuntimeException("Entity must not be null"));
        assertThrows(RuntimeException.class, () -> gateway.guardarUsuario(usuario));
    }
}
