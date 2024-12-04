import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private static List<byte[]> memoryHog = new ArrayList<>();


    public static void main(String[] args) {
//        cpuIntensiveTask();
        memoryIntensiveTask();
//        threadMonitoringTask();
//        deadlockExample();
//        outOfMemoryTask();


//        long heapSize = Runtime.getRuntime().maxMemory();
//        System.out.println("Max Heap Size: " + (heapSize / 1024 / 1024) + " MB");
        while (true) {


        }


        // stop the world

//        List<byte[]> mem = new ArrayList<>();
//
//        System.out.println("Start allocating memory...");
//
//        // Vòng lặp tạo nhiều đối tượng lớn
//        while (true) {
//            for (int i = 0; i < 100; i++) {
//                // Tạo mảng byte chiếm nhiều bộ nhớ
//                mem.add(new byte[1024 * 1024]); // 1MB mỗi lần
//            }
//
//            // Giảm bớt danh sách để kích hoạt GC
//            if (mem.size() > 50) {
//                mem.subList(0, 10).clear();
//            }
//
//            // Tạm dừng một chút để giảm tốc độ cấp phát
//            try {
//                Thread.sleep(50);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }


    }


    // 1. Tác vụ tính toán nặng (CPU Intensive)
    private static void cpuIntensiveTask() {
        System.out.println("Running CPU-intensive task...");

        // Tạo nhiều luồng để tăng tải CPU
        int numThreads = 10;  // Sử dụng 10 luồng
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        for (int threadId = 0; threadId < numThreads; threadId++) {
            final int id = threadId;
            executor.submit(() -> {
                long i = id;
                while (true) {
                    i += numThreads;

                    // Các phép toán tốn CPU
                    Math.sin(i);
                    Math.cos(i);
                    Math.tan(i);

                    // Thêm phép toán phức tạp
                    double result = Math.pow(i, 2);
                    result += Math.log(i);
                    result *= Math.exp(i);

                    // Thực hiện nhiều phép toán trong mỗi luồng
                    for (int j = 0; j < 500; j++) {
                        result += Math.sqrt(i + j);
                        result -= Math.log(i + j + 1);
                    }

                    if (i % 1_000_000 == 0) {
                        System.out.println("Thread " + id + " still running at i = " + i + " (Result: " + result + ")");
                    }
                }
            });
        }

        // Dừng Executor khi kết thúc
        executor.shutdown();
    }


    // 2. Tạo tải bộ nhớ (Memory Intensive)
    // tao memory leak
    private static void memoryIntensiveTask() {
            System.out.println("Running memory-intensive task...");
            int sizePerAllocation = 1_000_000; // 10 MB mỗi lần cấp phát
            int numberOfAllocations = 2000;     // 200 lần cấp phát (tổng ~2GB)

            for (int i = 0; i < numberOfAllocations; i++) {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                memoryHog.add(new byte[sizePerAllocation]);
                System.out.println("Allocated: " + (i + 1) * 10 + " MB");
            }

            System.out.println("Memory allocation completed. Nhập bất kỳ phím nào để thoát...");
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
            System.out.println("Memory task completed.");
    }

    // 3. Đa luồng (Thread Monitoring)
    private static void threadMonitoringTask() {
        System.out.println("Running thread monitoring task...");
        int threadCount = 50;

        for (int i = 1; i <= threadCount; i++) {
            int threadId = i;
            new Thread(() -> {
                while (true) {
                    System.out.println("Thread " + threadId + " is running...");
                    try {
                        Thread.sleep(1000); // Mỗi luồng ngủ 1 giây
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        System.out.println("Started " + threadCount + " threads.");

    }

    // 4. Tạo deadlock (Deadlock Example)
    private static void deadlockExample() {
        System.out.println("Running deadlock example...");
        final Object LOCK1 = new Object();
        final Object LOCK2 = new Object();

        Thread thread1 = new Thread(() -> {
            synchronized (LOCK1) {
                System.out.println("Thread 1: Locked LOCK1");
                try { Thread.sleep(100); } catch (InterruptedException e) {}
                synchronized (LOCK2) {
                    System.out.println("Thread 1: Locked LOCK2");
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            synchronized (LOCK2) {
                System.out.println("Thread 2: Locked LOCK2");
                try { Thread.sleep(100); } catch (InterruptedException e) {}
                synchronized (LOCK1) {
                    System.out.println("Thread 2: Locked LOCK1");
                }
            }
        });

        thread1.start();
        thread2.start();

    }

    // 5. Tạo OutOfMemoryError (OutOfMemory Task)
    private static void outOfMemoryTask() {
        System.out.println("Running out-of-memory task...");
        List<byte[]> memoryHog = new ArrayList<>();
        try {
            while (true) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                memoryHog.add(new byte[10_000]); // Mỗi lần cấp 1KB
                System.out.println("Allocated more memory. Total: " + memoryHog.size() * 10 + " MB");
            }
        } catch (OutOfMemoryError e) {
            System.err.println("Caught OutOfMemoryError!");
        }
    }

}
