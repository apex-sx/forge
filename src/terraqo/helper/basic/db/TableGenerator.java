package potatoxf.helper.basic.db;

import com.baomidou.mybatisplus.generator.config.TemplateConfig;

import javax.sql.DataSource;
import java.util.function.Consumer;

/**
 * @author potatoxf
 * @date 2022/7/2
 */
public interface TableGenerator {

  void generate(DataSource dataSource, TableGeneratorProperties tableGeneratorProperties);

  default Consumer<TemplateConfig.Builder> templateConfig() {
    return builder ->
        builder
            .entity("/helper/basic/db/templates/entity.java")
            .service("/helper/basic/db/templates/service.java")
            .serviceImpl("/helper/basic/db/templates/serviceImpl.java")
            .controller("/helper/basic/db/templates/controller.java");
  }
}
