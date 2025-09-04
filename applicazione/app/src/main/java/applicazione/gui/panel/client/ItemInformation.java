package applicazione.gui.panel.client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import applicazione.gui.panel.client.item.category.AccessoriInformation;
import applicazione.gui.panel.client.item.category.CarteCollezionabiliInformation;
import applicazione.gui.panel.client.item.category.GadgetEGiocattoliInformation;
import applicazione.gui.panel.client.item.category.GiochiDiRuoloInformation;
import applicazione.gui.panel.client.item.category.GiochiInScatolaInformation;
import applicazione.gui.panel.client.item.category.LibriEFumettiInformation;
import applicazione.gui.panel.client.item.category.ModellismoInformation;
import applicazione.model.Item;

public class ItemInformation extends JPanel {
    private final JTextArea id = new JTextArea("Iserire id dell'articolo di cui si vogliono maggiori informazioni");
    private final JTextArea insertId = new JTextArea();
    private final JTextArea type = new JTextArea(
            "Selezionare il tipologia dell'articolo di cui si vogliono maggiori informazioni");
    private final String[] option = { "CARTE_COLLEZIONABILI", "GIOCHI_DI_RUOLO", "GIOCHI_IN_SCATOLA",
            "LIBRI_E_FUMETTI", "MODELLISMO", "GADGET_E_GIOCATTOLI", "ACCESSORI" };
    private final JComboBox<String> selction = new JComboBox<>(this.option);
    private final JButton ok = new JButton("confirm");

    public ItemInformation() {
        setLayout(new GridLayout(5, 1));
        add(id);
        add(insertId);
        add(type);
        add(selction);
        add(ok);
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        for (Component comp : this.getComponents()) {
            if (comp instanceof JTextArea textArea) {
                textArea.setBorder(border);
                if (!textArea.getText().isEmpty()) {
                    textArea.setEnabled(false);
                    textArea.setDisabledTextColor(Color.BLACK);
                }
            }
        }
        TitledBorder borderTitle = BorderFactory.createTitledBorder("Area informazioni");
        borderTitle.setTitleFont(new Font("Arial", Font.BOLD, 14));
        borderTitle.setTitleColor(Color.BLUE);
        setBorder(borderTitle);
        this.ok.addActionListener(e -> visualizeInformation());

    }

    private void visualizeInformation() {
        if (!Item.existId(this.insertId.getText())) {
            JOptionPane.showMessageDialog(this, "id articolo inesistente", "Error", JOptionPane.PLAIN_MESSAGE);
            return;
        }
        switch ((String) selction.getSelectedItem()) {
            case "ACCESSORI":
                JOptionPane.showMessageDialog(this, new AccessoriInformation(this.insertId.getText()),
                        (String) selction.getSelectedItem(), JOptionPane.PLAIN_MESSAGE);
                break;
            case "CARTE_COLLEZIONABILI":
                JOptionPane.showMessageDialog(this, new CarteCollezionabiliInformation(this.insertId.getText()),
                        (String) selction.getSelectedItem(), JOptionPane.PLAIN_MESSAGE);
                break;
            case "GADGET_E_GIOCATTOLI":
                JOptionPane.showMessageDialog(this, new GadgetEGiocattoliInformation(this.insertId.getText()),
                        (String) selction.getSelectedItem(), JOptionPane.PLAIN_MESSAGE);
                break;
            case "GIOCHI_DI_RUOLO":
                JOptionPane.showMessageDialog(this, new GiochiDiRuoloInformation(this.insertId.getText()),
                        (String) selction.getSelectedItem(), JOptionPane.PLAIN_MESSAGE);
                break;
            case "GIOCHI_IN_SCATOLA":
                JOptionPane.showMessageDialog(this, new GiochiInScatolaInformation(this.insertId.getText()),
                        (String) selction.getSelectedItem(), JOptionPane.PLAIN_MESSAGE);
                break;
            case "LIBRI_E_FUMETTI":
                JOptionPane.showMessageDialog(this, new LibriEFumettiInformation(this.insertId.getText()),
                        (String) selction.getSelectedItem(), JOptionPane.PLAIN_MESSAGE);
                break;
            case "MODELLISMO":
                JOptionPane.showMessageDialog(this, new ModellismoInformation(this.insertId.getText()),
                        (String) selction.getSelectedItem(), JOptionPane.PLAIN_MESSAGE);
                break;
            default:
                JOptionPane.showMessageDialog(this, "non corrisponde nulla", "Error", JOptionPane.PLAIN_MESSAGE);
                break;
        }
    }

}
