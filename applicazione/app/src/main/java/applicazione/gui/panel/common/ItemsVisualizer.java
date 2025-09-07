package applicazione.gui.panel.common;

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

public class ItemsVisualizer extends JScrollPane {
    private static final String VISUALIZE_QUERY = "SELECT * FROM ARTICOLO ORDER BY tipologia, nome";

    private final Connection connection = DAOUtils.localMySQLConnection(DAOData.DATABASE, DAOData.USERNAME,
            DAOData.PASSWORD);

    public record VisualItem(String articoloId, String nome, String descrizione, int prezzo, int disponibilità,
            String tipo) {
    }

    private List<VisualItem> items = new LinkedList<>();

    private static JTable table;
    private static DefaultTableModel model;

    public ItemsVisualizer() {
        try (
                var stm = DAOUtils.prepare(connection, VISUALIZE_QUERY);
                var rS = stm.executeQuery();) {
            while (rS.next()) {
                this.items.add(
                        new VisualItem(rS.getString("id_articolo"), rS.getString("nome"), rS.getString("descrizione"),
                                rS.getInt("prezzo_vendita"), rS.getInt("disponibilità"), rS.getString("tipologia")));

            }
        } catch (Exception e) {
            throw new DAOException(e);
        }

        String[] column = { "id articolo", "nome", "descrizione", "prezzo (€)", "copie disponibili", "tipologia" };
        Object[][] data = new Object[items.size()][column.length];
        for (int i = 0; i < this.items.size(); i++) {
            data[i][0] = this.items.get(i).articoloId;
            data[i][1] = this.items.get(i).nome;
            data[i][2] = this.items.get(i).descrizione;
            data[i][3] = this.items.get(i).prezzo;
            data[i][4] = this.items.get(i).disponibilità;
            data[i][5] = this.items.get(i).tipo;
        }
        setBorder(new TitledBorder("Tutti gli eventio"));
        model = new DefaultTableModel(data, column);
        table = new JTable(model);
        setViewportView(table);

    }
}
