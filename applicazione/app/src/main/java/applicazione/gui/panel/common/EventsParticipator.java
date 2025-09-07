package applicazione.gui.panel.common;

import java.awt.GridLayout;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import applicazione.dao.DAODataConfig;
import applicazione.dao.DAOException;
import applicazione.dao.DAOUtils;
import applicazione.model.Event;

public class EventsParticipator extends JPanel {
    public static final int CLIENT_USER = 0;
    public static final int ADMIN_USER = 1;
    private static final String VISUALIZE_EVENT_QUERY = "SELECT * FROM EVENTO " + "WHERE data_ora_inizio > ? "
            + "AND max_partecipanti > num_partecipanti " + "AND visibilità = 'pubblico'" + " ORDER BY data_ora_inizio ";
    private static final String SELECT_MY_EVENT_CLIENT_QUERY = "SELECT id_evento FROM CLIENTE_PARTECIPA WHERE id_utente = ?";
    private static final String SELECT_MY_EVENT_ADMIN_QUERY = "SELECT id_evento FROM ADMIN_PARTECIPA WHERE mail = ?";
    private static final String INSERT_NEW_PARTECIPA_CLIENT_QUERY = "INSERT INTO CLIENTE_PARTECIPA (id_utente, id_evento) "
            + " VALUE (?, ?)";
    private static final String INSERT_NEW_PARTECIPA_ADMIN_QUERY = "INSERT INTO ADMIN_PARTECIPA (id_evento, mail) "
            + " VALUE (?, ?)";
    private static final String UPDATE_EVENT_QUERY = "UPDATE EVENTO SET num_partecipanti = num_partecipanti + 1 WHERE id_evento = ?";

    private final Connection connection = DAOUtils.localMySQLConnection(DAODataConfig.DATABASE, DAODataConfig.USERNAME,
            DAODataConfig.PASSWORD);
    private List<Event> events = new LinkedList<>();
    private final List<String> marked = new LinkedList<>();
    private final int userType;

    private JTable table;
    private DefaultTableModel model;
    private final JScrollPane tablePanel = new JScrollPane();
    private final JPanel insertPanel = new JPanel();
    private final JTextArea eventId = new JTextArea();
    private final JButton addEvent = new JButton("Partecipa");
    private final String userId;
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String formattedDateTime = now.format(formatter);

