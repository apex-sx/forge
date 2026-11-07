package potatoxf.helper.basic.db;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import potatoxf.helper.basic.common.entity.*;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * @author potatoxf
 * @date 2022/7/2
 */
public class InteractiveTableGenerator implements TableGenerator {

  @Override
  public void generate(DataSource dataSource, TableGeneratorProperties tableGeneratorProperties) {

    FastAutoGenerator.create(new DataSourceConfig.Builder(dataSource))
        .templateConfig(templateConfig())
        // 全局配置
        .globalConfig(
            (scanner, builder) -> {
              builder
                  .author(scanner.apply("请输入作者名称？"))
                  .outputDir(scanner.apply("请输入输出目录？"))
                  .enableSwagger();
            })
        // 包配置
        .packageConfig(
            (scanner, builder) -> {
              builder
                  .parent(qa(scanner, tableGeneratorProperties.getPackageForRoot(), "请输入模块名1"))
                  .moduleName(
                      qa(scanner, tableGeneratorProperties.getPackageForModule(), "请输入模块名2"))
                  .entity(qa(scanner, tableGeneratorProperties.getPackageForEntity(), "请输入模块名3"))
                  .xml(qa(scanner, tableGeneratorProperties.getPackageForXml(), "请输入模块名4"))
                  .mapper(qa(scanner, tableGeneratorProperties.getPackageForMapper(), "请输入模块名5"))
                  .service(qa(scanner, tableGeneratorProperties.getPackageForService(), "请输入模块名6"))
                  .serviceImpl(
                      qa(scanner, tableGeneratorProperties.getPackageForServiceImpl(), "请输入模块名7"))
                  .controller(
                      qa(scanner, tableGeneratorProperties.getPackageForController(), "请输入模块名8"));
            })
        // 策略配置
        .strategyConfig(
            (scanner, builder) -> {
              if ("y".equalsIgnoreCase(scanner.apply("主键是否是数字类型(y/n)"))) {
                builder.entityBuilder().superClass(AbstractBaseTableWithLong.class);
              } else {
                builder.entityBuilder().superClass(AbstractBaseTableWithString.class);
              }
              builder
                  .addInclude(getTables(scanner.apply("请输入表名，多个英文逗号分隔？所有输入 all")))
                  .addTablePrefix("")
                  .enableCapitalMode()
                  .entityBuilder()
                  .enableLombok()
                  .enableActiveRecord()
                  .addSuperEntityColumns(
                      TableSupportPrimaryKey.DEFAULT_FIELD_NAME_GID,
                      TableSupportDeleteFlag.DEFAULT_FIELD_NAME_DELETE_FLAG,
                      TableSupportStatusFlag.DEFAULT_FIELD_NAME_STATUS_FLAG,
                      TableSupportCommon.DEFAULT_FIELD_NAME_CREATED_BY,
                      TableSupportCommon.DEFAULT_FIELD_NAME_CREATED_TIME,
                      TableSupportCommon.DEFAULT_FIELD_NAME_UPDATED_BY,
                      TableSupportCommon.DEFAULT_FIELD_NAME_UPDATED_TIME,
                      TableSupportCommon.DEFAULT_FIELD_NAME_REVISION,
                      TableSupportTree.DEFAULT_FIELD_NAME_PARENT)
                  .controllerBuilder()
                  .enableRestStyle()
                  .enableHyphenStyle()
                  .build();
            })
        .execute();
  }

  // 处理 all 情况
  protected static List<String> getTables(String tables) {
    return "all".equalsIgnoreCase(tables)
        ? Collections.emptyList()
        : Arrays.asList(tables.split(","));
  }

  private String qa(Function<String, String> scanner, String packageForRoot, String question) {
    return packageForRoot == null ? scanner.apply(question) : packageForRoot;
  }
}
