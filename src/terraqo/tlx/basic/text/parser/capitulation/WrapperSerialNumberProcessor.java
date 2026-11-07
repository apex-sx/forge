package pxf.toolkit.basic.text.parser.capitulation;


import javax.annotation.Nonnull;
import pxf.toolkit.basic.util.Assert;

/**
 * 包装序号处理器
 *
 * @author potatoxf
 * @date 2021/5/22
 */
public class WrapperSerialNumberProcessor implements SerialNumberProcessor{

  private final String prefix;

  private final String suffix;

  private final SerialNumberProcessor serialNumberProcessor;

  public WrapperSerialNumberProcessor(String prefix, String suffix){

    this(prefix, suffix, null);
  }

  public WrapperSerialNumberProcessor(String prefix, String suffix, SerialNumberProcessor serialNumberProcessor){

    this.prefix = prefix == null ? "" : prefix;
    this.suffix = suffix == null ? "" : suffix;
    this.serialNumberProcessor = serialNumberProcessor;
  }

  @Nonnull
  @Override
  public String handle(int serialNumber){

    Assert.beTrue(serialNumber > 1, "The serial number must be greater 0");
    if(serialNumberProcessor == null){
      return prefix + serialNumber + suffix;
    }
    String result = serialNumberProcessor.handle(serialNumber);
    return prefix + result + suffix;
  }

}
