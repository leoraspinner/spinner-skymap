package spinner.skymap;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SkyPanel extends JPanel {
    private List<CelestialBody> bodies = new ArrayList<>();

    public void setBodies(List<CelestialBody> bodies) {
        this.bodies = bodies;
        repaint();
    }

    public SkyPanel() {
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(800, 800));
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid((Graphics2D) g);
        drawBodies((Graphics2D) g);
    }

    private void drawGrid(Graphics2D g) {
        int width = getWidth();
        int height = getHeight();
        int centerX = width / 2;
        int centerY = height / 2;

        g.setColor(Color.YELLOW);
        g.setStroke(new BasicStroke(2));



        // Draw horizon line (horizontal)
        g.drawLine(0, centerY, width, centerY);

        // Draw meridian line (vertical)
        g.drawLine(centerX, 0, centerX, height);

        // Draw additional grid lines
        g.drawLine(centerX / 2, 0, centerX / 2, height);
        g.drawLine(centerX + centerX / 2, 0, centerX + centerX / 2, height);

        // Draw cardinal directions
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Horizon", centerX + 10, centerY - 10);

        int southY = height - 10;
        g.drawString("South", 10, southY);
        int westX = width / 4 + 2;
        g.drawString("West", westX, southY);
        int northX = width / 2 + 2;
        g.drawString("North", northX, southY);
        int eastX = width * 3 / 4 + 2;
        g.drawString("East", eastX, southY);
        int southRight = width - g.getFontMetrics().stringWidth("South") - 14;
        g.drawString("South", southRight, southY);
    }

    private void drawBodies(Graphics2D g) {
        for (CelestialBody body : bodies) {
            if (body.altitude < 0) {
                continue;
            }

            Point point = projectToScreen(body.azimuth, body.altitude);

            //white circle for celestial body
            g.setColor(Color.WHITE);
            g.fillOval(point.x - 5, point.y - 5, 10, 10);

            //red label
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 12));
            g.drawString(body.name, point.x + 8, point.y + 4);
        }
    }

    private Point projectToScreen(double azimuth, double altitude) {
        int width = getWidth();
        int height = getHeight();
        int centerX = width / 2;
        int centerY = height / 2;
        int maxRadius = Math.min(width, height) / 2 - 50;

        //altitude to radius
        double radius = maxRadius * (90 - altitude) / 90.0;

        //azimuth to radians
        double angleRed = Math.toRadians(azimuth - 90);

        int x = (int) (centerX + radius * Math.cos(angleRed));
        int y = (int) (centerY + radius * Math.sin(angleRed));

        return new Point(x, y);
    }


}
