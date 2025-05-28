package spinner.astronomy;

import io.reactivex.rxjava3.core.Single;
import spinner.astronomy.json.AstronomyResponse;

import javax.swing.*;

public class AstronomyFrame extends JFrame {
    private final JLabel altitudeLabel = new JLabel();
    private final JLabel azimuthLabel = new JLabel();

    public AstronomyFrame() {
        setTitle("Astronomy Viewer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.add(altitudeLabel);
        panel.add(azimuthLabel);
        add(panel);

        AstronomyService service = new AstronomyServiceFactory().getService();
        new AstronomyController(altitudeLabel, azimuthLabel, service).display();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AstronomyFrame frame = new AstronomyFrame();
            frame.setVisible(true);
        });
    }

}
