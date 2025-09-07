package applicazione.gui.panel.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Window;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import applicazione.dao.DAODataConfig;
import applicazione.dao.DAOException;
import applicazione.dao.DAOUtils;
import applicazione.gui.panel.admin.item.category.Accessori;
import applicazione.gui.panel.admin.item.category.CarteCollezionabili;
import applicazione.gui.panel.admin.item.category.GadgetEGiocattoli;
import applicazione.gui.panel.admin.item.category.GiochiDiRuolo;
import applicazione.gui.panel.admin.item.category.GiochiInScatola;
import applicazione.gui.panel.admin.item.category.LibriEFumetti;
import applicazione.gui.panel.admin.item.category.Modellismo;
import applicazione.model.Item;
import applicazione.model.Order;
import applicazione.model.Product;

public class AddItem extends JPanel {
    private final Connection connection = DAOUtils.localMySQLConnection(DAODataConfig.DATABASE, DAODataConfig.USERNAME,
            DAODataConfig.PASSWORD);
    private static final String INSERT_COMPORRE_QUERY = "INSERT INTO COMPORRE (id_prodotto, id_ordine, qta) " +
            "VALUES (?, ?, ?); ";
    private static final String INSERT_ORDINE_QUERY = "INSERT INTO ORDINE (id_ordine, somma, data, mail) " +
            "VALUES (?, "
            + "(SELECT sum(C.qta * P.costo) as somma "
            + "FROM COMPORRE C, PRODOTTO P " + "WHERE C.id_ordine = ? " + "AND C.id_prodotto = P.id_prodotto), "
            + "?, ?); ";
    private final static String INSERT_ARTICOLO_QUERY = "INSERT INTO ARTICOLO (id_articolo, nome, descrizione, prezzo_vendita, disponibilità, tipologia) "
            + "VALUE (?, ?, ?, ?, ?, ?); ";
    private static final String UPDATE_ARTICOLO_QUARY = "UPDATE ARTICOLO " + "SET disponibilità = disponibilità + ? "
            + "WHERE id_articolo = ?; ";
    private final JPanel centralPanel = new JPanel();
    private final JScrollPane descriptionPanel = new JScrollPane();
    private final JTextArea description = new JTextArea("Descrizione");
    private final JTextArea insertDescription = new JTextArea();
    private final JTextArea price = new JTextArea("Prezzo di vendita");
    private final JTextArea insertPrice = new JTextArea();
    private final JTextArea type = new JTextArea("Tipologia");
    private final JTextArea id = new JTextArea("id prodotto");
    private final JTextArea insertId = new JTextArea();
    private final JTextArea qta = new JTextArea("Quantità");
    private final JTextArea insertQta = new JTextArea();
    private final String[] option = { "CARTE_COLLEZIONABILI", "GIOCHI_DI_RUOLO", "GIOCHI_IN_SCATOLA",
            "LIBRI_E_FUMETTI", "MODELLISMO", "GADGET_E_GIOCATTOLI", "ACCESSORI" };
    private final JComboBox<String> selction = new JComboBox<>(this.option);
    private final JButton addItem = new JButton("aggiungi");
    private final JButton finish = new JButton("chiudi ordine");

    private final String userId;
    private String orderId = UUID.randomUUID().toString();

    private record Information(String productId, String name, String description, int price, String type) {
    }

    private Map<Information, Integer> numberInformation = new HashMap<>();

    public AddItem(final String userId) {
        this.userId = userId;
        setLayout(new BorderLayout());
        add(centralPanel, BorderLayout.CENTER);
        this.centralPanel.setLayout(new GridLayout(6, 2));
        this.centralPanel.add(id);
        this.centralPanel.add(insertId);
        this.centralPanel.add(qta);
        this.centralPanel.add(insertQta);
        this.centralPanel.add(description);
        this.centralPanel.add(descriptionPanel);
        this.centralPanel.add(price);
        this.centralPanel.add(insertPrice);
        this.centralPanel.add(type);
        this.centralPanel.add(selction);
        this.centralPanel.add(addItem);
        this.centralPanel.add(finish);
        this.descriptionPanel.setViewportView(insertDescription);
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        for (Component comp : centralPanel.getComponents()) {
            if (comp instanceof JTextArea textArea) {
                textArea.setBorder(border);
                if (!textArea.getText().isEmpty()) {
                    textArea.setEnabled(false);
                    textArea.setDisabledTextColor(Color.BLACK);
                }
            }
        }
        this.insertDescription.setLineWrap(true);
        this.insertDescription.setWrapStyleWord(true);
        this.insertDescription.setBorder(border);
        this.descriptionPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.addItem.addActionListener(e -> this.addInformation());
        this.finish.addActionListener(e -> this.makeOrder());

    }

    private void addInformation() {
        if (areFieldsEmpty() || !isValidProductId() || !isValidNumber()) {
            return;
        } else if (productInMap()) {
            numberInformation.entrySet().stream()
                    .filter(entry -> entry.getKey().productId.equals(this.insertId.getText()))
                    .findFirst()
                    .ifPresent(entry -> numberInformation.put(entry.getKey(),
                            entry.getValue() + textAreaToInt(this.insertQta).get()));
            JOptionPane.showMessageDialog(this, "prodotti aggiunti con successo", "Success",
                    JOptionPane.PLAIN_MESSAGE);
            return;
        }
        this.numberInformation.put(new Information(this.insertId.getText(),
                Product.getProductName(this.insertId.getText()), this.insertDescription.getText(),
                textAreaToInt(insertPrice).get(), (String) this.selction.getSelectedItem()),
                textAreaToInt(this.insertQta).get());
        JOptionPane.showMessageDialog(this, "prodotti aggiunti con successo", "Success",
                JOptionPane.PLAIN_MESSAGE);
    }

