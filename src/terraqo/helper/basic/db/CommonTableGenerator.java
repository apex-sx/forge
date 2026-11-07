package potatoxf.helper.basic.db;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import potatoxf.helper.api.HelperOnClass;
import potatoxf.helper.api.Tools;
import potatoxf.helper.basic.common.entity.AbstractBaseTable;
import potatoxf.helper.basic.common.entity.AbstractBaseTableWithLong;
import potatoxf.helper.basic.common.entity.AbstractBaseTableWithString;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author potatoxf
 * @date 2022/7/2
 */
public class CommonTableGenerator implements TableGenerator {

  private static final Logger LOGGER = LoggerFactory.getLogger(CommonTableGenerator.class);

  private TableGeneratorProperties defaultTableGeneratorProperties;

  public TableGeneratorProperties getDefaultTableGeneratorProperties() {
    return defaultTableGeneratorProperties;
  }

  public CommonTableGenerator setDefaultTableGeneratorProperties(
      TableGeneratorProperties defaultTableGeneratorProperties) {
    this.defaultTableGeneratorProperties = defaultTableGeneratorProperties;
    return this;
  }

  @Override
  public void generate(DataSource dataSource, TableGeneratorProperties tableGeneratorProperties) {
    FastAutoGenerator.create(new DataSourceConfig.Builder(dataSource))
        .templateConfig(templateConfig())
        // 全局配置
        .globalConfig(
            builder -> {
              builder
                  .author(getAuthor(tableGeneratorProperties))
                  .outputDir(getOutputDir(tableGeneratorProperties))
                  .enableSwagger();
            })
        // 包配置
        .packageConfig(
            builder -> {
              builder
                  .parent(getPackageForRoot(tableGeneratorProperties))
                  .moduleName(getPackageForModule(tableGeneratorProperties))
                  .entity(getPackageForEntity(tableGeneratorProperties))
                  .xml(getPackageForXml(tableGeneratorProperties))
                  .mapper(getPackageForMapper(tableGeneratorProperties))
                  .service(getPackageForService(tableGeneratorProperties))
                  .serviceImpl(getPackageForServiceImpl(tableGeneratorProperties))
                  .controller(getPackageForController(tableGeneratorProperties));
            })
        // 策略配置
        .strategyConfig(
            builder -> {
              if (isStringPrimaryKey(tableGeneratorProperties)) {
                builder.entityBuilder().superClass(AbstractBaseTableWithString.class);
              } else {
                builder.entityBuilder().superClass(AbstractBaseTableWithLong.class);
              }

              builder
                  .addInclude(getTables(tableGeneratorProperties))
                  .addTablePrefix(getTablePrefixes(tableGeneratorProperties))
                  .enableCapitalMode()
                  .entityBuilder()
                  .enableLombok()
                  .enableActiveRecord()
                  .addIgnoreColumns(getIgnoredFieldNames(tableGeneratorProperties))
                  .controllerBuilder()
                  .enableRestStyle()
                  .enableHyphenStyle()
                  .build();
            })
        .execute();
  }

  private String[] getIgnoredFieldNames(TableGeneratorProperties tableGeneratorProperties) {

    Set<String> set = new HashSet<>();

    Collections.addAll(
        set,
        Arrays.stream(HelperOnClass.getCurrentFields(AbstractBaseTable.class, null, Modifier.STATIC))
            .map(Field::getName)
            .toArray(String[]::new));

    set.add("gid");

    Collections.addAll(
        set,
        Tools.safeGetOrDefault(
            tableGeneratorProperties,
            defaultTableGeneratorProperties,
            TableGeneratorProperties::getIgnoredFieldNames,
            Tools.EMPTY_STRING_ARRAY));

    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("Ignore generate fields:" + set);
    }

    return set.toArray(new String[0]);
  }

  private String[] getTables(TableGeneratorProperties tableGeneratorProperties) {
    return Tools.safeGet(
        tableGeneratorProperties,
        defaultTableGeneratorProperties,
        TableGeneratorProperties::getTables);
  }

  private String[] getTablePrefixes(TableGeneratorProperties tableGeneratorProperties) {
    return Tools.safeGet(
        tableGeneratorProperties,
        defaultTableGeneratorProperties,
        TableGeneratorProperties::getTablePrefixes);
  }

  private String getOutputDir(TableGeneratorProperties tableGeneratorProperties) {
    return Tools.safeGet(
        tableGeneratorProperties,
        defaultTableGeneratorProperties,
        TableGeneratorProperties::getOutputDir);
  }

  private boolean isStringPrimaryKey(TableGeneratorProperties tableGeneratorProperties) {
    return Boolean.TRUE.equals(
        Tools.safeGet(
            tableGeneratorProperties,
            defaultTableGeneratorProperties,
            TableGeneratorProperties::isStringPrimaryKey));
  }

  private String getAuthor(TableGeneratorProperties tableGeneratorProperties) {
    return Tools.safeGet(
        tableGeneratorProperties,
        defaultTableGeneratorProperties,
        TableGeneratorProperties::getAuthor);
  }

  private String getPackageForRoot(TableGeneratorProperties tableGeneratorProperties) {
    return Tools.safeGet(
        tableGeneratorProperties,
        defaultTableGeneratorProperties,
        TableGeneratorProperties::getPackageForRoot);
  }

  private String getPackageForModule(TableGeneratorProperties tableGeneratorProperties) {
    return Tools.safeGet(
        tableGeneratorProperties,
        defaultTableGeneratorProperties,
        TableGeneratorProperties::getPackageForModule);
  }

  private String getPackageForEntity(TableGeneratorProperties tableGeneratorProperties) {
    return Tools.safeGet(
        tableGeneratorProperties,
        defaultTableGeneratorProperties,
        TableGeneratorProperties::getPackageForEntity);
  }

  private String getPackageForXml(TableGeneratorProperties tableGeneratorProperties) {
    return Tools.safeGet(
        tableGeneratorProperties,
        defaultTableGeneratorProperties,
        TableGeneratorProperties::getPackageForXml);
  }

  private String getPackageForMapper(TableGeneratorProperties tableGeneratorProperties) {
    return Tools.safeGet(
        tableGeneratorProperties,
        defaultTableGeneratorProperties,
        TableGeneratorProperties::getPackageForMapper);
  }

  private String getPackageForService(TableGeneratorProperties tableGeneratorProperties) {
    return Tools.safeGet(
        tableGeneratorProperties,
        defaultTableGeneratorProperties,
        TableGeneratorProperties::getPackageForService);
  }

  private String getPackageForServiceImpl(TableGeneratorProperties tableGeneratorProperties) {
    return Tools.safeGet(
        tableGeneratorProperties,
        defaultTableGeneratorProperties,
        TableGeneratorProperties::getPackageForServiceImpl);
  }

  private String getPackageForController(TableGeneratorProperties tableGeneratorProperties) {
    return Tools.safeGet(
        tableGeneratorProperties,
        defaultTableGeneratorProperties,
        TableGeneratorProperties::getPackageForController);
  }
}
