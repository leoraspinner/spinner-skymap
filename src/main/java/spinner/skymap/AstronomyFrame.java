package spinner.skymap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AstronomyFrame extends JFrame {
    private final SkyPanel skyPanel = new SkyPanel();
    private final JTextField locationField = new JTextField(20);
    private final JButton searchButton = new JButton("Search");
    private AstronomyController astronomyController;

    public AstronomyFrame() {
        setTitle("Sky");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);

        setupUi();
        setupControllers();
    }

    private void setupUi() {
        setLayout(new BorderLayout());

        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.add(new JLabel("Location:"));
        searchPanel.add(locationField);
        searchPanel.add(searchButton);

        add(searchPanel, BorderLayout.NORTH);
        add(skyPanel, BorderLayout.CENTER);

        locationField.setText("Cedarhurst, NY");

        pack();
        setLocationRelativeTo(null);
    }

    private void setupControllers() {
        astronomyController = new AstronomyController(skyPanel);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                astronomyController.searchLocation(locationField.getText());
            }
        });

        locationField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                astronomyController.searchLocation(locationField.getText());
            }
        });

        astronomyController.refreshSkyData();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AstronomyFrame frame = new AstronomyFrame();
            frame.setVisible(true);
        });
    }

}
