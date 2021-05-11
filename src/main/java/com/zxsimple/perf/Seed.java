package com.zxsimple.perf;

import java.util.SplittableRandom;

public class Seed {

    private final SplittableRandom random = new SplittableRandom(2021L);

    // FastRandom random = new FastRandom(2021L);
    public static void main(String[] args) {
        Seed seed = new Seed();

        for (int i = 0; i < 10; i++) {
            float[] values = seed.randFloatArray(8);
            for (float v : values) {
                System.out.print(v + ",");
            }

            System.out.println();
        }
    }

    public float[] randFloatArray(int length) {
        int i = 0;
        float[] array = new float[length];
        double[] values = random.doubles(length, 0, 100).toArray();
        for (double value : values) {
            array[i] = (float) value;
            i++;
        }

        return array;
    }
}
