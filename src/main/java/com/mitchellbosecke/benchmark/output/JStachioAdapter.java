package com.mitchellbosecke.benchmark.output;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import io.jstach.jstachio.Output.EncodedOutput;

public class JStachioAdapter implements EncodedOutput<RuntimeException>{

    private final Utf8Output output;
    
    public JStachioAdapter(Utf8Output output) {
        super();
        this.output = output;
    }

    @Override
    public void write(byte[] bytes) throws RuntimeException {
        output.write(bytes);
        
    }

    @Override
    public void write(byte[] bytes, int off, int len) throws RuntimeException {
        output.write(bytes, off, len);
        
    }
    
    @Override
    public void append(String s) throws RuntimeException {
        output.write(s.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public Charset charset() {
        return StandardCharsets.UTF_8;
    }
    
    
    public Utf8Output getOutput() {
        return output;
    }

    @Override
    public void append(CharSequence s) throws RuntimeException {
        append(s.toString());
    }
}
