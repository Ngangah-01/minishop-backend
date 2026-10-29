package io.httpaccesslog.service;


import io.httpaccesslog.entity.HttpAccessLog;
import io.httpaccesslog.repository.HttpAccessLogRepository;
import io.lib.exception.CommonRuntimeException;
import io.lib.exception.ExceptionType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class HttpAccessLogService   {
    private HttpAccessLogRepository httpAccessLogRepository;

    public HttpAccessLog preHandle(HttpServletRequest req){
        HttpAccessLog log = new HttpAccessLog();

        String ipAddress = req.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = req.getRemoteAddr();
        }
        log.setIpAddress(ipAddress);

        log.setUrl(req.getRequestURI());
        log.setRequestType(req.getMethod());

        log.setUserAgent(req.getHeader("user-agent"));
        log.setAppVersion(req.getHeader("Version"));

        List<String> headerKeys = Collections.list(req.getHeaderNames());
        log.setHeaders(String.join(", ", headerKeys));

        log.setAndroidSdkVersion(req.getHeader("Android-SDK-Version"));
        log.setAndroidVersionName(req.getHeader("Android-Version-Name"));
        log.setDeviceName(req.getHeader("Device-Name"));

        return httpAccessLogRepository.save(log);
    }

    public void afterCompletion(String entityId, HttpServletResponse response)  {
        this.afterCompletion(entityId, response, null);
    }

    public void afterCompletion(String entityId, HttpServletResponse response, String userEntityId)  {
        if(entityId == null){
            return;
        }

        HttpAccessLog log = findById(entityId);
        log.setCreatedByEntityId(userEntityId);
        log.setTimeOfResponse(LocalDateTime.now());
        log.setHttpResponseStatus(response.getStatus());
        log.setUserEntityId(userEntityId);
        httpAccessLogRepository.save(log);
    }

    public HttpAccessLog findById(String id){
        Optional<HttpAccessLog> opt = httpAccessLogRepository.findById(id);
        if(opt.isEmpty()){
            throw new CommonRuntimeException(
                ExceptionType.NOT_FOUND,
                "error.entity.not.found"
            );
        }
        return opt.get();
    }

    @Autowired
    public void setHttpAccessLogRepository(HttpAccessLogRepository httpAccessLogRepository) {
        this.httpAccessLogRepository = httpAccessLogRepository;
    }
}
