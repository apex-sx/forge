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
public class Journal extends EssentialEntity< Journal> {
    /**
     * 0 其他 1登录 2新增 3修改 4删除 5授权 6导出 7导入 8强退 9生成代码 10清空
     */
    private Integer journalType;
    private String requestUrl;
    private String requestMethod;
    private String requestParameters;
    private String requestResult;
    private String clientIP;
    private String clientLocation;
    private String clientDevice;
    private String clientOS;
    private String clientType;
}
