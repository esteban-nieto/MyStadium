package com.MyStadium.MapaDeCalor.infrastructure.adapter.output.jpa;

import com.MyStadium.MapaDeCalor.domain.entity.Boleto;
import com.MyStadium.MapaDeCalor.domain.gateway.BoletoGateway;
import com.MyStadium.MapaDeCalor.infrastructure.adapter.output.jpa.mapper.BoletoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class BoletoDataGatewayImpl implements BoletoGateway {
    private final BoletoDataJpaRepository repository;
    private final BoletoMapper mapper;

    @Override public Boleto guardar(Boleto boleto) {
        return mapper.toModel(repository.save(mapper.toData(boleto)));
    }
    @Override public Optional<Boleto> buscarPorCodigo(String codigoUnico) {
        return repository.findByCodigoUnico(codigoUnico).map(mapper::toModel);
    }
    @Override public List<Boleto> buscarPorUsuarioId(String usuarioId) {
        return repository.findByUsuarioId(usuarioId).stream()
                .map(mapper::toModel).collect(Collectors.toList());
    }
}
