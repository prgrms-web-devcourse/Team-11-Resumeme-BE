package org.devcourse.resumeme.business.resume.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.common.util.Validator;
import org.devcourse.resumeme.global.exception.ExceptionCode;

import static org.devcourse.resumeme.common.util.Validator.check;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ForeignLanguage {

    @Id
    @Getter
    @GeneratedValue
    @Column(name = "foreign_language_id")
    private Long id;

    @Getter
    @ManyToOne
    @JoinColumn(name = "resume_id")
    private Resume resume;

    @Getter
    private String language;

    @Getter
    private String examName;

    @Getter
    private String scoreOrGrade;

    public ForeignLanguage(String language, String examName, String scoreOrGrade, Resume resume) {
        Validator.check(language == null, ExceptionCode.NO_EMPTY_VALUE);
        Validator.check(examName == null, ExceptionCode.NO_EMPTY_VALUE);
        Validator.check(scoreOrGrade == null, ExceptionCode.NO_EMPTY_VALUE);

        this.resume = resume;
        this.language = language;
        this.examName = examName;
        this.scoreOrGrade = scoreOrGrade;
    }

}