package applicazione.gui.panel.admin;

import java.awt.BorderLayout;
import java.awt.Component;
import java.sql.Connection;
import java.util.Map;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import applicazione.dao.DAODataConfig;
import applicazione.dao.DAOException;
import applicazione.dao.DAOUtils;
import applicazione.model.Table;

public class AddTable extends JDialog {
    private static final int MAX_SEATS_FOR_TABLE = 16;
    private static final int MAX_SEATS_AT_ALL = 100;
    private static final String INSERT_TABLE_QUERY = "INSERT INTO TAVOLO (capienza) VALUE (?)";
    private final static Connection connection = DAOUtils.localMySQLConnection(DAODataConfig.DATABASE, DAODataConfig.USERNAME,
            DAODataConfig.PASSWORD);
    private final JTextArea seats = new JTextArea(1, 25);
    private final JButton confirm = new JButton("aggiungi tavolo");

    public AddTable(Component parent) {
        super(SwingUtilities.getWindowAncestor(parent), "Aggiungi Tavolo", ModalityType.DOCUMENT_MODAL);
        setLayout(new BorderLayout());
        add(seats, BorderLayout.CENTER);
        add(confirm, BorderLayout.SOUTH);
        this.seats.setBorder(new TitledBorder("Capienza nuovo tavolo"));
        pack();
        this.confirm.addActionListener(e -> add());
    }

    private void add() {
        if (!isValidNumber()) {
            return;
        } else if (totalSeatsTable() == MAX_SEATS_AT_ALL) {
            JOptionPane.showMessageDialog(this,
                    "Si è già raggiunto il massimo dei posti, impossibile aggiungerne altri", "Error",
                    JOptionPane.PLAIN_MESSAGE);
            this.dispose();
            return;
        }
        int numberOfSeats = textAreaToInt(seats).get();
        if (numberOfSeats + totalSeatsTable() > MAX_SEATS_AT_ALL) {
            JOptionPane.showMessageDialog(this,
                    "così ci saranno troppi posti a sedere, c'è ne sono già " + totalSeatsTable()
                            + " non possono superare i " + MAX_SEATS_AT_ALL + " posti",
                    "Error",
                    JOptionPane.PLAIN_MESSAGE);
            return;
        }
        try (
                var stm = DAOUtils.prepare(connection, INSERT_TABLE_QUERY, numberOfSeats);) {
            stm.executeUpdate();
        } catch (Exception e) {
            throw new DAOException(e);
        }
        JOptionPane.showMessageDialog(this,"Tavolo aggiunto con successo\nse si vuole usci premere la x\naltrimenti inserire un nuovo tavolo","Succes",JOptionPane.PLAIN_MESSAGE);
    }

    private int totalSeatsTable() {
        Map<Integer, Integer> tables = Table.getAllTable();
        return tables.values().stream().mapToInt(Integer::intValue).sum();
    }

    private boolean isValidNumber() {
        if (textAreaToInt(this.seats).isEmpty()) {
            JOptionPane.showMessageDialog(this, "inserire una quantità valida (solo numeri, senza spazi)", "Error",
                    JOptionPane.PLAIN_MESSAGE);
            return false;
        } else if (textAreaToInt(this.seats).get() > MAX_SEATS_FOR_TABLE) {
            JOptionPane.showMessageDialog(this, "non credo esistano tavoli con più di " + MAX_SEATS_FOR_TABLE + "posti",
                    "Error",
                    JOptionPane.PLAIN_MESSAGE);
            return false;
        }
        return true;
    }

    private Optional<Integer> textAreaToInt(final JTextArea number) {
        int newNumb = 0;
        try {
            newNumb = Integer.parseInt(number.getText());
        } catch (Exception e) {
            return Optional.empty();
        }
        return Optional.of(newNumb);
    }

}
