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

public class ProductVisualizer extends JScrollPane {
    private static final String VISUALIZE_QUERY = "SELECT P.*, F.dominio, F.nominativo FROM PRODOTTO P, FORNITORE F WHERE P.p_iva = F.p_iva ORDER BY F.nominativo, P.nome_prodotto";

    private record VisualProduct(String prodottoId, String nome, int costo, String pIva, String dominio,
            String nominativo) {
    }

    private final Connection connection = DAOUtils.localMySQLConnection(DAODataConfig.DATABASE, DAODataConfig.USERNAME,
            DAODataConfig.PASSWORD);

    private List<VisualProduct> tables = new LinkedList<>();
    private static JTable table;
    private static DefaultTableModel model;

    public ProductVisualizer() {
        try (
                var stm = DAOUtils.prepare(connection, VISUALIZE_QUERY);
                var rS = stm.executeQuery();) {
            while (rS.next()) {
                this.tables
                        .add(new VisualProduct(rS.getString("id_prodotto"), rS.getString("nome_prodotto"), rS.getInt("costo"),
                                rS.getString("p_iva"), rS.getString("dominio"), rS.getString("nominativo")));
            }
        } catch (Exception e) {
            throw new DAOException(e);
        }

        String[] column = { "id prodotto", "nome prodotto", "costo (€)", "partita iva", "dominio", "nominativo azienda" };
        Object[][] data = new Object[tables.size()][column.length];

        for (int i = 0; i < this.tables.size(); i++) {
            data[i][0] = this.tables.get(i).prodottoId();
            data[i][1] = this.tables.get(i).nome();
            data[i][2] = this.tables.get(i).costo();
            data[i][3] = this.tables.get(i).pIva();
            data[i][4] = this.tables.get(i).dominio();
            data[i][5] = this.tables.get(i).nominativo();
        }
        setBorder(new TitledBorder("Tutti i prodotti"));

        model = new DefaultTableModel(data, column);
        table = new JTable(model);
        setViewportView(table);
    }

}
