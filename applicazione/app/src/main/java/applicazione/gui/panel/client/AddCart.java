package applicazione.gui.panel.client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import applicazione.dao.DAOData;
import applicazione.dao.DAOException;
import applicazione.dao.DAOUtils;
import applicazione.model.Cart;
import applicazione.model.Item;

public class AddCart extends JPanel {
    private static final String INSERT_AGGIUNGERE_QUERY = "INSERT INTO AGGIUNGERE (id_articolo, id_carrello, carico) "
            + "VALUE (?, ?, ?)";
    private static final String INSERT_CARRELLO_QUERY = "INSERT INTO CARRELLO (id_carrello, data, totale_carrello, id_utente) "
            + "VALUE (?, ?, " + "(SELECT sum(AG.carico *  AR.prezzo_vendita) as totale_carrello "
            + "FROM AGGIUNGERE AG, ARTICOLO AR " + "WHERE AG.id_carrello = ? "
            + "AND AG.id_articolo = AR.id_articolo), "
            + "?)";
    private static final String UPDATE_ARTICOLO_QUERY = "UPDATE ARTICOLO " + "SET disponibilità = disponibilità - ? "
            + "WHERE id_articolo = ?";
    private final Connection connection = DAOUtils.localMySQLConnection(DAOData.DATABASE, DAOData.USERNAME,
            DAOData.PASSWORD);
    private final JTextArea id = new JTextArea("id");
    private final JTextArea insertId = new JTextArea();
    private final JTextArea number = new JTextArea("Qta");
    private final JTextArea insertNumber = new JTextArea();
    private final JButton addItem = new JButton("aggiungi");
    private final JButton finish = new JButton("chiudi ordine");

    private String cartId = UUID.randomUUID().toString();
    private Map<String, Integer> items = new HashMap<>();
    private final String userId;

    public AddCart(String userId) {
        this.userId = userId;
        setLayout(new GridLayout(3, 2));
        add(id);
        add(insertId);
        add(number);
        add(insertNumber);
        add(addItem);
        add(finish);
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        for (Component comp : this.getComponents()) {
            if (comp instanceof JTextArea textArea) {
                textArea.setBorder(border);
                if (!textArea.getText().isEmpty()) {
                    textArea.setEnabled(false);
                    textArea.setDisabledTextColor(Color.BLACK);
                }
            }
        }
        TitledBorder borderTitle = BorderFactory.createTitledBorder("Area Carrello");
        borderTitle.setTitleFont(new Font("Arial", Font.BOLD, 14));
        borderTitle.setTitleColor(Color.BLUE);
        setBorder(borderTitle);
        this.addItem.addActionListener(e -> this.fillCart());
        this.finish.addActionListener(e -> this.pay());

    }

    private void fillCart() {
        System.out.println(this.items);
        final String itemId;
        final int qta;
        if (areFieldsEmpty() || !isValidItemId() || !isValidNumber()) {
            return;
        }
        itemId = this.insertId.getText();
        qta = textAreaToInt(insertNumber).get();
        if (this.items.containsKey(itemId)) {
            if (Item.getQta(itemId) < this.items.get(itemId) + qta) {
                JOptionPane.showMessageDialog(this, "inserire una quantità che non super la disponibilità", "Error",
                        JOptionPane.PLAIN_MESSAGE);
                return;
            }
            this.items.put(itemId, this.items.get(itemId) + qta);
        } else if (Item.getQta(itemId) < qta) {
            JOptionPane.showMessageDialog(this, "inserire una quantità che non super la disponibilità", "Error",
                    JOptionPane.PLAIN_MESSAGE);
            return;
        } else {
            this.items.put(itemId, qta);
        }
        ClientOrder.SubtractOnCell(ClientOrder.getRowById(itemId).get(), qta);
        JOptionPane.showMessageDialog(this, "prodotti aggiunti con successo", "Success",
                JOptionPane.PLAIN_MESSAGE);

    }

    private void pay() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDay = today.format(formatter);
        while (Cart.existId(cartId)) {
            this.cartId = UUID.randomUUID().toString();
        }
        for (var entry : this.items.entrySet()) {
            try (
                    var stm = DAOUtils.prepare(connection, INSERT_AGGIUNGERE_QUERY, entry.getKey(), this.cartId,
                            entry.getValue());
                    var stm2 = DAOUtils.prepare(connection, UPDATE_ARTICOLO_QUERY, entry.getValue(), entry.getKey());) {
                stm.executeUpdate();
                stm2.executeUpdate();
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }
        try (
                var stm = DAOUtils.prepare(connection, INSERT_CARRELLO_QUERY, this.cartId, formattedDay, this.cartId,
                        this.userId);) {
            stm.executeUpdate();
        } catch (Exception e) {
            throw new DAOException(e);
        }
        this.items.clear();
    }

    private boolean isValidItemId() {
        if (this.insertId.getText().length() != 36) {
            JOptionPane.showMessageDialog(this, "inserire un id articolo valido (36 caratteri)", "Error",
                    JOptionPane.PLAIN_MESSAGE);
            return false;
        } else if (!Item.existId(this.insertId.getText())) {
            JOptionPane.showMessageDialog(this, "inserire un id articolo presente", "Error", JOptionPane.PLAIN_MESSAGE);
            return false;
        } else {
            return true;
        }
    }

    private boolean areFieldsEmpty() {
        if (this.insertId.getText().isEmpty() || this.insertNumber.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "non lasciare nessun campo vuoto", "Error", JOptionPane.PLAIN_MESSAGE);
            return true;
        }
        return false;
    }

    private boolean isValidNumber() {
        if (textAreaToInt(this.insertNumber).isEmpty()) {
            JOptionPane.showMessageDialog(this, "inserire una quantità valida (solo numeri, senza spazi)", "Error",
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
