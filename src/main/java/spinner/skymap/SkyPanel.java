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
        setPreferredSize(new Dimension(800, 600));
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawGrid(g2d);
        drawBodies(g2d);
    }

    private void drawGrid(Graphics2D g) {
        g.setColor(Color.YELLOW);
        g.setStroke(new BasicStroke(1));

        int width = getWidth();
        int height = getHeight();
        int centerX = width / 2;
        int centerY = height / 2;

        //horizontal line
        g.drawLine(0, centerY, width, centerY);

        //vertical line
        g.drawLine(centerX, 0, centerX, height);

        //grid lines
        g.drawLine(centerX / 2, 0, centerX / 2, height);
        g.drawLine(centerX + centerX / 2, 0, centerX + centerX / 2, height);

        //directions
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("North", centerX - 20, 20);
        g.drawString("South", centerX - 20, height - 10);
        g.drawString("East", width - 35, centerY - 10);
        g.drawString("West", 5, centerY - 10);
        g.drawString("Horizon", centerX + 10, centerY - 10);
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
