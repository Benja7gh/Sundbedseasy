package Sunbedseasy;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserMainFrame extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int userId;

    public UserMainFrame(int userId) {
        this.userId = userId;
        setTitle(" Reserva con Sunbedseasy ");
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

        JButton reservarButton = new JButton("Reservar Hamaca");
        reservarButton.setBounds(10, 20, 150, 25);
        panel.add(reservarButton);

        JButton anularButton = new JButton("Anular Reserva");
        anularButton.setBounds(10, 60, 150, 25);
        panel.add(anularButton);

        JButton consultarButton = new JButton("Consultar Hamacas");
        consultarButton.setBounds(10, 100, 150, 25);
        panel.add(consultarButton);

        reservarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ReservarHamacaFrame(userId).setVisible(true);
            }
        });

        anularButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AnularReservaFrame().setVisible(true); // Cambiar a constructor sin argumentos
            }
        });

        consultarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ConsultarHamacasFrame().setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        new UserMainFrame(1); // Para probar con userId = 1
    }
}