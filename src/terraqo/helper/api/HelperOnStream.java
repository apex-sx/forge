package potatoxf.helper.api;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * IO助手类
 *
 * @author potatoxf
 * @date 2021/06/19
 */
public final class HelperOnStream {

  private HelperOnStream() throws IllegalAccessException {
    throw new IllegalAccessException(
        "The instance creation is not allowed,because this is static method utils class");
  }

  /**
   * 静默关闭可关闭对象
   *
   * @param closeable 可关闭对象
   */
  public static void closeSilently(Closeable closeable) {
    try {
      if (closeable != null) {
        closeable.close();
      }
    } catch (IOException ignored) {
    }
  }

  /**
   * 静默关闭可关闭对象
   *
   * @param closeables 一系列可关闭对象
   */
  public static void closesSilently(Closeable... closeables) {
    for (Closeable closeable : closeables) {
      closeSilently(closeable);
    }
  }

  public static String readAllString(File file) throws IOException {
    return readAllString(file, null);
  }

  public static String readAllString(File file, CharsetToken charsetToken) throws IOException {
    FileInputStream fileInputStream = new FileInputStream(file);
    try {
      return readAllString(fileInputStream, charsetToken);
    } finally {
      closeSilently(fileInputStream);
    }
  }

  public static String readAllString(URL url) throws IOException {
    return readAllString(url.openStream(), CharsetToken.UTF8);
  }

  public static String readAllString(URL url, CharsetToken charsetToken) throws IOException {
    return readAllString(url.openStream(), charsetToken);
  }

  public static String readAllString(InputStream inputStream) throws IOException {
    return readAllString(inputStream, CharsetToken.UTF8);
  }

  public static String readAllString(InputStream inputStream, CharsetToken charsetToken)
      throws IOException {
    char[] results = readAllChars(inputStream, charsetToken);
    if (Whether.empty(results)) {
      return Tools.EMPTY_STRING;
    }
    return new String(results);
  }

  public static String readAllString(Reader reader) throws IOException {
    char[] results = readAllChars(reader);
    if (Whether.empty(results)) {
      return Tools.EMPTY_STRING;
    }
    return new String(results);
  }

  public static char[] readAllChars(File file) throws IOException {
    FileReader fileReader = new FileReader(file);
    try {
      return readAllChars(fileReader);
    } finally {
      closeSilently(fileReader);
    }
  }

  public static char[] readAllChars(Reader reader) throws IOException {
    CharArrayWriter charArrayWriter = new CharArrayWriter();
    char[] buffer = new char[Tools.IO_CACHE_SIZE];
    int read;
    while ((read = reader.read(buffer)) != -1) {
      charArrayWriter.write(buffer, 0, read);
    }
    return charArrayWriter.toCharArray();
  }

  public static char[] readAllChars(InputStream inputStream, CharsetToken charsetToken)
      throws IOException {
    return readAllChars(newReader(inputStream, charsetToken));
  }

  public static byte[] readAllBytes(File file) throws IOException {
    FileInputStream fileInputStream = new FileInputStream(file);
    try {
      return readAllBytes(fileInputStream);
    } finally {
      closeSilently(fileInputStream);
    }
  }

  public static byte[] readAllBytes(JarFile jarFile, JarEntry jarEntry) throws IOException {
    InputStream inputStream = null;
    try {
      inputStream = jarFile.getInputStream(jarEntry);
      return readAllBytes(inputStream);
    } finally {
      closeSilently(inputStream);
    }
  }

