package com.MyStadium.MapaDeCalor.domain.gateway;

import com.MyStadium.MapaDeCalor.domain.entity.Boleto;
import java.util.List;
import java.util.Optional;

public interface BoletoGateway {
    Boleto guardar(Boleto boleto);
    Optional<Boleto> buscarPorCodigo(String codigoUnico);
    List<Boleto> buscarPorUsuarioId(String usuarioId);
}
