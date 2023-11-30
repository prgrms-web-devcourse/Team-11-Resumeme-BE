package org.devcourse.resumeme.business.user.controller.mentee;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devcourse.resumeme.business.user.controller.mentee.dto.MenteeInfoResponse;
import org.devcourse.resumeme.business.user.controller.mentee.dto.MenteeInfoUpdateRequest;
import org.devcourse.resumeme.business.user.controller.mentee.dto.MenteeRegisterInfoRequest;
import org.devcourse.resumeme.business.user.domain.mentee.Mentee;
import org.devcourse.resumeme.business.user.entity.User;
import org.devcourse.resumeme.business.user.entity.UserService;
import org.devcourse.resumeme.business.user.service.AccountService;
import org.devcourse.resumeme.business.user.service.vo.RegisterAccountVo;
import org.devcourse.resumeme.common.response.IdResponse;
import org.devcourse.resumeme.global.auth.model.jwt.Claims;
import org.devcourse.resumeme.global.auth.model.login.OAuth2TempInfo;
import org.devcourse.resumeme.global.auth.service.jwt.Token;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.devcourse.resumeme.global.auth.service.jwt.Token.ACCESS_TOKEN_NAME;
import static org.devcourse.resumeme.global.auth.service.jwt.Token.REFRESH_TOKEN_NAME;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mentees")
public class MenteeController {

    private final UserService userService;

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<Void> register(@RequestBody MenteeRegisterInfoRequest registerInfoRequest) {
        String cacheKey = registerInfoRequest.cacheKey();
        log.info("registerInfoRequest.cacheKey = {}", cacheKey);

        OAuth2TempInfo oAuth2TempInfo = accountService.getTempInfo(cacheKey);
        Mentee mentee = registerInfoRequest.toEntity(oAuth2TempInfo);
        User user = userService.create(mentee.from());
        Mentee savedMentee = Mentee.of(user);

        Token token = getToken(cacheKey, savedMentee);
        userService.updateRefreshToken(savedMentee.getId(), token.refreshToken());

        return ResponseEntity.status(200)
                .header(ACCESS_TOKEN_NAME, token.accessToken())
                .header(REFRESH_TOKEN_NAME, token.refreshToken())
                .build();
    }

    private Token getToken(String cacheKey, Mentee savedMentee) {
        RegisterAccountVo accountVo = new RegisterAccountVo(cacheKey, Claims.of(savedMentee));

        return accountService.registerAccount(accountVo);
    }

    @PatchMapping("/{menteeId}")
    public IdResponse update(@PathVariable Long menteeId, @RequestBody MenteeInfoUpdateRequest updateRequest) {
        Long updatedMenteeId = userService.update(menteeId, updateRequest);

        return new IdResponse(updatedMenteeId);
    }

    @GetMapping("/{menteeId}")
    public MenteeInfoResponse getOne(@PathVariable Long menteeId) {
        User user = userService.getOne(menteeId);
        Mentee findMentee = Mentee.of(user);

        return new MenteeInfoResponse(findMentee);
    }

}
