package org.devcourse.resumeme.business.resume.domain;

public enum Property {
    COMPANY,
    POSITION,
    SKILL,
    DUTY,
    DESCRIPTION,
    TITLE,
    START_DATE,
    END_DATE,
    LINK,
    AUTHORITY,
    LANGUAGE,
    EXAM_NAME,
    SCORE,
    URL,
    CONTENT,
    MEMBER,
    PROJECT,
    TYPE,
    TRAINING,
    MAJOR,
    DEGREE,
    MAX_SCORE;

    public String startDate() {
        return this.name() + START_DATE.name();
    }

    public String endDate() {
        return this.name() + END_DATE.name();
    }

}
