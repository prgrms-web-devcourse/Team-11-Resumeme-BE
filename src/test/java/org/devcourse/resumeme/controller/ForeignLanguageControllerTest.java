package org.devcourse.resumeme.controller;

import org.devcourse.resumeme.common.ControllerUnitTest;
import org.devcourse.resumeme.controller.dto.ForeignLanguageRequestDto;
import org.devcourse.resumeme.domain.mentee.Mentee;
import org.devcourse.resumeme.domain.mentee.RequiredInfo;
import org.devcourse.resumeme.domain.resume.ForeignLanguage;
import org.devcourse.resumeme.domain.resume.Resume;
import org.devcourse.resumeme.domain.user.Provider;
import org.devcourse.resumeme.domain.user.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Set;

import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentRequest;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ForeignLanguageControllerTest extends ControllerUnitTest {

    private Resume resume;

    private Mentee mentee;

    @BeforeEach
    void init() {
        mentee = Mentee.builder()
                .id(1L)
                .imageUrl("menteeimage.png")
                .provider(Provider.valueOf("KAKAO"))
                .email("backdong1@kakao.com")
                .refreshToken("ddefweferfrte")
                .requiredInfo(new RequiredInfo("김백둥", "백둥둥", "01022223722", Role.ROLE_MENTEE))
                .interestedPositions(Set.of())
                .interestedFields(Set.of())
                .introduce(null)
                .build();

        resume = new Resume("title", mentee);
    }

    @Test
    void 외국어_저장에_성공한다() throws Exception {
        ForeignLanguageRequestDto request = new ForeignLanguageRequestDto("English", "TOEIC", "900");
        Long resumeId = 1L;
        ForeignLanguage foreignLanguage = request.toEntity(resume);

        given(resumeService.getOne(resumeId)).willReturn(resume);
        given(foreignLanguageService.create(foreignLanguage)).willReturn(1L);

        ResultActions result = mvc.perform(post("/api/v1/resume/" + resumeId + "/foreign-languages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request))
        );

        result
                .andExpect(status().isOk())
                .andDo(
                        document("foreignLanguage/create",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                requestFields(
                                        fieldWithPath("language").type(STRING).description("언어"),
                                        fieldWithPath("examName").type(STRING).description("시험명"),
                                        fieldWithPath("scoreOrGrade").type(STRING).description("점수 또는 학점")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(NUMBER).description("생성된 외국어 정보 ID")
                                )
                        )
                );
    }

    @Test
    @WithMockUser
    void 외국어_조회에_성공한다() throws Exception {
        Long resumeId = 1L;
        ForeignLanguage foreignLanguage = new ForeignLanguage("영어", "토익", "990", resume);
        Resume savedResume = resume.builder()
                .foreignLanguage(List.of(foreignLanguage))
                .build();

        given(resumeService.getOne(resumeId)).willReturn(savedResume);

        ResultActions result = mvc.perform(get("/api/v1/resume/" + resumeId + "/foreign-languages"));

        result
                .andExpect(status().isOk())
                .andDo(
                        document("foreignLanguage/find",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                responseFields(
                                        fieldWithPath("[].language").type(STRING).description("언어"),
                                        fieldWithPath("[].examName").type(STRING).description("시험명"),
                                        fieldWithPath("[].scoreOrGrade").type(STRING).description("점수 또는 학점")
                                )
                        )
                );
    }

}

