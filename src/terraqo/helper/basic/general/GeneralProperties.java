package potatoxf.helper.basic.general;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author potatoxf
 * @date 2022/7/13
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties("helper.basic.general")
public class GeneralProperties {

  public static final String PROPERTIES_PREFIX = "helper.basic.general.";
}
