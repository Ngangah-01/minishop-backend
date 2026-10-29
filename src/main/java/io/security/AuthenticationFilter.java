package io.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.httpaccesslog.entity.HttpAccessLog;
import io.httpaccesslog.service.HttpAccessLogService;
import io.lib.exception.CommonRuntimeException;
import io.lib.exception.ExceptionType;
import io.lib.service.SystemConfig;
import io.user.entity.User;
import io.user.entity.UserStatus;
import io.user.entity.UserType;
import io.user.service.UserAuthService;
import io.userauthentication.service.IntegrationAuthenticationService;
import io.userauthentication.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.crypto.Cipher;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

class AuthenticationFilter<U extends User> extends BasicAuthenticationFilter {
    private final HttpAccessLogService httpAccessLogService;
    private final JwtService jwtService;
    private final Map<UserType, UserAuthService<U, ?>> userServiceMap;

    AuthenticationFilter(
            JwtService jwtService,
            Map<UserType, UserAuthService<U, ?>> userServiceMap,
            HttpAccessLogService httpAccessLogService
    ) {
        super(authentication -> authentication);
        this.jwtService = jwtService;
        this.userServiceMap = userServiceMap;
        this.httpAccessLogService = httpAccessLogService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        HttpAccessLog accessLog = httpAccessLogService.preHandle(req);
        req.setAttribute(HttpAccessLog.LOG_ID, accessLog.getId());

        String authToken = req.getHeader(HttpHeaders.AUTHORIZATION);
        UserType userType = getUserType(req.getServletPath());
        String userEntityId = null;

        if (StringUtils.isNotBlank(authToken) && userType != null) {
            try {
                var  auth = authenticateWithBearerToken(userType, authToken);
                SecurityContextHolder.getContext().setAuthentication(auth);
                userEntityId = auth.getName();
            } catch (JWTVerificationException | CommonRuntimeException ignore) {
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        chain.doFilter(req, res);
        httpAccessLogService.afterCompletion((String) req.getAttribute(HttpAccessLog.LOG_ID), res, userEntityId);
    }

    private UserType getUserType(String url) {
        UserType userType = null;
        if (StringUtils.startsWithIgnoreCase(url, SystemConfig.INTERNAL_USER_BASE_URL)) {
            userType = UserType.INTERNAL_USER;
        } else if (StringUtils.startsWithIgnoreCase(url, SystemConfig.CUSTOMER_USER_BASE_URL)) {
            userType = UserType.CUSTOMER_USER;
        }
        return userType;
    }

    private UsernamePasswordAuthenticationToken authenticateWithBearerToken(UserType userType, String authToken) {
        U user;

        if (StringUtils.startsWithIgnoreCase(authToken, "Signature")) {
            user = validateSignature(authToken, userType);
        } else {
            user = authenticateBearerToken(authToken, userType);
        }

        if (user.getUserStatus() != UserStatus.ACTIVE) {
            throw new CommonRuntimeException(
                ExceptionType.UNAUTHORIZED,
                "error.user.not.active"
            );
        }

        UserAuthService<U, ?> userAuthService = userServiceMap.get(userType);
        List<String> authorities = new ArrayList<>(userAuthService.getUserPermissions(user));
        authorities.add(userType.toString());

        return new UsernamePasswordAuthenticationToken(
            user.getEntityId(),
            null,
            authorities.stream().map(SimpleGrantedAuthority::new).toList()
        );
    }

    private U authenticateBearerToken(String authToken, UserType userType) {
        DecodedJWT decodedJWT = jwtService.getDecodedJWT(authToken, userType);
        UserAuthService<U, ?> userAuthService = userServiceMap.get(userType);
        U user = userAuthService.findByEntityId(decodedJWT.getSubject());
        jwtService.validateJwtId(decodedJWT.getId(), user.getRecentAuthId());

        return user;
    }

    private U validateSignature(String authToken, UserType userType) {
        String token = StringUtils.substringAfter(authToken, "Signature ");
        token = token.trim();
        String[] tokenParts = token.split(":");

        if (tokenParts.length != 2) {
            throw new CommonRuntimeException(
                ExceptionType.FORBIDDEN,
                "error.invalid.auth"
            );
        }

        String userId = tokenParts[0];
        String signature = tokenParts[1];

        UserAuthService<U, ?> userAuthService = userServiceMap.get(userType);
        U user = userAuthService.findByEntityId(userId);

        try {
            decrypt(signature, user.getPublicKey());
        }catch (Exception e){
            throw new CommonRuntimeException(
                ExceptionType.FORBIDDEN,
                "error.invalid.auth"
            );
        }
        return user;
    }

    private void decrypt(String encryptedText, String publicKeyStr) throws Exception{
        PublicKey publicKey = loadPublicKey(publicKeyStr);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        cipher.doFinal(Base64.getDecoder().decode(encryptedText));
    }

    private PublicKey loadPublicKey(String keyBase64Str) throws Exception{
        byte[] keyBytes = Base64.getDecoder().decode(keyBase64Str);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        return KeyFactory.getInstance("RSA").generatePublic(spec);
    }

    private UsernamePasswordAuthenticationToken authenticateErpApiUser(
            IntegrationAuthenticationService authService,
            UserType userType,
            String apiKey) {
        boolean isValid = authService.isValid(apiKey);

        if (!isValid) {
            throw new CommonRuntimeException(
                    ExceptionType.UNAUTHORIZED,
                    ""
            );
        }

        return new UsernamePasswordAuthenticationToken(
                userType.toString(),
                null,
                Stream.of(userType.toString()).map(SimpleGrantedAuthority::new).toList()
        );
    }
}