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

import applicazione.dao.DAOData;
import applicazione.dao.DAOException;
import applicazione.dao.DAOUtils;

public class LibriEFumetti extends JPanel{
    private static final String CATEGORY_ITEM_QUERY = "SELECT * FROM LIBRI_E_FUMETTI WHERE id_articolo = ?";
    private static final String QUERY = "INSERT INTO LIBRI_E_FUMETTI (serie, categoria, autore, id_articolo) "
            + " VALUE (?, ?, ?, ?)";
    private final static Connection connection = DAOUtils.localMySQLConnection(DAOData.DATABASE, DAOData.USERNAME,
            DAOData.PASSWORD);
    private final JTextArea serie = new JTextArea("serie");
    private final JTextArea insertSerie = new JTextArea();
    private final JTextArea categoria = new JTextArea("categoria");
    private final JTextArea insertCategoria = new JTextArea();
    private final JTextArea autore = new JTextArea("autore");
    private final JTextArea insertAutore = new JTextArea();
    private final JButton ok = new JButton("confirm");
    private final JPanel gridPanel = new JPanel();
    private final String itemId;

    public LibriEFumetti(final String itemId) {
        this.itemId = itemId;
        setLayout(new BorderLayout());
        add(gridPanel, BorderLayout.CENTER);
        this.gridPanel.setLayout(new GridLayout(3, 2));
        this.gridPanel.add(serie);
        this.gridPanel.add(insertSerie);
        this.gridPanel.add(categoria);
        this.gridPanel.add(insertCategoria);
        this.gridPanel.add(autore);
        this.gridPanel.add(insertAutore);
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
                    var stm = DAOUtils.prepare(connection, QUERY, this.insertSerie.getText(),
                            this.insertCategoria.getText(), this.insertAutore.getText(),
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
        if (this.insertCategoria.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "il linea categoria è vuoto", "Error",
                    JOptionPane.PLAIN_MESSAGE);
            return false;            
        } else if (this.insertAutore.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "il linea autore è vuoto", "Error",
                    JOptionPane.PLAIN_MESSAGE);
            return false;            
        } else if (this.insertSerie.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "il campo serie è vuoto", "Error",
                    JOptionPane.PLAIN_MESSAGE);
            return false;            
        } else if (this.insertSerie.getText().length() > 100) {
            JOptionPane.showMessageDialog(this, "il campo serie è troppo lungo (max 100 caratteri)", "Error",
                    JOptionPane.PLAIN_MESSAGE);
            return false;
        } else if (this.insertCategoria.getText().length() > 100) {
            JOptionPane.showMessageDialog(this, "il campo categoria è troppo lungo (max 100 caratteri)", "Error",
                    JOptionPane.PLAIN_MESSAGE);
            return false;            
        } else if (this.insertAutore.getText().length() > 100) {
            JOptionPane.showMessageDialog(this, "il campo autore è troppo lungo (max 100 caratteri)", "Error",
                    JOptionPane.PLAIN_MESSAGE);
            return false;            
        } else {
            return true;
        }
    }
}
