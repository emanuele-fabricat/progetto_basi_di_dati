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
import applicazione.gui.panel.client.ClientOrder;
import applicazione.gui.panel.client.OwnOrderVisualizer;
import applicazione.gui.panel.common.EventsParticipator;
import applicazione.gui.panel.common.EventsVisualizer;
import applicazione.gui.panel.common.ItemsVisualizer;
import applicazione.gui.panel.common.StartEvent;
import applicazione.model.Client;

public class ClientMainPanel extends JPanel {
    private static final Dimension SCREE_DIMENSION = Toolkit.getDefaultToolkit().getScreenSize();
    private GridLayout console = new GridLayout(6, 1);
    private JPanel consolePanel = new JPanel();
    private JButton back = new JButton("Indietro");
    private LinkedList<JButton> buttons = new LinkedList<>(List.of(
            new JButton("Fai un ordine"),
            new JButton("Visualizza gli ordini fatti"),
            new JButton("Crea un evento"),
            new JButton("Partecipa a un evento"),
            new JButton("Visualizza tutti gli eventi"),
            new JButton("Visualizza gli articoli")));

    public ClientMainPanel(final Controller controller, final String mail) {
        setPreferredSize(SCREE_DIMENSION);
        this.setLayout(new BorderLayout());
        this.add(this.consolePanel, BorderLayout.EAST);
        this.consolePanel.setLayout(console);
        for (JButton jButton : buttons) {
            this.consolePanel.add(jButton);
        }
        this.add(this.back, BorderLayout.WEST);
        this.back.addActionListener(e -> controller.accessBoard());
        this.buttons.get(0).addActionListener(e -> this.visualizeCenter(new ClientOrder(Client.getId(mail))));
        this.buttons.get(1).addActionListener(e -> this.visualizeCenter(new OwnOrderVisualizer(Client.getId(mail))));
        this.buttons.get(2).addActionListener(e -> this.visualizeCenter(new StartEvent(Client.getId(mail), StartEvent.CLIENT)));
        this.buttons.get(3).addActionListener(
                e -> this.visualizeCenter(new EventsParticipator(Client.getId(mail), EventsParticipator.CLIENT_USER)));
        this.buttons.get(4).addActionListener(e -> this.visualizeCenter(new EventsVisualizer(Client.getId(mail), EventsParticipator.CLIENT_USER)));
        this.buttons.get(5).addActionListener(e -> this.visualizeCenter(new ItemsVisualizer()));
    }

    private void visualizeCenter(final Component panel) {
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
