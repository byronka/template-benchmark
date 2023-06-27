package com.mitchellbosecke.benchmark.output;

import java.io.IOException;

import com.fizzed.rocker.ContentType;
import com.fizzed.rocker.RockerOutputFactory;
import com.fizzed.rocker.runtime.AbstractRockerOutput;

public class RockerAdapter extends AbstractRockerOutput<RockerAdapter> implements RockerOutputFactory<RockerAdapter> {

    private final Utf8Output output;
    
    public RockerAdapter(Utf8Output output) {
        super(ContentType.HTML, "UTF-8", 0);
        this.output = output;
    }

    @Override
    public RockerAdapter w(String string) throws IOException {
        output.write(string);
        return this;
    }

    @Override
    public RockerAdapter w(byte[] bytes) throws IOException {
        output.write(bytes);
        return this;
    }
    
    public Utf8Output getOutput() {
        return output;
    }

    @Override
    public RockerAdapter create(ContentType contentType, String charsetName) {
        return this;
    }

}
