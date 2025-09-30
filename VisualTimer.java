package productivityappcac;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VisualTimer extends JFrame {
    private int timeLeft; // seconds
    private Timer timer;
    private JLabel timeLabel;
    private JButton startButton, pauseButton, resetButton, setButton;
    private JButton plusButton, minusButton;
    private JTextField setTimeField;

    public VisualTimer() {
        super("Visual Timer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 250);
        setLayout(new BorderLayout());

        // Display
        timeLabel = new JLabel("00:00", SwingConstants.CENTER);
        timeLabel.setFont(new Font("Serif", Font.BOLD, 40));
        add(timeLabel, BorderLayout.CENTER);

        // Main Controls
        JPanel controlPanel = new JPanel();
        startButton = new JButton("Start");
        pauseButton = new JButton("Pause");
        resetButton = new JButton("Reset");

        controlPanel.add(startButton);
        controlPanel.add(pauseButton);
        controlPanel.add(resetButton);

        add(controlPanel, BorderLayout.SOUTH);

        // Set Time Panel
        JPanel setPanel = new JPanel();
        setPanel.add(new JLabel("Set (sec):"));
        setTimeField = new JTextField(5);
        setButton = new JButton("Set");
        setPanel.add(setTimeField);
        setPanel.add(setButton);

        add(setPanel, BorderLayout.NORTH);

        // Adjustment Panel
        JPanel adjustPanel = new JPanel();
        plusButton = new JButton("+1 min");
        minusButton = new JButton("-1 min");
        adjustPanel.add(plusButton);
        adjustPanel.add(minusButton);

        add(adjustPanel, BorderLayout.EAST);

        // Timer logic
        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (timeLeft > 0) {
                    timeLeft--;
                    updateLabel();
                } else {
                    timer.stop();
                    JOptionPane.showMessageDialog(null, "Time's Up!");
                }
            }
        });

        // Listeners
        startButton.addActionListener(e -> {
            if (!timer.isRunning() && timeLeft > 0) {
                timer.start();
            }
        });

        pauseButton.addActionListener(e -> {
            if (timer.isRunning()) {
                timer.stop();
            }
        });

        resetButton.addActionListener(e -> {
            timer.stop();
            timeLeft = 0;
            
            
            
            
            
            
            
            
            
            
            
            
            
            Label();
        });

        setButton.addActionListener(e -> {
            try {
                int newTime = Integer.parseInt(setTimeField.getText());
                timeLeft = newTime;
                updateLabel();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Enter a valid number!");
            }
        });

        plusButton.addActionListener(e -> {
            timeLeft += 60;
            updateLabel();
        });

        minusButton.addActionListener(e -> {
            if (timeLeft >= 60) {
                timeLeft -= 60;
            } else {
                timeLeft = 0;
                timer.stop();
            }
            updateLabel();
        });

        setVisible(true);
    }

    private void updateLabel() {
        int minutes = timeLeft / 60;
        int seconds = timeLeft % 60;
        timeLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VisualTimer());
    }
}