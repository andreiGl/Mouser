import java.awt.*;

public class Mouser {

    public static void main(String[] args) throws Exception {
        Robot r = new Robot();
        int direction = 1;
        while (true) {
            r.delay(1000 * 60);
            Point p = MouseInfo.getPointerInfo().getLocation();
            int x = (int) p.getX();
            int y = (int) p.getY();
            r.mouseMove(x + direction, y);
            direction *= -1;
        }
    }
}