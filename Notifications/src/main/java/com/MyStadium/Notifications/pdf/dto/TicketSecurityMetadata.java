package com.MyStadium.Notifications.pdf.dto;

import lombok.Builder;
import lombok.Data;
import java.time.Instant;

@Data
@Builder
public class TicketSecurityMetadata {
    private String generatedBy;
    private Instant generatedAt;
    private String checksum;
    private String signature;
}
