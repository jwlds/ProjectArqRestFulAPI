package com.puccampinas.projectarqrestfulapi.services.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.puccampinas.projectarqrestfulapi.domain.user.RefreshToken;
import com.puccampinas.projectarqrestfulapi.domain.user.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
@Log4j2
public class TokenService {
    static final String issuer = "MyApp";

    private long accessTokenExpirationMs;
    private long refreshTokenExpirationMs;

    private Algorithm accessTokenAlgorithm;
    private Algorithm refreshTokenAlgorithm;
    private JWTVerifier accessTokenVerifier;
    private JWTVerifier refreshTokenVerifier;

    public TokenService(@Value("${accessTokenSecret}") String accessTokenSecret, @Value("${refreshTokenSecret}") String refreshTokenSecret, @Value("${com.example.demo.refreshTokenExpirationDays}") int refreshTokenExpirationDays, @Value("${com.example.demo.accessTokenExpirationMinutes}") int accessTokenExpirationMinutes) {
        initializeTokenParameters(accessTokenSecret, refreshTokenSecret, refreshTokenExpirationDays, accessTokenExpirationMinutes);
    }

    public String generateAccessToken(User user) {
        return createToken(user.getId(), accessTokenExpirationMs, accessTokenAlgorithm);
    }

    public String generateRefreshToken(User user, RefreshToken refreshToken) {
        return createTokenWithClaim(user.getId(), refreshToken.getId(), refreshTokenExpirationMs, refreshTokenAlgorithm);
    }

    public boolean validateAccessToken(String token) {
        return isTokenValid(token, accessTokenVerifier);
    }

    public boolean validateRefreshToken(String token) {
        return isTokenValid(token, refreshTokenVerifier);
    }

    public String getUserIdFromAccessToken(String token) {
        return getUserIdFromToken(token, accessTokenVerifier);
    }

    public String getUserIdFromRefreshToken(String token) {
        return getUserIdFromToken(token, refreshTokenVerifier);
    }

    public String getTokenIdFromRefreshToken(String token) {
        return getClaimFromToken(token, "tokenId", refreshTokenVerifier);
    }

    private void initializeTokenParameters(String accessTokenSecret, String refreshTokenSecret, int refreshTokenExpirationDays, int accessTokenExpirationMinutes) {
        accessTokenExpirationMs = (long) accessTokenExpirationMinutes * 60 * 1000;
        refreshTokenExpirationMs = (long) refreshTokenExpirationDays * 24 * 60 * 60 * 1000;
        accessTokenAlgorithm = Algorithm.HMAC512(accessTokenSecret);
        refreshTokenAlgorithm = Algorithm.HMAC512(refreshTokenSecret);
        accessTokenVerifier = JWT.require(accessTokenAlgorithm)
                .withIssuer(issuer)
                .build();
        refreshTokenVerifier = JWT.require(refreshTokenAlgorithm)
                .withIssuer(issuer)
                .build();
    }

    private String createToken(String subject, long expirationMs, Algorithm algorithm) {
        return JWT.create()
                .withIssuer(issuer)
                .withSubject(subject)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(new Date().getTime() + expirationMs))
                .sign(algorithm);
    }

    private String createTokenWithClaim(String subject, String claimValue, long expirationMs, Algorithm algorithm) {
        return JWT.create()
                .withIssuer(issuer)
                .withSubject(subject)
                .withClaim("tokenId", claimValue)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date((new Date()).getTime() + expirationMs))
                .sign(algorithm);
    }

    private boolean isTokenValid(String token, JWTVerifier verifier) {
        return decodeToken(token, verifier).isPresent();
    }

    private String getUserIdFromToken(String token, JWTVerifier verifier) {
        return decodeToken(token, verifier).get().getSubject();
    }

    private String getClaimFromToken(String token, String claim, JWTVerifier verifier) {
        return decodeToken(token, verifier).get().getClaim(claim).asString();
    }

    private Optional<DecodedJWT> decodeToken(String token, JWTVerifier verifier) {
        try {
            return Optional.of(verifier.verify(token));
        } catch (JWTVerificationException e) {
            log.error("invalid token", e);
        }
        return Optional.empty();
    }
}
