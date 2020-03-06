package com.study.restapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.restapi.common.RestDocsConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-events").exists())
                .andExpect(jsonPath("_links.update-event").exists())

                //RestDocs
                .andDo(document("create-event",   //문서의 이름
                        links(  //링크 목록
                                linkWithRel("self").description("셀프 링크"),
                                linkWithRel("query-events").description("쿼리 이벤트 링크"),
                                linkWithRel("update-event").description("업데이트 이벤트 링크")
                        ),
                        requestHeaders(  //요청 헤더
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        requestFields( //요청 필드
                                fieldWithPath("name").description("이벤트 이름"),
                                fieldWithPath("description").description("이벤트 설명"),
                                fieldWithPath("beginEnrollmentDateTime").description("이벤트 등록 시작일"),
                                fieldWithPath("closeEnrollmentDateTime").description("이벤트 등록 종료일"),
                                fieldWithPath("beginEventDateTime").description("이벤트 시작일"),
                                fieldWithPath("endEventDateTime").description("이벤트 종료일"),
                                fieldWithPath("location").description("이벤트 장소"),
                                fieldWithPath("basePrice").description("기본 가격"),
                                fieldWithPath("maxPrice").description("최대 가격"),
                                fieldWithPath("limitOfEnrollment").description("등록 제한")
                        )
                        ,responseHeaders( //응답 헤더
                                headerWithName(HttpHeaders.LOCATION).description("location header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        // relaxedResponseField 장점 : 문서 일부분만 테스트 할 수 있다 / 단점 : 정확한 문서를 생성하지 못한다.
                        responseFields( //응답 필드
                                fieldWithPath("id").description("이벤트 PK"),
                                fieldWithPath("name").description("이벤트 이름"),
                                fieldWithPath("description").description("이벤트 설명"),
                                fieldWithPath("beginEnrollmentDateTime").description("이벤트 등록 시작일"),
                                fieldWithPath("closeEnrollmentDateTime").description("이벤트 등록 종료일"),
                                fieldWithPath("beginEventDateTime").description("이벤트 시작일"),
                                fieldWithPath("endEventDateTime").description("이벤트 종료일"),
                                fieldWithPath("location").description("이벤트 장소"),
                                fieldWithPath("basePrice").description("기본 가격"),
                                fieldWithPath("maxPrice").description("최대 가격"),
                                fieldWithPath("limitOfEnrollment").description("등록 제한"),
                                fieldWithPath("free").description("무료"),
                                fieldWithPath("offline").description("오프라인"),
                                fieldWithPath("eventStatus").description("이벤트 상태"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.query-events.href").description("link to query event list"),
                                fieldWithPath("_links.update-event.href").description("link to update existing event")
                        )

                ))
               ;
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
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST PI Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 16, 11, 11))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 17, 11, 11))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 15, 11, 11))
                .endEventDateTime(LocalDateTime.of(2018, 11, 16, 11, 11))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역")
                .build();

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].objectName").exists())
                .andExpect(jsonPath("$[1].defaultMessage").exists())
                .andExpect(jsonPath("$[0].code").exists());
    }

}
