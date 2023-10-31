package org.devcourse.resumeme.domain.resume;

import org.devcourse.resumeme.domain.mentee.Mentee;
import org.devcourse.resumeme.global.advice.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class TrainingTest {

    private Resume resume;

    @BeforeEach
    void init() {
        resume = new Resume("title", Mentee.builder()
                .interestedPositions(Set.of("BACK"))
                .interestedFields(Set.of("FINANCE"))
                .build());
    }

    @Test
    void 입학일자는_졸업일자_보다_먼저여야_한다() {
        assertThatThrownBy(() -> new Training("School", "Major", "Degree",
                LocalDate.now(), LocalDate.now().minusDays(1), 3.5, 4.0, "Explanation", resume))
                .isInstanceOf(CustomException.class);
    }

    @Test
    void 최대학점은_내_학점보다_커야한다() {
        double gpa = 5.5;
        double maxGpa = 4.0;

        assertThatThrownBy(() -> new Training("School", "Major", "Degree",
                LocalDate.now(), LocalDate.now().plusDays(1), gpa, maxGpa, "Explanation", resume))
                .isInstanceOf(CustomException.class);
    }

}