  public static byte[] readAllBytes(InputStream inputStream) throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    byte[] buffer = new byte[Tools.IO_CACHE_SIZE];
    int read;
    while ((read = inputStream.read(buffer)) != -1) {
      byteArrayOutputStream.write(buffer, 0, read);
    }
    return byteArrayOutputStream.toByteArray();
  }

  public static void writeTextData(
      File file, Iterable<?> data, String split, CharsetToken charsetToken) throws IOException {
    try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
      writeTextData(fileOutputStream, data.iterator(), split, charsetToken);
    }
  }

  public static void writeTextData(
      OutputStream outputStream, Iterable<?> data, String split, CharsetToken charsetToken)
      throws IOException {
    writeTextData(outputStream, data.iterator(), split, charsetToken);
  }

  public static void writeTextData(
      File file, Iterator<?> data, String split, CharsetToken charsetToken) throws IOException {
    try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
      writeTextData(new FileOutputStream(file), data, split, charsetToken);
    }
  }

  public static void writeTextData(
      OutputStream outputStream, Iterator<?> data, String split, CharsetToken charsetToken)
      throws IOException {
    if (data == null) {
      return;
    }
    Charset charset = Tools.safeValue(charsetToken, CharsetToken.UTF8).get();
    byte[] splitBytes = Tools.safeValue(split, Tools.LINE_SEPARATOR).getBytes(charset);
    Object datum;
    while (data.hasNext()) {
      datum = data.next();
      if (datum != null) {
        outputStream.write(datum.toString().getBytes(charset));
      }
      outputStream.write(splitBytes);
    }
  }

  public static void writeTextData(File file, Iterable<?> data, String split) throws IOException {
    try (FileWriter fileWriter = new FileWriter(file)) {
      writeTextData(fileWriter, data.iterator(), split);
    }
  }

  public static void writeTextData(Writer writer, Iterable<?> data, String split)
      throws IOException {
    writeTextData(writer, data.iterator(), split);
  }

  public static void writeTextData(File file, Iterator<?> data, String split) throws IOException {
    try (FileWriter fileWriter = new FileWriter(file)) {
      writeTextData(new FileWriter(file), data, split);
    }
  }

  public static void writeTextData(Writer writer, Iterator<?> data, String split)
      throws IOException {
    if (data == null) {
      return;
    }
    split = Tools.safeValue(split, Tools.LINE_SEPARATOR);
    Object datum;
    while (data.hasNext()) {
      datum = data.next();
      if (datum != null) {
        writer.write(datum.toString());
      }
      writer.write(split);
    }
  }

  public static void write(File file, String string, CharsetToken charsetToken) throws IOException {
    try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
      write(fileOutputStream, new ByteArrayInputStream(string.getBytes(charsetToken.get())));
    }
  }

  public static void write(File file, byte[] bytes) throws IOException {
    try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
      write(fileOutputStream, bytes);
    }
  }

  public static void write(File file, InputStream inputStream) throws IOException {
    try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
      write(fileOutputStream, inputStream);
    }
  }

  public static void write(File file, char[] chars) throws IOException {
    try (FileWriter fileWriter = new FileWriter(file)) {
      write(fileWriter, chars);
    }
  }

  public static void write(File file, Reader reader) throws IOException {
    try (FileWriter fileWriter = new FileWriter(file)) {
      write(fileWriter, reader);
    }
  }

  public static void write(OutputStream outputStream, File file) throws IOException {
    try (FileInputStream fileInputStream = new FileInputStream(file)) {
      write(outputStream, fileInputStream);
    }
  }

  public static void write(OutputStream outputStream, byte[] bytes) throws IOException {
    outputStream.write(bytes);
  }

  public static void write(OutputStream outputStream, byte[] bytes, int off, int len)
      throws IOException {
    outputStream.write(bytes, off, len);
  }

  public static void write(OutputStream outputStream, InputStream inputStream) throws IOException {
    byte[] cache = new byte[Tools.IO_CACHE_SIZE];
    int len;
    while ((len = inputStream.read(cache)) > 0) {
      outputStream.write(cache, 0, len);
    }
  }

  public static void write(Writer writer, char[] chars) throws IOException {
    writer.write(chars);
  }

  public static void write(Writer writer, char[] chars, int off, int len) throws IOException {
    writer.write(chars, off, len);
  }

  public static void write(Writer writer, Reader reader) throws IOException {
    char[] cache = new char[Tools.IO_CACHE_SIZE];
    int len;
    while ((len = reader.read(cache)) > 0) {
      writer.write(cache, 0, len);
    }
  }

  /**
   * new Reader
   *
   * @param inputStream 输入流
   * @param charsetToken 字符集
   * @return {@code Reader}
   */
  public static Reader newReader(InputStream inputStream, CharsetToken charsetToken) {
    Reader reader;
    if (charsetToken == null) {
      reader = new InputStreamReader(inputStream);
    } else {
      reader = new InputStreamReader(inputStream, charsetToken.get());
    }
    return reader;
  }

  /**
   * new Writer
   *
   * @param outputStream 输出流
   * @param charsetToken 字符集
   * @return {@code Writer}
   */
  public static Writer newWriter(OutputStream outputStream, CharsetToken charsetToken) {
    Writer writer;
    if (charsetToken == null) {
      writer = new OutputStreamWriter(outputStream);
    } else {
      writer = new OutputStreamWriter(outputStream, charsetToken.get());
    }
    return writer;
  }

  /**
   * new BufferedOutputStream
   *
   * @param outputStream {@code OutputStream}
   * @return {@code BufferedOutputStream}
   */
  public static BufferedOutputStream newBufferedReader(OutputStream outputStream) {
    if (outputStream instanceof BufferedOutputStream) {
      return (BufferedOutputStream) outputStream;
    } else {
      return new BufferedOutputStream(outputStream, Tools.IO_CACHE_SIZE);
    }
  }

  /**
   * new BufferedInputStream
   *
   * @param inputStream {@code InputStream}
   * @return {@code BufferedInputStream}
   */
  public static BufferedInputStream newBufferedInputStream(InputStream inputStream) {
    if (inputStream instanceof BufferedInputStream) {
      return (BufferedInputStream) inputStream;
    } else {
      return new BufferedInputStream(inputStream, Tools.IO_CACHE_SIZE);
    }
  }

  /**
   * new BufferedWriter
   *
   * @param writer {@code Writer}
   * @return {@code BufferedWriter}
   */
  public static BufferedWriter newBufferedWriter(Writer writer) {
    if (writer instanceof BufferedWriter) {
      return (BufferedWriter) writer;
    } else {
      return new BufferedWriter(writer, Tools.IO_CACHE_SIZE);
    }
  }

  /**
   * new BufferedReader
   *
   * @param reader {@code Reader}
   * @return {@code BufferedReader}
   */
  public static BufferedReader newBufferedReader(Reader reader) {
    if (reader instanceof BufferedReader) {
      return (BufferedReader) reader;
    } else {
      return new BufferedReader(reader, Tools.IO_CACHE_SIZE);
    }
  }
}
