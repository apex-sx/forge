/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.text;

import com.github.potatoxf.generals.image.ImageData;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文本图片
 *
 * @author potatoxf
 */
public class TextImageData extends ImageData {
    private static final Pattern PATTERN = Pattern.compile("text:(.*?);(.*?)");
    /**
     * 文本
     */
    private final String text;

    public static TextImageData ofBase64String(String data) {
        Matcher matcher = PATTERN.matcher(data);
        if (matcher.matches()) {
            return new TextImageData(ImageData.ofBase64String(matcher.group(2)), matcher.group(1));
        } else {
            throw new IllegalArgumentException("The input string data not match regexp '" + PATTERN + "'");
        }
    }

    public static TextImageData ofBase64String(BufferedImage bufferedImage, String format, String text) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, format, output);
        return new TextImageData(output.toByteArray(), format, text);
    }

    public TextImageData(ImageData imageData, String text) {
        super(imageData);
        this.text = text;
    }

    public TextImageData(byte[] image, String format, String text) {
        super(image, format);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toBase64String() {
        return "text:" + text + ";" + super.toBase64String();
    }

    @Override
    public String toString() {
        return super.toString() + " text='" + text + "'";
    }
}
