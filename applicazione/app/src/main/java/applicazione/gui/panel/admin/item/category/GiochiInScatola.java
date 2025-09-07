package applicazione.gui.panel.admin.item.category;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Window;
import java.sql.Connection;
import java.util.Optional;

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

public class GiochiInScatola extends JPanel {
    private static final String CATEGORY_ITEM_QUERY = "SELECT * FROM GIOCHI_IN_SCATOLA WHERE id_articolo = ?";
    private static final String QUERY = "INSERT INTO GIOCHI_IN_SCATOLA (linea, meccanica, min_giocatori, max_giocatori, tempo_medio, id_articolo) "
            + " VALUE (?, ?, ?, ?, ?, ?)";
    private final static Connection connection = DAOUtils.localMySQLConnection(DAODataConfig.DATABASE, DAODataConfig.USERNAME,
            DAODataConfig.PASSWORD);
    private final JTextArea linea = new JTextArea("linea");
    private final JTextArea insertLinea = new JTextArea();
    private final JTextArea meccanica = new JTextArea("meccanica");
    private final JTextArea insertMeccanica = new JTextArea();
    private final JTextArea minGiocatori = new JTextArea("min giocatori");
    private final JTextArea insertMinGiocatori = new JTextArea();
    private final JTextArea maxGiocatori = new JTextArea("max giocatori");
    private final JTextArea insertMaxGiocatori = new JTextArea();
    private final JTextArea tempoMedio = new JTextArea("tempo medio");
    private final JTextArea insertTempoMedio = new JTextArea();
    private final JButton ok = new JButton("confirm");
    private final JPanel gridPanel = new JPanel();
    private final String itemId;

    public GiochiInScatola(final String itemId) {
        this.itemId = itemId;
        setLayout(new BorderLayout());
        add(gridPanel, BorderLayout.CENTER);
        this.gridPanel.setLayout(new GridLayout(5, 2));
        this.gridPanel.add(linea);
        this.gridPanel.add(insertLinea);
        this.gridPanel.add(meccanica);
        this.gridPanel.add(insertMeccanica);
        this.gridPanel.add(minGiocatori);
        this.gridPanel.add(insertMinGiocatori);
        this.gridPanel.add(maxGiocatori);
        this.gridPanel.add(insertMaxGiocatori);
        this.gridPanel.add(tempoMedio);
        this.gridPanel.add(insertTempoMedio);
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
        if (isValidText() && isValidNumber()) {
            try (
                    var stm = DAOUtils.prepare(connection, QUERY, this.insertLinea.getText(),
                            this.insertMeccanica.getText(), textAreaToInt(insertMinGiocatori).get(),
                            textAreaToInt(insertMaxGiocatori).get(), textAreaToInt(insertTempoMedio).get(),
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
        if (this.insertLinea.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "il cmapo linea è vuoto", "Error",
                    JOptionPane.PLAIN_MESSAGE);
            return false;            
        } else if (this.insertMeccanica.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "il campo meccanica è vuoto", "Error",
                    JOptionPane.PLAIN_MESSAGE);
            return false;            
        } else if (this.insertLinea.getText().length() > 100) {
            JOptionPane.showMessageDialog(this, "il campo linea è troppo lungo (max 100 caratteri)", "Error",
                    JOptionPane.PLAIN_MESSAGE);
            return false;
        } else if (this.insertMeccanica.getText().length() > 100) {
            JOptionPane.showMessageDialog(this, "il campo meccanica è troppo lungo (max 100 caratteri)", "Error",
                    JOptionPane.PLAIN_MESSAGE);
            return false;            
        } else {
            return true;
        }
    }

    private boolean isValidNumber() {
        if (textAreaToInt(this.insertMaxGiocatori).isEmpty() || textAreaToInt(this.insertMinGiocatori).isEmpty()
                || textAreaToInt(insertTempoMedio).isEmpty()) {
            JOptionPane.showMessageDialog(this, "inserire una quantità valida (solo numeri, senza spazi)", "Error",
                    JOptionPane.PLAIN_MESSAGE);
            return false;
        }
        return true;
    }

    private Optional<Integer> textAreaToInt(final JTextArea number) {
        int newNumb = 0;
        try {
            newNumb = Integer.parseInt(number.getText());
        } catch (Exception e) {
            return Optional.empty();
        }
        return Optional.of(newNumb);
    }
}
