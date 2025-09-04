package applicazione.gui.panel.common;

import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import applicazione.dao.DAOData;
import applicazione.dao.DAOException;
import applicazione.dao.DAOUtils;
import applicazione.model.Table;

public class VisualizeFreeTable extends JScrollPane {
    private static final String ITEM_QUERY = "SELECT * FROM TAVOLO ORDER BY numero";

    private record EventTable(String number, String capienza) {
    }

    private final Connection connection = DAOUtils.localMySQLConnection(DAOData.DATABASE, DAOData.USERNAME,
            DAOData.PASSWORD);
    private List<EventTable> tables = new LinkedList<>();

    private static JTable table;
    private static DefaultTableModel model;

    public VisualizeFreeTable(String startDate, String endDate) {
        try (
                var stm = DAOUtils.prepare(connection, ITEM_QUERY);
                var rS = stm.executeQuery();) {
            while (rS.next()) {
                this.tables.add(new EventTable(rS.getString("numero"), rS.getString("capienza")));
            }
        } catch (Exception e) {
            throw new DAOException(e);
        }
        tables.removeIf(table -> Table.isOccupied(table.number, startDate, endDate));

        String[] column = { "numero tavolo", "capienza" };
        Object[][] data = new Object[tables.size()][column.length];

        for (int i = 0; i < this.tables.size(); i++) {
            data[i][0] = this.tables.get(i).number;
            data[i][1] = this.tables.get(i).capienza;
        }
        TitledBorder borderTitle = BorderFactory.createTitledBorder("Tavoli liberi tra " + startDate + " e " + endDate);
        borderTitle.setTitleFont(new Font("Arial", Font.BOLD, 14));
        borderTitle.setTitleColor(Color.BLUE);
        setBorder(borderTitle);

        model = new DefaultTableModel(data, column);
        table = new JTable(model);
        setViewportView(table);
    }

    public static void deleteRow(final String number) {
        for (int i = 0; i < model.getRowCount(); i++) {
            Object cellValue = model.getValueAt(i, 0);
            if (number.equals(cellValue)) {
                model.removeRow(i);
                return;
            }
        }

    }

}
