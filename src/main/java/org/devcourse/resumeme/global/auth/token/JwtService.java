package org.devcourse.resumeme.global.auth.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.global.auth.model.Claims;
import org.devcourse.resumeme.repository.MenteeRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final MenteeRepository menteeRepository;

    private static final String ACCESS_TOKEN_NAME = "access";

    private static final String REFRESH_TOKEN_NAME = "refresh";

    private static final int ACCESS_TOKEN_EXP = 3600 * 1000;

    private static final int REFRESH_TOKEN_EXP = 3600 * 24 * 7 * 1000;

    private static final String ID = "id";

    private static final String ROLE = "role";

    private static final String BEARER = "Bearer ";

    private static final String SECRET_KEY = "resumeJWT";

    public String createAccessToken(Claims claims) {
        return JWT.create()
                .withSubject(ACCESS_TOKEN_NAME)
                .withExpiresAt(new Date(claims.expiration().getTime() + ACCESS_TOKEN_EXP))
                .withClaim(ID, claims.id())
                .withClaim(ROLE, claims.role())
                .sign(Algorithm.HMAC512(SECRET_KEY));

    }

    public String createRefreshToken() {
        Date now = new Date();
        return JWT.create()
                .withSubject(REFRESH_TOKEN_NAME)
                .withExpiresAt(new Date(now.getTime() + REFRESH_TOKEN_EXP))
                .sign(Algorithm.HMAC512(SECRET_KEY));
    }

    public void sendAccessToken(HttpServletResponse response, String accessToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader(ACCESS_TOKEN_NAME, accessToken);
    }

    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        setAccessTokenHeader(response, accessToken);
        setRefreshTokenHeader(response, refreshToken);
    }

    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(REFRESH_TOKEN_NAME))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(ACCESS_TOKEN_NAME))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(ACCESS_TOKEN_NAME, accessToken);
    }

    public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
        response.setHeader(REFRESH_TOKEN_NAME, refreshToken);
    }

}
