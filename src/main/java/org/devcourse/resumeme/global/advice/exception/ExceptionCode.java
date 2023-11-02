package org.devcourse.resumeme.global.advice.exception;

import org.devcourse.resumeme.common.domain.DocsEnumType;

public enum ExceptionCode implements DocsEnumType {

    MENTEE_NOT_FOUND("해당 멘티를 찾을 수 없습니다"),
    RESUME_NOT_FOUND("해당 이력서를 찾을 수 없습니다"),
    EVENT_NOT_FOUND("이벤트를 찾을 수 없습니다"),
    PROJECT_NOT_FOUND("해당 프로젝트를 찾을 수 없습니다"),
    APPLICANT_NOT_FOUND("신청 이력이 없습니다"),
    NO_EMPTY_VALUE("빈 값일 수 없습니다"),
    MENTEE_ONLY_RESUME("멘티만 이력서를 작성할 수 있습니다"),
    ROLE_NOT_ALLOWED("허용되지 않은 역할입니다"),
    MENTOR_ALREADY_APPROVED("이미 승인된 멘토입니다"),
    DUPLICATE_APPLICATION_EVENT("이미 신청한 이력이 있습니다"),
    NOT_OPEN_TIME("예약한 오픈 시간이 아닙니다"),
    APPLICATION_NOT_FOUND("예약 이력을 찾을 수 없습니다"),
    RANGE_MAXIMUM_ATTENDEE("참여 인원 수를 2~10명 사이에서 정해주세요"),
    NO_REMAIN_SEATS("이미 모든 신청이 마감되었습니다"),
    NO_AVAILABLE_SEATS("잔여 자리가 없어서 재 오픈이 불가능합니다"),
    CANNOT_OPEN_EVENT("예약한 이벤트에 한에서만 오픈 신청을 할 수 있습니다"),
    TIME_ERROR("시간 순서를 다시 확인해주세요"),
    CAN_NOT_RESERVATION("현재 시간보다 이전 시간으로는 예약할 수 없습니다"),
    DUPLICATED_EVENT_OPEN("이미 호픈된 이벤트가 있습니다")
    ;

    private final String message;

    ExceptionCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String getType() {
        return name();
    }

    @Override
    public String getDescription() {
        return message;
    }

}
