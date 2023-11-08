package org.devcourse.resumeme.business.comment.controller;

import org.devcourse.resumeme.common.ControllerUnitTest;
import org.devcourse.resumeme.business.comment.controller.dto.CommentCreateRequest;
import org.devcourse.resumeme.business.comment.domain.Comment;
import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.event.domain.EventInfo;
import org.devcourse.resumeme.business.event.domain.EventTimeInfo;
import org.devcourse.resumeme.business.user.domain.mentee.Mentee;
import org.devcourse.resumeme.business.user.domain.mentee.RequiredInfo;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;
import org.devcourse.resumeme.business.resume.domain.BlockType;
import org.devcourse.resumeme.business.resume.domain.Resume;
import org.devcourse.resumeme.business.user.domain.Provider;
import org.devcourse.resumeme.business.user.domain.Role;
import org.devcourse.resumeme.common.support.WithMockCustomUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.devcourse.resumeme.common.util.DocumentLinkGenerator.DocUrl.BLOCK_TYPE;
import static org.devcourse.resumeme.common.util.DocumentLinkGenerator.generateLinkCode;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentRequest;
import static org.devcourse.resumeme.common.util.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommentControllerTest extends ControllerUnitTest {

    private Mentor mentor;

    private Mentee mentee;

    @BeforeEach
    void setUp() {
        mentor =  Mentor.builder()
                .id(1L)
                .imageUrl("profile.png")
                .provider(Provider.valueOf("GOOGLE"))
                .email("progrers33@gmail.com")
                .refreshToken("redididkeeeeegg")
                .requiredInfo(new RequiredInfo("김주승", "주승멘토", "01022332375", Role.ROLE_MENTOR))
                .experiencedPositions(Set.of("FRONT"))
                .careerContent("금융회사 다님")
                .careerYear(3)
                .build();

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
    }

    @Test
    @WithMockCustomUser
    void 리뷰_생성에_성공한다() throws Exception {
        // given
        CommentCreateRequest request = new CommentCreateRequest("이력서가 맘에 안들어요", "ACTIVITY");
        Long resumeId = 1L;
        Resume resume = new Resume("titlem", mentee);

        Comment comment = request.toEntity(resume);
        setId(comment, 1L);

        given(resumeService.getOne(resumeId)).willReturn(resume);
        given(reviewService.create(any(Comment.class))).willReturn(comment);

        // when
        ResultActions result = mvc.perform(post("/api/v1/events/{eventId}/resume/{resumeId}/comments", 1L, resumeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)));

        // then
        result
                .andExpect(status().isOk())
                .andDo(
                        document("comment/create",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                requestFields(
                                        fieldWithPath("content").type(STRING).description("리뷰 내용"),
                                        fieldWithPath("blockType").type(STRING).description("이력서 각 블럭 타입")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(NUMBER).description("리뷰 아이디"),
                                        fieldWithPath("content").type(STRING).description("리뷰 내용"),
                                        fieldWithPath("blockType").type(STRING).description(generateLinkCode(BLOCK_TYPE))
                                )
                        )
                );
    }

    @Test
    void 이력서에_작성된_전체_리뷰를_조회한다() throws Exception {
        // given
        long eventId = 1L;
        long resumeId = 1L;

        EventInfo openEvent = EventInfo.open(3, "제목", "내용");
        EventTimeInfo eventTimeInfo = EventTimeInfo.onStart(LocalDateTime.now(), LocalDateTime.now().plusHours(1L), LocalDateTime.now().plusHours(2L));
        Event event = new Event(openEvent, eventTimeInfo, mentor, List.of());
        event.acceptMentee(1L, 1L);

        given(eventService.getOne(eventId)).willReturn(event);
        Comment comment = new Comment("리뷰 내용내용", BlockType.CAREER, new Resume("title", mentee));
        setId(comment, 1L);

        given(reviewService.getAllWithResumeId(resumeId)).willReturn(List.of(comment));

        // when
        ResultActions result = mvc.perform(get("/api/v1/events/{eventId}/resume/{resumeId}/comments", eventId, resumeId));

        // then
        result.andExpect(status().isOk())
                .andDo(
                        document("comment/getAll",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                pathParameters(
                                        parameterWithName("eventId").description("참여한 이벤트 아이디"),
                                        parameterWithName("resumeId").description("이벤트에 참여 시 사용한 이력서 아이디")
                                ),
                                responseFields(
                                        subsectionWithPath("*").description(generateLinkCode(BLOCK_TYPE)),
                                        fieldWithPath("*.id").type(NUMBER).description("리뷰 아이디"),
                                        fieldWithPath("*.content").type(STRING).description("리뷰 내용"),
                                        fieldWithPath("*.blockType").type(STRING).description(generateLinkCode(BLOCK_TYPE))
                                )

                        )
                );
    }

}