package applicazione.gui.panel.client;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import applicazione.dao.DAOData;
import applicazione.dao.DAOException;
import applicazione.dao.DAOUtils;

public class OwnOrderVisualizer extends JScrollPane {
    private static final String VISUALIZE_QUERY = "SELECT id_carrello, data, totale_carrello "
            + "FROM CARRELLO " + "WHERE id_utente = ? " + "ORDER BY data";

    private record Order(String CartId, String data, int totale) {
    }

    private final Connection connection = DAOUtils.localMySQLConnection(DAOData.DATABASE, DAOData.USERNAME,
            DAOData.PASSWORD);
    private List<Order> orders = new LinkedList<>();

    private static JTable table;
    private static DefaultTableModel model;

    public OwnOrderVisualizer(final String userId) {
        System.out.println(userId);
        try (
                var stm = DAOUtils.prepare(connection, VISUALIZE_QUERY, userId);
                var rS = stm.executeQuery();) {
            while (rS.next()) {
                this.orders
                        .add(new Order(rS.getString("id_carrello"), rS.getString("data"), rS.getInt("totale_carrello")));
            }
        } catch (Exception e) {
            throw new DAOException(e);
        }

        String[] column = { "Id ordine", "data", "totale speso (€)"};
        Object[][] data = new Object[orders.size()][column.length];

        for (int i = 0; i < this.orders.size(); i++) {
            data[i][0] = this.orders.get(i).CartId;
            data[i][1] = this.orders.get(i).data;
            data[i][2] = this.orders.get(i).totale;
        }
        setBorder(new TitledBorder("I tuoi ordini"));
        model = new DefaultTableModel(data, column);
        table = new JTable(model);
        setViewportView(table);

    }
}
