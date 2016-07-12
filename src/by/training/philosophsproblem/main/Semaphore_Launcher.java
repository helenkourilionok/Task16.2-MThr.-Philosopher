package by.training.philosophsproblem.main;

import by.training.philosophsproblem.service.SemaphoreEngine;

public class Semaphore_Launcher {

    public static int N = 5;

    public static void main(String[] args) throws InterruptedException {
        run(1, 1);
        //философу даётся одна секунда
        //на то чтобы подумать и на то чтобы поесть
    }

    private static void run(long eatTime, long thinkTime) {
        System.out.println("eat= " + eatTime + "ms, think = " + thinkTime + "ms");
        new SemaphoreEngine(N, eatTime, thinkTime).newRun();
    }

}
