package com.MyStadium.MapaDeCalor.infrastructure.adapter.output.jpa;

import com.MyStadium.MapaDeCalor.domain.entity.Zona;
import com.MyStadium.MapaDeCalor.domain.gateway.ZonaGateway;
import com.MyStadium.MapaDeCalor.infrastructure.adapter.output.jpa.mapper.ZonaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ZonaDataGatewayImpl implements ZonaGateway {
    private final ZonaDataJpaRepository repository;
    private final ZonaMapper mapper;

    @Override public Zona guardar(Zona zona) {
        return mapper.toModel(repository.save(mapper.toData(zona)));
    }
    @Override public Optional<Zona> buscarPorId(String id) {
        return repository.findById(id).map(mapper::toModel);
    }
    @Override public List<Zona> buscarPorConciertoId(String conciertoId) {
        return repository.findByConciertoId(conciertoId).stream()
                .map(mapper::toModel).collect(Collectors.toList());
    }
    @Override public void actualizarOcupados(String zonaId, int nuevosOcupados) {
        repository.findById(zonaId).ifPresent(z -> {
            z.setOcupados(nuevosOcupados);
            repository.save(z);
        });
    }
}
