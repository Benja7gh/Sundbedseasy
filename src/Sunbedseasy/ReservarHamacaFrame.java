package Sunbedseasy;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ReservarHamacaFrame extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField habitacionField;
    private JPanel hamacasPanel;
    private int userId;

    public ReservarHamacaFrame(int userId) {
        this.userId = userId;
        setTitle("Reservar Hamaca");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        add(panel);
        placeComponents(panel);

        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel habitacionLabel = new JLabel("Número de Habitación:");
        topPanel.add(habitacionLabel);

        habitacionField = new JTextField(20);
        topPanel.add(habitacionField);

        JButton buscarHamacasButton = new JButton("Buscar Hamacas");
        topPanel.add(buscarHamacasButton);

        panel.add(topPanel, BorderLayout.NORTH);

        hamacasPanel = new JPanel();
        hamacasPanel.setLayout(new GridLayout(0, 4, 10, 10));
        JScrollPane scrollPane = new JScrollPane(hamacasPanel);
        panel.add(scrollPane, BorderLayout.CENTER);

        buscarHamacasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarHamacasDisponibles();
            }
        });
    }

    private void buscarHamacasDisponibles() {
        hamacasPanel.removeAll();
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT idHamaca, numeroHamaca FROM hamaca WHERE estado = 'libre'";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int idHamaca = resultSet.getInt("idHamaca");
                int numeroHamaca = resultSet.getInt("numeroHamaca");
                JButton hamacaButton = new JButton("Hamaca " + numeroHamaca);
                hamacaButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        reservarHamaca(idHamaca);
                    }
                });
                hamacasPanel.add(hamacaButton);
            }
            hamacasPanel.revalidate();
            hamacasPanel.repaint();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al buscar hamacas disponibles", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void reservarHamaca(int idHamaca) {
        String numeroHabitacion = habitacionField.getText();

        if (numeroHabitacion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El número de habitación es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            // Verificar que el número de habitación existe
            String checkHabitacionQuery = "SELECT idHabitacion FROM habitacion WHERE numeroHabitacion = ?";
            PreparedStatement checkHabitacionStmt = connection.prepareStatement(checkHabitacionQuery);
            checkHabitacionStmt.setString(1, numeroHabitacion);
            ResultSet habitacionResult = checkHabitacionStmt.executeQuery();

            if (!habitacionResult.next()) {
                JOptionPane.showMessageDialog(this, "Número de habitación no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int idHabitacion = habitacionResult.getInt("idHabitacion");

            // Actualizar el estado de la hamaca
            String updateHamacaQuery = "UPDATE hamaca SET estado = 'reservada' WHERE idHamaca = ?";
            PreparedStatement updateHamacaStmt = connection.prepareStatement(updateHamacaQuery);
            updateHamacaStmt.setInt(1, idHamaca);
            updateHamacaStmt.executeUpdate();

            // Insertar la reserva
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String fechaInicio = today.format(formatter);
            String fechaFin = today.plusDays(1).format(formatter);

            String insertReservaQuery = "INSERT INTO reserva (idUsuario, idHabitacion, idHamaca, fechaInicio, fechaFin) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement insertReservaStmt = connection.prepareStatement(insertReservaQuery);
            insertReservaStmt.setInt(1, userId);
            insertReservaStmt.setInt(2, idHabitacion);
            insertReservaStmt.setInt(3, idHamaca); // Incluir idHamaca en la reserva
            insertReservaStmt.setString(4, fechaInicio);
            insertReservaStmt.setString(5, fechaFin);
            insertReservaStmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Hamaca reservada exitosamente");
            this.dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al reservar hamaca", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}