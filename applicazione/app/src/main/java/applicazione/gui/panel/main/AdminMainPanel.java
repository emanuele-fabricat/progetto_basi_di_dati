package applicazione.gui.panel.main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import applicazione.controller.Controller;
import applicazione.gui.panel.admin.AdminOrder;
import applicazione.gui.panel.common.StartAndStopEvent;

public class AdminMainPanel extends JPanel{
    private static final Dimension SCREE_DIMENSION = Toolkit.getDefaultToolkit().getScreenSize();
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

    public AdminMainPanel (final Controller controller, final String mail) {
        setPreferredSize(SCREE_DIMENSION);
        setLayout(new BorderLayout());
        add(this.consolePanel, BorderLayout.EAST);
        this.consolePanel.setLayout(console);
        for (JButton jButton : buttons) {
            this.consolePanel.add(jButton);
        }
        add(this.back, BorderLayout.WEST);
        this.back.addActionListener(e -> controller.accessBoard());
        this.buttons.get(0).addActionListener(e -> visualizeCenter(new AdminOrder(mail)));
        this.buttons.get(3).addActionListener(e -> this.visualizeCenter(new StartAndStopEvent(mail, StartAndStopEvent.ADMIN)));
    }

    private void visualizeCenter(final JPanel panel) {
        BorderLayout layout = (BorderLayout) this.getLayout();
        Component center = layout.getLayoutComponent(BorderLayout.CENTER);
        if (center != null) {
            this.remove(center);
        }
        add(panel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}
