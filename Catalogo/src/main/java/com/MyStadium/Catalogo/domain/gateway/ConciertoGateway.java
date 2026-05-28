package com.MyStadium.Catalogo.domain.gateway;

import com.MyStadium.Catalogo.domain.entity.Concierto;
import java.util.List;

public interface ConciertoGateway {
    Concierto guardar(Concierto concierto);
    Concierto buscarPorId(String id);
    List<Concierto> buscarTodos();
    void eliminarPorId(String id);
}
