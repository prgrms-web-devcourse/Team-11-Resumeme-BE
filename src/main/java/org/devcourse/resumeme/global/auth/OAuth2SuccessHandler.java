package org.devcourse.resumeme.global.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.domain.user.Role;
import org.devcourse.resumeme.domain.user.UserCommonInfo;
import org.devcourse.resumeme.global.advice.exception.CustomException;
import org.devcourse.resumeme.global.auth.model.Claims;
import org.devcourse.resumeme.global.auth.token.JwtService;
import org.devcourse.resumeme.service.MenteeService;
import org.devcourse.resumeme.service.MentorService;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;

    private final MentorService mentorService;

    private final MenteeService menteeService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        try {
            OAuth2CustomUser oAuth2CustomUser = (OAuth2CustomUser) authentication.getPrincipal();
            UserCommonInfo commonInfo = oAuth2CustomUser.getUserCommonInfo();
            String accessToken = jwtService.createAccessToken(Claims.of(commonInfo));
            String refreshToken = jwtService.createRefreshToken();

            if (commonInfo.role().equals(Role.ROLE_MENTEE)) {
                menteeService.updateRefreshToken(commonInfo.id(), refreshToken);
            } else {
                mentorService.updateRefreshToken(commonInfo.id(), refreshToken);
            }

            response.setCharacterEncoding("UTF-8");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);

        } catch (Exception e) {
            throw new CustomException("FAIL_OAUTH_LOGIN", "로그인에 실패했습니다.");
        }
    }

}
