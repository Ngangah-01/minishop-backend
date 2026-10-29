package io.internaluser.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.orgbranch.entity.OrgBranch;
import io.orgdepartment.entity.OrgDepartment;
import io.user.entity.User;
import io.user.entity.UserType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class InternalUser extends User {

    @ManyToOne
    @JsonIgnore
    private OrgBranch orgBranch;

    @ManyToOne
    private OrgDepartment orgDepartment;

    @Column(unique = true)
    private String erpSalesPersonCode;

    @Override
    @Transient
    public UserType getUserType() {
        return UserType.INTERNAL_USER;
    }

    @Override
    public String getUserTypeStr() {
        return "Internal user";
    }
}