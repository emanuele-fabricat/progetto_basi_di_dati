package applicazione.gui.panel.admin.item.category;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Window;
import java.sql.Connection;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import applicazione.dao.DAODataConfig;
import applicazione.dao.DAOException;
import applicazione.dao.DAOUtils;

public class Modellismo extends JPanel {
    private static final String CATEGORY_ITEM_QUERY = "SELECT * FROM MODELLISMO WHERE id_articolo = ?";
    private static final String QUERY = "INSERT INTO MODELLISMO (marca, id_articolo) "
            + " VALUE (?, ?)";
    private final static Connection connection = DAOUtils.localMySQLConnection(DAODataConfig.DATABASE, DAODataConfig.USERNAME,
            DAODataConfig.PASSWORD);
    private final JTextArea marca = new JTextArea("marca");
    private final JTextArea insertMarca = new JTextArea();
    private final JButton ok = new JButton("confirm");
    private final JPanel gridPanel = new JPanel();
    private final String itemId;

    public Modellismo(final String itemId) {
        this.itemId = itemId;
        setLayout(new BorderLayout());
        add(gridPanel, BorderLayout.CENTER);
        this.gridPanel.setLayout(new GridLayout(1, 2));
        this.gridPanel.add(marca);
        this.gridPanel.add(insertMarca);
        add(ok, BorderLayout.SOUTH);
        this.ok.addActionListener(e -> this.fillIt());
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        for (Component comp : this.gridPanel.getComponents()) {
            if (comp instanceof JTextArea textArea) {
                textArea.setBorder(border);
                if (!textArea.getText().isEmpty()) {
                    textArea.setEnabled(false);
                    textArea.setDisabledTextColor(Color.BLACK);
                }
            }
        }
    }

    private void fillIt() {
        if (isValidText()) {
            try (
                    var stm = DAOUtils.prepare(connection, QUERY, this.insertMarca.getText(),
                            this.itemId);) {
                stm.executeUpdate();
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }
        Window w = SwingUtilities.getWindowAncestor(this);
        w.dispose();
    }

    public static boolean exist(final String id) {
        try (
                var stm = DAOUtils.prepare(connection, CATEGORY_ITEM_QUERY, id);
                var rS = stm.executeQuery();) {
            return rS.next();
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

    private boolean isValidText() {
        if (this.insertMarca.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "il campo marca è vuoto", "Error",
                    JOptionPane.PLAIN_MESSAGE);
            return false;            
        } else if (this.insertMarca.getText().length() > 100) {
            JOptionPane.showMessageDialog(this, "il campo marca è troppo lungo (max 100 caratteri)", "Error",
                    JOptionPane.PLAIN_MESSAGE);
            return false;
        } else {
            return true;
        }
    }
}
