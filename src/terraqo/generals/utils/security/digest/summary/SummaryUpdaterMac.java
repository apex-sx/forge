/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils.security.digest.summary;

import javax.crypto.Mac;
import java.nio.ByteBuffer;

/**
 * @author potatoxf
 */
class SummaryUpdaterMac implements SummaryUpdater {
    private final Mac mac;

    SummaryUpdaterMac(Mac mac) {
        this.mac = mac;
    }

    @Override
    public void update(byte[] input, int offset, int len) {
        mac.update(input, offset, len);
    }

    @Override
    public void update(ByteBuffer input) {
        mac.update(input);
    }

    @Override
    public void reset() {
        mac.reset();
    }

    @Override
    public byte[] finish() {
        return mac.doFinal();
    }

    @Override
    public int getDigestLength() {
        return mac.getMacLength();
    }
}
