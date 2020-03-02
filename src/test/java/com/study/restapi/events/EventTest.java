package com.study.restapi.events;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

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


    @ParameterizedTest
    @CsvSource({
            "0, 0, true",
            "0, 100, false",
            "100, 0, false",
    })
    public void testFree(int basePrice, int maxPrice, boolean isFree) {
        System.out.println(basePrice);
        System.out.println(maxPrice);
        System.out.println(isFree);
        // Given 이런 상태에서
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();

        // When 이런 일이 벌어지면
        event.update();

        // Then 이렇게 된다
        assertThat(event.isFree()).isEqualTo(isFree);
    }

    @ParameterizedTest
    @MethodSource("isOffline")
    public void testOffline(String location, boolean isOffline) {
        // Given 이런 상태에서
        Event event = Event.builder()
                .location(location)
                .build();

        // When 이런 일이 벌어지면
        event.update();

        // Then 이렇게 된다
        assertThat(event.isOffline()).isEqualTo(isOffline);
    }

    private static Stream<Arguments> isOffline() {
        return Stream.of(
                Arguments.of("강남역", true),
                Arguments.of(null, false),
                Arguments.of("", false)
        );
    }

}
