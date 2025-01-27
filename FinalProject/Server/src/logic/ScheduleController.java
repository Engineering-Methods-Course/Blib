package logic;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduleController {

    private static volatile ScheduleController instance;
    private static DBController dbController;
    private final ScheduledExecutorService scheduler;

    /**
     * Constructor to initialize the ScheduleController object.
     */
    private ScheduleController() {
        scheduler = Executors.newScheduledThreadPool(1);
        dbController = DBController.getInstance();
        setSchedulerDailyTask();
        setSchedulerWeeklyTask();
        setSchedulerMonthlyTask();

    }

    /**
     * This method creates a new instance of ScheduleController if it doesn't exist
     *
     * @return the instance of ScheduleController
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
     * This method sets the scheduler for the export logs
     */
    public void setSchedulerMonthlyTask() {

        /*
         * Set the initial delay to the first of the month
         */
        long initialDelay = computeInitialDelayForFirstOfMonth(1, 0, 0);
        long period = TimeUnit.DAYS.toMillis(30);

        /*
         * Schedule the task to run every month
         */
        scheduler.scheduleAtFixedRate(() -> {
            try {
                System.out.println("Running monthly task");
                System.out.println("Exporting report");
                dbController.exportReport().run();
                System.out.println("finished monthly task");
            } catch (Exception e) {
                System.out.println("Error in export log" + e);
            }
        }, initialDelay, period, TimeUnit.MILLISECONDS);
    }


    /**
     * This method runs the daily task
     */
    public void setSchedulerDailyTask() {

        /*
         * Set the initial delay to run the task every day at 7:00:00
         */
        long initialDelay = computeInitialDelayEvreyDayAtTime(7, 0, 0);
        long period = TimeUnit.DAYS.toMillis(1);

        /*
         * Schedule the task to run every day
         */
        scheduler.scheduleAtFixedRate(() -> {
            try {
                System.out.println("Running daily task");
                System.out.println("Unfreezing accounts");
                dbController.unfreezeAccount().run();
                System.out.println("Checking due books");
                dbController.checkDueBooks().run();
                System.out.println("Checking reserved books");
                dbController.checkReservedBooks().run();
                System.out.println("finished daily task");
            } catch (Exception e) {
                System.out.println("Error in daily task" + e);
            }
        }, initialDelay, period, TimeUnit.MILLISECONDS);
    }
    /**
     * This method sets the scheduler for a weekly task.
     */
    public void setSchedulerWeeklyTask() {

        /*
         * Set the initial delay to run the task every Sunday at 6:00:00
         */
        long initialDelay = computeInitialDelayForNextWeek(6, 0, 0);
        long period = TimeUnit.DAYS.toMillis(7);

        /*
         * Schedule the task to run every week
         */
        scheduler.scheduleAtFixedRate(() -> {
            try {
                System.out.println("Running weekly task");
                System.out.println("Checking subscribers status");
                dbController.checkSubscribersStatus().run();
                System.out.println("finished weekly task");
            } catch (Exception e) {
                System.out.println("Error in weekly task" + e);
            }
        }, initialDelay, period, TimeUnit.MILLISECONDS);
    }

    /**
     * This method computes the initial delay for the next week.
     *
     * @param hour   the hour
     * @param minute the minute
     * @param second the second
     * @return the initial delay
     */
    private long computeInitialDelayForNextWeek(int hour, int minute, int second) {
        Calendar nextRun = Calendar.getInstance();

        /*
         * Set the next run time to the specified inputs
         */
        nextRun.set(Calendar.HOUR_OF_DAY, hour);
        nextRun.set(Calendar.MINUTE, minute);
        nextRun.set(Calendar.SECOND, second);
        nextRun.set(Calendar.MILLISECOND, 0);

        /*
         * Set the day of the week to the desired day (e.g., Calendar.MONDAY)
         */
        nextRun.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

        /*
         * If the next run time is before the current time, add one week
         */
        if (nextRun.getTimeInMillis() <= System.currentTimeMillis()) {
            nextRun.add(Calendar.WEEK_OF_YEAR, 1);
        }

        /*
         * Return the difference between the next run time and the current time
         */
        return nextRun.getTimeInMillis() - System.currentTimeMillis();
    }

    /**
     * This method computes the initial delay
     *
     * @param hour   the hour
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
     * @param hour   the hour
     * @param minute the minute
     * @param second the second
     * @return long the initial delay
     */
    private long computeInitialDelayForFirstOfMonth(int hour, int minute, int second) {

        /*
         * Set the next run time to the first of the month
         */
        Calendar nextRun = Calendar.getInstance();
        nextRun.set(Calendar.HOUR_OF_DAY, hour);
        nextRun.set(Calendar.MINUTE, minute);
        nextRun.set(Calendar.SECOND, second);
        nextRun.set(Calendar.MILLISECOND, 0);
        nextRun.set(Calendar.DAY_OF_MONTH, 1);

        /*
         * If the next run time is before the current time, add one month
         */
        if (nextRun.getTime().before(new Date())) {
            nextRun.add(Calendar.MONTH, 1);
        }

        /*
         * Return the difference between the next run time and the current time
         */
        return nextRun.getTimeInMillis() - System.currentTimeMillis();
    }

}