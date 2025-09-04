package applicazione.gui.panel.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.dnd.DropTarget;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import applicazione.dao.DAOData;
import applicazione.dao.DAOException;
import applicazione.dao.DAOUtils;
import applicazione.gui.panel.common.StartAndStopEvent;
import applicazione.gui.panel.common.VisualizeFreeTable;
import applicazione.model.Event;
import applicazione.model.Item;
import applicazione.model.Table;

public class MakePublicEvent extends JPanel {
    private static final String INSERT_ADMIN_CREA_QUERY = "INSERT INTO ADMIN_CREA (id_evento, mail) "
            + "VALUE (?, ?)";
    private static final String INSERT_EVENTO_QUERY = "INSERT INTO EVENTO (id_evento, num_partecipanti, data_ora_inizio, data_ora_fine, visibilità, nome, presentazione, max_partecipanti) "
            + "VALUE (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String INSERT_UTILIZZARE_QUERY = "INSERT INTO UTILIZZARE (id_evento, id_articolo, numero, responsabile, telefono)"
            + "VALUE (?, ?, ?, ?, ?)";
    private final Connection connection = DAOUtils.localMySQLConnection(DAOData.DATABASE, DAOData.USERNAME,
            DAOData.PASSWORD);
    private final JTextArea responsabile = new JTextArea();
    private final JTextArea telefono = new JTextArea();
    private final JTextArea articoloId = new JTextArea();
    private final JTextArea tavolo = new JTextArea();
    private final JButton addItem = new JButton("Aggiungi materiale");
    private final JButton createEvent = new JButton("Crea evento senza tavoli");
    private final JButton stopAdding = new JButton("Conferma tavolo");
    private final JButton freeze = new JButton("Conferma responsabile, recapito  e tavolo");

    private record TableData(String responsabile, String telefono, int tavolo) {
    }

    private List<String> materials = new LinkedList<>();
    private TableData tableData;
    private Map<TableData, List<String>> tableMaterials = new HashMap<>();
    private final Event event;

    public MakePublicEvent(Event event) {
        this.event = event;
        setLayout(new GridLayout(8, 1));
        add(responsabile);
        add(telefono);
        add(tavolo);
        add(freeze);
        add(articoloId);
        add(addItem);
        add(stopAdding);
        add(createEvent);
        this.responsabile.setBorder(new TitledBorder("Iserire il nome del responsabile per questo tavolo"));
        this.telefono.setBorder(new TitledBorder("Inserire il recapito telefonico del responsabile"));
        this.articoloId.setBorder(new TitledBorder(
                "Inserire l'identificativo di un articolo che si vuole aggiungere, non ci possono essere doppioni"));
        this.tavolo.setBorder(new TitledBorder("Inserire il tavolo da utilizzare"));
        this.freeze.addActionListener(e -> acceptFirstInformation());
        this.addItem.addActionListener(e -> addMaterials());
        this.stopAdding.addActionListener(e -> newTable());
    }

    private void acceptFirstInformation() {
        if (areFieldsEmpty(this.responsabile, this.telefono, this.tavolo) || !isValidNumber(this.tavolo)
                || isLong()
                || !isValidTableId()) {
            return;
        }
        VisualizeFreeTable.deleteRow(this.tavolo.getText());
        this.tableData = new TableData(this.responsabile.getText(), this.telefono.getText(),
                textAreaToInt(this.tavolo).get());
        this.responsabile.setEnabled(false);
        this.telefono.setEnabled(false);
        this.tavolo.setEnabled(false);
        this.freeze.setEnabled(false);
        this.createEvent.setText("Crea evento");
        this.createEvent.setEnabled(false);
    }

