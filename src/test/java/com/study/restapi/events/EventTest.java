package com.study.restapi.events;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    @Test
    public void builder() {
        Event event = Event.builder()
                .name("Inflearn Sprign REST API")
                .description("REST API development with Spring")
                .build();
        assertThat(event).isNotNull();

    }

    @Test
    public void javaBean() {
        // Given
        String name = "Event";
        String description = "Spring";

        // When
        Event event = Event.builder()
                .name(name)
                .description(description)
                .build();

        // Then
        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);
    }

}