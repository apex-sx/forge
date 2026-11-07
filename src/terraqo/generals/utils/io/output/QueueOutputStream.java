/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils.io.output;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.github.potatoxf.generals.utils.io.input.QueueInputStream;

/**
 * Simple alternative to JDK {@link PipedOutputStream}; queue input stream provides what's written in queue
 * output stream.
 * <p>
 * Example usage:
 * </p>
 *
 * <pre>
 * QueueOutputStream outputStream = new QueueOutputStream();
 * QueueInputStream inputStream = outputStream.newPipeInputStream();
 *
 * outputStream.write("hello world".getBytes(UTF_8));
 * inputStream.read();
 * </pre>
 *
 * Unlike JDK {@link PipedInputStream} and {@link PipedOutputStream}, queue input/output streams may be used safely in a
 * single thread or multiple threads. Also, unlike JDK classes, no special meaning is attached to initial or current
 * thread. Instances can be used longer after initial threads exited.
 * <p>
 * Closing a {@link QueueOutputStream} has no effect. The methods in this class can be called after the stream has been
 * closed without generating an {@link IOException}.
 * </p>
 *
 * @see QueueInputStream
 * @author potatoxf
 */
public class QueueOutputStream extends OutputStream {

    private final BlockingQueue<Integer> blockingQueue;

    /**
     * Constructs a new instance with no limit to internal buffer size.
     */
    public QueueOutputStream() {
        this(new LinkedBlockingQueue<>());
    }

    /**
     * Constructs a new instance with given buffer.
     *
     * @param blockingQueue backing queue for the stream
     */
    public QueueOutputStream(final BlockingQueue<Integer> blockingQueue) {
        this.blockingQueue = Objects.requireNonNull(blockingQueue, "blockingQueue");
    }

    /**
     * Constructs a new QueueInputStream instance connected to this. Writes to this output stream will be visible to the
     * input stream.
     *
     * @return QueueInputStream connected to this stream
     */
    public QueueInputStream newQueueInputStream() {
        return QueueInputStream.builder().setBlockingQueue(blockingQueue).get();
    }

    /**
     * Writes a single byte.
     *
     * @throws InterruptedIOException if the thread is interrupted while writing to the queue.
     */
    @Override
    public void write(final int b) throws InterruptedIOException {
        try {
            blockingQueue.put(0xFF & b);
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
            final InterruptedIOException interruptedIoException = new InterruptedIOException();
            interruptedIoException.initCause(e);
            throw interruptedIoException;
        }
    }
}
