package com.mitchellbosecke.benchmark.output;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
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
        //return Channels.newChannel(new ByteArrayInputStream(array, 0, length));
        return asReadableByteChannel(ByteBuffer.wrap(array, 0, length));
    }
    
    public static byte[] consumeChannel(ReadableByteChannel channel) {
        
        try (channel) {

            // ideally we would match Jetty and others default of 32K!
            // but that causes windows to run out of heap space on github actions
            ByteBuffer buffer = ByteBuffer.allocate(1024 * 8); 
            int bytesRead = channel.read(buffer);
            // Because the buffer is gigantic we are just going to assume it fits
            // for benchmarking purposes as it probably will in real life for 32K
            // and we do not want to test tight loops of filling outputstreams
            if (bytesRead == -1) {
                return new byte[] {};
            }
            byte[] bytes = new byte[bytesRead];
            buffer.get(bytes);
            return bytes;
            
            //ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            while (bytesRead != -1) {
//                buffer.flip();
//                while (buffer.hasRemaining()) {
//                    baos.write(buffer.get());
//                }
//                buffer.clear();
//                bytesRead = channel.read(buffer);
//            }
//
//            return baos.toByteArray();
        }
        catch (IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }
    
    public static ReadableByteChannel asReadableByteChannel(
            final ByteBuffer buffer) {

        return new ReadableByteChannel() {

            private boolean open = true;

            public int read(ByteBuffer dst) throws IOException {
                if (open == false) {
                    throw new ClosedChannelException();
                }
                
                // This is really naive on purpose
                int size = buffer.remaining();
                dst.put(buffer);
                return size;

            }

            public void close() throws IOException {
                open = false;
            }

            public boolean isOpen() {
                return open;
            }

        };
    }
    
}
