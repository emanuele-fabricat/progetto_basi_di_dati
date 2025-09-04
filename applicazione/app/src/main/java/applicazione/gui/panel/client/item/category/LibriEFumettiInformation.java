package applicazione.gui.panel.client.item.category;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import applicazione.dao.DAOData;
import applicazione.dao.DAOException;
import applicazione.dao.DAOUtils;

public class LibriEFumettiInformation extends JScrollPane{
    private static final String ITEM_QUERY = "SELECT * FROM LIBRI_E_FUMETTI WHERE id_articolo = ?";

    private record Item(String serie, String categoria, String autore, String id) {
    }

    private final Connection connection = DAOUtils.localMySQLConnection(DAOData.DATABASE, DAOData.USERNAME,
            DAOData.PASSWORD);
    private List<Item> items = new LinkedList<>();
    private final JScrollPane tablePanel = new JScrollPane();

    public LibriEFumettiInformation(final String itemId) {
        setViewportView(tablePanel);
        try (
                var stm = DAOUtils.prepare(connection, ITEM_QUERY, itemId);
                var rS = stm.executeQuery();) {
            while (rS.next()) {
                this.items.add(new Item(rS.getString("serie"), rS.getString("categoria"),
                        rS.getString("autore"), rS.getString("id_articolo")));
            }
        } catch (Exception e) {
            throw new DAOException(e);
        }

        String[] column = { "serie", "categoria", "autore", "id" };
        Object[][] data = new Object[items.size()][column.length];

        for (int i = 0; i < this.items.size(); i++) {
            data[i][0] = this.items.get(i).serie;
            data[i][1] = this.items.get(i).categoria;
            data[i][5] = this.items.get(i).id;
        }

        JTable table = new JTable(data, column);
        tablePanel.setViewportView(table);

    }

}
