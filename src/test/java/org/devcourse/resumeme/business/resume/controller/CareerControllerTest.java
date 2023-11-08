package org.devcourse.resumeme.business.resume.controller;

import org.devcourse.resumeme.business.resume.controller.career.dto.CareerCreateRequest;
import org.devcourse.resumeme.business.resume.domain.Resume;
import org.devcourse.resumeme.business.resume.domain.career.Career;
import org.devcourse.resumeme.business.resume.domain.career.Duty;
import org.devcourse.resumeme.business.resume.entity.Component;
import org.devcourse.resumeme.business.user.domain.Provider;
import org.devcourse.resumeme.business.user.domain.Role;
import org.devcourse.resumeme.business.user.domain.mentee.Mentee;
import org.devcourse.resumeme.business.user.domain.mentee.RequiredInfo;
import org.devcourse.resumeme.common.ControllerUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.devcourse.resumeme.common.util.ApiDocumentUtils.constraints;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentRequest;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CareerControllerTest extends ControllerUnitTest {

    private Resume resume;

    private Mentee mentee;

    @BeforeEach
    void init() {
        mentee = Mentee.builder()
                .id(1L)
                .imageUrl("image.png")
                .provider(Provider.valueOf("KAKAO"))
                .email("devcourse@naver.com")
                .refreshToken("fjiejrwoosdfsfsddfss")
                .requiredInfo(new RequiredInfo("이동호", "동동", "01022283833", Role.ROLE_MENTEE))
                .interestedPositions(Set.of())
                .interestedFields(Set.of())
                .introduce(null)
                .build();

        resume = new Resume("title", mentee);
    }

    @Test
    void 업무경험_저장에_성공한다() throws Exception {
        CareerCreateRequest request = new CareerCreateRequest("company name", "BACK", List.of("java", "spring"), List.of(new CareerCreateRequest.DutyRequest("title", "description", LocalDate.now(), LocalDate.now().plusYears(1L))), false, LocalDate.now(), LocalDate.now().plusYears(1L), "content");
        Long resumeId = 1L;

        given(resumeService.getOne(resumeId)).willReturn(resume);

        ResultActions result = mvc.perform(post("/api/v1/resume/" + resumeId + "/careers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("resume/career/create",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                requestFields(
                                        fieldWithPath("companyName").type(STRING).description("회사명"),
                                        fieldWithPath("position").type(STRING).description("포지션").optional(),
                                        fieldWithPath("skills[]").type(ARRAY).description("List of skills").optional(),
                                        fieldWithPath("duties[].title").type(STRING).description("제목"),
                                        fieldWithPath("duties[].description").type(STRING).description("설명").optional(),
                                        fieldWithPath("duties[].startDate").type(STRING).description("시작일"),
                                        fieldWithPath("duties[].endDate").type(STRING).description("종료일"),
                                        fieldWithPath("isCurrentlyEmployed").type(BOOLEAN).description("현재 근무 여부").attributes(constraints("false일 시 endDate 필수")),
                                        fieldWithPath("careerStartDate").type(STRING).description("경력 시작일"),
                                        fieldWithPath("endDate").type(STRING).description("종료일"),
                                        fieldWithPath("careerContent").type(STRING).description("경력 내용").optional()
                                ),
                                responseFields(
                                        fieldWithPath("id").type(NUMBER).description("생성된 경력 ID")
                                )
                        )
                );
    }

    @Test
    @WithMockUser
    void 업무경험_조회에_성공한다() throws Exception {
        // given
        Long resumeId = 1L;
        Career career = new Career("그렙", "백엔드", resumeId, List.of("자바", "스프링"),
                List.of(new Duty("로그인 기능 개발", LocalDate.of(2023, 11,1), LocalDate.of(2023,12,31), "소셜 로그인 개발")),
                LocalDate.of(2022, 10, 12), LocalDate.of(2023, 11, 1), "그렙 회사 다님");

        Component component = career.of(resumeId);
        Component career1 = new Component("CAREER", null, null, null, resumeId, List.of(component));
        given(blockService.getAll(resumeId)).willReturn(List.of(career1));

        // when
        ResultActions result = mvc.perform(get("/api/v1/resume/" + resumeId + "/careers"));

        // then
        result
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("resume/career/find",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                responseFields(
                                        fieldWithPath("[].companyName").type(STRING).description("회사명"),
                                        fieldWithPath("[].position").type(STRING).description("직책"),
                                        fieldWithPath("[].skills").type(ARRAY).description("기술 목록"),
                                        fieldWithPath("[].duties[].title").type(STRING).description("업무 제목"),
                                        fieldWithPath("[].duties[].startDate").type(STRING).description("업무 시작일"),
                                        fieldWithPath("[].duties[].endDate").type(STRING).description("업무 종료일"),
                                        fieldWithPath("[].duties[].description").type(STRING).description("업무 설명"),
                                        fieldWithPath("[].isCurrentlyEmployed").type(BOOLEAN).description("현재 재직 상태"),
                                        fieldWithPath("[].careerStartDate").type(STRING).description("경력 시작일"),
                                        fieldWithPath("[].endDate").type(STRING).description("경력 종료일"),
                                        fieldWithPath("[].careerContent").type(STRING).description("경력 상세 내용")
                                )
                        ));

    }

}
