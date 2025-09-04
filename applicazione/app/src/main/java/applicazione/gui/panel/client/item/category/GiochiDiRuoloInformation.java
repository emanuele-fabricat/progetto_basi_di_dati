package applicazione.gui.panel.client.item.category;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import applicazione.dao.DAOData;
import applicazione.dao.DAOException;
import applicazione.dao.DAOUtils;

public class GiochiDiRuoloInformation extends JScrollPane{
    private static final String ITEM_QUERY = "SELECT * FROM GIOCHI_DI_RUOLO WHERE id_articolo = ?";

    private record Item(String tipo, String id) {
    }

    private final Connection connection = DAOUtils.localMySQLConnection(DAOData.DATABASE, DAOData.USERNAME,
            DAOData.PASSWORD);
    private List<Item> items = new LinkedList<>();
    private final JScrollPane tablePanel = new JScrollPane();

    public GiochiDiRuoloInformation(final String itemId) {
        setViewportView(tablePanel);
        try (
                var stm = DAOUtils.prepare(connection, ITEM_QUERY, itemId);
                var rS = stm.executeQuery();) {
            while (rS.next()) {
                this.items.add(new Item(rS.getString("tipo"), rS.getString("id_articolo")));
            }
        } catch (Exception e) {
            throw new DAOException(e);
        }

        String[] column = { "tipo", "id" };
        Object[][] data = new Object[items.size()][column.length];

        for (int i = 0; i < this.items.size(); i++) {
            data[i][0] = this.items.get(i).tipo;
            data[i][5] = this.items.get(i).id;
        }

        JTable table = new JTable(data, column);
        tablePanel.setViewportView(table);

    }

}
