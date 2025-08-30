package controller;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

import gui.panel.main.Acces;
import gui.panel.main.AdminMainPanel;
import gui.panel.main.ClientMainPanel;

public class Controller {
    private static final Dimension SCREE_DIMENSION = Toolkit.getDefaultToolkit().getScreenSize();
    private JFrame mainFrame;

    public Controller(final JFrame frame) {
        this.mainFrame = frame;
        this.mainFrame.add(new Acces(this, this.mainFrame));
        this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.mainFrame.setSize(300, 150);
        this.mainFrame.setVisible(true);
    }

    public void admin() {
        this.setNewFrame(new AdminMainPanel(this));
    }

    public void client() {
        this.setNewFrame(new ClientMainPanel(this));
    }

    public void accessBoard() {
        this.mainFrame.dispose();
        this.mainFrame = new JFrame();
        this.mainFrame.add(new Acces(this, this.mainFrame));
        this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.mainFrame.setSize(300, 150);
        this.mainFrame.setVisible(true);
    }

    private void setNewFrame (final JPanel newPanel) {
        this.mainFrame.dispose();
        this.mainFrame = new JFrame();
        this.mainFrame.setSize(SCREE_DIMENSION);
        this.mainFrame.setResizable(false);
        this.mainFrame.setUndecorated(false);
        this.mainFrame.add(newPanel);
        this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.mainFrame.setVisible(true);
    }

}
