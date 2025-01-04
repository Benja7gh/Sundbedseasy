package Sunbedseasy;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConsultarHamacasFrame extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel hamacasPanel;

    public ConsultarHamacasFrame() {
        setTitle("Consultar Hamacas");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        add(panel);
        placeComponents(panel);

        setVisible(true);

       
    }

    private void placeComponents(JPanel panel) {
        hamacasPanel = new JPanel();
        hamacasPanel.setLayout(new GridLayout(0, 4, 10, 10));
        JScrollPane scrollPane = new JScrollPane(hamacasPanel);
        panel.add(scrollPane, BorderLayout.CENTER);
     // Llamar a consultarHamacas después de configurar el panel
        consultarHamacas();
    }

    private void consultarHamacas() {
        hamacasPanel.removeAll();
        try (Connection connection = DatabaseConnection.getConnection()) {
        	String query = "SELECT h.numeroHamaca, COALESCE(h2.numeroHabitacion, 'Libre') AS estado " +
                    "FROM hamaca h " +
                    "LEFT JOIN reserva r ON h.idHamaca = r.idHamaca " +
                    "LEFT JOIN habitacion h2 ON r.idHabitacion = h2.idHabitacion";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int numeroHamaca = resultSet.getInt("numeroHamaca");
                String estado = resultSet.getString("estado");
                JLabel hamacaLabel = new JLabel("Hamaca " + numeroHamaca + ": " + (estado.equals("Libre") ? "Libre" : "Habitación " + estado));
                hamacasPanel.add(hamacaLabel);
            }

            hamacasPanel.revalidate();
            hamacasPanel.repaint();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al consultar hamacas", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new ConsultarHamacasFrame();
    }
}