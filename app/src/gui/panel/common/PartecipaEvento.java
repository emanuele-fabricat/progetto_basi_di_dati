package gui.panel.common;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class PartecipaEvento  extends JPanel{
    private final String id;
    
    public PartecipaEvento (final String id) {
        this.id = id;
        add(new JLabel("Sono il partecipa evento di " + this.id));
    }
}
