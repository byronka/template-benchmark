package com.mitchellbosecke.benchmark.output;

import gg.jte.TemplateOutput;

public class JteAdapter implements TemplateOutput {

    private final Utf8Output output;
    
    
    public JteAdapter(Utf8Output output) {
        super();
        this.output = output;
    }

    @Override
    public void writeContent(String value) {
        output.write(value);
    }

    @Override
    public void writeContent(String value, int beginIndex, int endIndex) {
        output.write(value, beginIndex, endIndex );
    }
    
    @Override
    public void writeBinaryContent(byte[] value) {
        output.write(value);
    }

    
    public Utf8Output getOutput() {
        return output;
    }
}
