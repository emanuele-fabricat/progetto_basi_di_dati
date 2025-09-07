package applicazione.gui.panel.common;

import java.awt.GridLayout;
import java.sql.Connection;
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

public class EventsVisualizer extends JPanel {
    public static final int CLIENT_USER = 0;
    public static final int ADMIN_USER = 1;
    private static final String VISUALIZE_QUERY = "SELECT * FROM EVENTO ORDER BY data_ora_inizio ";
    private static final String SELECT_MY_EVENT_CLIENT_QUERY = "SELECT id_evento FROM CLIENTE_PARTECIPA WHERE id_utente = ?";
    private static final String SELECT_MY_EVENT_ADMIN_QUERY = "SELECT id_evento FROM ADMIN_PARTECIPA WHERE mail = ?";

    private final Connection connection = DAOUtils.localMySQLConnection(DAODataConfig.DATABASE, DAODataConfig.USERNAME,
            DAODataConfig.PASSWORD);
    private List<Event> events = new LinkedList<>();
    private final List<String> marked = new LinkedList<>();

    private JTable table;
    private DefaultTableModel model;
    private final JScrollPane tablePanel = new JScrollPane();
    private final JPanel insertPanel = new JPanel();
    private final JTextArea eventId = new JTextArea();
    private final JButton getInformation = new JButton("Informazioni");

    public EventsVisualizer(final String userId, final int userType) {
        setLayout(new GridLayout(3, 1));
        add(tablePanel);
        add(insertPanel);
        add(new EventsCreatedVisualizer(userId, userType));
        this.insertPanel.setLayout(new GridLayout(1, 2));
        this.insertPanel.add(eventId);
        this.insertPanel.add(getInformation);
        try (
                var stm = DAOUtils.prepare(connection, VISUALIZE_QUERY);
                var rS = stm.executeQuery();) {
            while (rS.next()) {
                this.events.add(new Event(rS.getString("id_evento"), rS.getInt("num_partecipanti"),
                        rS.getString("data_ora_inizio"), rS.getString("data_ora_fine"), rS.getString("visibilità"),
                        Optional.ofNullable(rS.getString("nome")), Optional.ofNullable(rS.getString("presentazione")),
                        Optional.ofNullable(rS.getInt("max_partecipanti"))));

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
                new TitledBorder("Tutti gli eventi creati, quelli evidenziati sono quelli a cui già partecipi"));
        this.eventId.setBorder(
                new TitledBorder("iserisci l'id di un evento di cui vuoi le informazioni"));
        this.tablePanel.setViewportView(table);
        this.getInformation.addActionListener(e -> Information());

    }

    private void Information() {
        if (!isValidId()) {
            return;
        }
        final EventInformation information = new EventInformation(this, this.eventId.getText());
        information.setVisible(true); 
    }

    private boolean isValidId() {
        if (this.eventId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "inserire un id valido prima di premere il pulsante", "Error",
                    JOptionPane.PLAIN_MESSAGE);
            return false;
        } else if (this.eventId.getText().length() != 36) {
            JOptionPane.showMessageDialog(this, "inserire un id prodotto valido (36 caratteri)", "Error",
                    JOptionPane.PLAIN_MESSAGE);
            return false;
        } else if (!Event.existId(this.eventId.getText())) {
            JOptionPane.showMessageDialog(this, "inserire un id prodotto presente", "Error", JOptionPane.PLAIN_MESSAGE);
            return false;
        }
        return true;
    }
}
