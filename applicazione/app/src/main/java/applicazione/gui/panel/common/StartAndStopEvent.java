package applicazione.gui.panel.common;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import applicazione.gui.panel.admin.AdminEvent;
import applicazione.model.Event;

public class StartAndStopEvent extends JPanel {
    public static final int CLIENT = 0;
    public static final int ADMIN = 1;
    private final JTextArea inizio = new JTextArea();
    private final JTextArea fine = new JTextArea();
    private final JTextArea numPartecipanti = new JTextArea();
    private final JTextArea nome = new JTextArea();
    private final JScrollPane presentazionePanel = new JScrollPane();
    private final JTextArea presentazione = new JTextArea();
    private final JTextArea maxPartecipanti = new JTextArea();
    private final JButton confirm = new JButton("confirm");
    private String eventId = UUID.randomUUID().toString();
    private final String userId;
    private final int typeOfUser;

    public StartAndStopEvent(final String userId, final int typeOfUser) {
        this.typeOfUser = typeOfUser;
        this.userId = userId;
        if (typeOfUser == ADMIN) {
            setLayout(new GridLayout(7, 1));
            add(this.nome);
            add(this.presentazionePanel);
            add(this.maxPartecipanti);
            this.nome.setBorder(new TitledBorder("Inseririsci il nome dell'evento"));
            this.presentazione.setBorder(new TitledBorder("Inserisci una presentazione dell'evento"));
            this.maxPartecipanti.setBorder(new TitledBorder("Inseririsci il numero massimo di partecipanti"));
            this.presentazionePanel.setViewportView(presentazione);
            this.presentazione.setLineWrap(true);
            this.presentazione.setWrapStyleWord(true);
            this.presentazionePanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        } else {
            setLayout(new GridLayout(4, 1));
        }
        add(this.inizio);
        add(this.fine);
        add(this.numPartecipanti);
        add(this.confirm);
        this.inizio.setBorder(new TitledBorder("Inseririsci data (yyy-mm-dd) e ora (hh:mm:ss) di inizio evento"));
        this.fine.setBorder(new TitledBorder("Inseririsci data (yyy-mm-dd) e ora (hh:mm:ss) di fine evento"));
        this.numPartecipanti.setBorder(new TitledBorder("Inseririsci il numero di partecipanti"));
        this.confirm.addActionListener(e -> this.acept());
    }

    private void acept() {
        while (Event.existId(this.eventId)) {
            this.eventId = UUID.randomUUID().toString();
        }
        if (areEmpty(this.inizio, this.fine, this.numPartecipanti) || !isRightDates(this.inizio, this.fine)
                || !isValidNumber(this.numPartecipanti)) {
            return;
        } else if (typeOfUser == ADMIN) {
            if (areEmpty(this.nome, this.presentazione, this.maxPartecipanti, this.inizio, this.fine,
                    this.numPartecipanti) || isLong() || !isValidNumber(this.maxPartecipanti)) {
                return;
            }
            removeAll();
            setLayout(new BorderLayout());
            add(new AdminEvent(new Event(this.userId, this.eventId, textAreaToInt(this.numPartecipanti).get(),
                    this.inizio.getText(), this.fine.getText(), "pubblico", Optional.of(this.nome.getText()),
                    Optional.of(this.presentazione.getText()),
                    Optional.of(textAreaToInt(this.maxPartecipanti).get()))));
        } else {

        }
        revalidate();
        repaint();
    }

    private boolean areEmpty(final JTextArea... fields) {
        for (JTextArea field : fields) {
            if (field.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Riempi tutti i campi", "Error", JOptionPane.PLAIN_MESSAGE);
                return true;
            }
        }
        return false;
    }

    private boolean isLong() {
        if (this.nome.getText().length() > 30) {
            JOptionPane.showMessageDialog(this, "Il nome è troppo lungo (max 30 caratteri)", "Error",
                    JOptionPane.PLAIN_MESSAGE);
            return true;
        }
        return false;
    }

    private boolean isRightDates(final JTextArea... dates) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (JTextArea date : dates) {
            try {
                LocalDateTime.parse(date.getText(), formatter);
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this,
                        "controlla i dati inseriti (yyyy-MM-dd HH:mm:ss) è il formato corretto",
                        "Error", JOptionPane.PLAIN_MESSAGE);
                return false;
            }
        }
        return true;
    }

    private boolean isValidNumber(JTextArea... fields) {
        for (JTextArea field : fields) {
            if (textAreaToInt(field).isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "inserire una quantità valida (solo numeri, senza spazi)",
                        "Error",
                        JOptionPane.PLAIN_MESSAGE);
                return false;
            }
        }
        return true;
    }

    private Optional<Integer> textAreaToInt(final JTextArea number) {
        int newNumb;
        try {
            newNumb = Integer.parseInt(number.getText());
        } catch (Exception e) {
            return Optional.empty();
        }
        return Optional.of(newNumb);
    }

}
