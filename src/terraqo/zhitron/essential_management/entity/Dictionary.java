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
public class Dictionary extends EssentialEntity<Dictionary> {
    private String module;
    private String category;
    private String kind;
    private String key;
    private String val;
}
