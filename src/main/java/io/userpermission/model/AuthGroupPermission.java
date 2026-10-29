package io.userpermission.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.lib.entity.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "auth_group_id", "user_permission_id"})})
public class AuthGroupPermission extends BaseJpaEntity {

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "auth_group_id")
    private AuthGroup authGroup;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_permission_id")
    private UserPermission userPermission;
}
