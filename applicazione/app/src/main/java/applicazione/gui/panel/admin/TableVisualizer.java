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
import applicazione.model.Table;

public class TableVisualizer extends JScrollPane{
    private static final String VISUALIZE_QUERY = "SELECT * FROM TAVOLO ORDER BY numero ";

    private final Connection connection = DAOUtils.localMySQLConnection(DAODataConfig.DATABASE, DAODataConfig.USERNAME,
            DAODataConfig.PASSWORD);

    private List<Table> tables = new LinkedList<>();

    private static JTable table;
    private static DefaultTableModel model;


    public TableVisualizer() {
        try (
                var stm = DAOUtils.prepare(connection, VISUALIZE_QUERY);
                var rS = stm.executeQuery();) {
            while (rS.next()) {
                this.tables.add(new Table(rS.getInt("numero"), rS.getInt("capienza")));

            }
        } catch (Exception e) {
            throw new DAOException(e);
        }

        String[] column = { "numero del tavolo", "numero posti"};
        Object[][] data = new Object[tables.size()][column.length];
        for (int i = 0; i < this.tables.size(); i++) {
            data[i][0] = this.tables.get(i).numero();
            data[i][1] = this.tables.get(i).capienza();
        }
        setBorder(new TitledBorder("Tutti i tavoli"));
        model = new DefaultTableModel(data, column);
        table = new JTable(model);
        setViewportView(table);

    }
}
