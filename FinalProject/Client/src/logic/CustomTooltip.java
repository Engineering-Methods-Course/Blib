package logic;

import com.sun.org.slf4j.internal.LoggerFactory;
import javafx.scene.control.Tooltip;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import javafx.util.Duration;

public class CustomTooltip extends Tooltip
{
    /**
     * Creates a new CustomTooltip with the given text.
     */
    public CustomTooltip(String text)
    {
        super(text);
        this.setOpacity(0.8);
    }

    /**
     * Returns true if successful.
     * Current defaults are 1000, 5000, 200;
     *
     * @param openDelay The delay before the tooltip opens
     * @param visibleDuration The duration the tooltip is visible
     * @param closeDelay The delay before the tooltip closes
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static boolean setTooltipTimers(long openDelay, long visibleDuration, long closeDelay)
    {
        try
        {
            // Get the BEHAVIOR field from the Tooltip class
            Field f = Tooltip.class.getDeclaredField("BEHAVIOR");
            f.setAccessible(true);

            // Get the TooltipBehavior class from the Tooltip class
            Class[] classes = Tooltip.class.getDeclaredClasses();
            for (Class clazz : classes)
            {
                // If the class is the TooltipBehavior class then set the tooltip timers
                if (clazz.getName().equals("javafx.scene.control.Tooltip$TooltipBehavior"))
                {
                    // Get the constructor of the TooltipBehavior class
                    Constructor ctor = clazz.getDeclaredConstructor(Duration.class, Duration.class, Duration.class, boolean.class);
                    ctor.setAccessible(true);
                    Object tooltipBehavior = ctor.newInstance(new Duration(openDelay), new Duration(visibleDuration), new Duration(closeDelay), false);
                    f.set(null, tooltipBehavior);
                    break;
                }
            }
        }
        // Catch any exceptions and log them
        catch (Exception e)
        {
            LoggerFactory.getLogger(CustomTooltip.class).error("Unexpected", e);
            return false;
        }
        return true;
    }
}