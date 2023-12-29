package zone.pusu.mybatisCodeGenerator.tool;


import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadUtil {
    /**
     * 间隔执行多个Runnable
     *
     * @param runnable
     * @param timespan
     */
    public static void execute(Runnable[] runnable, int timespan) {
        if (runnable.length > 0) {
            runnable[0].run();
        }
        for (int i = 1; i < runnable.length; i++) {
            ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
            scheduledExecutorService.schedule(runnable[i], timespan * i, TimeUnit.SECONDS);
            scheduledExecutorService.shutdown();
        }
    }
}
