package applicazione.controller;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

import applicazione.gui.panel.main.Acces;
import applicazione.gui.panel.main.AdminMainPanel;
import applicazione.gui.panel.main.AdminRegister;
import applicazione.gui.panel.main.ClientMainPanel;
import applicazione.gui.panel.main.ClientRegister;

public class Controller {
    private static final Dimension SCREE_DIMENSION = Toolkit.getDefaultToolkit().getScreenSize();
    private JFrame mainFrame;

    public Controller(final JFrame frame) {
        this.mainFrame = frame;
        this.mainFrame.add(new Acces(this));
        this.mainFrame.pack();
        this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.mainFrame.setSize(SCREE_DIMENSION);
        this.mainFrame.pack();
        this.mainFrame.setVisible(true);
    }

    public void admin(final String mail) {
        this.setNewFrame(new AdminMainPanel(this, mail));
    }

    public void client(final String mail) {
        this.setNewFrame(new ClientMainPanel(this, mail));
    }

    public void registerNewAdmin () {
        this.setNewFrame(new AdminRegister(this));
    }

    public void registerNewClient () {
        this.setNewFrame(new ClientRegister(this));
    }

    public void accessBoard() {
        this.setNewFrame(new Acces(this));
    }

    private void setNewFrame (final JPanel newPanel) {
        this.mainFrame.getContentPane().removeAll();
        this.mainFrame.add(newPanel);
        this.mainFrame.revalidate();
        this.mainFrame.pack();
        this.mainFrame.repaint();
    }

}
