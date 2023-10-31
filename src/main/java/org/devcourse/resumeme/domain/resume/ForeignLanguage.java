package org.devcourse.resumeme.domain.resume;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.global.advice.exception.ExceptionCode;

import static org.devcourse.resumeme.common.util.Validator.validate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ForeignLanguage {

    @Id
    @Getter
    @GeneratedValue
    @Column(name = "foreign_language_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "resume_id")
    private Resume resume;

    private String language;

    private String examName;

    private String scoreOrGrade;

    public ForeignLanguage(String language, String examName, String scoreOrGrade, Resume resume) {
        validate(language == null, ExceptionCode.NO_EMPTY_VALUE);
        validate(examName == null, ExceptionCode.NO_EMPTY_VALUE);
        validate(scoreOrGrade == null, ExceptionCode.NO_EMPTY_VALUE);

        this.resume = resume;
        this.language = language;
        this.examName = examName;
        this.scoreOrGrade = scoreOrGrade;
    }

}
