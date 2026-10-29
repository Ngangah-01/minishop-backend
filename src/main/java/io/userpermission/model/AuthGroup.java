package io.userpermission.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.lib.entity.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
public class AuthGroup extends BaseJpaEntity {
    @Column(length = 100)
    private String name;

    private String description;
    private boolean isEditable;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="authGroup")
    private Set<AuthGroupPermission> authGroupPermissions;

    public Set<AuthGroupPermission> getAuthGroupPermissions() {
        if(authGroupPermissions == null) {
            return new HashSet<>();
        }
        return authGroupPermissions.stream().filter(d -> !d.getIsDeleted()).collect(Collectors.toSet());
    }
}
