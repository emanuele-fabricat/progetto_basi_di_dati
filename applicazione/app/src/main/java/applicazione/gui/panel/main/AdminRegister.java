package applicazione.gui.panel.main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.sql.Connection;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import applicazione.controller.Controller;
import applicazione.dao.DAOException;
import applicazione.dao.DAOUtils;

public class AdminRegister extends JPanel{
    private static final String INSERT_ADMIN = "INSERT INTO ADMIN (id_admin, mail, password) VALUES (?, ?, ?)";
    private static final String SECURITY_ID_QUERY = "SELECT id_admin FROM ADMIN WHERE id_admin = ?";
    private static final String SECURITY_MAIL_QUERY = "SELECT mail FROM ADMIN WHERE mail = ?";
    private final Controller controller;
    private final Connection connection = DAOUtils.localMySQLConnection("casa_dei_giochi", "root", "el@pFG2020");
    private final JTextArea mail = new JTextArea("mail");
    private final JTextArea password = new JTextArea("password");
    private final JTextArea insertMail = new JTextArea();
    private final JTextArea insertPassword = new JTextArea();
    private final JButton confirm = new JButton("confirm");
    private final JButton back = new JButton("indietro");


    public AdminRegister(final Controller controller) {
        this.controller = controller;
        setPreferredSize(new Dimension(500, 150));
        setLayout(new GridLayout(3, 2));
        add(this.mail);
        add(this.insertMail);
        add(this.password);
        add(this.insertPassword);
        add(this.confirm);
        add(this.back);
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        for (Component comp : this.getComponents()) {
            if (comp instanceof JTextArea) {
                ((JTextArea) comp).setBorder(border);
            }
        }
        this.mail.setEditable(false);
        this.password.setEditable(false);
        this.back.addActionListener(e -> this.controller.accessBoard());
        this.confirm.addActionListener(e -> this.newAdmin(this.insertMail.getText(), this.insertPassword.getText()));
    }

    private void newAdmin (final String mail, final String password) {
        String id = UUID.randomUUID().toString();
        if (!mail.endsWith("@casa.dei.giochi.it")) {
            JOptionPane.showMessageDialog(this, "La mail deve essere aziendale", "Error", JOptionPane.PLAIN_MESSAGE);
            return;
        }
        if (isWrongMail(mail)) {
            JOptionPane.showMessageDialog(this,"Mail già esistente, riprovare","Error", JOptionPane.PLAIN_MESSAGE);
            return;
        }
        while (isRightId(id)) {
            id = UUID.randomUUID().toString();
        }
        try (
            var stm = DAOUtils.prepare(connection, INSERT_ADMIN, id, mail, password);
        ) {
            stm.executeUpdate();
            JOptionPane.showMessageDialog(this,"Admin creato correttamente","Success", JOptionPane.PLAIN_MESSAGE);
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

    private boolean isRightId (final String id) {
        try (
            var stm = DAOUtils.prepare(connection, SECURITY_ID_QUERY, id);
            var rS = stm.executeQuery();
        ) {
            return rS.next();
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

    private boolean isWrongMail (final String mail) {
        try (
            var stm = DAOUtils.prepare(connection, SECURITY_MAIL_QUERY, mail);
            var rS = stm.executeQuery();
        ) {
            return rS.next();
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }
}
