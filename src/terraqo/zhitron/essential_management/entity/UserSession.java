package com.github.zhitron.essential_management.entity;


import com.github.zhitron.essential_management.EssentialEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author zhitron
 */
@Getter
@Setter
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UserSession  extends EssentialEntity< UserSession> {
    private LocalDateTime sessionStart;
    private Long sessionExpire;
    private String organizationName;
    private String userAccount;
    private String ipAddress;
    private String location;
    private String device;
    private String system;
    private String client;
}