    private boolean productInMap() {
        return this.numberInformation.entrySet().stream()
                .anyMatch(entry -> entry.getKey().productId.equals(this.insertId.getText()));
    }

    private boolean isValidProductId() {
        if (this.insertId.getText().length() != 36) {
            JOptionPane.showMessageDialog(this, "inserire un id prodotto valido (36 caratteri)", "Error",
                    JOptionPane.PLAIN_MESSAGE);
            return false;
        } else if (!Product.existId(this.insertId.getText())) {
            JOptionPane.showMessageDialog(this, "inserire un id prodotto presente", "Error", JOptionPane.PLAIN_MESSAGE);
            return false;
        } else {
            return true;
        }
    }

    private boolean areFieldsEmpty() {
        if (this.insertId.getText().isEmpty() || this.insertQta.getText().isEmpty()
                || this.insertDescription.getText().isEmpty() || this.insertPrice.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "non lasciare nessun campo vuoto", "Error", JOptionPane.PLAIN_MESSAGE);
            return true;
        }
        return false;
    }

    private boolean isValidNumber() {
        if (textAreaToInt(this.insertPrice).isEmpty() || textAreaToInt(this.insertQta).isEmpty()) {
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

    private void makeOrder() {
        if (numberInformation.isEmpty()) {
            JOptionPane.showMessageDialog(this, "inserire prima dei prodotti", "Error", JOptionPane.PLAIN_MESSAGE);
            return;
        }
        while (Order.existId(this.orderId)) {
            this.orderId = UUID.randomUUID().toString();
        }
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDay = today.format(formatter);
        for (var entry : this.numberInformation.entrySet()) {
            try (
                    var stm = DAOUtils.prepare(connection, INSERT_COMPORRE_QUERY, entry.getKey().productId,
                            this.orderId, entry.getValue());) {
                stm.executeUpdate();
            } catch (Exception e) {
                throw new DAOException(e);
            }
            if (Item.existId(entry.getKey().productId)) {
                try (
                        var stm = DAOUtils.prepare(connection, UPDATE_ARTICOLO_QUARY, entry.getValue(),
                                entry.getKey().productId);) {
                    stm.executeUpdate();
                } catch (Exception e) {
                    throw new DAOException(e);
                }
            } else {
                try (
                        var stm = DAOUtils.prepare(connection, INSERT_ARTICOLO_QUERY, entry.getKey().productId,
                                entry.getKey().name,
                                entry.getKey().description, entry.getKey().price, entry.getValue(),
                                entry.getKey().type);) {
                    stm.executeUpdate();
                } catch (Exception e) {
                    throw new DAOException(e);
                }
            }
            makeCategory(entry.getKey().productId, entry.getKey().type, entry.getKey().name);
        }
        try (
                var stm = DAOUtils.prepare(connection, INSERT_ORDINE_QUERY, this.orderId,
                        this.orderId, formattedDay, this.userId);) {
            stm.executeUpdate();
        } catch (Exception e) {
            throw new DAOException(e);
        }
        this.numberInformation.clear();
    }

    private void makeCategory(final String itemId, final String type, final String name) {
        JFrame frame = new JFrame();
        Window w = SwingUtilities.getWindowAncestor(this);
        if (w instanceof JFrame) {
            frame = (JFrame) w;
        }
        JDialog dialog = new JDialog(frame, name, true);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        switch (type) {
            case "ACCESSORI":
                if (!Accessori.exist(itemId)) {
                    dialog.setContentPane(new Accessori(itemId));
                } else {
                    return;
                }
                break;
            case "CARTE_COLLEZIONABILI":
                if (!CarteCollezionabili.exist(itemId)) {
                    dialog.setContentPane(new CarteCollezionabili(itemId));
                } else {
                    return;
                }
                break;
            case "GADGET_E_GIOCATTOLI":
                if (!GadgetEGiocattoli.exist(itemId)) {
                    dialog.setContentPane(new GadgetEGiocattoli(itemId));
                } else {
                    return;
                }
                break;
            case "GIOCHI_DI_RUOLO":
                if (!GiochiDiRuolo.exist(itemId)) {
                    dialog.setContentPane(new GiochiDiRuolo(itemId));
                } else {
                    return;
                }
                break;
            case "GIOCHI_IN_SCATOLA":
                if (!GiochiInScatola.exist(itemId)) {
                    dialog.setContentPane(new GiochiInScatola(itemId));
                } else {
                    return;
                }
                break;
            case "LIBRI_E_FUMETTI":
                if (!LibriEFumetti.exist(itemId)) {
                    dialog.setContentPane(new LibriEFumetti(itemId));
                } else {
                    return;
                }
                break;
            case "MODELLISMO":
                if (!Modellismo.exist(itemId)) {
                    dialog.setContentPane(new Modellismo(itemId));
                }
                break;
            default:
                JOptionPane.showMessageDialog(this, "non corrisponde nulla", "Error", JOptionPane.PLAIN_MESSAGE);
                break;
        }
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }
}
