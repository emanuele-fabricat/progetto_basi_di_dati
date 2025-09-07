package applicazione.gui.panel.client.item.category;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import applicazione.dao.DAODataConfig;
import applicazione.dao.DAOException;
import applicazione.dao.DAOUtils;

public class CarteCollezionabiliInformation extends JScrollPane {
    private static final String ITEM_QUERY = "SELECT * FROM CARTE_COLLEZIONABILI WHERE id_articolo = ?";

    private record Item(String gioco, String espansione, String formato, String id) {
    }

    private final Connection connection = DAOUtils.localMySQLConnection(DAODataConfig.DATABASE, DAODataConfig.USERNAME,
            DAODataConfig.PASSWORD);
    private List<Item> items = new LinkedList<>();
    private final JScrollPane tablePanel = new JScrollPane();

    public CarteCollezionabiliInformation(final String itemId) {
        setViewportView(tablePanel);
        try (
                var stm = DAOUtils.prepare(connection, ITEM_QUERY, itemId);
                var rS = stm.executeQuery();) {
            while (rS.next()) {
                this.items.add(new Item(rS.getString("gioco"), rS.getString("espansione"),
                        rS.getString("formato"), rS.getString("id_articolo")));
            }
        } catch (Exception e) {
            throw new DAOException(e);
        }

        String[] column = { "gioco", "espansione", "formato", "id" };
        Object[][] data = new Object[items.size()][column.length];

        for (int i = 0; i < this.items.size(); i++) {
            data[i][0] = this.items.get(i).gioco;
            data[i][1] = this.items.get(i).espansione;
            data[i][2] = this.items.get(i).formato;
            data[i][3] = this.items.get(i).id;
        }

        JTable table = new JTable(data, column);
        tablePanel.setViewportView(table);

    }

}
