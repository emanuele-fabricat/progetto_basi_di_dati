package gui.panel.common;

import java.sql.Connection;
import javax.swing.JScrollPane;

import dao.DAOException;
import dao.DAOUtils;

public class VisualizzaEventi extends JScrollPane{
    private final String id;
    private final Connection connection = DAOUtils.localMySQLConnection("casa_dei_giochi", "root", "el@pFG2020");
    
    public VisualizzaEventi (final String id) {
        this.id = id;
        try (
            var stm = DAOUtils.prepare(connection, "SELECT * FROM FORNITORE", "");
            var rS = stm.executeQuery();
        ) {

        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

}
