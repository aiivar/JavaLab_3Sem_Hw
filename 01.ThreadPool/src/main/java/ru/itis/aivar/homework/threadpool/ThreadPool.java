package ru.itis.aivar.homework.threadpool;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ThreadPool {
    private Deque<Runnable> tasks;

    private PoolWorker[] pool;

    public ThreadPool(int treadsCount) {
        this.tasks = new ConcurrentLinkedDeque<>();
        this.pool = new PoolWorker[treadsCount];
        for (int i = 0; i < this.pool.length; i++) {
            this.pool[i] = new PoolWorker();
            this.pool[i].start();
        }
    }

    public void submit(Runnable task) {
        synchronized (tasks) {
            tasks.add(task);
        }
    }

    private class PoolWorker extends Thread {
        @Override
        public void run() {
            while (true) {
                Runnable task;
                synchronized (tasks) {
                    task = tasks.poll();
                }
                if (task != null) {
                    task.run();
                }
            }
        }
    }
}
