package com.mitchellbosecke.benchmark.utf8;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import com.fizzed.rocker.ContentType;
import com.fizzed.rocker.RockerOutput;

public class ByteBufferedRockerOutput extends ByteBufferedOutputStream implements RockerOutput<ByteBufferedRockerOutput> {

    public ByteBufferedRockerOutput(int bufferSize) {
        super(bufferSize);
    }

    @Override
    public ContentType getContentType() {
        return ContentType.HTML;
    }

    @Override
    public Charset getCharset() {
        return StandardCharsets.UTF_8;
    }

    @Override
    public ByteBufferedRockerOutput w(String string) throws IOException {
        write(string.getBytes(getCharset()));
        return this;
    }

    @Override
    public ByteBufferedRockerOutput w(byte[] bytes) throws IOException {
        write(bytes);
        return this;
    }

    @Override
    public int getByteLength() {
        return size();
    }

}
