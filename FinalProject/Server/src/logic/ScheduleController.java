package logic;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduleController {

    private static ScheduleController instance = null;
    private final ScheduledExecutorService scheduler;
    private DBController dbController;
    private NotificationController notificationController;

    private ScheduleController() {
        scheduler = Executors.newScheduledThreadPool(1);
        notificationController = NotificationController.getInstance();
        dbController = DBController.getInstance();
    }

    /**
     * Gets the instance of the schedule controller
     *
     * @return the instance of the schedule controller
     */

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
     * Schedules a monthly task
     *
     * @param task the task to be scheduled
     */
    public void setSchedulerExportLog(Runnable task) {
        long initialDelay = computeInitialDelay(1, 0, 0);
        long period = TimeUnit.DAYS.toMillis(30);
        scheduler.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.MILLISECONDS);
    }
    /**
     * Schedules a task to be executed
     *
     * @param task the task to be scheduled
     */
    public void scheduleUnfreeze(Runnable task) {
        int days = 30;
        scheduler.schedule(task, days, TimeUnit.DAYS);
        System.out.println("Unfreeze task scheduled to run in " + days + " days");
    }
    /**
     * Schedules a daily task
     */
    public void scheduleBorrowReturnNotification() {
        long initialDelay = computeInitialDelay(5, 0, 0);
        long period = TimeUnit.DAYS.toMillis(1);
        scheduler.scheduleAtFixedRate(dbController.checkDueBooks(), initialDelay, period, TimeUnit.MILLISECONDS);
    }

    public void checkReservationDue() {
        long initialDelay = computeInitialDelay(5, 0, 0);
        long period = TimeUnit.DAYS.toMillis(1);
        scheduler.scheduleAtFixedRate(dbController.checkReservationDue(), initialDelay, period, TimeUnit.MILLISECONDS);
    }

    /**
     * Computes the initial delay for the scheduler
     *
     * @param hour   the hour of the day
     * @param minute the minute of the hour
     * @param second the second of the minute
     * @return the initial delay
     */
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
     * Stops the scheduler called when the server is closed
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
