package applicazione;

import java.awt.Toolkit;

import javax.swing.JFrame;

import applicazione.controller.Controller;
import applicazione.gui.panel.admin.AddItem;
import applicazione.gui.panel.admin.AdminEvent;
import applicazione.gui.panel.admin.AdminOrder;
import applicazione.gui.panel.admin.AdminEvent;
import applicazione.gui.panel.admin.MakePublicEvent;
import applicazione.gui.panel.admin.item.category.Accessori;
import applicazione.gui.panel.admin.item.category.CarteCollezionabili;
import applicazione.gui.panel.admin.item.category.GadgetEGiocattoli;
import applicazione.gui.panel.admin.item.category.GiochiDiRuolo;
import applicazione.gui.panel.admin.item.category.GiochiInScatola;
import applicazione.gui.panel.admin.item.category.LibriEFumetti;
import applicazione.gui.panel.client.AddCart;
import applicazione.gui.panel.client.ClientOrder;
import applicazione.gui.panel.client.ItemInformation;
import applicazione.gui.panel.client.item.category.GiochiInScatolaInformation;
import applicazione.gui.panel.common.StartAndStopEvent;
import applicazione.gui.panel.main.Acces;
import applicazione.gui.panel.main.AdminMainPanel;
import applicazione.gui.panel.main.ClientMainPanel;
import applicazione.model.Event;

public class TestGui {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.add(new AdminEvent(new Event(null, null, 0, null, null, null, null, null, null)));
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        frame.pack();
        frame.setVisible(true);

    }

}
