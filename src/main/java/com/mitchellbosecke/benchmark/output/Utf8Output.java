package com.mitchellbosecke.benchmark.output;

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
}
