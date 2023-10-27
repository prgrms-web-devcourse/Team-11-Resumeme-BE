package org.devcourse.resumeme.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devcourse.resumeme.controller.dto.RegisterInfo;
import org.devcourse.resumeme.domain.mentee.Mentee;
import org.devcourse.resumeme.domain.mentee.RequiredInfo;
import org.devcourse.resumeme.domain.user.Provider;
import org.devcourse.resumeme.global.advice.exception.CustomException;
import org.devcourse.resumeme.global.auth.model.Claims;
import org.devcourse.resumeme.global.auth.model.OAuth2TempInfo;
import org.devcourse.resumeme.global.auth.token.JwtService;
import org.devcourse.resumeme.repository.OAuth2InfoRedisRepository;
import org.devcourse.resumeme.service.MenteeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mentees")
public class MenteeController {

    private final OAuth2InfoRedisRepository oAuth2InfoRedisRepository;

    private final MenteeService menteeService;

    private final JwtService jwtService;

    @PostMapping
    public Map<String, String> register(@RequestBody RegisterInfo registerInfo) {

        log.debug("registerInfo.cacheKey = {}", registerInfo.cacheKey());
        OAuth2TempInfo oAuth2TempInfo = oAuth2InfoRedisRepository.findById(registerInfo.cacheKey())
                .orElseThrow(() -> new CustomException("REGISTER_FAIL", "회원가입에 실패했습니다."));

        RequiredInfo requiredInfo = registerInfo.requiredInfo();

        Mentee mentee = Mentee.builder()
                .email(oAuth2TempInfo.getEmail())
                .provider(Provider.of(oAuth2TempInfo.getProvider()))
                .imageUrl(oAuth2TempInfo.getImageUrl())
                .requiredInfo(
                        new RequiredInfo(requiredInfo.getRealName(), requiredInfo.getNickname(), requiredInfo.getPhoneNumber(), requiredInfo.getRole())
                ).build();

        Mentee savedMentee = menteeService.create(mentee);

        String accessToken = jwtService.createAccessToken(new Claims(savedMentee.getId(), savedMentee.getRequiredInfo().getRole().toString(), new Date()));
        String refreshToken = jwtService.createRefreshToken();

        menteeService.updateRefreshToken(savedMentee.getId(), refreshToken);

        return Map.of("access", accessToken, "refresh", refreshToken);
    }

}
