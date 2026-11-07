/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils;

import com.github.potatoxf.generals.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.*;


/**
 * 通用压缩包
 *
 * @author potatoxf
 */
@SuppressWarnings("unused")
public interface GeneralCompress {
    /**
     * 解压内容从压缩包
     *
     * @param input     指定压缩包文件路径
     * @param output    输出内容目录路径
     * @param classpath 输入文件是否市类路径
     * @return 如果成功返回true，否则返回false
     */
    static boolean unpack(String input, String output, boolean classpath) {
        return GeneralCompress.unpack(Paths.get(input), Paths.get(output), classpath);
    }

    /**
     * 解压内容从压缩包
     *
     * @param input     指定压缩包文件路径
     * @param output    输出内容目录路径
     * @param classpath 输入文件是否市类路径
     * @return 如果成功返回true，否则返回false
     */
    static boolean unpack(Path input, Path output, boolean classpath) {
        if (classpath) {
            InputStream inputStream = ClassLoader.getSystemResourceAsStream(input.toString());
            if (inputStream == null) return false;
            return GeneralCompress.unpack(inputStream, output);
        } else {
            try (InputStream inputStream = Files.newInputStream(input)) {
                return GeneralCompress.unpack(inputStream, output);
            } catch (IOException e) {
                Log.error("Error to open input file [" + input + "]", e);
                return false;
            }
        }
    }

    /**
     * 解压内容从压缩包
     *
     * @param input  指定压缩包
     * @param output 输出内容目录路径
     * @return 如果成功返回true，否则返回false
     */
    static boolean unpack(InputStream input, Path output) {
        try (ZipInputStream zip = new ZipInputStream(input)) {
            while (GeneralCompress.get(zip, output) != null) ;
            return true;
        } catch (IOException e) {
            Log.error("Error to open input stream", e);
            return false;
        }
    }

    /**
     * 获取内容从压缩包
     *
     * @param input  指定压缩包
     * @param output 输出内容目录路径
     * @return 如果成功返回true，否则返回false
     */
    static Boolean get(ZipInputStream input, Path output) {
        ZipEntry zipEntry;
        try {
            zipEntry = input.getNextEntry();
        } catch (IOException e) {
            Log.error("Error to get next zip entry", e);
            return false;
        }
        if (zipEntry == null) return null;
        if (zipEntry.isDirectory()) return false;
        if (!Files.exists(output)) {
            try {
                Files.createDirectories(output);
            } catch (IOException e) {
                Log.error("Error to create directory of [" + output + "]", e);
                return false;
            }
        }
        Path outputPath = output.resolve(zipEntry.toString());
        try (OutputStream outputStream = Files.newOutputStream(outputPath)) {
            try {
                GeneralIO.copy(input, outputStream);
                input.closeEntry();
                return true;
            } catch (IOException e) {
                Log.error("Error to read data from zip entry of [" + zipEntry + "]", e);
                try {
                    input.closeEntry();
                } catch (IOException ex) {
                    Log.error("Error to close entry of [" + zipEntry + "]", ex);
                }
                return false;
            }
        } catch (IOException e) {
            Log.error("Error to open output file [" + outputPath + "]", e);
            return false;
        }
    }

    /**
     * 获取内容从压缩包
     *
     * @param input  指定压缩包
     * @param output 输出内容文件路径
     * @param name   内容名称
     * @return 如果成功返回true，否则返回false
     */
    static boolean get(ZipFile input, Path output, Path name) {
        try (OutputStream outputStream = Files.newOutputStream(output)) {
            return GeneralCompress.get(input, outputStream, name);
        } catch (IOException e) {
            Log.error("Error to open output file [" + output + "]", e);
            return false;
        }

    }

    /**
     * 获取内容从压缩包
     *
     * @param input  指定压缩包
     * @param output 输出内容
     * @param name   内容名称
     * @return 如果成功返回true，否则返回false
     */
    static boolean get(ZipFile input, OutputStream output, Path name) {
        ZipEntry zipEntry = input.getEntry(name.toString());
        if (zipEntry.isDirectory()) {
            return false;
        } else {
            try (InputStream inputStream = input.getInputStream(zipEntry)) {
                GeneralIO.copy(inputStream, output);
                return true;
            } catch (IOException e) {
                Log.error("Error to extract zip entry of [" + name + "] from [" + zipEntry + "]", e);
                return false;
            }
        }
    }

    /**
     * 压缩内容到压缩包
     *
     * @param input  指定内容目录路径
     * @param output 输出压缩包文件路径
     * @return 如果成功返回true，否则返回false
     */
    static boolean pack(String input, String output) {
        return GeneralCompress.pack(Paths.get(input), Paths.get(output));
    }

    /**
     * 压缩内容到压缩包
     *
     * @param input  指定内容目录路径
     * @param output 输出压缩包文件路径
     * @return 如果成功返回true，否则返回false
     */
    static boolean pack(Path input, Path output) {
        if (!Files.exists(input)) return false;
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(output))) {
            zipOutputStream.setLevel(Deflater.BEST_COMPRESSION);
            if (Files.isDirectory(input)) {
                Files.walkFileTree(input, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                        add(file, zipOutputStream, input.relativize(file));
                        return FileVisitResult.CONTINUE;
                    }
                });
                return true;
            } else {
                return add(input, zipOutputStream, input.getFileName());
            }
        } catch (IOException e) {
            Log.error("Error to open output file [" + output + "]", e);
            return false;
        }
    }

    /**
     * 添加内容到压缩包
     *
     * @param input  指定内容文件路径
     * @param output 输出压缩包
     * @param name   内容名称
     * @return 如果成功返回true，否则返回false
     */
    static boolean add(Path input, ZipOutputStream output, Path name) {
        try (InputStream inputStream = Files.newInputStream(input)) {
            return GeneralCompress.add(inputStream, output, name);
        } catch (IOException e) {
            Log.error("Error to open input file [" + input + "]", e);
            return false;
        }
    }

    /**
     * 添加内容到压缩包
     *
     * @param input  指定内容
     * @param output 输出压缩包
     * @param name   内容名称
     * @return 如果成功返回true，否则返回false
     */
    static boolean add(InputStream input, ZipOutputStream output, Path name) {
        ZipEntry zipEntry = new ZipEntry(name.toString());
        try {
            output.putNextEntry(zipEntry);
        } catch (IOException e) {
            Log.error("Error to add zip entry of [" + name + "]", e);
            try {
                output.closeEntry();
            } catch (IOException ex) {
                Log.error("Error to close entry of [" + name + "]", ex);
            }
            return false;
        }
        try {
            GeneralIO.copy(input, output);
            output.closeEntry();
            return true;
        } catch (IOException e) {
            Log.error("Error to write data to zip entry of [" + name + "]", e);
            try {
                output.closeEntry();
            } catch (IOException ex) {
                Log.error("Error to close entry of [" + name + "]", ex);
            }
            return false;
        }
    }
}
