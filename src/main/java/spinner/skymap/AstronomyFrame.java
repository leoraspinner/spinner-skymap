package spinner.skymap;

import javax.swing.*;

public class AstronomyFrame extends JFrame {
    private final SkyPanel skyPanel = new SkyPanel();

    public AstronomyFrame() {
        setTitle("Sky");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);

        add(skyPanel);
        pack();
        setLocationRelativeTo(null);

        AstronomyService service = new AstronomyServiceFactory().getService();
        AstronomyController controller = new AstronomyController(skyPanel, service);
        controller.display();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AstronomyFrame frame = new AstronomyFrame();
            frame.setVisible(true);
        });
    }

}