    public EventsParticipator(final String userId, final int userType) {
        this.userType = userType;
        this.userId = userId;
        setLayout(new GridLayout(2, 1));
        add(tablePanel);
        add(insertPanel);
        this.insertPanel.setLayout(new GridLayout(1, 2));
        this.insertPanel.add(eventId);
        this.insertPanel.add(addEvent);
        try (
                var stm = DAOUtils.prepare(connection, VISUALIZE_EVENT_QUERY, this.formattedDateTime);
                var rS = stm.executeQuery();) {
            while (rS.next()) {
                this.events.add(new Event(rS.getString("id_evento"), rS.getInt("num_partecipanti"),
                        rS.getString("data_ora_inizio"), rS.getString("data_ora_fine"), rS.getString("visibilità"),
                        Optional.of(rS.getString("nome")), Optional.of(rS.getString("presentazione")),
                        Optional.of(rS.getInt("max_partecipanti"))));

            }
        } catch (Exception e) {
            throw new DAOException(e);
        }
        switch (userType) {
            case CLIENT_USER:
                try (
                        var stm = DAOUtils.prepare(connection, SELECT_MY_EVENT_CLIENT_QUERY, userId);
                        var rS = stm.executeQuery();) {
                    while (rS.next()) {
                        this.marked.add(rS.getString("id_evento"));
                    }

                } catch (Exception e) {
                    throw new DAOException(e);
                }
                break;
            case ADMIN_USER:
                try (
                        var stm = DAOUtils.prepare(connection, SELECT_MY_EVENT_ADMIN_QUERY, userId);
                        var rS = stm.executeQuery();) {
                    while (rS.next()) {
                        this.marked.add(rS.getString("id_evento"));
                    }

                } catch (Exception e) {
                    throw new DAOException(e);
                }
                break;
            default:
                throw new IllegalAccessError("utilizzare un tipo di utente valido");
        }

        String[] column = { "id_evento", "num_partecipanti", "data e ora di inizio", "data e ora di fine",
                "visibilità", "nome", "presentazione", "max partecipanti" };
        Object[][] data = new Object[events.size()][column.length];
        for (int i = 0; i < this.events.size(); i++) {
            data[i][0] = this.events.get(i).eventId();
            data[i][1] = this.events.get(i).numPartecipanti();
            data[i][2] = this.events.get(i).inizio();
            data[i][3] = this.events.get(i).fine();
            data[i][4] = this.events.get(i).type();
            if (this.events.get(i).nome().isPresent() && this.events.get(i).presentazione().isPresent()
                    && this.events.get(i).maxPartecipanti().isPresent()) {
                data[i][5] = this.events.get(i).nome().get();
                data[i][6] = this.events.get(i).presentazione().get();
                data[i][7] = this.events.get(i).maxPartecipanti().get();
            }
        }
        model = new DefaultTableModel(data, column);
        table = new JTable(model);
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int col) {

                var c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

                String eventId = table.getValueAt(row, 0).toString();
                if (marked.contains(eventId)) {
                    c.setBackground(java.awt.Color.YELLOW);
                } else {
                    c.setBackground(isSelected ? table.getSelectionBackground() : java.awt.Color.WHITE);
                }

                return c;
            }
        });
        this.tablePanel.setBorder(
                new TitledBorder("Gli eventi da oggi in poi, quelli evidenziati sono quelli a cui già partecipi"));
        this.eventId.setBorder(
                new TitledBorder("iserisci l'id di un evento a cui vuoi partecipare e premi sul bottone di fianco"));
        this.tablePanel.setViewportView(table);
        this.addEvent.addActionListener(e -> participate());
    }

    private void participate() {
        if (!isValidEventId()) {
            return;
        }
        this.marked.add(this.eventId.getText());
        this.updateTable();
        try (
                var stm = DAOUtils.prepare(connection, UPDATE_EVENT_QUERY, this.eventId.getText());) {
            stm.executeUpdate();
        } catch (Exception e) {
            throw new DAOException(e);
        }
        switch (userType) {
            case CLIENT_USER:
                try (
                        var stm = DAOUtils.prepare(connection, INSERT_NEW_PARTECIPA_CLIENT_QUERY, this.userId,
                                this.eventId.getText());) {
                    stm.executeUpdate();

                } catch (Exception e) {
                    throw new DAOException(e);
                }
                break;
            case ADMIN_USER:
                try (
                        var stm = DAOUtils.prepare(connection, INSERT_NEW_PARTECIPA_ADMIN_QUERY, this.eventId.getText(),
                                this.userId);) {
                    stm.executeUpdate();

                } catch (Exception e) {
                    throw new DAOException(e);
                }
                break;
            default:
                throw new IllegalAccessError("utilizzare un tipo di utente valido");
        }
    }

    private boolean isValidEventId() {
        if (this.eventId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "riempire il  campo evento", "Error",
                    JOptionPane.PLAIN_MESSAGE);
            return false;
        } else if (this.eventId.getText().length() != 36) {
            JOptionPane.showMessageDialog(this, "inserire un id evento valido (36 caratteri)", "Error",
                    JOptionPane.PLAIN_MESSAGE);
            return false;
        } else if (!Event.existId(this.eventId.getText())) {
            JOptionPane.showMessageDialog(this, "inserire un id evento presente", "Error", JOptionPane.PLAIN_MESSAGE);
            return false;
        } else if (!Event.isBefore(this.eventId.getText(), this.formattedDateTime)) {
            JOptionPane.showMessageDialog(this, "inserire un evento che deve ancora avvenire", "Error",
                    JOptionPane.PLAIN_MESSAGE);
            return false;
        } else if (!Event.getType(this.eventId.getText()).equals("pubblico")) {
            JOptionPane.showMessageDialog(this, "inserire un evento pubblico", "Error", JOptionPane.PLAIN_MESSAGE);
            return false;
        } else if (!Event.stillAvailable(this.eventId.getText())) {
            JOptionPane.showMessageDialog(this, "inserire un evento a cui si possa partecipare ", "Error",
                    JOptionPane.PLAIN_MESSAGE);
            return false;
        } else if (this.marked.contains(this.eventId.getText())) {
            JOptionPane.showMessageDialog(this, "inserire un evento a cui non si abbia già aderito", "Error",
                    JOptionPane.PLAIN_MESSAGE);
            return false;
        }
        return true;
    }

    private void updateTable() {
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).equals(this.eventId.getText())) {
                int num = (int) model.getValueAt(i, 1);
                model.setValueAt(num + 1, i, 1);
            }
        }
        table.repaint();
    }
}
