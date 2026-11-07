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
public class Menu extends EssentialEntity<Menu> {
    private String type;
    private String method;
    private String request;
    private String icon;
}
