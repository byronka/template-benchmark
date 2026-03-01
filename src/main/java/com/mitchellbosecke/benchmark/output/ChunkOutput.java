package com.mitchellbosecke.benchmark.output;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ReadableByteChannel;
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
    
    @Override
    public ReadableByteChannel asReadableByteChannel() {
        return new ReadableByteChannel() {

            private boolean closed = false;
            private int offset = 0;
            private final int length = contentLength;
            private int chunkIndex = 0;
            private int chunkOffset = 0;
            
            @Override
            public int read(ByteBuffer dst) throws IOException {
                if (closed) {
                    throw new ClosedChannelException();
                }
                
                // end of stream?
                if (chunks.isEmpty() || offset >= length) {
                    return -1;
                }
                
                int readBytes = 0;
                
                // keep trying to fill up buffer while it has capacity and we
                // still have data to fill it up with
                while (dst.hasRemaining() && (offset < length)) {
                
                    byte[] chunk = chunks.get(chunkIndex);
                    int chunkLength = chunk.length - chunkOffset;

                    // number of bytes capable of being read
                    int capacity = dst.remaining();
                    if (capacity < chunkLength) {
                        chunkLength = capacity;
                    }

                    dst.put(chunk, chunkOffset, chunkLength);

                    // update everything
                    offset += chunkLength;
                    chunkOffset += chunkLength;

                    if (chunkOffset >= chunk.length) {
                        // next chunk next time
                        chunkIndex++;
                        chunkOffset = 0;
                    }
                    
                    readBytes += chunkLength;
                }
                
                return readBytes;
            }

            @Override
            public boolean isOpen() {
                return !closed;
            }

            @Override
            public void close() throws IOException {
                closed = true;
            }
        };
    }

}
