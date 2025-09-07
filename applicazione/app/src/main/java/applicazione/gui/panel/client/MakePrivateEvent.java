package applicazione.gui.panel.client;

import java.awt.Component;
import java.awt.GridLayout;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import applicazione.dao.DAOData;
import applicazione.dao.DAOException;
import applicazione.dao.DAOUtils;
import applicazione.gui.panel.common.VisualizeFreeTable;
import applicazione.model.Event;
import applicazione.model.Item;
import applicazione.model.Table;

public class MakePrivateEvent extends JPanel {
    private static final String INSERT_CLIENT_CREA_QUERY = "INSERT INTO CLIENTE_CREA (id_evento, id_utente) "
            + "VALUE (?, ?)";
    private static final String INSERT_EVENTO_QUERY = "INSERT INTO EVENTO (id_evento, num_partecipanti, data_ora_inizio, data_ora_fine, visibilità) "
            + "VALUE (?, ?, ?, ?, ?)";
    private static final String INSERT_UTILIZZARE_QUERY = "INSERT INTO UTILIZZARE (id_evento, numero, responsabile, telefono)"
            + "VALUE (?, ?, ?, ?)";
    private static final String INSERT_SERVIRE_QUERY = "INSERT INTO SERVIRE (id_evento, numero, id_articolo, quantità) "
            + "VALUE (?, ?, ?, ?)";
    private static final String UPDATE_ARTICOLO_QUERY = "UPDATE ARTICOLO " + "SET disponibilità = disponibilità - ? "
            + "WHERE id_articolo = ?";
    private final Connection connection = DAOUtils.localMySQLConnection(DAOData.DATABASE, DAOData.USERNAME,
            DAOData.PASSWORD);
    private final JTextArea responsabile = new JTextArea();
    private final JTextArea telefono = new JTextArea();
    private final JTextArea articoloId = new JTextArea();
    private final JTextArea tavolo = new JTextArea();
    private final JButton addItem = new JButton("Aggiungi materiale");
    private final JButton createEvent = new JButton("Crea evento");
    private final JButton freeze = new JButton("Conferma responsabile, recapito  e tavolo");

    private record TableData(String responsabile, String telefono, int tavolo) {
    }

    private Map<String, Integer> materials = new HashMap<>();
    private Optional<TableData> tableData = Optional.empty();
    private final Event event;
    private final String userId;

    public MakePrivateEvent(final String userId, final Event event) {
        this.event = event;
        this.userId = userId;
        setLayout(new GridLayout(4, 2));
        add(responsabile);
        add(telefono);
        add(tavolo);
        add(articoloId);
        add(freeze);
        add(addItem);
        add(createEvent);
        this.responsabile.setBorder(new TitledBorder("Iserire il nome del responsabile per questo tavolo"));
        this.telefono.setBorder(new TitledBorder("Inserire il recapito telefonico del responsabile"));
        this.articoloId.setBorder(new TitledBorder(
                "Inserire l'identificativo di un articolo che si vuole aggiungere, non ci possono essere doppioni"));
        this.tavolo.setBorder(new TitledBorder("Inserire il tavolo da utilizzare"));
        this.freeze.addActionListener(e -> acceptFirstInformation());
        this.addItem.addActionListener(e -> addMaterials());
        this.createEvent.addActionListener(e -> createEvent());
        this.createEvent.setEnabled(false);

    }

    private void acceptFirstInformation() {
        if (areFieldsEmpty(this.responsabile, this.telefono, this.tavolo) || !isValidNumber(this.tavolo)
                || isLong()
                || !isValidTableId()) {
            return;
        }
        VisualizeFreeTable.deleteRow(this.tavolo.getText());
        this.tableData = Optional.of(new TableData(this.responsabile.getText(), this.telefono.getText(),
                textAreaToInt(this.tavolo).get()));
        this.responsabile.setEnabled(false);
        this.telefono.setEnabled(false);
        this.tavolo.setEnabled(false);
        this.freeze.setEnabled(false);
        this.createEvent.setText("Crea evento");
        this.createEvent.setEnabled(true);
    }

