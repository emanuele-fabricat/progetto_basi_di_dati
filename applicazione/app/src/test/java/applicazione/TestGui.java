package applicazione;

import java.awt.Toolkit;

import javax.swing.JFrame;

import applicazione.gui.panel.admin.AddTable;
import applicazione.gui.panel.admin.ProductVisualizer;
import applicazione.gui.panel.client.ItemInformation;
import applicazione.gui.panel.client.OwnOrderVisualizer;
import applicazione.gui.panel.common.EventsParticipator;
import applicazione.gui.panel.common.EventsVisualizer;
import applicazione.gui.panel.main.AdminMainPanel;
import applicazione.gui.panel.main.ClientMainPanel;

public class TestGui {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.add(new OwnOrderVisualizer("ca119548-e9c4-4e98-9428-2d18d9420c1d"));
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        frame.pack();
        frame.setVisible(true);

    }

}
