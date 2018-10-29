package com.lucky;

/**
 * Created by wukuijun on 2018/10/29
 */
public class Test {
    public static void main(String[] args) {
        LockService lockService = new LockService();
        for (int i = 0; i < 100; i++) {
            new ThreadRedis(lockService).start();

        }
    }
}
