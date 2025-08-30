package main;


import javax.swing.JFrame;

import controller.Controller;

public class CasaDeiGiochi {
    public static void main(String[] args) {
        Controller controller;
        JFrame frame = new JFrame();
        controller = new Controller(frame);
    }
}
