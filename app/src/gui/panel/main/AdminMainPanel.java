package gui.panel.main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import controller.Controller;
import gui.panel.common.VisualizzaEventi;

public class AdminMainPanel extends JPanel{
    private final Controller controller;
    private GridLayout console = new GridLayout(8, 1);
    private JPanel consolePanel = new JPanel();
    private JButton back = new JButton("Indietro");
    private LinkedList<JButton> buttons = new LinkedList<>(List.of(
        new JButton("Fai un ordine"), 
        new JButton("Visualizza gli ordini degli admin"),
        new JButton("Visualizza gli ordini dei clienti"), 
        new JButton("Crea un evento"), 
        new JButton("Partecipa a un evento"), 
        new JButton("Visualizza tutti gli eventi"), 
        new JButton("Visualizza i tavoli"), 
        new JButton("Visualizza gli articoli")
    ));

    public AdminMainPanel (final Controller controller) {
        this.controller = controller;
        this.setLayout(new BorderLayout());
        this.add(this.consolePanel, BorderLayout.EAST);
        this.consolePanel.setLayout(console);
        for (JButton jButton : buttons) {
            this.consolePanel.add(jButton);
        }
        this.add(this.back, BorderLayout.WEST);
        this.back.addActionListener(e -> controller.accessBoard());
        this.buttons.get(5).addActionListener(e -> visualizeCenter(new VisualizzaEventi("aa")));//da cambiare
    }

    private void visualizeCenter(final JScrollPane panel) {
        BorderLayout layout = (BorderLayout) this.getLayout();
        Component center = layout.getLayoutComponent(BorderLayout.CENTER);
        if (center != null) {
            this.remove(center);
        }
        this.add(panel, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
    }
}
