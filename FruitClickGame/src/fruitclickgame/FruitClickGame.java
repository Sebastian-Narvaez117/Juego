package fruitclickgame;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class FruitClickGame extends JPanel implements ActionListener {
    private Timer timer;
    private Timer timeCounter;
    private ArrayList<Fruit> fruits;
    private int score;
    private int timeAlive;
    private Random random;
    private Image backgroundImage;
    private boolean fruitDragged;
    private String[] messages = {"Well done!", "Great!", "Impressive!", "Nice!", "Good job!"};
    private String currentMessage = "";
    private int messageCounter = 0; // Counter to display the message for a few frames
    private Clip sliceClip; // Clip to play slice sound
    private Clip backgroundClip; // Clip to play background music

    public FruitClickGame() {
        setPreferredSize(new Dimension(800, 600));
        
        // Cargar la imagen de fondo
        backgroundImage = new ImageIcon(getClass().getResource("/images/background.png")).getImage();
        
        fruits = new ArrayList<>();
        random = new Random();
        score = 100; // Puntuación inicial
        timeAlive = 0; // Tiempo inicial
        fruitDragged = false;

        timer = new Timer(1000 / 60, this);
        timer.start();

        timeCounter = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeAlive++;
                repaint();
            }
        });
        timeCounter.start();

        new FruitGenerator().start();

        // Cargar el sonido de cortar
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource("/sounds/slice.wav"));
            sliceClip = AudioSystem.getClip();
            sliceClip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }

        // Cargar la música de fondo
        try {
            AudioInputStream backgroundInputStream = AudioSystem.getAudioInputStream(getClass().getResource("/sounds/background.wav"));
            backgroundClip = AudioSystem.getClip();
            backgroundClip.open(backgroundInputStream);
            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY); // Reproduce en bucle
            backgroundClip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                checkDrag(e.getX(), e.getY());
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (!fruitDragged) {
                    score -= 5;
                    repaint();
                }
                fruitDragged = false; // Reset the flag after each drag operation
            }
        });
    }

    private synchronized void checkDrag(int x, int y) {
        Iterator<Fruit> iterator = fruits.iterator();
        while (iterator.hasNext()) {
            Fruit fruit = iterator.next();
            if (fruit.getBounds().contains(x, y)) {
                score += 10;
                currentMessage = messages[random.nextInt(messages.length)];
                messageCounter = 60; // Show message for 60 frames
                playSound(sliceClip);
                iterator.remove();
                fruitDragged = true;
            }
        }
        repaint();
    }

    private void playSound(Clip clip) {
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();  // Stop the player if it is still running
            }
            clip.setFramePosition(0); // Rewind to the beginning
            clip.start(); // Start playing
        }
    }

    @Override
    protected synchronized void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Dibujar la imagen de fondo
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);

        synchronized (fruits) {
            for (Fruit fruit : fruits) {
                fruit.draw(g);
            }
        }
        
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Score: " + score, 10, 20);
        g.drawString("Time Alive: " + timeAlive + "s", getWidth() - 150, 20);

        // Mostrar el mensaje actual
        if (messageCounter > 0 && !currentMessage.isEmpty()) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            int messageWidth = g.getFontMetrics().stringWidth(currentMessage);
            g.drawString(currentMessage, (getWidth() - messageWidth) / 2, getHeight() / 2);
            messageCounter--;
        }
    }

    @Override
    public synchronized void actionPerformed(ActionEvent e) {
        synchronized (fruits) {
            Iterator<Fruit> iterator = fruits.iterator();
            while (iterator.hasNext()) {
                Fruit fruit = iterator.next();
                fruit.move();
                if (fruit.getY() > getHeight()) {
                    iterator.remove();
                    score -= 5;
                }
            }
        }
        repaint();

        if (score <= 0) {
            timer.stop();
            timeCounter.stop();
            backgroundClip.stop(); // Detener la música de fondo
            StringBuilder message = new StringBuilder();
            message.append("PERDISTE!\nTu puntaje fue: ").append(score).append("\nSobreviviste: ").append(timeAlive).append("s");

            if (timeAlive >= 60) {
                message.append("\n¡IMPRESIONANTE!");
            } else if (timeAlive >= 30) {
                message.append("\nSIGUE ENTRENANDO");
            } else {
                message.append("\nAÚN ERES UN NOVATO.");
            }

            JOptionPane.showMessageDialog(this, message.toString());
            System.exit(0);
        }
    }

    private class FruitGenerator extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(Math.max(1000 - (score * 2), 200));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (score > 0) {
                    synchronized (fruits) {
                        fruits.add(new Fruit(random.nextInt(getWidth() - 50), 0));
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Fruit Ninja");
        FruitClickGame game = new FruitClickGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    class Fruit {
        private int x, y;
        private int size;
        private int speed;
        private Image image;

        public Fruit(int x, int y) {
            this.x = x;
            this.y = y;
            this.size = 50;
            this.speed = random.nextInt(5) + 2;
            // Cargar una imagen aleatoria de frutas
            String[] fruitImages = {"/images/apple.png", "/images/banana.png", 
                "/images/cherry.png", "/images/watermelon.png", 
                "/images/pear.png", "/images/uvas.png", "/images/mango.png",
                "/images/orange.png",  "/images/juice.png",  "/images/fruitdragon.png",  "/images/kiwi.png",};
            this.image = new ImageIcon(getClass().getResource(fruitImages[random.nextInt(fruitImages.length)])).getImage();
        }

        public void move() {
            y += speed;
        }

        public void draw(Graphics g) {
            g.drawImage(image, x, y, size, size, null);
        }

        public int getY() {
            return y;
        }

        public Rectangle getBounds() {
            return new Rectangle(x, y, size, size);
        }
    }
}
