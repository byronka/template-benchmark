package com.mitchellbosecke.benchmark.output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;

public interface Utf8Output extends AutoCloseable {

    default void write(String s) {
        var bytes = s.getBytes(StandardCharsets.UTF_8);
        write(bytes);
    }
    
    default void write(String s, int off, int len) {
        throw new UnsupportedOperationException();
    }
    
    default void write(byte[] bytes, int off, int len) {
        throw new UnsupportedOperationException();
    }
    
    void write(byte[] bytes);
    
    public int size();
    
    public byte[] toByteArray();
    
    default void close() {}
    
    default byte[] output() {
        return NO_COPY ? IGNORE : /* toByteArray() */ consumeChannel(asReadableByteChannel());
    }
    
    public static boolean NO_COPY = false;
    
    public static byte[] IGNORE = new byte[] {};
    
    public static byte[] output(StringBuilder sb) {
        return NO_COPY ? IGNORE : sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    default ReadableByteChannel asReadableByteChannel() {
        var array = toByteArray();
        return toChannel(array, array.length);
    }
    
    public static ReadableByteChannel toChannel(byte[] array, int length) {
        /*
         * ByteArrayInputStream is probably not fast but its builtin
         */
        return Channels.newChannel(new ByteArrayInputStream(array, 0, length));
    }
    
    public static byte[] consumeChannel(ReadableByteChannel channel) {
        
        try (channel) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            ByteBuffer buffer = ByteBuffer.allocate(1024 * 4);

            int bytesRead = channel.read(buffer);
            while (bytesRead != -1) {
                buffer.flip();
                while (buffer.hasRemaining()) {
                    baos.write(buffer.get());
                }
                buffer.clear();
                bytesRead = channel.read(buffer);
            }

            return baos.toByteArray();
        }
        catch (IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }
    
}
