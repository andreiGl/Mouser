import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Random;

public class Mouser {
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

    public static void process(String stop) {
        LocalTime stopTime = stop != null ? getStopTime(stop) : null;
        String os = System.getProperty("os.name").toLowerCase();
        boolean isMac = os.contains("mac");
        Process caffeinateProcess = null;
        try {
            if (isMac) {
                caffeinateProcess = new ProcessBuilder("caffeinate", "-dimsu").start();
                System.out.println("Started caffeinate process to prevent sleep.");
            }
            Robot r = null;
            Random rand = null;
            PointerInfo pInfo;
            int direction = 1;
            if (!isMac) {
                r = new Robot();
                rand = new Random();
            }
            while (true) {
                if (stopTime != null && isStopTimeNow(stopTime)) {
                    break;
                }
                if (isMac) {
                    // Just wait, caffeinate keeps the system awake
                    Thread.sleep(1000 * 10); // sleep for 10 seconds
                } else {
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
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            if (isMac && caffeinateProcess != null) {
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

    public static void main(String[] args) {
        if (args == null || args.length < 1) {
            System.out.println("Optional stopTime could be set in 24h time format, like: 14:28");
            Mouser.process(null);
        } else {
            Mouser.process(args[0]);
        }
    }
}
