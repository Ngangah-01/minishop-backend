package io.internaluser.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.lib.entity.BaseJpaEntity;
import io.userpermission.model.AuthGroup;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "internal_user_id", "auth_group_id"})})
public class InternalUserAuthGroup extends BaseJpaEntity {

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "internal_user_id")
    private InternalUser internalUser;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "auth_group_id")
    private AuthGroup authGroup;
}
