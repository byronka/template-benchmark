package com.mitchellbosecke.benchmark.utf8;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import gg.jte.TemplateOutput;

public class ByteBufferedJteOutput extends ByteBufferedOutputStream implements TemplateOutput {

    private Writer writer;
    
    public ByteBufferedJteOutput(int bufferSize) {
        super(bufferSize);
        writer = new OutputStreamWriter(this, StandardCharsets.UTF_8);
    }

    @Override
    public Writer getWriter() {
        return writer;
    }

    @Override
    public void writeContent(String value) {
        write(value.getBytes(StandardCharsets.UTF_8));
    }
    
    @Override
    public void writeBinaryContent(byte[] value) {
        write(value);
    }
    
}
