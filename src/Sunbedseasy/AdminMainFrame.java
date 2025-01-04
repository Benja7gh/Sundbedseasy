package Sunbedseasy;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AdminMainFrame extends JFrame {
    
	private static final long serialVersionUID = 1L;
	private int adminId;

    public AdminMainFrame(int adminId) {
        this.adminId = adminId;
        setTitle("Admin Panel");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);

        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(10, 20, 80, 25);
        panel.add(usernameLabel);

        JTextField usernameField = new JTextField(20);
        usernameField.setBounds(100, 20, 165, 25);
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        JTextField passwordField = new JTextField(20);
        passwordField.setBounds(100, 50, 165, 25);
        panel.add(passwordField);

        JCheckBox adminCheckBox = new JCheckBox("Es Administrador");
        adminCheckBox.setBounds(100, 80, 165, 25);
        panel.add(adminCheckBox);

        JButton registerButton = new JButton("Registrar");
        registerButton.setBounds(10, 110, 150, 25);
        panel.add(registerButton);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = passwordField.getText();
                boolean esAdministrador = adminCheckBox.isSelected();
                registerUser(username, password, esAdministrador);
            }
        });
    }

    private void registerUser(String username, String password, boolean esAdministrador) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO usuario (username, password, esAdministrador, role) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setBoolean(3, esAdministrador);
            statement.setString(4, esAdministrador ? "ADMIN" : "USER");
            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Usuario registrado exitosamente");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al registrar usuario", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}