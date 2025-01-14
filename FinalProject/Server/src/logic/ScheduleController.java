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
        runDailyTask();
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
    /**
     * This method sets the scheduler for the export logs
     */
    public void setSchedulerExportLog() {
        long initialDelay = computeInitialDelayForFirstOfMonth(1, 0, 0);
        long period = TimeUnit.DAYS.toMillis(30);
        scheduler.scheduleAtFixedRate(() -> {
            try {
                System.out.println("Running daily task");
                dbController.exportLogSubscribersStatus().run();
                dbController.exportLogBorrowTime().run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, initialDelay, period, TimeUnit.MILLISECONDS);
    }


    /**
     * This method runs the daily task
     */
    public void runDailyTask() {
        long initialDelay = computeInitialDelayEvreyDayAtTime(7, 0, 0);
        long period = TimeUnit.DAYS.toMillis(1);
        scheduler.scheduleAtFixedRate(() -> {
            try {
                System.out.println("Running daily task");
                dbController.unfreezeAccount().run();
                dbController.checkDueBooks().run();
                dbController.checkReservedBooks().run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, initialDelay, period, TimeUnit.MILLISECONDS);
    }

    /**
     * This method computes the initial delay
     *
     * @param hour the hour
     * @param minute the minute
     * @param second the second
     * @return the initial delay
     */
    private long computeInitialDelayEvreyDayAtTime(int hour, int minute, int second) {

        Calendar nextRun = Calendar.getInstance();

        /*
         * Set the next run time to the specified inputs
         */
        nextRun.set(Calendar.HOUR_OF_DAY, hour);
        nextRun.set(Calendar.MINUTE, minute);
        nextRun.set(Calendar.SECOND, second);
        nextRun.set(Calendar.MILLISECOND, 0);

        /*
         * If the next run time is before the current time, add one day
         */
        if (nextRun.getTimeInMillis() <= System.currentTimeMillis()) {
            nextRun.add(Calendar.DAY_OF_MONTH, 1);
        }

        /*
         * Return the difference between the next run time and the current time
         */
        return nextRun.getTimeInMillis() - System.currentTimeMillis();
    }
    /**
     * This sets the scheduler for the first of the month
     *
     * @param hour the hour
     * @param minute the minute
     * @param second the second
     * @return long the initial delay
     */
    private long computeInitialDelayForFirstOfMonth(int hour, int minute, int second) {
        Calendar nextRun = Calendar.getInstance();
        nextRun.set(Calendar.HOUR_OF_DAY, hour);
        nextRun.set(Calendar.MINUTE, minute);
        nextRun.set(Calendar.SECOND, second);
        nextRun.set(Calendar.MILLISECOND, 0);
        nextRun.set(Calendar.DAY_OF_MONTH, 1);

        if (nextRun.getTime().before(new Date())) {
            nextRun.add(Calendar.MONTH, 1);
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