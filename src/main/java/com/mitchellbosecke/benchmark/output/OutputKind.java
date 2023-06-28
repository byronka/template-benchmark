package com.mitchellbosecke.benchmark.output;


public enum OutputKind {
    ARRAY() {
        @Override
        public Utf8Output create() {
            return new ByteArrayOutput();
        }
    },
    CHUNK() {
        @Override
        public Utf8Output create() {
            return new ChunkOutput(512);
        }
    };
//    DEQUE() {
//        @Override
//        public Utf8Output create() {
//            return new DequeOutput();
//        }
//    };
    
    public abstract Utf8Output create();
}
