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
public class User extends EssentialEntity<User> {
    private String account;
    private String email;
    private String mobile;
    private Integer gender;
    private String avatar;
    private String password;
    private String salt;
    private String organizationId;
    private String organizationName;
    private String lastLoginIp;
}
