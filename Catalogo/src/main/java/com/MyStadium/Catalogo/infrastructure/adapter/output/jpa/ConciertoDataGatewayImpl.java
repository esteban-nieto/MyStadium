package com.MyStadium.Catalogo.infrastructure.adapter.output.jpa;

import com.MyStadium.Catalogo.domain.entity.Concierto;
import com.MyStadium.Catalogo.domain.gateway.ConciertoGateway;
import com.MyStadium.Catalogo.infrastructure.adapter.output.jpa.mapper.ConciertoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ConciertoDataGatewayImpl implements ConciertoGateway {
    private final ConciertoDataJpaRepository repository;
    private final ConciertoMapper mapper;

    @Override public Concierto guardar(Concierto concierto) {
        return mapper.toModel(repository.save(mapper.toData(concierto)));
    }
    @Override public Concierto buscarPorId(String id) {
        return repository.findById(id).map(mapper::toModel).orElse(null);
    }
    @Override public List<Concierto> buscarTodos() {
        return repository.findAll().stream().map(mapper::toModel).collect(Collectors.toList());
    }
    @Override public void eliminarPorId(String id) {
        repository.deleteById(id);
    }
}
