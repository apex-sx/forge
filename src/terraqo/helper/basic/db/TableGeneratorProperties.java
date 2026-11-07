package potatoxf.helper.basic.db;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import potatoxf.helper.api.Whether;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author potatoxf
 * @date 2022/7/2
 */
@Setter
@Getter
public class TableGeneratorProperties {
  public static final String ROOT = "helper/basic/table-generator/";
  private String author;
  private String outputDir;
  private String[] tablePrefixes;
  private String[] tables;
  private String[] ignoredFieldNames;
  private boolean isStringPrimaryKey;
  private String packageForRoot;
  private String packageForModule;
  private String packageForEntity;
  private String packageForXml;
  private String packageForMapper;
  private String packageForService;
  private String packageForServiceImpl;
  private String packageForController;

  public static TableGeneratorProperties loadModule(String module, String... tables) {
    InputStream inputStream = ClassLoader.getSystemResourceAsStream(ROOT + module + ".json");
    if (inputStream == null) {
      return null;
    }
    TableGeneratorProperties tableGeneratorProperties =
        new Gson().fromJson(new InputStreamReader(inputStream), TableGeneratorProperties.class);

    if (Whether.noEmpty(tables)) {
      tableGeneratorProperties.setTables(tables);
    }

    return tableGeneratorProperties;
  }
}
