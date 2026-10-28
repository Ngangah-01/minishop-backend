package io.security;

import io.httpaccesslog.entity.HttpAccessLog;
import io.httpaccesslog.service.HttpAccessLogService;
import io.lib.service.Message;
import io.lib.view.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Locale;

@Component
public class AuthenticationExceptionHandler implements AuthenticationEntryPoint {
    private HttpAccessLogService httpAccessLogService;
    private ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest req,
                         HttpServletResponse res,
                         AuthenticationException authEx)
            throws IOException {
        String languageCode = req.getHeader("Accept-Language");
        if(languageCode == null){
            languageCode = "en";
        }
        Locale locale = new Locale(languageCode);
        String msg;
        int statusCode = res.getStatus();
        switch (res.getStatus()){
            case HttpServletResponse.SC_UNAUTHORIZED:
                msg = Message.get("error.invalid.auth", locale);
                break;
            case HttpServletResponse.SC_FORBIDDEN:
                msg = Message.get("error.operation.not.allowed", locale);
                break;
            case HttpServletResponse.SC_NOT_FOUND:
                msg = Message.get("error.entity.not.found", locale);
                break;
            case HttpServletResponse.SC_METHOD_NOT_ALLOWED:
                msg = Message.get("error.method.not.allowed", locale);
                break;
            case HttpServletResponse.SC_INTERNAL_SERVER_ERROR:
                msg = Message.get("error.internal.server.error", locale);
                break;
            default:
                msg = authEx.getLocalizedMessage();
                statusCode = HttpServletResponse.SC_UNAUTHORIZED;
        }

        ApiResponse apiResponse = new ApiResponse(
                false,
                statusCode,
                msg
        );

        apiResponse.setTime(LocalDateTime.now());

        String responseMsg = objectMapper.writeValueAsString(apiResponse);
        res.setStatus(statusCode);
        res.getWriter().write(responseMsg);

        httpAccessLogService.afterCompletion((String) req.getAttribute(HttpAccessLog.LOG_ID), res);
    }

    @Autowired
    public void setHttpAccessLogService(HttpAccessLogService httpAccessLogService) {
        this.httpAccessLogService = httpAccessLogService;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
