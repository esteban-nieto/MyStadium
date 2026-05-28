package com.MyStadium.MapaDeCalor.infrastructure.config;

import com.MyStadium.MapaDeCalor.domain.gateway.BoletoGateway;
import com.MyStadium.MapaDeCalor.domain.gateway.ZonaGateway;
import com.MyStadium.MapaDeCalor.domain.usecase.MapaDeCalorUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UseCaseConfigTest {
    @Mock private ZonaGateway zonaGateway;
    @Mock private BoletoGateway boletoGateway;

    @Test
    void mapaDeCalorUseCase_BeanCreado() {
        UseCaseConfig config = new UseCaseConfig();
        MapaDeCalorUseCase useCase = config.mapaDeCalorUseCase(zonaGateway, boletoGateway);
        assertNotNull(useCase);
    }
}
