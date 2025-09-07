package applicazione.gui.panel.client;

import java.awt.GridLayout;
import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;

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
import applicazione.model.Cart;

public class OwnOrderVisualizer extends JPanel {
    private static final String VISUALIZE_QUERY = "SELECT id_carrello, data, totale_carrello "
            + "FROM CARRELLO " + "WHERE id_utente = ? " + "ORDER BY data";

    private record Order(String CartId, String data, int totale) {
    }

    private final Connection connection = DAOUtils.localMySQLConnection(DAODataConfig.DATABASE, DAODataConfig.USERNAME,
            DAODataConfig.PASSWORD);
    private List<Order> orders = new LinkedList<>();

    private static JTable table;
    private static DefaultTableModel model;
    private final JPanel insertPanel = new JPanel();
    private final JScrollPane tablePanel = new JScrollPane();
    private final JTextArea cartId = new JTextArea();
    private final JButton visualizeInformation = new JButton("Visualizza informazioni");

    public OwnOrderVisualizer(final String userId) {
        setLayout(new GridLayout(2, 1));
        add(this.tablePanel);
        add(this.insertPanel);
        this.insertPanel.setLayout(new GridLayout(1, 2));
        this.insertPanel.add(cartId);
        this.insertPanel.add(visualizeInformation);
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
        this.cartId.setBorder(new TitledBorder("Inserisci l'id di un carrello per averne le informazioni"));
        this.tablePanel.setBorder(new TitledBorder("I tuoi ordini"));
        model = new DefaultTableModel(data, column);
        table = new JTable(model);
        this.tablePanel.setViewportView(table);
        this.visualizeInformation.addActionListener(e -> Information());
    }

    private void Information() {
        if (!isValidId()) {
            return;
        }
        final CartInformation information = new CartInformation(this, this.cartId.getText());
        information.setVisible(true); 
    }

    private boolean isValidId() {
        if (this.cartId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "inserire un id valido prima di premere il pulsante", "Error",
                    JOptionPane.PLAIN_MESSAGE);
            return false;
        } else if (this.cartId.getText().length() != 36) {
            JOptionPane.showMessageDialog(this, "inserire un id carello valido (36 caratteri)", "Error",
                    JOptionPane.PLAIN_MESSAGE);
            return false;
        } else if (!Cart.existId(this.cartId.getText())) {
            JOptionPane.showMessageDialog(this, "inserire un id carello presente", "Error", JOptionPane.PLAIN_MESSAGE);
            return false;
        }
        return true;
    }
}
