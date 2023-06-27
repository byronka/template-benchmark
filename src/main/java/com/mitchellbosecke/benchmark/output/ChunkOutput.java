package com.mitchellbosecke.benchmark.output;

import java.util.ArrayList;
import java.util.List;

public class ChunkOutput implements Utf8Output {
    
    private final List<byte[]> chunks;
    private int contentLength = 0;
    
    public ChunkOutput(int initialCapacity) {
        chunks = new ArrayList<>(initialCapacity);
    }
    
    @Override
    public void write(byte[] bytes) {
        addChunk(bytes);
    }
    
    private void addChunk(byte[] chunk) {
        chunks.add(chunk);
        contentLength += chunk.length;
    }
    
    public byte[] toByteArray() {
        byte[] result = new byte[contentLength];

        int index = 0;
        for (byte[] chunk : chunks) {
            System.arraycopy(chunk, 0, result, index, chunk.length);
            index += chunk.length;
        }

        return result;
    }

    @Override
    public int size() {
        return contentLength;
    }

}
