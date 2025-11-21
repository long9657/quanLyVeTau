package app;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import com.app.qlvetau.view.MainFrame;

/**
 * Entry point to launch the Quản lý bán vé tàu application.
 * Place this file in src/app/App.java and run it.
 */
public class app {
    public static void main(String[] args) {
        // Set system look & feel if possible
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Launch UI on EDT
        SwingUtilities.invokeLater(() -> {
            MainFrame main = new MainFrame();
            main.setVisible(true);
        });
    }
}