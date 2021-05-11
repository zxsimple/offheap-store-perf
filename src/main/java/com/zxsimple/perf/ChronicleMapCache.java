package com.zxsimple.perf;

import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;

import java.io.File;
import java.io.IOException;
import java.util.SplittableRandom;

public class ChronicleMapCache {

    private static final int EMBEDDING_SIZE = 8;
    private final String basePath = System.getProperty("java.io.tmpdir") + "/chronicle-map.db";
    private final Seed seed = new Seed();
    private final SplittableRandom random = new SplittableRandom(2021L);
    private ChronicleMap<Long, float[]> map = null;

    public static void main(String[] args) throws IOException {
        ChronicleMapCache cache = new ChronicleMapCache();
        int size = Integer.parseInt(args[1]);
        if (args[0].equals("w")) {
            cache.init(true, size);
            cache.testWrite(10000, size);
        } else if (args[0].equals("r")) {
            cache.init(false, size);
            cache.testRead(10000, size);
        }
    }

    public void init(boolean write, int size) throws IOException {

        if (write) {
            map = ChronicleMapBuilder.of(Long.class, float[].class)
                    .name("map")
                    .entries(size)
                    .averageValueSize(4 * EMBEDDING_SIZE)
                    .createPersistedTo(new File(basePath));
        } else {
            map = ChronicleMapBuilder.of(Long.class, float[].class)
                    .name("map")
                    .entries(size)
                    .averageValueSize(4 * EMBEDDING_SIZE)
                    .recoverPersistedTo(new File(basePath), true);
        }
    }

    public void testWrite(int warmups, int size) {
        long t = 0;
        for (long i = (-warmups); i < size; i++) {
            if (i == 0) {
                t = System.nanoTime();
            }
            if (i % (10_000_000) == 0) {
                String progress = ".".repeat((int)(i / (size / 50) > 0 ? (i / (size / 50)) : 1));
                System.out.println(progress);
            }
            map.put(Math.abs(i), seed.randFloatArray(EMBEDDING_SIZE));
        }
        System.out.println("Total cost : " + (System.nanoTime() - t));
        map.close();
    }

    public void testRead(int warmups, int size) {
        long t = 0;
        for (long i = (-warmups); i < size; i++) {
            if (i == 0) {
                t = System.nanoTime();
            }
            map.get(random.nextLong(size));
        }
        System.out.println("Total cost : " + (System.nanoTime() - t));
        map.close();
    }
}
