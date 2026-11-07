package com.github.zhitron.essential_management.entity;

import com.github.zhitron.essential_management.EssentialEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;


/**
 * @author zhitron
 */
@Getter
@Setter
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Organization extends EssentialEntity<Organization> {
    private String organizationCode;
    private String organizationType;
    private Integer sortIndex;
    private String contactPhone;
    private String email;
    private Long maxChildIndex;
}
