package main;

import javax.swing.JFrame;

import gui.panel.Acces;

public class CasaDeiGiochi {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.add(new Acces());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);
        frame.setVisible(true);
    }
}
