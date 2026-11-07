/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils.security.digest.summary;

import java.nio.ByteBuffer;
import java.security.MessageDigest;

/**
 * @author potatoxf
 */
final class SummaryUpdaterMessageDigest implements SummaryUpdater {
    private final MessageDigest messageDigest;

    public SummaryUpdaterMessageDigest(MessageDigest messageDigest) {
        this.messageDigest = messageDigest;
    }

    @Override
    public void update(byte[] input, int offset, int len) {
        messageDigest.update(input, offset, len);
    }

    @Override
    public void update(ByteBuffer input) {
        messageDigest.update(input);
    }

    @Override
    public void reset() {
        messageDigest.reset();
    }

    @Override
    public byte[] finish() {
        return messageDigest.digest();
    }

    @Override
    public int getDigestLength() {
        return messageDigest.getDigestLength();
    }
}
