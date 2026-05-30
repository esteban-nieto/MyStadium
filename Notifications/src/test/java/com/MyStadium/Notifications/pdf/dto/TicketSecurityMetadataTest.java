package com.MyStadium.Notifications.pdf.dto;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;

class TicketSecurityMetadataTest {

    @Test
    void builderAndGetters() {
        Instant now = Instant.now();
        TicketSecurityMetadata m = TicketSecurityMetadata.builder()
                .generatedBy("MyStadium")
                .generatedAt(now)
                .checksum("abc123")
                .signature("sig456")
                .build();
        assertEquals("MyStadium", m.getGeneratedBy());
        assertEquals(now, m.getGeneratedAt());
        assertEquals("abc123", m.getChecksum());
        assertEquals("sig456", m.getSignature());
    }

    @Test
    void setters() {
        TicketSecurityMetadata m = TicketSecurityMetadata.builder().build();
        m.setGeneratedBy("System");
        m.setChecksum("xyz");
        assertEquals("System", m.getGeneratedBy());
        assertEquals("xyz", m.getChecksum());
    }

    @Test
    void defaultsAreNull() {
        TicketSecurityMetadata m = TicketSecurityMetadata.builder().build();
        assertNull(m.getGeneratedBy());
    }

    @Test
    void toStringContainsGeneratedBy() {
        TicketSecurityMetadata m = TicketSecurityMetadata.builder().generatedBy("MyStadium").build();
        assertTrue(m.toString().contains("MyStadium"));
    }
}
