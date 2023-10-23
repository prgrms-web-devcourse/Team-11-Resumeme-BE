package org.devcourse.resumeme.domain.event;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.common.domain.Position;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;
import static org.devcourse.resumeme.common.util.Validator.validate;

@Entity
@NoArgsConstructor(access = PROTECTED)
public class EventPosition {

    @Id
    @GeneratedValue
    @Column(name = "event_position_id")
    private Long id;

    @Enumerated(STRING)
    private Position position;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    public EventPosition(Position position, Event event) {
        validate(position == null, "NO_EMPTY_VALUE", "포지션은 필수 값입니다");
        validate(event == null, "NO_EMPTY_VALUE", "이벤트는 필수 값입니다");

        this.position = position;
        this.event = event;
    }

}