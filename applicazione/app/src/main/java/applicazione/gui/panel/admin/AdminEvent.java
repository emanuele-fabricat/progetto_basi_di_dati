package applicazione.gui.panel.admin;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import applicazione.dao.DAOData;
import applicazione.dao.DAOException;
import applicazione.dao.DAOUtils;
import applicazione.gui.panel.common.VisualizeFreeTable;
import applicazione.model.Event;

public class AdminEvent extends JPanel {
    private static final String ITEM_QUERY = "SELECT * FROM ARTICOLO ORDER BY nome";

    private record Item(String id, String name, String description, int price, int qta, String type) {
    }

    private final Connection connection = DAOUtils.localMySQLConnection(DAOData.DATABASE, DAOData.USERNAME,
            DAOData.PASSWORD);
    private List<Item> items = new LinkedList<>();
    private final JScrollPane tablePanel = new JScrollPane();

    private static JTable table;
    private static DefaultTableModel model;

    public AdminEvent(Event event) {
        add(tablePanel);
        add(new VisualizeFreeTable(event.inizio(), event.fine()));
        add(new MakePublicEvent(event));
        setLayout(new GridLayout(3, 1));
        try (
                var stm = DAOUtils.prepare(connection, ITEM_QUERY);
                var rS = stm.executeQuery();) {
            while (rS.next()) {
                this.items.add(new Item(rS.getString("id_articolo"), rS.getString("nome"), rS.getString("descrizione"),
                        rS.getInt("prezzo_vendita"), rS.getInt("disponibilità"), rS.getString("tipologia")));
            }
        } catch (Exception e) {
            throw new DAOException(e);
        }

        String[] column = { "id Articolo", "Nome", "descrizione", "costo €", "disponibilità", "tipologia" };
        Object[][] data = new Object[items.size()][column.length];

        for (int i = 0; i < this.items.size(); i++) {
            data[i][0] = this.items.get(i).id;
            data[i][1] = this.items.get(i).name;
            data[i][2] = this.items.get(i).description;
            data[i][3] = this.items.get(i).price;
            data[i][4] = this.items.get(i).qta;
            data[i][5] = this.items.get(i).type;
        }
        TitledBorder borderTitle = BorderFactory.createTitledBorder("Articoli in negozio");
        borderTitle.setTitleFont(new Font("Arial", Font.BOLD, 14));
        borderTitle.setTitleColor(Color.BLUE);
        setBorder(borderTitle);

        model = new DefaultTableModel(data, column);
        table = new JTable(model);
        tablePanel.setViewportView(table);

    }

    public static void deleteRow(final String id) {
        if (table.isEditing()) {
            table.getCellEditor().stopCellEditing();
        }
        for (int i = 0; i < model.getRowCount(); i++) {
            Object cellValue = model.getValueAt(i, 0);
            if (id.equals(cellValue)) {
                model.removeRow(i);
                table.revalidate();
                table.repaint();
                return;
            }
        }
    }
}
