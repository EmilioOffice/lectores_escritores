import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Problema de los Lectores y Escritores");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 700);
            frame.setLocationRelativeTo(null);
            
            PanelSimulacion panel = new PanelSimulacion();
            frame.add(panel);
            
            frame.setVisible(true);
        });
    }
}