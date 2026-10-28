package io.userpermission.model;

import io.lib.entity.BaseJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserPermission extends BaseJpaEntity {
    @Column(unique = true, length = 50)
    private String permissionName;

    @Column(unique = true, length = 256)
    private String description;
}
