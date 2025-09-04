package applicazione.gui.panel.admin;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;
import java.awt.GridLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import applicazione.dao.DAOData;
import applicazione.dao.DAOException;
import applicazione.dao.DAOUtils;

public class AdminOrder extends JPanel {
    private static final String PRODUCT_QUERY = "SELECT * FROM PRODOTTO ORDER BY p_iva, nome_prodotto ";

    private record Product(String id, String nome, int costo, String pIva) {
    }

    private final Connection connection = DAOUtils.localMySQLConnection(DAOData.DATABASE, DAOData.USERNAME,
            DAOData.PASSWORD);
    private List<Product> products = new LinkedList<>();
    private final JScrollPane tablePanel = new JScrollPane();

    public AdminOrder(final String userId) {
        setLayout(new GridLayout(2, 1));
        add(tablePanel);
        add(new AddItem(userId));
        try (
                var stm = DAOUtils.prepare(connection, PRODUCT_QUERY);
                var rS = stm.executeQuery();) {
            while (rS.next()) {
                this.products.add(new Product(rS.getString("id_prodotto"), rS.getString("nome_prodotto"),
                        rS.getInt("costo"), rS.getString("p_iva")));
            }
        } catch (Exception e) {
            throw new DAOException(e);
        }

        String[] column = { "Prodotto id", "Nome prodotto", "costo €", "p.iva fornitore" };
        Object[][] data = new Object[products.size()][column.length];

        for (int i = 0; i < this.products.size(); i++) {
            data[i][0] = this.products.get(i).id;
            data[i][1] = this.products.get(i).nome;
            data[i][2] = this.products.get(i).costo;
            data[i][3] = this.products.get(i).pIva;
        }

        JTable table = new JTable(data, column);
        tablePanel.setViewportView(table);
        JOptionPane.showMessageDialog(this,
                "Questa schermata serve per creare ordini e aggiungere il relativo articolo nel negozio, " +
                        "se un articolo è già presente non saranno salvate altre informazioni se non l'aumento di disponibilità, "
                        +
                        "per quelli nuovi invece saranno aggiunte tutte le informazioni inserite",
                "info", JOptionPane.PLAIN_MESSAGE);

    }
}
