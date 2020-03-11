package com.study.restapi.events;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder @AllArgsConstructor @NoArgsConstructor @ToString
@Getter @Setter @EqualsAndHashCode(of = "id")
@Entity
public class Event {

    @Id @GeneratedValue
    private Long id;
    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location; // (optional) 이게 없으면 온라인 모임
    private int basePrice; // (optional)
    private int maxPrice; // (optional)
    private int limitOfEnrollment;
    private boolean offline;
    private boolean free;
    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus = EventStatus.DRAFT;

    @ManyToOne
    // 간단한 비즈니스로직은 도메인에서 처리하는 것도 나쁘지 않다.
    // 또는 서비스에 위임해서 분리하도록 하는 것이 좋다.
    public void update() {
        // Update free
        if (this.basePrice == 0  && this.maxPrice == 0) {
            this.free =true;
        } else {
            this.free = false;
        }
        // isBlank는 자바 11에서 추가됨
        // Update offline
        if (this.location == null || this.location.isEmpty()) {
            this.offline = false;
        } else {
            this.offline = true;
        }
    }
}