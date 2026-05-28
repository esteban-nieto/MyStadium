package com.MyStadium.Catalogo.infrastructure.config;

import com.MyStadium.Catalogo.domain.gateway.ConciertoGateway;
import com.MyStadium.Catalogo.domain.usecase.ConciertoUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UseCaseConfigTest {
    @Mock private ConciertoGateway conciertoGateway;

    @Test
    void conciertoUseCase_BeanCreado() {
        UseCaseConfig config = new UseCaseConfig();
        ConciertoUseCase useCase = config.conciertoUseCase(conciertoGateway);
        assertNotNull(useCase);
    }
}
