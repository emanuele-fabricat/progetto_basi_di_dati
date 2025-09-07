package applicazione.gui.panel.client;

import java.awt.Component;
import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import applicazione.dao.DAODataConfig;
import applicazione.dao.DAOException;
import applicazione.dao.DAOUtils;

public class CartInformation extends JDialog {
    private static final String VISUALIZE_QUERY = "SELECT AG.carico, AR.nome, AR.descrizione, AR.prezzo_vendita "
            + "FROM AGGIUNGERE AG, ARTICOLO AR " + "WHERE AG.id_carrello = ? " + "AND AG.id_articolo = AR.id_articolo";
    private final static Connection connection = DAOUtils.localMySQLConnection(DAODataConfig.DATABASE,
            DAODataConfig.USERNAME,
            DAODataConfig.PASSWORD);
    private JTable table;
    private final JScrollPane tablePanel = new JScrollPane();
    private DefaultTableModel model;

    private record CartInfo(int carico, String nome, String descrizione, int prezzo) {}
    private final List<CartInfo> information = new LinkedList<>();

    public CartInformation(final Component parent, final String cartId) {
        super(SwingUtilities.getWindowAncestor(parent), "Informazioni carrello", ModalityType.DOCUMENT_MODAL);
        add(tablePanel);
        try (
                var stm = DAOUtils.prepare(connection, VISUALIZE_QUERY, cartId);
                var rS = stm.executeQuery();) {
            while (rS.next()) {
                this.information.add(new CartInfo(rS.getInt("carico"), rS.getString("nome"), rS.getString("descrizione"),
                        rS.getInt("prezzo_vendita")));

            }
        } catch (Exception e) {
            throw new DAOException(e);
        }

        String[] column = { "quantità acquistata", "nome dell'articolo", "descrizione", "prezzo (€)"};
        Object[][] data = new Object[information.size()][column.length];
        for (int i = 0; i < this.information.size(); i++) {
            data[i][0] = this.information.get(i).carico();
            data[i][1] = this.information.get(i).nome;
            data[i][2] = this.information.get(i).descrizione();
            data[i][3] = this.information.get(i).prezzo();
        }
        model = new DefaultTableModel(data, column);
        table = new JTable(model);
        this.tablePanel.setViewportView(table);
        pack();

    }

}
