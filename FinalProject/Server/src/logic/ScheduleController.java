package logic;

import java.lang.invoke.VolatileCallSite;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduleController {

    private static volatile ScheduleController instance;
    private final ScheduledExecutorService scheduler;
    private static  DBController dbController;
    private NotificationController notificationController;

    private ScheduleController() {
        scheduler = Executors.newScheduledThreadPool(1);
        notificationController = NotificationController.getInstance();
        dbController = DBController.getInstance();
        runDayleTask();
    }

    public static ScheduleController getInstance() {
        if (instance == null) {
            synchronized (ScheduleController.class) {
                if (instance == null) {
                    instance = new ScheduleController();
                }
            }
        }
        return instance;
    }
    //!WIP
    public void setSchedulerExportLog(Runnable task) {
        long initialDelay = computeInitialDelay(1, 0, 0);
        long period = TimeUnit.DAYS.toMillis(30);
        scheduler.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.MILLISECONDS);
    }
    /**
     * This method runs the daily task
     */
    public void runDayleTask() {
        long initialDelay = computeInitialDelay(0, 0, 0); //! Not Working
        System.out.println("Initial Delay: " + initialDelay + " ms");
        long period = TimeUnit.DAYS.toMillis(1);
        scheduler.scheduleAtFixedRate(() -> {
            try {
                System.out.println("Running daily task");
                dbController.unfreezeAccount().run();
                dbController.checkDueBooks().run();
                dbController.checkReservationDue().run(); //! remove it not needed
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, period, TimeUnit.DAYS);
    }
    /**
     * This method computes the initial delay
     *
     * @param hour the hour
     * @param minute the minute
     * @param second the second
     * @return the initial delay
     */
    //! Not Working
    private long computeInitialDelay(int hour, int minute, int second) {
        Calendar nextRun = Calendar.getInstance();
        nextRun.set(Calendar.HOUR_OF_DAY, hour);
        nextRun.set(Calendar.MINUTE, minute);
        nextRun.set(Calendar.SECOND, second);
        nextRun.set(Calendar.MILLISECOND, 0);

        if (nextRun.getTime().before(new Date())) {
            nextRun.add(Calendar.DAY_OF_MONTH, 1);
        }

        return nextRun.getTimeInMillis() - System.currentTimeMillis();
    }
    /**
     * This method stops the scheduler
     */
    public void stopScheduler() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }
    }
}