package potatoxf.helper.basic.general.global;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import potatoxf.helper.basic.general.GeneralProperties;

import java.util.Map;

/**
 * @author potatoxf
 * @date 2022/7/16
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(GeneralProperties.PROPERTIES_PREFIX + "global")
public class GlobalProperties {

  public Map<Class<?>, Integer> beanTypeSorted;

  public Map<String, Integer> beanNameSorted;
}
