package applicazione.gui.panel.main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

public class ClientRegister extends JPanel{
    private static final String INSERT_CLIENT = "INSERT INTO CLIENTE (id_utente, mail, password, nome, cognome, data_di_nascita, indirizzo_1, indirizzo_2, indirizzo_3) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SECURITY_ID_QUERY = "SELECT id_utente FROM CLIENTE WHERE id_utente = ?";
    private static final String SECURITY_MAIL_QUERY = "SELECT mail FROM CLIENTE WHERE mail = ?";
    private static final int MAX_LENGTH_MAIL = 254;
    private static final int MAX_LENGTH_PASSWORD = 36;
    private static final int MAX_LENGTH_NAME = 20;
    private static final int MAX_LENGTH_SURNAME = 20;
    private static final int MAX_LENGTH_ADDRESS = 100;
    private final Controller controller;
    private final Connection connection = DAOUtils.localMySQLConnection("casa_dei_giochi", "root", "el@pFG2020");
    private final JTextArea mail = new JTextArea("mail");
    private final JTextArea insertMail = new JTextArea();
    private final JTextArea password = new JTextArea("password");
    private final JTextArea insertPassword = new JTextArea();
    private final JTextArea name = new JTextArea("nome");
    private final JTextArea insertName = new JTextArea();
    private final JTextArea surname = new JTextArea("cognome");
    private final JTextArea insertSurname = new JTextArea();
    private final JTextArea date = new JTextArea("data di nascita anno-mese-data");
    private final JTextArea insertDate = new JTextArea();
    private final JTextArea address1 = new JTextArea("indirizzo prinicpale");
    private final JTextArea insertAddress1 = new JTextArea();
    private final JTextArea address2 = new JTextArea("indirizzo secondario (opzionale)");
    private final JTextArea insertAddress2 = new JTextArea();
    private final JTextArea address3 = new JTextArea("indirizzo terziario (opzionale)");
    private final JTextArea insertAddress3 = new JTextArea();
    private final JButton confirm =  new JButton("confirm");
    private final JButton back =  new JButton("indietro"); 

    public ClientRegister(final Controller controller) {
        this.controller = controller;
        setPreferredSize(new Dimension(500, 200));
        setLayout(new GridLayout(9, 2));
        add(this.mail);
        this.mail.setEditable(false);
        add(this.insertMail);
        add(this.password);
        this.password.setEditable(false);
        add(this.insertPassword);
        add(this.name);
        this.name.setEditable(false);
        add(this.insertName);
        add(this.surname);
        this.surname.setEditable(false);
        add(this.insertSurname);
        add(this.date);
        this.date.setEditable(false);
        add(this.insertDate);
        add(this.address1);
        this.address1.setEditable(false);
        add(this.insertAddress1);
        add(this.address2);
        this.address2.setEditable(false);
        add(this.insertAddress2);
        add(address3);
        this.address3.setEditable(false);
        add(insertAddress3);
        add(this.confirm);
        add(this.back);
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        for (Component comp : this.getComponents()) {
            if (comp instanceof JTextArea) {
                ((JTextArea) comp).setBorder(border);
            }
        }
        this.back.addActionListener(e -> this.controller.accessBoard());
        this.confirm.addActionListener(e -> newClient("", this.insertMail.getText(), this.insertPassword.getText(), this.insertName.getText(), this.insertSurname.getText(), this.insertDate.getText(), this.insertAddress1.getText(), this.insertAddress2.getText(), this.insertAddress3.getText()));
    }

    private void newClient (final String... data) {
        String id = UUID.randomUUID().toString();
        while (isRightId(id)) {
            id = UUID.randomUUID().toString();
        }
        data[0] = id;
        if (!security(data)) {
            return;
        }
        try (
            var stm = DAOUtils.prepare(connection, INSERT_CLIENT, (Object[])data);
        ) {
            stm.executeUpdate();
            JOptionPane.showMessageDialog(this,"Cliente creato correttamente","Success", JOptionPane.PLAIN_MESSAGE);
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

    private boolean security (final String... data) {
        for (int i = 0; i < 7; i++) {
            if (data[i].isEmpty()) {
                JOptionPane.showMessageDialog(this, "Inserire tutti i dati almeno fino al primo inidirizzo", "Error", JOptionPane.PLAIN_MESSAGE);
                return false;
            }
        }
        return (isRightMail(data[1]) && isRightPassword(data[2]) && isRightName(data[3]) && isRightSurname(data[4]) && isRightDate(data[5]) && isRightAddress(data[6]) &&  isRightAddress(data[7]) && isRightAddress(data[8]));
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

    private boolean isRightMail (final String mail) {
        try (
            var stm = DAOUtils.prepare(connection, SECURITY_MAIL_QUERY, mail);
            var rS = stm.executeQuery();
        ) {
            if (mail.length() > MAX_LENGTH_MAIL) {
                JOptionPane.showMessageDialog(this,"Mail troppo lunga, al massimo " + MAX_LENGTH_MAIL + " cartteri","Error", JOptionPane.PLAIN_MESSAGE);
                return false;                
            } else if (rS.next()) {
                JOptionPane.showMessageDialog(this,"Mail già esistente, riprovare","Error", JOptionPane.PLAIN_MESSAGE);
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

    private boolean isRightPassword (final String password) {
        if (password.length() > MAX_LENGTH_PASSWORD) {
            JOptionPane.showMessageDialog(this, "Password troppo lunga, al massimo " + MAX_LENGTH_PASSWORD + " caratteri", "Error", JOptionPane.PLAIN_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean isRightName (final String name) {
        if (name.length() > MAX_LENGTH_NAME) {
            JOptionPane.showMessageDialog(this, "Nome troppo lungo, al massimo " + MAX_LENGTH_NAME + " caratteri", "Error", JOptionPane.PLAIN_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean isRightSurname (final String surnname) {
        if (surnname.length() > MAX_LENGTH_SURNAME) {
            JOptionPane.showMessageDialog(this, "Cognome troppo lungo, al massimo " + MAX_LENGTH_SURNAME + " caratteri", "Error", JOptionPane.PLAIN_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean isRightDate (final String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate.parse(date, formatter);
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Data nel formato sbagliato o non esiste inserire yyyy-MM-dd", "Error", JOptionPane.PLAIN_MESSAGE);
        }
        return false;
    }

    private boolean isRightAddress (final String address) {
        if (address.length() > MAX_LENGTH_ADDRESS) {
            JOptionPane.showMessageDialog(this, "Indirizzo troppo lungo, al massimo " + MAX_LENGTH_ADDRESS + " caratteri", "Error", JOptionPane.PLAIN_MESSAGE);
            return false;
        }
        return true;
    }

}
