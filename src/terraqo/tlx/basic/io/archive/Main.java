package pxf.toolkit.basic.io.archive;

import java.io.File;
import java.util.ArrayList;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.ZipParameters.SymbolicLinkAction;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;

/**
 * @author potatoxf
 * @date 2021/5/27
 */
public class Main {

  public static void main(String[] args) throws ZipException {
    ZipFile zipFile = new ZipFile("d:\\test2.zip");
    ArrayList<File> filesToAdd = new ArrayList<>();
    filesToAdd.add(new File("C:\\Users\\Potato\\Desktop\\settings.xml"));
    filesToAdd.add(new File("C:\\Users\\Potato\\Desktop\\1f2dd9d06a0f964e3f3318e91da81d0.jpg"));
    filesToAdd.add(new File("C:\\Users\\Potato\\Desktop\\部署"));

    ZipParameters parameters = new ZipParameters();
    parameters.setCompressionMethod(CompressionMethod.DEFLATE);
    parameters.setCompressionLevel(CompressionLevel.ULTRA);
    parameters.setSymbolicLinkAction(SymbolicLinkAction.INCLUDE_LINK_AND_LINKED_FILE);
    // Set password
    zipFile.setPassword("123245".toCharArray());
    parameters.setEncryptFiles(true);
    parameters.setEncryptionMethod(EncryptionMethod.ZIP_STANDARD);
    parameters.setUnixMode(true);
    zipFile.addFiles(filesToAdd, parameters);
    for (FileHeader fileHeader : zipFile.getFileHeaders()) {
      System.out.println(fileHeader);
    }
  }
}
