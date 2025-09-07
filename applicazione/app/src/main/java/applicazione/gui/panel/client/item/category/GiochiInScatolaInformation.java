package applicazione.gui.panel.client.item.category;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import applicazione.dao.DAODataConfig;
import applicazione.dao.DAOException;
import applicazione.dao.DAOUtils;

public class GiochiInScatolaInformation extends JScrollPane {
    private static final String ITEM_QUERY = "SELECT * FROM GIOCHI_IN_SCATOLA WHERE id_articolo = ?";

    private record Item(String linea, String meccanica, int min, int max, int tempo, String id) {
    }

    private final Connection connection = DAOUtils.localMySQLConnection(DAODataConfig.DATABASE, DAODataConfig.USERNAME,
            DAODataConfig.PASSWORD);
    private List<Item> items = new LinkedList<>();
    private final JScrollPane tablePanel = new JScrollPane();

    public GiochiInScatolaInformation(final String itemId) {
        setViewportView(tablePanel);
        try (
                var stm = DAOUtils.prepare(connection, ITEM_QUERY, itemId);
                var rS = stm.executeQuery();) {
            while (rS.next()) {
                this.items.add(new Item(rS.getString("linea"), rS.getString("meccanica"),
                        rS.getInt("min_giocatori"), rS.getInt("max_giocatori"), rS.getInt("tempo_medio"),
                        rS.getString("id_articolo")));
            }
        } catch (Exception e) {
            throw new DAOException(e);
        }

        String[] column = { "linea", "meccanica", "giocatori minimi", "giocatori massimi", "tempo medio", "id" };
        Object[][] data = new Object[items.size()][column.length];

        for (int i = 0; i < this.items.size(); i++) {
            data[i][0] = this.items.get(i).linea;
            data[i][1] = this.items.get(i).meccanica;
            data[i][2] = this.items.get(i).min;
            data[i][3] = this.items.get(i).max;
            data[i][4] = this.items.get(i).tempo;
            data[i][5] = this.items.get(i).id;
        }

        JTable table = new JTable(data, column);
        tablePanel.setViewportView(table);

    }

}
