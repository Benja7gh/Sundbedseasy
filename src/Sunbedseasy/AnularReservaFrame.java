package Sunbedseasy;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AnularReservaFrame extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField habitacionField;
    private JPanel hamacasPanel;

    public AnularReservaFrame() {
        setTitle("Anular Reserva");
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

        JButton buscarHamacasButton = new JButton("Buscar Hamacas Reservadas");
        topPanel.add(buscarHamacasButton);

        JButton cancelarTodasButton = new JButton("Cancelar Todas las Reservas");
        topPanel.add(cancelarTodasButton);

        panel.add(topPanel, BorderLayout.NORTH);

        hamacasPanel = new JPanel();
        hamacasPanel.setLayout(new GridLayout(0, 4, 10, 10));
        JScrollPane scrollPane = new JScrollPane(hamacasPanel);
        panel.add(scrollPane, BorderLayout.CENTER);

        buscarHamacasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarHamacasReservadas();
            }
        });

        cancelarTodasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String numeroHabitacion = habitacionField.getText();

                if (numeroHabitacion.isEmpty()) {
                    JOptionPane.showMessageDialog(AnularReservaFrame.this, 
                                                  "El número de habitación es obligatorio", 
                                                  "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int confirmacion = JOptionPane.showConfirmDialog(AnularReservaFrame.this,
                        "¿Estás seguro de que deseas cancelar todas las reservas de esta habitación?",
                        "Confirmar Cancelación",
                        JOptionPane.YES_NO_OPTION);
                if (confirmacion == JOptionPane.YES_OPTION) {
                    cancelarTodasReservasDeHabitacion(numeroHabitacion);
                }
            }
        });
    }

    
    private void cancelarTodasReservasDeHabitacion(String numeroHabitacion) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            // Primero, obtener las reservas de la habitación
            String getReservasQuery = "SELECT r.idReserva, h.idHamaca FROM reserva r " +
                                      "JOIN hamaca h ON r.idHamaca = h.idHamaca " +
                                      "JOIN habitacion hab ON r.idHabitacion = hab.idHabitacion " +
                                      "WHERE hab.numeroHabitacion = ?";
            PreparedStatement getReservasStmt = connection.prepareStatement(getReservasQuery);
            getReservasStmt.setString(1, numeroHabitacion);
            ResultSet reservasResultSet = getReservasStmt.executeQuery();

            // Bucle a través de todas las reservas para anularlas
            while (reservasResultSet.next()) {
                int idReserva = reservasResultSet.getInt("idReserva");
                int idHamaca = reservasResultSet.getInt("idHamaca");

                // Eliminar la reserva
                String deleteReservaQuery = "DELETE FROM reserva WHERE idReserva = ?";
                PreparedStatement deleteReservaStmt = connection.prepareStatement(deleteReservaQuery);
                deleteReservaStmt.setInt(1, idReserva);
                deleteReservaStmt.executeUpdate();

                // Actualizar el estado de la hamaca a 'libre'
                String updateHamacaQuery = "UPDATE hamaca SET estado = 'libre' WHERE idHamaca = ?";
                PreparedStatement updateHamacaStmt = connection.prepareStatement(updateHamacaQuery);
                updateHamacaStmt.setInt(1, idHamaca);
                updateHamacaStmt.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Todas las reservas de la habitación han sido anuladas y las hamacas están ahora libres.");
            buscarHamacasReservadas();  // Actualizar la lista de hamacas reservadas

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cancelar las reservas de la habitación", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    private void buscarHamacasReservadas() {
        hamacasPanel.removeAll();
        String numeroHabitacion = habitacionField.getText();

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT h.numeroHamaca, r.idReserva " +
                    "FROM hamaca h " +
                    "LEFT JOIN reserva r ON h.idHamaca = r.idHamaca " +
                    "LEFT JOIN habitacion hab ON r.idHabitacion = hab.idHabitacion " +
                    "WHERE hab.numeroHabitacion = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, numeroHabitacion);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int numeroHamaca = resultSet.getInt("numeroHamaca");
                int idReserva = resultSet.getInt("idReserva");
                JButton hamacaButton = new JButton("Hamaca " + numeroHamaca);
                hamacaButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        confirmarCancelarReserva(idReserva);
                    }
                });
                hamacasPanel.add(hamacaButton);
            }

            hamacasPanel.revalidate();
            hamacasPanel.repaint();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al buscar hamacas reservadas", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void confirmarCancelarReserva(int idReserva) {
        int confirmacion = JOptionPane.showConfirmDialog(AnularReservaFrame.this,
                "¿Estás seguro de que deseas cancelar esta reserva?",
                "Confirmar Cancelación",
                JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            cancelarReserva(idReserva);
        }
    }

    private void cancelarReserva(int idReserva) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            // Primero, obtener el idHamaca asociado a la reserva
            String getHamacaQuery = "SELECT idHamaca FROM reserva WHERE idReserva = ?";
            PreparedStatement getHamacaStatement = connection.prepareStatement(getHamacaQuery);
            getHamacaStatement.setInt(1, idReserva);
            ResultSet resultSet = getHamacaStatement.executeQuery();

            int idHamaca = -1;
            if (resultSet.next()) {
                idHamaca = resultSet.getInt("idHamaca");
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró la reserva para anular", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Eliminar la reserva
            String deleteQuery = "DELETE FROM reserva WHERE idReserva = ?";
            PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
            deleteStatement.setInt(1, idReserva);
            int rowsAffected = deleteStatement.executeUpdate();

            if (rowsAffected > 0) {
                // Actualizar el estado de la hamaca
                String updateHamacaQuery = "UPDATE hamaca SET estado = 'libre' WHERE idHamaca = ?";
                PreparedStatement updateHamacaStatement = connection.prepareStatement(updateHamacaQuery);
                updateHamacaStatement.setInt(1, idHamaca);
                updateHamacaStatement.executeUpdate();

                JOptionPane.showMessageDialog(this, "Reserva anulada correctamente y hamaca marcada como disponible");
                buscarHamacasReservadas(); // Actualizar la lista después de cancelar la reserva
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró la reserva para anular", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al anular la reserva", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    }