package org.devcourse.resumeme.domain.mentee;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.common.domain.Position;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static org.devcourse.resumeme.common.util.Validator.validate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenteePosition {

    @Id
    @GeneratedValue
    @Column(name = "mentee_position_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "mentee_id")
    private Mentee mentee;

    @Enumerated(STRING)
    private Position position;

    public MenteePosition(Mentee mentee, Position position) {
        validate(position == null, "NO_EMPTY_VALUE", "포지션은 필수 값입니다");
        validate(mentee == null, "NO_EMPTY_VALUE", "사용자는 필수 값입니다");

        this.position = position;
        this.mentee = mentee;
    }

}