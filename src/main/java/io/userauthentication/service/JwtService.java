package io.userauthentication.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.lib.exception.CommonRuntimeException;
import io.lib.exception.ExceptionType;
import io.lib.exception.NotImplementedException;
import io.user.entity.User;
import io.user.entity.UserType;
import io.userauthentication.view.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
public class JwtService {
    private String jwtKeyInternalUser;
    private String jwtKeyCustomerUser;
    private Integer jwtValidityMins;

    private BCryptPasswordEncoder passwordEncoder;

    public <U extends User> Jwt generateJwt(U user) {
        String entityId = user.getEntityId();
        String jwtId = UUID.randomUUID().toString();
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(jwtValidityMins);
        String jwtToken = JWT.create()
                .withSubject(entityId)
                .withExpiresAt(expiryTime.toInstant(ZoneOffset.UTC))
                .withJWTId(jwtId)
                .sign(Algorithm.HMAC512(getJwtSecret(user.getUserType()).getBytes()));

        Jwt jwt = new Jwt();
        jwt.setJwtId(jwtId);
        jwt.setAuthToken(jwtToken);
        jwt.setExpiryTime(expiryTime);
        return jwt;
    }

    public DecodedJWT getDecodedJWT(String jwt, UserType userType) throws JWTVerificationException {
        return JWT.require(Algorithm.HMAC512(getJwtSecret(userType).getBytes()))
                .build()
                .verify(jwt.trim());
    }

    public void  validateJwtId(String tokenJwtId, String savedJwtId){
        if(!passwordEncoder.matches(tokenJwtId, savedJwtId)){
            throw new CommonRuntimeException(
                    ExceptionType.FORBIDDEN,
                    "error.invalid.auth"
            );
        }
    }

    private String getJwtSecret(UserType userType){
        return switch (userType){
            case INTERNAL_USER -> jwtKeyInternalUser;
            case CUSTOMER_USER -> jwtKeyCustomerUser;
            default -> throw new NotImplementedException();
        };
    }

    @Autowired
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Value("${jwt.key.internal.user}")
    public void setJwtKeyInternalUser(String jwtKeyInternalUser) {
        this.jwtKeyInternalUser = jwtKeyInternalUser;
    }

    @Value("${jwt.key.customer.user}")
    public void setJwtKeyCustomerUser(String jwtKeyCustomerUser) {
        this.jwtKeyCustomerUser = jwtKeyCustomerUser;
    }

    @Value("${jwt.validity.mins}")
    public void setJwtValidityMins(Integer jwtValidityMins) {
        this.jwtValidityMins = jwtValidityMins;
    }
}
