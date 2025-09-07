package applicazione.gui.panel.admin;

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

public class OrderClientVisualizer extends JScrollPane {
    private static final String VISUALIZE_QUERY = "SELECT CA.id_carrello, CA.data, CA.totale_carrello, CL.mail "
            + "FROM CARRELLO CA, CLIENTE CL " + "WHERE CA.id_utente = CL.id_utente " + "ORDER BY data";

    private record Order(String CartId, String data, int totale, String mail) {
    }

    private final Connection connection = DAOUtils.localMySQLConnection(DAOData.DATABASE, DAOData.USERNAME,
            DAOData.PASSWORD);
    private List<Order> orders = new LinkedList<>();

    private static JTable table;
    private static DefaultTableModel model;

    public OrderClientVisualizer() {
        try (
                var stm = DAOUtils.prepare(connection, VISUALIZE_QUERY);
                var rS = stm.executeQuery();) {
            while (rS.next()) {
                this.orders.add(new Order(rS.getString("id_carrello"), rS.getString("data"), rS.getInt("totale_carrello"),
                        rS.getString("mail")));
            }
        } catch (Exception e) {
            throw new DAOException(e);
        }

        String[] column = { "Id ordine", "data", "totale speso (€)", "mail del cliente che ha fatto l'ordine" };
        Object[][] data = new Object[orders.size()][column.length];

        for (int i = 0; i < this.orders.size(); i++) {
            data[i][0] = this.orders.get(i).CartId;
            data[i][1] = this.orders.get(i).data;
            data[i][2] = this.orders.get(i).totale;
            data[i][3] = this.orders.get(i).mail;
        }
        setBorder(new TitledBorder("Ordini dei clienti"));
        model = new DefaultTableModel(data, column);
        table = new JTable(model);
        setViewportView(table);

    }

}
