package com.MyStadium.Auth.infrastructure.config;

import com.MyStadium.Auth.domain.gateway.EncriptadorGateway;
import com.MyStadium.Auth.domain.gateway.UsuarioGateway;
import com.MyStadium.Auth.domain.usecase.UsuarioUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UseCaseConfigTest {
    @Mock private UsuarioGateway usuarioGateway;
    @Mock private EncriptadorGateway encriptadorGateway;

    @Test
    void usuarioUseCase_BeanCreado() {
        UseCaseConfig config = new UseCaseConfig();
        UsuarioUseCase useCase = config.usuarioUseCase(usuarioGateway, encriptadorGateway);
        assertNotNull(useCase);
    }
}
