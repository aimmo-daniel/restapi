package com.study.restapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Transactional
    @Test
    @Description("정상적으로 이벤트를 생성하는 테스트")
    public void createEvent() throws Exception {
        EventDto event = EventDto.builder()
                .name("Spring")
                .description("REST PI Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 10, 11, 11, 11))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 10, 12, 11, 11))
                .beginEventDateTime(LocalDateTime.of(2018, 10, 13, 11, 11))
                .endEventDateTime(LocalDateTime.of(2018, 10, 14, 11, 11))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역")
                .build();

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string("Content-Type", "application/hal+json"))
                .andExpect(jsonPath("id").value(Matchers.not(1L)))
                .andExpect(jsonPath("free").value(Matchers.not(true)));
    }

    @Test
    @Description("입력 받을 수 없는 값을 사용한 경우에 에러가 발생하는 테스트")
    public void createEvent_BadRequest() throws Exception {
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

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Description("입력 값이 비어있는 경우에 에러가 발생하는 테스트")
    public void createEvent_BadRequest_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Description("입력 값이 잘못된 경우에 에러가 발생하는 테스트")
    public void createEvent_BadRequest_Wrong_Input() throws Exception {
        Event event = Event.builder()
                .name("Spring")
                .description("REST PI Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 16, 11, 11))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 15, 11, 11))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 17, 11, 11))
                .endEventDateTime(LocalDateTime.of(2018, 11, 16, 11, 11))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역")
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isBadRequest());
    }

}
