package com.darkcloudn.websocket.config.security;

import com.darkcloudn.common.constant.CommonConstant;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.impl.TextCodec;
import java.security.SecureRandom;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtProvider.class);

    @Value("${darkcloudn.jwtSecret}")
    private String jwtSecret;


    @Value("${darkcloudn.jwtExpiration}")
    private long jwtExpiration;

    @Autowired
    private Gson gson;

    public String generateJwtToken(JwtSubject jwtSubject) {
        String sub = gson.toJson(jwtSubject);
        return Jwts.builder()
                .setSubject(sub)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpiration * 1000))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    public String generateJwtToken(SignatureAlgorithm alg, String secret, long exp, String iss,
            int ivLength) {
        byte[] ivData = SecureRandom.getSeed(ivLength);
        String jti = Base64.encodeBase64String(ivData);

        Date now = new Date();
        return Jwts.builder()
                .setId(jti)
                .setIssuer(iss)
                .setIssuedAt(now)
                .setNotBefore(now)
                .setExpiration(new Date(now.getTime() + exp * 1000))
                .signWith(alg, Base64.encodeBase64String(secret.getBytes()))
                .compact();
    }

    public boolean validateJwtToken(String token) {
        return validateJwtToken(token, jwtSecret);
    }

    public boolean validateJwtToken(String token, String secret) {
        return this.validateJwtToken(token, TextCodec.BASE64.decode(secret));
    }

    public boolean validateJwtToken(String token, byte[] secret) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            LOGGER.error("Invalid JWT signature -> Message: {} ", e.getMessage());
            LOGGER.debug("Invalid JWT signature ", e);
        } catch (MalformedJwtException e) {
            LOGGER.error("Invalid JWT token -> Message: {}", e.getMessage());
            LOGGER.debug("Invalid JWT token ", e);
        } catch (ExpiredJwtException e) {
            LOGGER.error("Expired JWT token -> Message: {}", e.getMessage());
            LOGGER.debug("Expired JWT token ", e);
        } catch (UnsupportedJwtException e) {
            LOGGER.error("Unsupported JWT token -> Message: {}", e.getMessage());
            LOGGER.debug("Unsupported JWT token ", e);
        } catch (IllegalArgumentException e) {
            LOGGER.error("JWT claims string is empty -> Message: {}", e.getMessage());
            LOGGER.debug("JWT claims string is empty ", e);
        } catch (Exception e) {
            LOGGER.error("Validate Jwt token error -> Message: {}", e.getMessage());
            LOGGER.debug("Validate Jwt token error: ", e);
        }

        return false;
    }

    public JwtSubject getSubjectFromJwtToken(String token) {
        try {
            String sub = this.getBodyFromJwtToken(token, jwtSecret).getSubject();
            JwtSubject jwtSub = gson.fromJson(sub, JwtSubject.class);
            return jwtSub;
        } catch (Exception e) {
            LOGGER.error("Unsupported JWT token, trying for customer portal");
        }
        return null;
    }

    public Claims getBodyFromJwtToken(String token, String secret) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateJwtToken(String token, byte[] secret, HttpServletRequest request) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            LOGGER.error("Validate Jwt token error -> Message: {}", e.getMessage());
            LOGGER.debug("Validate Jwt token error: ", e);
            if (request != null && e instanceof ExpiredJwtException) {
                request.setAttribute(CommonConstant.AUTH_ERROR_TYPE,
                        CommonConstant.AUTH_ERROR_TOKEN_EXPIRE);
            }
        }

        return false;
    }

    public boolean validateJwtToken(String token, HttpServletRequest request) {
        return validateJwtToken(token, TextCodec.BASE64.decode(jwtSecret), request);
    }

}
