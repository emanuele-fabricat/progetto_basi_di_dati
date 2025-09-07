package applicazione.gui.panel.common;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import applicazione.dao.DAOData;
import applicazione.dao.DAOException;
import applicazione.dao.DAOUtils;
import applicazione.model.Event;

public class EventsVisualizer extends JScrollPane {
    private static final String VISUALIZE_QUERY = "SELECT * FROM EVENTO ORDER BY data_ora_inizio ";

    private final Connection connection = DAOUtils.localMySQLConnection(DAOData.DATABASE, DAOData.USERNAME,
            DAOData.PASSWORD);
    private List<Event> events = new LinkedList<>();

    private static JTable table;
    private static DefaultTableModel model;

    public EventsVisualizer() {
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

        String[] column = { "id_evento", "num_partecipanti", "data e ora di inizio", "data e ora di fine",
                "visibilità", "nome", "presentazione", "max partecipanti" };
        Object[][] data = new Object[events.size()][column.length];
        for (int i = 0; i < this.events.size(); i++) {
            data[i][0] = this.events.get(i).eventId();
            data[i][1] = this.events.get(i).numPartecipanti();
            data[i][2] = this.events.get(i).inizio();
            data[i][3] = this.events.get(i).fine();
            data[i][4] = this.events.get(i).type();
            data[i][5] = this.events.get(i).nome().orElse(null);
            data[i][6] = this.events.get(i).presentazione().orElse(null);
            data[i][7] = this.events.get(i).maxPartecipanti().orElse(null);
        }
        setBorder(new TitledBorder("Tutti gli eventio"));
        model = new DefaultTableModel(data, column);
        table = new JTable(model);
        setViewportView(table);

    }
}
