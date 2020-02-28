package com.study.restapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.head;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class EventControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    EventRepository eventRepository;

    @Test
    public void createEvent() throws Exception {
        Event event = Event.builder()
                .name("Spring")
                .description("REST PI Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 11, 11, 11))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 11, 11, 11))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 11, 11, 11))
                .endEventDateTime(LocalDateTime.of(2018, 11, 11, 11, 11))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역")
                .build();

        event.setId(1L);
        Mockito.when(eventRepository.save(event)).thenReturn(event);

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                //.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON))
                .andExpect(jsonPath("id").value(Matchers.not(1L)))
                .andExpect(jsonPath("free").value(Matchers.not(false)));
    }

    @Test
    public void createEvent_BedRequest() throws Exception {
        Event event = Event.builder()
                .name("Spring")
                .description("REST PI Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 11, 11, 11))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 11, 11, 11))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 11, 11, 11))
                .endEventDateTime(LocalDateTime.of(2018, 11, 11, 11, 11))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역")
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        event.setId(1L);
        Mockito.when(eventRepository.save(event)).thenReturn(event);

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
        Event event = Event.builder()
                .name("Spring")
                .description("REST PI Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 11, 11, 11))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 11, 11, 11))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 11, 11, 11))
                .endEventDateTime(LocalDateTime.of(2018, 11, 11, 11, 11))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역")
                .build();

        EventDto eventDto = EventDto.builder().build();

        this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest());
    }


}
