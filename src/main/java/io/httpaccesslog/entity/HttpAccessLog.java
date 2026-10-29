package io.httpaccesslog.entity;

import io.lib.entity.BaseMongoEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@Getter
@Setter
public class HttpAccessLog extends BaseMongoEntity {
    public static final String LOG_ID = "logId";

    private String ipAddress;
    private String url;
    private String requestType;
    private String userAgent;
    private String appVersion;

    private String headers;

    private String androidSdkVersion;
    private String androidVersionName;
    private String deviceName;

    private String userEntityId;
    private Integer httpResponseStatus;
    private LocalDateTime timeOfResponse;
}
