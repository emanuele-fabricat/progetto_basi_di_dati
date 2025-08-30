package gui.panel.main;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import controller.Controller;

public class ClientMainPanel extends JPanel{
    private final Controller controller;
    private GridLayout console = new GridLayout(6, 1);
    private JPanel consolePanel = new JPanel();
    private JButton back = new JButton("Indietro");
    private LinkedList<JButton> buttons = new LinkedList<>(List.of(
        new JButton("Fai un ordine"), 
        new JButton("Visualizza gli ordini fatti"), 
        new JButton("Crea un evento"), 
        new JButton("Partecipa a un evento"), 
        new JButton("Visualizza tutti gli eventi"),
        new JButton("Visualizza gli articoli")
    ));

    public ClientMainPanel (final Controller controller) {
        this.controller = controller;
        this.setLayout(new BorderLayout());
        this.add(this.consolePanel, BorderLayout.EAST);
        this.consolePanel.setLayout(console);
        for (JButton jButton : buttons) {
            this.consolePanel.add(jButton);
        }
        this.add(this.back, BorderLayout.WEST);
        this.back.addActionListener(e -> controller.accessBoard());
    }
}
