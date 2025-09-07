package applicazione.gui.panel.admin;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import applicazione.dao.DAODataConfig;
import applicazione.dao.DAOException;
import applicazione.dao.DAOUtils;
import applicazione.gui.panel.common.StartEvent;
import applicazione.gui.panel.common.VisualizeFreeTable;
import applicazione.model.Event;

public class AdminEvent extends JPanel {
    private static final String ITEM_QUERY = "SELECT * FROM ARTICOLO ORDER BY tipologia, nome";

    private record Item(String id, String name, String description, int price, int qta, String type) {
    }

    private final Connection connection = DAOUtils.localMySQLConnection(DAODataConfig.DATABASE, DAODataConfig.USERNAME,
            DAODataConfig.PASSWORD);
    private List<Item> items = new LinkedList<>();
    private final JScrollPane tablePanel = new JScrollPane();
    private final JButton back = new JButton("Inizia un nuovo ordine");

    private static JTable table;
    private static DefaultTableModel model;
    private final String userId;

    public AdminEvent(final String userId, final Event event) {
        this.userId = userId;
        add(back);
        add(tablePanel);
        add(new VisualizeFreeTable(event.inizio(), event.fine()));
        add(new MakePublicEvent(userId, event));
        setLayout(new GridLayout(4, 1));
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
        this.tablePanel.setBorder(new TitledBorder(
                "Articoli in negozio"));

        model = new DefaultTableModel(data, column);
        table = new JTable(model);
        tablePanel.setViewportView(table);
        this.back.addActionListener(e -> goBack());

    }

    public static Optional<Integer> getRowById(String idArticolo) {
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).equals(idArticolo)) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }

    public static void SubtractOnCell(int row) {
        if (row >= 0 && row < model.getRowCount()) {
            Object cellValue = model.getValueAt(row, 4);
            if (cellValue instanceof Number) {
                int valoreAttuale = ((Number) cellValue).intValue();
                int nuovoValore = valoreAttuale - 1;
                model.setValueAt(nuovoValore, row, 4);
            }
        }
    }

    private void goBack() {
        removeAll();
        setLayout(new BorderLayout());
        add(new StartEvent(this.userId, StartEvent.ADMIN));
        revalidate();
        repaint();
    }
}
