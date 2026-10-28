package io.lib.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Getter
@Setter
public abstract class BaseMongoEntity {
    @Id
    private String id;

    private LocalDateTime createdAt = LocalDateTime.now();
    private String createdByEntityId;

    private LocalDateTime updateAt = LocalDateTime.now();
    private String updatedByEntityId;

    private LocalDateTime deletedAt;
    private String deletedByEntityId;
}
