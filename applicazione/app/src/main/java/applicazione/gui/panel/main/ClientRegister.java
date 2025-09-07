package applicazione.gui.panel.main;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

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
    private final JTextArea mail = new JTextArea();
    private final JTextArea password = new JTextArea();
    private final JTextArea name = new JTextArea();
    private final JTextArea surname = new JTextArea();
    private final JTextArea date = new JTextArea();
    private final JTextArea address1 = new JTextArea();
    private final JTextArea address2 = new JTextArea();
    private final JTextArea address3 = new JTextArea();
    private final JButton confirm =  new JButton("confirm");
    private final JButton back =  new JButton("indietro"); 

    public ClientRegister(final Controller controller) {
        this.controller = controller;
        setPreferredSize(new Dimension(500, 200));
        setLayout(new GridLayout(5, 2));
        add(this.mail);
        add(this.password);
        add(this.name);
        add(this.surname);
        add(this.date);
        add(this.address1);
        add(this.address2);
        add(this.address3);
        add(this.confirm);
        add(this.back);
        this.mail.setBorder(new TitledBorder("Inserisci mail"));
        this.password.setBorder(new TitledBorder("Inserisci password"));
        this.name.setBorder(new TitledBorder("Inserisci nome"));
        this.surname.setBorder(new TitledBorder("Inserisci cognome"));
        this.date.setBorder(new TitledBorder("Inserisci data di nascita"));
        this.address1.setBorder(new TitledBorder("Inserisci indirizzo di consegna"));
        this.address2.setBorder(new TitledBorder("Inserisci indirizzo di consegna opzionale"));
        this.address3.setBorder(new TitledBorder("Inserisci indirizzo di consegna opzionale"));
        this.back.addActionListener(e -> this.controller.accessBoard());
        this.confirm.addActionListener(e -> newClient("", this.mail.getText(), this.password.getText(), this.name.getText(), this.surname.getText(), this.date.getText(), this.address1.getText(), this.address2.getText(), this.address3.getText()));
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
