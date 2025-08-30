package gui.panel.main;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

import controller.Controller;
import dao.DAOException;
import dao.DAOUtils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.sql.Connection;

public class Acces extends JPanel{
    private static final Dimension SCREE_DIMENSION = Toolkit.getDefaultToolkit().getScreenSize();
    private static final String MAIL = "admin";
    private static final String PASSWORD = "admin";
    private static final String MAIL_C = "client";
    private static final String PASSWORD_C = "client";
    private final Connection connection = DAOUtils.localMySQLConnection("casa_dei_giochi", "root", "el@pFG2020");
    private final JButton login = new JButton("Login");
    private final JTextArea insertMail = new JTextArea();
    private final JTextArea insertPassword = new JTextArea();
    private final JPanel accesData = new JPanel();
    private final Controller controller;
    private final JFrame mainFrame;

    public Acces (final Controller controller, final JFrame mainFrame) {
        this.controller = controller;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        add(new JTextArea("Casa dei Giochi"), BorderLayout.NORTH);
        login.addActionListener(e -> security(insertMail.getText(), insertPassword.getText()));
        add(login, BorderLayout.SOUTH);
        accesData.setLayout(new GridLayout(2, 2));
        accesData.add(new JTextArea("mail"));
        insertMail.setEditable(true);
        insertMail.setBorder(new LineBorder(Color.BLACK));
        accesData.add(insertMail);
        accesData.add(new JTextArea("password"));
        insertPassword.setEditable(true);
        insertPassword.setBorder(new LineBorder(Color.BLACK));
        accesData.add(insertPassword);
        add(accesData, BorderLayout.CENTER);
        accesData.setBorder(new LineBorder(Color.BLACK));
    }

    private void security (final String mail, final String password) {
        System.out.println(mail + password);
        if ( checkAdmin(mail) && PASSWORD.equals(password)) {
            System.out.println("sei admin");
            this.controller.admin();
        } else if (MAIL_C.equals(mail) && PASSWORD_C.equals(password)) {
            System.out.println("sei cliente");
            this.controller.client();
        } else {
            System.out.println("sei fuori");
            JOptionPane.showMessageDialog(this.mainFrame,"Mail o password sbagliate","Error", JOptionPane.PLAIN_MESSAGE);
        }
    }

    private boolean checkAdmin (final String mail) {
        try (
            var stm = DAOUtils.prepare(connection, "SELECT mail FROM ADMIN", "");
            var eR = stm.executeQuery();
        ) {
            while (eR.next()) {
                if (eR.getString("mail").equals(mail)) {
                    return true;
                }
            }
        } catch (Exception e) {
            throw new DAOException(e);
        }
        return false;
    }

}
