package applicazione;

import java.awt.Toolkit;

import javax.swing.JFrame;

import applicazione.gui.panel.admin.AddTable;
import applicazione.gui.panel.client.ItemInformation;
import applicazione.gui.panel.main.AdminMainPanel;
import applicazione.gui.panel.main.ClientMainPanel;

public class TestGui {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.add(new ItemInformation());
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        frame.pack();
        frame.setVisible(true);

    }

}
