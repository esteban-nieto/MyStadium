package com.MyStadium.Notifications.pdf.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class TicketEntry {
    private String zona;
    private String asiento;
    private String codigoUnico;
    private String qrUrl;
    private BigDecimal precio;
    private String tipoEntrada;
    private int index;
    private int total;
}
