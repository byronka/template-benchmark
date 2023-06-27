package com.mitchellbosecke.benchmark.output;


public enum OutputKind {
    CHUNK() {
        @Override
        public Utf8Output create() {
            return new ChunkOutput(512);
        }
    },
    ARRAY() {
        @Override
        public Utf8Output create() {
            return new ByteArrayOutput();
        }
    },
    DEQUE() {
        @Override
        public Utf8Output create() {
            return new DequeOutput();
        }
    };
    
    public abstract Utf8Output create();
}
