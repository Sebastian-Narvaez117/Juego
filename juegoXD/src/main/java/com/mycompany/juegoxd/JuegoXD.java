/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */


package com.mycompany.juegoxd;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

public class JuegoXD extends JFrame {
    private ColorPanel colorPanel1;
    private ColorPanel colorPanel2;
    private JLabel scoreLabel;
    private JLabel timerLabel;
    private GameTimer gameTimer;
    private int score;
    private int totalMatches;
    private int totalFailures;
    private int negativePoints;
    private Color color1;
    private Color color2;
    private boolean isHardDifficulty;
    private boolean isExtremeDifficulty;
    private boolean backgroundFlash;
    private int selectedTime;
    private Timer flashTimer;
    private JPanel distractionBar;
    private boolean matchChecked;
    private JLabel flashingCounterLabel;
    private Timer flashingCounterTimer;

    public JuegoXD(String difficulty, int selectedTime) {
        this.selectedTime = selectedTime;
        setTitle("Coincidencia en Tiempo");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        int timeLimit = getTimeLimit(difficulty);
        score = 0;
        totalMatches = 0;
        totalFailures = 0;
        negativePoints = 0;
        isHardDifficulty = "hard".equals(difficulty);
        isExtremeDifficulty = "extreme".equals(difficulty);
        backgroundFlash = false;
        matchChecked = false;

        scoreLabel = new JLabel("Puntos: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timerLabel = new JLabel("Tiempo: " + timeLimit);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));

        colorPanel1 = new ColorPanel();
        colorPanel2 = new ColorPanel();

        JPanel topPanel = new JPanel();
        topPanel.add(scoreLabel);
        topPanel.add(timerLabel);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(1, 2, 10, 10));
        centerPanel.add(colorPanel1);
        centerPanel.add(colorPanel2);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        if ("normal".equals(difficulty) || isExtremeDifficulty) {
            distractionBar = new JPanel();
            distractionBar.setPreferredSize(new Dimension(600, 50));
            distractionBar.setBackground(Color.RED);
            add(distractionBar, BorderLayout.SOUTH);
            startDistractionBar();
        }

        if (isExtremeDifficulty) {
            startBackgroundFlash();
            addFlashingCounter();
        }

        gameTimer = new GameTimer(timeLimit, () -> {
            timerLabel.setText("Tiempo: " + gameTimer.getTimeLimit());
            if (gameTimer.getTimeLimit() <= 0) {
                endGame();
            } else {
                updateColors();
            }
        });
        gameTimer.start();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE && !matchChecked) {
                    checkMatch();
                }
            }
        });
        setFocusable(true);
        requestFocusInWindow();
    }

    private int getTimeLimit(String difficulty) {
        switch (difficulty) {
            case "easy":
                return selectedTime;
            case "normal":
                return selectedTime;
            case "hard":
                return selectedTime;
            case "extreme":
                return selectedTime;
            default:
                return 10;
        }
    }

    private void updateColors() {
        color1 = generateRandomColor();
        color2 = Math.random() < 0.7 ? color1 : generateRandomColor();
        colorPanel1.setColor(color1);
        colorPanel2.setColor(color2);
        matchChecked = false;
    }

    private Color generateRandomColor() {
        return new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256));
    }

    private void checkMatch() {
        matchChecked = true;
        if (colorPanel1.getColor().equals(colorPanel2.getColor())) {
            score++;
            totalMatches++;
        } else {
            totalFailures++;
            if (isHardDifficulty || isExtremeDifficulty) {
                score--;
                negativePoints++;
            }
        }
        scoreLabel.setText("Puntos: " + score);
    }

    private void endGame() {
        double percentage = totalMatches > 0 ? ((double) score / totalMatches) * 100 : 0;
        String statsMessage = String.format("Estadísticas:\n\nAciertos: %d\nFallos: %d\nPuntos Negativos: %d\nPuntaje Final: %d",
                                             totalMatches, totalFailures, negativePoints, score);
        int option = JOptionPane.showOptionDialog(this, statsMessage, "Fin del Juego",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{"Reiniciar", "Salir"}, "Reiniciar");
        
        if (option == 0) {
            dispose();
            SwingUtilities.invokeLater(() -> {
                String[] difficulties = {"easy", "normal", "hard", "extreme"};
                String difficulty = (String) JOptionPane.showInputDialog(null, "Selecciona la dificultad:", "Dificultad", JOptionPane.QUESTION_MESSAGE, null, difficulties, difficulties[0]);
                if (difficulty != null) {
                    Integer[] times = {10, 20, 30, 35};
                    Integer selectedTime = (Integer) JOptionPane.showInputDialog(null, "Selecciona el tiempo de juego:", "Tiempo de Juego", JOptionPane.QUESTION_MESSAGE, null, times, times[0]);
                    if (selectedTime != null) {
                        new JuegoXD(difficulty, selectedTime).setVisible(true);
                    }
                }
            });
        } else {
            System.exit(0);
        }
    }

    private void startBackgroundFlash() {
        flashTimer = new Timer();
        flashTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (backgroundFlash) {
                    getContentPane().setBackground(Color.BLACK);
                } else {
                    getContentPane().setBackground(Color.WHITE);
                }
                backgroundFlash = !backgroundFlash;
            }
        }, 0, 500);
    }

    private void startDistractionBar() {
        Timer distractionTimer = new Timer();
        distractionTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                distractionBar.setBackground(new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256)));
            }
        }, 0, 200);
    }

    private void addFlashingCounter() {
        flashingCounterLabel = new JLabel("TIEMPO", JLabel.CENTER);
        flashingCounterLabel.setFont(new Font("Arial", Font.BOLD, 24));
        flashingCounterLabel.setForeground(Color.RED);
        add(flashingCounterLabel, BorderLayout.NORTH);

        flashingCounterTimer = new Timer();
        flashingCounterTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                flashingCounterLabel.setVisible(!flashingCounterLabel.isVisible());
            }
        }, 0, 500);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String[] difficulties = {"easy", "normal", "hard", "extreme"};
            String difficulty = (String) JOptionPane.showInputDialog(null, "Selecciona la dificultad:", "Dificultad", JOptionPane.QUESTION_MESSAGE, null, difficulties, difficulties[0]);
            if (difficulty != null) {
                Integer[] times = {10, 20, 30, 35};
                Integer selectedTime = (Integer) JOptionPane.showInputDialog(null, "Selecciona el tiempo de juego:", "Tiempo de Juego", JOptionPane.QUESTION_MESSAGE, null, times, times[0]);
                if (selectedTime != null) {
                    new JuegoXD(difficulty, selectedTime).setVisible(true);
                }
            }
        });
    }

    class ColorPanel extends JPanel {
        private Color color;

        public ColorPanel() {
            setPreferredSize(new Dimension(200, 200));
            setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }

        public void setColor(Color color) {
            this.color = color;
            repaint();
        }

        public Color getColor() {
            return color;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(color);
            g2d.fillRoundRect(10, 10, getWidth() - 20, getHeight() - 20, 50, 50);
        }
    }

    class GameTimer {
        private int timeLimit;
        private Timer timer;
        private Runnable onTick;

        public GameTimer(int timeLimit, Runnable onTick) {
            this.timeLimit = timeLimit;
            this.onTick = onTick;
        }

        public void start() {
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    timeLimit--;
                    SwingUtilities.invokeLater(onTick);
                }
            }, 0, 1000);
        }

        public int getTimeLimit() {
            return timeLimit;
        }
    }
}
