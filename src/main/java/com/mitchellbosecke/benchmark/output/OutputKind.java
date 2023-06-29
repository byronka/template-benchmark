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
//    POOL() {
//        private final OutputPool pool = OutputPool.of(16 * 4);
//        @Override
//        public Utf8Output create() {
//            return pool.acquire();
//        }
//    },
//    THREAD_LOCAL() {
//        private static final ThreadLocal<Utf8Output> threadLocal = ThreadLocal.withInitial(() -> new ByteArrayOutput());
//        @Override
//        public Utf8Output create() {
//            return threadLocal.get();
//        }
//    };
//    DEQUE() {
//        @Override
//        public Utf8Output create() {
//            return new DequeOutput();
//        }
//    };
    
    public abstract Utf8Output create();
}
