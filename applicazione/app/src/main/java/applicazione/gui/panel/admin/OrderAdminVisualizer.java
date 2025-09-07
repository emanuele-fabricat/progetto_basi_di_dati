package applicazione.gui.panel.admin;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import applicazione.dao.DAODataConfig;
import applicazione.dao.DAOException;
import applicazione.dao.DAOUtils;

public class OrderAdminVisualizer extends JScrollPane {
    private static final String VISUALIZE_QUERY = "SELECT * FROM ORDINE ORDER BY data";

    private record Order(String OrderId, int somma, String data, String mail) {
    }

    private final Connection connection = DAOUtils.localMySQLConnection(DAODataConfig.DATABASE, DAODataConfig.USERNAME,
            DAODataConfig.PASSWORD);
    private List<Order> orders = new LinkedList<>();

    private static JTable table;
    private static DefaultTableModel model;

    public OrderAdminVisualizer() {
        try (
                var stm = DAOUtils.prepare(connection, VISUALIZE_QUERY);
                var rS = stm.executeQuery();) {
            while (rS.next()) {
                this.orders.add(new Order(rS.getString("id_ordine"), rS.getInt("somma"), rS.getString("data"),
                        rS.getString("mail")));
            }
        } catch (Exception e) {
            throw new DAOException(e);
        }

        String[] column = { "Id ordine", "totale speso (€)", "data", "mail dell'admin che ha fatto l'ordine"};
        Object[][] data = new Object[orders.size()][column.length];

        for (int i = 0; i < this.orders.size(); i++) {
            data[i][0] = this.orders.get(i).OrderId;
            data[i][1] = this.orders.get(i).somma;
            data[i][2] = this.orders.get(i).data;
            data[i][3] = this.orders.get(i).mail;
        }
        setBorder(new TitledBorder("Ordini degli admin"));
        model = new DefaultTableModel(data, column);
        table = new JTable(model);
        setViewportView(table);
    }
}
