package apexsx.forge.standard.iterator;

import java.io.File;
import java.util.Iterator;
import java.util.Objects;

/**
 * 一个迭代器类，用于生成带有编号的文件对象。
 *
 * @author zhitron
 */
public class NumberingFileIterator implements Iterator<File> {
    /**
     * 用于生成带编号字符串的迭代器。
     */
    private final NumberingStringIterator numberingStringIterator;
    /**
     * 文件的基础目录路径。
     */
    private final File dir;

    /**
     * 构造一个新的 NumberingFileIterator 实例。
     *
     * @param dir    要解析路径的基本目录，不能为 null
     * @param format 用于生成字符串的格式，可以包含{numbering}占位符，例如 "file-{numbering}.txt"
     */
    public NumberingFileIterator(File dir, String format) {
        this.numberingStringIterator = new NumberingStringIterator(format);
        this.dir = Objects.requireNonNull(dir);
    }

    /**
     * 构造一个新的 NumberingFileIterator 实例。
     *
     * @param dir    要解析路径的基本目录，不能为 null
     * @param format 用于生成字符串的格式，可以包含{numbering}占位符，例如 "file-{numbering}.txt"
     * @param start  起始编号
     */
    public NumberingFileIterator(File dir, String format, int start) {
        this.numberingStringIterator = new NumberingStringIterator(format, start);
        this.dir = Objects.requireNonNull(dir);
    }

    /**
     * 构造一个新的 NumberingFileIterator 实例。
     *
     * @param dir      要解析路径的基本目录，不能为 null
     * @param format   用于生成字符串的格式，可以包含{numbering}占位符，例如 "file-{numbering}.txt"
     * @param start    起始编号
     * @param interval 每次递增的步长
     */
    public NumberingFileIterator(File dir, String format, int start, int interval) {
        this.numberingStringIterator = new NumberingStringIterator(format, start, interval);
        this.dir = Objects.requireNonNull(dir);
    }

    /**
     * 构造一个新的 NumberingFileIterator 实例。
     *
     * @param dir               要解析路径的基本目录，不能为 null
     * @param format            用于生成字符串的格式，可以包含{numbering}占位符，例如 "file-{numbering}.txt"
     * @param start             起始编号
     * @param interval          每次递增的步长
     * @param numberingOfDigits 数字位数，决定编号的上限，控制前导零的数量
     */
    public NumberingFileIterator(File dir, String format, int start, int interval, int numberingOfDigits) {
        this.numberingStringIterator = new NumberingStringIterator(format, start, interval, numberingOfDigits);
        this.dir = Objects.requireNonNull(dir);
    }

    /**
     * 检查是否还有下一个元素。
     *
     * @return 始终返回 true，因为此迭代器不会结束。
     */
    public boolean hasNext() {
        return numberingStringIterator.hasNext();
    }

    /**
     * 返回下一个文件对象。
     *
     * @return 下一个带有编号的文件对象。
     */
    public File next() {
        return new File(dir, numberingStringIterator.next());
    }
}