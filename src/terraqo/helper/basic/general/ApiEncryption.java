package potatoxf.helper.basic.general;

import java.lang.annotation.*;

/**
 * @author potatoxf
 * @date 2022/7/19
 */
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiEncryption {}