    private void addMaterials() {
        if (!isValidItemId()) {
            return;
        } else if (areUnenable(this.responsabile, this.telefono, this.tavolo, this.freeze)) {
            JOptionPane.showMessageDialog(this, "Confermare prima: responsabile, telefono e tavolo", "Error",
                    JOptionPane.PLAIN_MESSAGE);
            return;
        } else if (Item.getQta(this.articoloId.getText()) == 0) {
            JOptionPane.showMessageDialog(this,
                    "Impossibile inserire altri materiali come questo (disponibilità finita)", "Error",
                    JOptionPane.PLAIN_MESSAGE);
            return;
        } else if (this.materials.containsKey(this.articoloId.getText())) {
            if (Item.getQta(this.articoloId.getText()) - this.materials.get(this.articoloId.getText()) == 0) {
                JOptionPane.showMessageDialog(this,
                        "Impossibile inserire altri materiali come questo (disponibilità finita)", "Error",
                        JOptionPane.PLAIN_MESSAGE);
                return;
            } else {
                this.materials.put(this.articoloId.getText(), this.materials.get(this.articoloId.getText()) + 1);
                ClientEvent.SubtractOnCell(ClientEvent.getRowById(this.articoloId.getText()).get());
                JOptionPane.showMessageDialog(this, "Materiale aggiunto con successo", "Success",
                        JOptionPane.PLAIN_MESSAGE);
                return;
            }
        } else {
            this.materials.put(this.articoloId.getText(), 1);
        }
        ClientEvent.SubtractOnCell(ClientEvent.getRowById(this.articoloId.getText()).get());
        JOptionPane.showMessageDialog(this, "Materiale aggiunto con successo", "Success", JOptionPane.PLAIN_MESSAGE);
    }

    private void createEvent() {
        try (
                var stm1 = DAOUtils.prepare(connection, INSERT_CLIENT_CREA_QUERY, this.event.eventId(),
                        this.userId);
                var stm2 = DAOUtils.prepare(connection, INSERT_EVENTO_QUERY, this.event.eventId(),
                        this.event.numPartecipanti(), this.event.inizio(), this.event.fine(), this.event.type())) {
            stm1.executeUpdate();
            stm2.executeUpdate();
        } catch (Exception e) {
            throw new DAOException(e);
        }
        if (tableData.isPresent()) {
            try (
                    var stm = DAOUtils.prepare(connection, INSERT_UTILIZZARE_QUERY, this.event.eventId(),
                            this.tableData.get().tavolo, this.tableData.get().responsabile,
                            this.tableData.get().telefono);) {
                stm.executeUpdate();
            } catch (Exception e) {
                throw new DAOException(e);
            }
            for (var entry : materials.entrySet()) {
                try (
                        var stm = DAOUtils.prepare(connection, INSERT_SERVIRE_QUERY, this.event.eventId(),
                                this.tableData.get().tavolo, entry.getKey(), entry.getValue());
                        var stm2 = DAOUtils.prepare(connection, UPDATE_ARTICOLO_QUERY, entry.getValue(),
                                entry.getKey());) {
                    stm.executeUpdate();
                    stm2.executeUpdate();
                } catch (Exception e) {
                    throw new DAOException(e);
                }

            }
        }
        this.createEvent.setEnabled(false);
    }

    private boolean areUnenable(Component... components) {
        for (Component component : components) {
            if (!component.isEnabled()) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidItemId() {
        if (this.articoloId.getText().length() != 36) {
            JOptionPane.showMessageDialog(this, "inserire un id articolo valido (36 caratteri)", "Error",
                    JOptionPane.PLAIN_MESSAGE);
            return false;
        } else if (!Item.existId(this.articoloId.getText())) {
            JOptionPane.showMessageDialog(this, "inserire un id articolo presente", "Error", JOptionPane.PLAIN_MESSAGE);
            return false;
        } else if (Item.getQta(this.articoloId.getText()) == 0) {
            JOptionPane.showMessageDialog(this, "inserire un articolo con una disponibilità > di 0 ", "Error",
                    JOptionPane.PLAIN_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean isValidTableId() {
        if (!Table.existId(this.tavolo.getText())) {
            JOptionPane.showMessageDialog(this, "Il tavolo selezionato non esiste", "Error",
                    JOptionPane.PLAIN_MESSAGE);
            return false;
        } else if (Table.isOccupied(this.tavolo.getText(), this.event.inizio(), this.event.fine())) {
            JOptionPane.showMessageDialog(this, "Il tavolo selezionato esiste ma è occupato per il tempo richiesto",
                    "Error",
                    JOptionPane.PLAIN_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean areFieldsEmpty(JTextArea... fields) {
        for (JTextArea field : fields) {
            if (field.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "riempire tutti i campo " + field.getName(), "Error",
                        JOptionPane.PLAIN_MESSAGE);
                return true;
            }
        }
        return false;
    }

    private boolean isLong() {
        if (this.responsabile.getText().length() > 20 || this.telefono.getText().length() > 20) {
            JOptionPane.showMessageDialog(this, "Un campo è troppo lungo (max 20 caratteri)", "Error",
                    JOptionPane.PLAIN_MESSAGE);
            return true;
        }
        return false;
    }

    private boolean isValidNumber(JTextArea... fields) {
        for (JTextArea field : fields) {
            if (textAreaToInt(field).isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "inserire una quantità valida (solo numeri, senza spazi)",
                        "Error",
                        JOptionPane.PLAIN_MESSAGE);
                return false;
            }
        }
        return true;
    }

    private Optional<Integer> textAreaToInt(final JTextArea number) {
        int newNumb;
        try {
            newNumb = Integer.parseInt(number.getText());
        } catch (Exception e) {
            return Optional.empty();
        }
        return Optional.of(newNumb);
    }

}
