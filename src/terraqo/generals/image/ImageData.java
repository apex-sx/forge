/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.image;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 图片数据
 *
 * @author potatoxf
 */
public class ImageData {
    private static final Pattern PATTERN = Pattern.compile("data:image/(.*?);base64,(.*?)");
    /**
     * 图像数据
     */
    private final byte[] image;
    /**
     * 图像格式
     */
    private final String format;

    public static ImageData ofBase64String(String data) {
        Matcher matcher = PATTERN.matcher(data);
        if (matcher.matches()) {
            byte[] image = Base64.getDecoder().decode(matcher.group(2));
            return new ImageData(image, matcher.group(1));
        } else {
            throw new IllegalArgumentException("The input string data not match regexp '" + PATTERN + "'");
        }
    }

    public static ImageData ofBase64String(BufferedImage bufferedImage, String format) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, format, output);
        return new ImageData(output.toByteArray(), format);
    }

    public ImageData(ImageData imageData) {
        this(imageData.image, imageData.format);
    }

    public ImageData(byte[] image, String format) {
        this.image = image;
        this.format = format;
    }

    public byte[] getImage() {
        return image;
    }

    public String getFormat() {
        return format;
    }

    public String toBase64String() {
        String encode = Base64.getEncoder().encodeToString(image);
        return "data:image/" + format + ";base64," + encode;
    }

    @Override
    public String toString() {
        return "Image format='" + format + "'";
    }
}
