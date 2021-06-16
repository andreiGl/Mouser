import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class Mouser {

    private static LocalTime getStopTime(String stop) {
        System.out.println("Setting stopTime to " + stop);
        LocalTime stopTime = null;
        try {
            stopTime = LocalTime.parse(stop);
        } catch (DateTimeParseException ex) {
            System.out.println("Invalid stopTime argument! \nTime format error: " + ex.getLocalizedMessage());
            return null;
        }
        System.out.println("StopTime: " + stopTime.toString());
        return stopTime;
    }

    public static void process(String stop) throws AWTException {
        LocalTime stopTime = stop != null ? getStopTime(stop) : null;

        Robot r = new Robot();
        int direction = 1;
        while (true) {
            if (stopTime != null && isStopTimeNow(stopTime)) {
                return;
            }

            r.delay(1000 * 60);
            PointerInfo pInfo;
            do {
                pInfo = MouseInfo.getPointerInfo();
                if (pInfo == null) {
                    r.delay(1000 * 2);
                }
            } while (pInfo == null);

            Point p = pInfo.getLocation();
            int x = (int) p.getX();
            int y = (int) p.getY();
            r.mouseMove(x + direction, y);
            direction *= -1;
        }
    }

    private static boolean isStopTimeNow(LocalTime stopTime) {
        LocalTime now = LocalTime.now();
        if (now.isAfter(stopTime)) {
            System.out.println("StopTime reached, current time: " + now);
            return true;
        }
        return false;
    }

    public static void main(String[] args) throws Exception {
        if (args == null || args.length < 1) {
            System.out.println("Optional stopTime could be set in 24h time format, like: 14:28");
            Mouser.process(null);
        } else {
            Mouser.process(args[0]);
        }
    }
}