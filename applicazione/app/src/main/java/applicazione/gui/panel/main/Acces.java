package applicazione.gui.panel.main;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

import applicazione.controller.Controller;
import applicazione.dao.DAOException;
import applicazione.dao.DAOUtils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.sql.Connection;

public class Acces extends JPanel{
    private static final String CONTROL_CLIENT_QUERY = "SELECT mail, password FROM CLIENTE WHERE mail = ?";
    private static final String CONTROL_ACCESS_QUERY = "SELECT mail, password FROM ADMIN WHERE mail = ? AND password = ?";
    private final Connection connection = DAOUtils.localMySQLConnection("casa_dei_giochi", "root", "el@pFG2020");
    private final Controller controller;
    private final JPanel accesData = new JPanel();
    private final JPanel buttons = new JPanel();
    private final JButton login = new JButton("Login");
    private final JButton clientRegister = new JButton("Registra cliente");
    private final JButton adminRegister = new JButton("Registra admin");
    private final JTextArea insertMail = new JTextArea();
    private final JTextArea insertPassword = new JTextArea();
    private final JTextArea mail = new JTextArea("mail");
    private final JTextArea password = new JTextArea("password");

    public Acces (final Controller controller) {
        this.controller = controller;
        setPreferredSize(new Dimension(500, 120));
        setLayout(new BorderLayout());
        add(new JTextArea("Casa dei Giochi"), BorderLayout.NORTH);
        login.addActionListener(e -> security(insertMail.getText(), insertPassword.getText()));
        add(login, BorderLayout.SOUTH);
        accesData.setLayout(new GridLayout(2, 2));
        accesData.add(mail);
        mail.setEditable(false);
        mail.setBorder(new LineBorder(Color.BLACK));
        accesData.add(insertMail);
        insertMail.setEditable(true);
        insertMail.setBorder(new LineBorder(Color.BLACK));
        accesData.add(password);
        insertPassword.setEditable(true);
        insertPassword.setBorder(new LineBorder(Color.BLACK));
        accesData.add(insertPassword);
        password.setEditable(false);
        password.setBorder(new LineBorder(Color.BLACK));
        accesData.setBorder(new LineBorder(Color.BLACK));
        add(accesData, BorderLayout.CENTER);
        buttons.setLayout(new GridLayout(1, 3));
        buttons.add(login);
        buttons.add(clientRegister);
        buttons.add(adminRegister);
        add(buttons, BorderLayout.SOUTH);
        adminRegister.addActionListener(e -> controller.registerNewAdmin());
        clientRegister.addActionListener(e -> controller.registerNewClient());
    }

    private void security (final String mail, final String password) {
        if (this.checkAdmin(mail, password)) {
            this.controller.admin(mail);
        } else if (this.checkClient(mail, password)) {
            this.controller.client(mail);
        } else {
            JOptionPane.showMessageDialog(this,"Mail o password sbagliate","Error", JOptionPane.PLAIN_MESSAGE);
        }
    }

    private boolean checkClient (final String mail, final String password) {
        try (
            var stm = DAOUtils.prepare(connection, CONTROL_CLIENT_QUERY, mail);
            var rS = stm.executeQuery();
        ) {
            return rS.next();
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

    private boolean checkAdmin (final String mail, final String password) {
        try (
            var stm = DAOUtils.prepare(connection, CONTROL_ACCESS_QUERY, mail, password);
            var rS = stm.executeQuery();
        ) {
            return rS.next();
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

    private void setNewPanel (final JPanel newPanel) {
        removeAll();
        add(newPanel);
        revalidate();
        repaint();
    }

}
