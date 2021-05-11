package com.zxsimple.perf;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

import java.util.SplittableRandom;
import java.util.concurrent.ConcurrentMap;

public class MapDbMMapCache {

    private static final int EMBEDDING_SIZE = 8;
    private final String basePath = System.getProperty("java.io.tmpdir") + "/mmap.db";
    private final Seed seed = new Seed();
    private final SplittableRandom random = new SplittableRandom(2021L);
    private DB db = null;

    public static void main(String[] args) {
        MapDbMMapCache cache = new MapDbMMapCache();
        int size = Integer.parseInt(args[1]);
        if (args[0].equals("w")) {
            cache.init(true, size);
            cache.testWrite(10000, size);
        } else if (args[0].equals("r")) {
            cache.init(false, size);
            cache.testRead(10000, size);
        }
    }

    public void init(boolean write, int size) {
        if (write) {
            db = DBMaker
                    .fileDB(basePath)
                    .fileChannelEnable()                            //By default MapDB uses RandomAccessFile to access disk storage. Outside fast mmap files there is third option based on FileChannel. It should be faster than RandomAccessFile
                    .fileMmapEnable()                               // Always enable mmap
                    .fileMmapPreclearDisable()                      // Make mmap file faster
                    .allocateStartSize(4L * EMBEDDING_SIZE * size)  // 初始容量
                    .allocateIncrement(512 * 1024 * 1024)           // 512MB，每次增加容量
                    .make();
        } else {
            db = DBMaker
                    .fileDB(basePath)
                    .fileChannelEnable()                            //By default MapDB uses RandomAccessFile to access disk storage. Outside fast mmap files there is third option based on FileChannel. It should be faster than RandomAccessFile
                    .fileMmapEnable()                               // Always enable mmap
                    .fileMmapPreclearDisable()                      // Make mmap file faster
                    .allocateStartSize(4L * EMBEDDING_SIZE * size)  // 初始容量
                    .allocateIncrement(512 * 1024 * 1024)           // 512MB，每次增加容量
                    .readOnly()
                    .make();
        }
    }

    public void testWrite(int warmups, int size) {
        ConcurrentMap<Long, float[]> map = db.hashMap("map", Serializer.LONG, Serializer.FLOAT_ARRAY).createOrOpen();

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
        db.commit();
        System.out.println("Total cost : " + (System.nanoTime() - t));
        db.close();
    }

    public void testRead(int warmups, int size) {
        ConcurrentMap<Long, float[]> map = db.hashMap("map", Serializer.LONG, Serializer.FLOAT_ARRAY).open();
        long t = 0;
        for (long i = (-warmups); i < size; i++) {
            if (i == 0) {
                t = System.nanoTime();
            }
            map.get(random.nextLong(size));
        }
        System.out.println("Total cost : " + (System.nanoTime() - t));
        db.close();
    }
}
