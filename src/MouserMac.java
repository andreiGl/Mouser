import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Random;

public class MouserMac {
    private static LocalTime getStopTime(String stop) {
        System.out.println("Setting stopTime to " + stop);
        LocalTime stopTime;
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

        // Start caffeinate process to prevent sleep/screen timeout on macOS
        Process caffeinateProcess = null;
        try {
            caffeinateProcess = new ProcessBuilder("caffeinate", "-dimsu").start();
            System.out.println("Started caffeinate process to prevent sleep.");
        } catch (Exception e) {
            System.out.println("Failed to start caffeinate: " + e.getMessage());
        }
        Robot r = new Robot();
        Random rand = new Random();
        PointerInfo pInfo;
        int direction = 1;
        try {
            while (true) {
                if (stopTime != null && isStopTimeNow(stopTime)) {
                    break;
                }
                r.delay(1000 * rand.nextInt(20) + 50);
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
        } finally {
            // Stop caffeinate process when done
            if (caffeinateProcess != null) {
                caffeinateProcess.destroy();
                System.out.println("Stopped caffeinate process.");
            }
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
            MouserMac.process(null);
        } else {
            MouserMac.process(args[0]);
        }
    }
}

