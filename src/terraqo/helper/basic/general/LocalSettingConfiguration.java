package potatoxf.helper.basic.general;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import potatoxf.helper.api.lang.MappingFolderCached;

/**
 * @author potatoxf
 * @date 2022/7/13
 */
@Getter
@Setter
@ConfigurationProperties(GeneralProperties.PROPERTIES_PREFIX + "local.setting")
@Configuration
public class LocalSettingConfiguration {
  /** */
  private String builtRootDirPath = "/helper/setting/";
  /** */
  private String outerRootDirPath = "./sync/setting/";

  @Bean
  public MappingFolderCached cachedFolderMapping() {
    return new MappingFolderCached(builtRootDirPath, outerRootDirPath);
  }
}
