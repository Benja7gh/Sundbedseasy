package Sunbedseasy;

import javax.swing.SwingUtilities;

public class MainFrame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            
            public void run() {
                LoginFrame LoginFrame = new LoginFrame();
                LoginFrame.setVisible(true);
            }
        });
    }
}