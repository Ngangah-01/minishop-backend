package io.orgdepartment.entity;

import io.lib.entity.BaseJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class OrgDepartment extends BaseJpaEntity {
    @Column(unique = true, length = 100)
    private String name;

    private String erpDepartmentCode;

    private Boolean allowsExpenses;
}
