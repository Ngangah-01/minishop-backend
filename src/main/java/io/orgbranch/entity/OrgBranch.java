package io.orgbranch.entity;

import io.lib.entity.BaseJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class OrgBranch extends BaseJpaEntity {
    @Column(unique = true, length = 100)
    private String name;

    @Column(unique = true, length = 100)
    private String orgBranchCode;

    private String mpesaPaybillNumber;
}
