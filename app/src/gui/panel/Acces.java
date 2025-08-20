package gui.panel;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

public class Acces extends JPanel{
    private static final String MAIL = "admin";
    private static final String PASSWORD = "admin";
    private JButton login = new JButton("Login");
    private JTextArea insertMail = new JTextArea();
    private JTextArea insertPassword = new JTextArea();
    private JPanel accesData = new JPanel();

    public Acces () {
        setLayout(new BorderLayout());
        add(new JTextArea("Casa dei Giochi"), BorderLayout.NORTH);
        login.addActionListener(e -> security(insertMail, insertPassword));
        add(login, BorderLayout.SOUTH);
        accesData.setLayout(new GridLayout(2, 2));
        accesData.add(new JTextArea("mail"));
        insertMail.setEditable(true);
        insertMail.setBorder(new LineBorder(Color.BLACK));
        accesData.add(insertMail);
        accesData.add(new JTextArea("password"));
        insertPassword.setEditable(true);
        insertPassword.setBorder(new LineBorder(Color.BLACK));
        accesData.add(insertPassword);
        add(accesData, BorderLayout.CENTER);
        accesData.setBorder(new LineBorder(Color.BLACK));
    }

    private void security (JTextArea mail, JTextArea password) {
        if (mail.getText() == MAIL || password.getText() == PASSWORD) {
            System.out.println("sei dentro");
        } else {
            System.out.println("sei fuori");
        }
    }

}
