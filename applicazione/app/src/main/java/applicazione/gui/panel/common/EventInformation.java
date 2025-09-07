package applicazione.gui.panel.common;

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

public class EventInformation extends JDialog {
    private static final String VISUALIZE_QUERY = "SELECT U.responsabile, U.telefono, T.*, S.quantità, A.nome "
            + "FROM UTILIZZARE U " + "JOIN TAVOLO T ON T.numero = U.numero "
            + "LEFT JOIN SERVIRE S ON T.numero = S.numero AND S.id_evento = U.id_evento "
            + "LEFT JOIN ARTICOLO A ON A.id_articolo = S.id_articolo " + "WHERE U.id_evento = ? ";
    private final static Connection connection = DAOUtils.localMySQLConnection(DAODataConfig.DATABASE,
            DAODataConfig.USERNAME,
            DAODataConfig.PASSWORD);
    private JTable table;
    private final JScrollPane tablePanel = new JScrollPane();
    private DefaultTableModel model;

    private record Info(String responsabile, String telefono, int numero, int capienza, int quantità, String nome) {
    }

    private final List<Info> information = new LinkedList<>();

    public EventInformation(final Component parent, final String eventId) {
        super(SwingUtilities.getWindowAncestor(parent), "Informazioni evento", ModalityType.DOCUMENT_MODAL);
        add(tablePanel);
        try (
                var stm = DAOUtils.prepare(connection, VISUALIZE_QUERY, eventId);
                var rS = stm.executeQuery();) {
            while (rS.next()) {
                this.information.add(new Info(rS.getString("responsabile"), rS.getString("telefono"),
                        rS.getInt("numero"), rS.getInt("capienza"), rS.getInt("quantità"),
                        rS.getString("nome")));

            }
        } catch (Exception e) {
            throw new DAOException(e);
        }

        String[] column = { "responsabile", "telefono", "numero tavolo", "capienza",
                "quantità", "nome" };
        Object[][] data = new Object[information.size()][column.length];
        for (int i = 0; i < this.information.size(); i++) {
            data[i][0] = this.information.get(i).responsabile();
            data[i][1] = this.information.get(i).telefono();
            data[i][2] = this.information.get(i).numero();
            data[i][3] = this.information.get(i).capienza();
            data[i][4] = this.information.get(i).quantità();
            data[i][5] = this.information.get(i).nome();
        }
        model = new DefaultTableModel(data, column);
        table = new JTable(model);
        this.tablePanel.setViewportView(table);
        pack();
    }
}
