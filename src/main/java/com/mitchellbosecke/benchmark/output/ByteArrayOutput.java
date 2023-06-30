package com.mitchellbosecke.benchmark.output;

import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.Arrays;

public class ByteArrayOutput implements Utf8Output, AutoCloseable {
    /** Default buffer size: <code>4k</code>. */
    public static final int BUFFER_SIZE = 4096;

    /**
     * The maximum size of array to allocate. Some VMs reserve some header words in an
     * array. Attempts to allocate larger arrays may result in OutOfMemoryError: Requested
     * array size exceeds VM limit
     */
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    /** The buffer where data is stored. */
    protected byte[] buf;

    /** The number of valid bytes in the buffer. */
    protected int count;

    /**
     * Creates buffered stream of given size
     * @param bufferSize initial size.
     */
    public ByteArrayOutput(int bufferSize) {
        this.buf = new byte[bufferSize];
    }

    /**
     * Creates buffered stream of buffer initial size: {@value #BUFFER_SIZE}.
     */
    public ByteArrayOutput() {
        this(BUFFER_SIZE);
    }

    void reset() {
        count = 0;
    }

    @Override
    public void close() {
        this.reset();
    }

    @Override
    public void write(byte[] bytes) {
        int len = bytes.length;
        ensureCapacity(count + len);
        System.arraycopy(bytes, 0, buf, count, len);
        count += len;
    }

//    @Override
//    public void write(byte[] bytes, int off, int len) {
//        ensureCapacity(count + len);
//        System.arraycopy(bytes, off, buf, count, len);
//        count += len;
//    }

    /**
     * How many bytes have been written so far.
     * @return 0 if empty, otherwise how many bytes so far
     */
    public int size() {
        return count;
    }

    /**
     * Copy internal byte array into a new array.
     * @return Byte array.
     */
    public byte[] toByteArray() {
        byte[] array = new byte[count];
        System.arraycopy(buf, 0, array, 0, count);
        return array;
    }

    /**
     * Get a view of the byte buffer.
     * @return Byte buffer.
     */
    public ByteBuffer toBuffer() {
        return ByteBuffer.wrap(buf, 0, count);
    }
    
    @Override
    public ReadableByteChannel asReadableByteChannel() {
        return Utf8Output.toChannel(buf, count);
    }

    private void ensureCapacity(int minCapacity) {
        // overflow-conscious code
        if (minCapacity - buf.length > 0) {
            grow(minCapacity);
        }
    }

    /**
     * Increases the capacity to ensure that it can hold at least the number of elements
     * specified by the minimum capacity argument.
     * @param minCapacity the desired minimum capacity
     */
    private void grow(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = buf.length;
        int newCapacity = oldCapacity << 1;
        if (newCapacity - minCapacity < 0) {
            newCapacity = minCapacity;
        }
        if (newCapacity - MAX_ARRAY_SIZE > 0) {
            newCapacity = hugeCapacity(minCapacity);
        }
        buf = Arrays.copyOf(buf, newCapacity);
    }

    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) {
            throw new OutOfMemoryError();
        }
        return (minCapacity > MAX_ARRAY_SIZE) ? Integer.MAX_VALUE : MAX_ARRAY_SIZE;
    }

}
