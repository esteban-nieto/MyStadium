package com.MyStadium.Notifications.pdf.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class TicketPdfData {
    private String eventName;
    private String artist;
    private String stadium;
    private LocalDateTime fechaEvento;
    private String orderCode;
    private String userName;
    private byte[] eventBanner;
    private byte[] logo;
    private List<TicketEntry> tickets;
    private double totalPaid;
}