    private void addMaterials() {
        if (!isValidItemId()) {
            return;
        } else if (areUnenable(this.responsabile, this.telefono, this.tavolo, this.freeze)) {
            JOptionPane.showMessageDialog(this, "Confermare prima: responsabile, telefono e tavolo", "Error",
                    JOptionPane.PLAIN_MESSAGE);
            return;
        }
        AdminEvent.deleteRow(this.articoloId.getText());
        this.materials.add(this.articoloId.getText());
        JOptionPane.showMessageDialog(this, "Materiale aggiunto con successo", "Success", JOptionPane.PLAIN_MESSAGE);
    }

    private void newTable() {
        this.tableMaterials.put(tableData, new ArrayList<>(materials));
        System.out.println(tableMaterials);
        this.materials.clear();
        this.responsabile.setEnabled(true);
        this.telefono.setEnabled(true);
        this.tavolo.setEnabled(true);
        this.freeze.setEnabled(true);
        this.createEvent.setEnabled(true);

    }

    private void createEvent() {
        try (
                var stm1 = DAOUtils.prepare(connection, INSERT_ADMIN_CREA_QUERY, this.event.eventId(),
                        this.event.userId());
                var stm2 = DAOUtils.prepare(connection, INSERT_EVENTO_QUERY, this.event.eventId(),
                        this.event.numPartecipanti(), this.event.inizio(), this.event.fine(), this.event.type(),
                        this.event.nome().get(), this.event.presentazione().get(),
                        this.event.maxPartecipanti().get())) {
            stm1.executeUpdate();
            stm2.executeUpdate();
        } catch (Exception e) {
            throw new DAOException(e);
        }
        if (!tableMaterials.isEmpty()) {
            for (var entry : tableMaterials.entrySet()) {
                if (entry.getValue().isEmpty()) {
                    try (
                            var stm = DAOUtils.prepare(connection, INSERT_UTILIZZARE_QUERY, this.event.eventId(), "",
                                    entry.getKey().tavolo, entry.getKey().responsabile, entry.getKey().telefono);) {
                        stm.executeUpdate();
                    } catch (Exception e) {
                        throw new DAOException(e);
                    }

                } else {
                    for (String item : entry.getValue()) {
                        try (
                                var stm = DAOUtils.prepare(connection, INSERT_UTILIZZARE_QUERY, this.event.eventId(),
                                        item,
                                        entry.getKey().tavolo, entry.getKey().responsabile, entry.getKey().telefono);) {
                            stm.executeUpdate();
                        } catch (Exception e) {
                            throw new DAOException(e);
                        }
                    }
                }
            }
        }
        removeAll();
        setLayout(new BorderLayout());
        add(new StartAndStopEvent(event.userId(), StartAndStopEvent.ADMIN));
        revalidate();
        repaint();
    }

    private boolean isTableTaken(final int table) {
        for (var entry : tableMaterials.entrySet()) {
            if (entry.getKey().tavolo == table) {
                return true;
            }
        }
        return false;
    }

    private boolean isItemTaken(final String item) {
        for (var entry : tableMaterials.entrySet()) {
            for (String string : entry.getValue()) {
                if (string.equals(item)) {
                    return true;
                }

            }
        }
        return false;
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
        System.out.println(tableMaterials);
        if (this.articoloId.getText().length() != 36) {
            JOptionPane.showMessageDialog(this, "inserire un id articolo valido (36 caratteri)", "Error",
                    JOptionPane.PLAIN_MESSAGE);
            return false;
        } else if (!Item.existId(this.articoloId.getText())) {
            JOptionPane.showMessageDialog(this, "inserire un id articolo presente", "Error", JOptionPane.PLAIN_MESSAGE);
            return false;
        } else if (this.materials.contains(this.articoloId.getText()) || isItemTaken(this.articoloId.getText())) {
            JOptionPane.showMessageDialog(this,
                    "inserire un articolo non ancora scelto (non ci possono essere doppioni)", "Error",
                    JOptionPane.PLAIN_MESSAGE);
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
        } else if (Table.isOccupied(this.tavolo.getText(), this.event.inizio(), this.event.fine())
                || isTableTaken(textAreaToInt(this.tavolo).get())) {
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
