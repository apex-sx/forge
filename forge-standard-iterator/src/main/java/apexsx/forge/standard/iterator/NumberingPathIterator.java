package apexsx.forge.standard.iterator;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.Objects;

/**
 * 一个迭代器类，用于生成带有编号的文件对象。
 *
 * @author zhitron
 */
public class NumberingPathIterator implements Iterator<Path> {
    /**
     * 用于生成带编号字符串的迭代器。
     */
    private final NumberingStringIterator numberingStringIterator;
    /**
     * 生成路径时的基本目录。
     */
    private final Path dir;

    /**
     * 构造一个NumberingPathIterator实例。
     *
     * @param dir    要解析路径的基本目录，不能为 null
     * @param format 用于生成字符串的格式，可以包含{numbering}占位符，例如 "file-{numbering}.txt"
     */
    public NumberingPathIterator(Path dir, String format) {
        this.numberingStringIterator = new NumberingStringIterator(format);
        this.dir = Objects.requireNonNull(dir);
    }

    /**
     * 构造一个NumberingPathIterator实例。
     *
     * @param dir    要解析路径的基本目录，不能为 null
     * @param format 用于生成字符串的格式，可以包含{numbering}占位符，例如 "file-{numbering}.txt"
     * @param start  起始编号
     */
    public NumberingPathIterator(Path dir, String format, int start) {
        this.numberingStringIterator = new NumberingStringIterator(format, start);
        this.dir = Objects.requireNonNull(dir);
    }

    /**
     * 构造一个NumberingPathIterator实例。
     *
     * @param dir      要解析路径的基本目录，不能为 null
     * @param format   用于生成字符串的格式，可以包含{numbering}占位符，例如 "file-{numbering}.txt"
     * @param start    起始编号
     * @param interval 每次递增的步长
     */
    public NumberingPathIterator(Path dir, String format, int start, int interval) {
        this.numberingStringIterator = new NumberingStringIterator(format, start, interval);
        this.dir = Objects.requireNonNull(dir);
    }

    /**
     * 构造一个NumberingPathIterator实例。
     *
     * @param dir               要解析路径的基本目录，不能为 null
     * @param format            用于生成字符串的格式，可以包含{numbering}占位符，例如 "file-{numbering}.txt"
     * @param start             起始编号
     * @param interval          每次递增的步长
     * @param numberingOfDigits 数字位数，决定编号的上限，控制前导零的数量
     */
    public NumberingPathIterator(Path dir, String format, int start, int interval, int numberingOfDigits) {
        this.numberingStringIterator = new NumberingStringIterator(format, start, interval, numberingOfDigits);
        this.dir = Objects.requireNonNull(dir);
    }

    /**
     * 判断是否还有下一个元素。
     *
     * @return 如果仍有下一个元素，则返回true；否则返回false
     */
    public boolean hasNext() {
        return numberingStringIterator.hasNext();
    }

    /**
     * 返回下一个路径对象。
     *
     * @return 下一个路径对象
     */
    public Path next() {
        return dir.normalize().resolve(numberingStringIterator.next());
    }
}