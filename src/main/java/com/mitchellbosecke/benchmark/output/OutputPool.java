package com.mitchellbosecke.benchmark.output;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.function.LongFunction;


public class OutputPool {
    
    private final Semaphore semaphore;
    private final int size;
    private final ConcurrentLinkedQueue<Pooled> pool;
    private final LongFunction<Utf8Output> createFunction;
    
    public static OutputPool of(int size) {
        return new OutputPool(size, id -> new ByteArrayOutput());
    }
    
    public OutputPool(int size, LongFunction<Utf8Output> createFunction) {
        super();
        this.size = size;
        this.semaphore = new Semaphore(size, true);
        this.createFunction = createFunction;
        this.pool = new ConcurrentLinkedQueue<Pooled>();
        for (int i = 0; i < size; i++) {
            pool.add(new Pooled(i, createFunction.apply(i)));
        }
    }
    
    public Utf8Output acquire() {
        if (semaphore.tryAcquire()) {
            var p = pool.poll();
            if (p != null) {
                return p;
            }
            System.out.println("MISSING");
            return new Pooled(-1L, createFunction.apply(-1));
        }
        System.out.println("NEW");
        return createFunction.apply(size);
    }
    
    public void release(Pooled p) {
        var id = p.id;
        if (id >= 0) {
            pool.offer(p);
        }
        semaphore.release();
    }
    
    public class Pooled implements Utf8Output {
        private final long id;
        private final Utf8Output output;
        
        public Pooled(long id, Utf8Output output) {
            super();
            this.id = id;
            this.output = output;
        }

        @Override
        public void write(byte[] bytes) {
            output.write(bytes);
        }

        @Override
        public int size() {
            return output.size();
        }

        @Override
        public byte[] toByteArray() {
            return output.toByteArray();
        }
        
        @Override
        public void close() {
            try {
                output.close();
            }
            finally {
                release(this);
            }
            
        }
        
    }


}
