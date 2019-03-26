package Graphics;

import Utils.Time;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;


public class GamePanel extends JPanel implements Runnable {

    //FIELDS


    public static int WIDTH = 400;
    public static int HEIGHT = 300;

    private Thread thread;
    private boolean running;

    private BufferedImage image;
    private Graphics2D g;

    public static final float UPDATE_RATE = 40.0f;
    public static final float UPDATE_INTERVAL = Time.SECOND / UPDATE_RATE;
    public static final long IDLE_TIME = 10;



    public long startRoundTime;


    //CONSTRUCTOR
    public GamePanel() {
        super();
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        requestFocus();
        running = false;
    }

    //FUNCTION
    @Override
    public void run() {
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        g = (Graphics2D) image.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);


        int fps = 0;
        int upd = 0;
        int updl = 0;
        long count = 0;
        float delta = 0;

        //GAME LOOP
        long lastTime = Time.get();
        while (running) {
            long now = Time.get();
            long elapsedTime = now - lastTime;
            lastTime = now;

            count += elapsedTime;

            delta += (elapsedTime / UPDATE_INTERVAL);

            boolean render = false;

            while (delta > 1) {
                update();
                update();
                upd++;
                delta--;
                if (render) {
                    updl++;
                } else {
                    render = true;
                }
            }

            if (render) {
                render();
                fps++;
            } else {
                try {
                    Thread.sleep(IDLE_TIME);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }

            if (count >= Time.SECOND) {
//                Main.setTitle(TITLE + " || FPS: " + fps
//                        + " | UPD: " + upd
//                        + " | UPDL: " + updl);
                fps = 0;
                upd = 0;
                updl = 0;
                count = 0;
            }
            gameDraw();
        }
    }

    private void render() {

    }

    private void update() {

    }


    private void gameDraw() {
        Graphics g2 = this.getGraphics();
        if (image != null && g2 != null) {
            g2.drawImage(image, 0, 0, null);
        }
//        g2.dispose();
    }

    public synchronized void start() {
        if (running) {
            return;
        }

        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        if (!running) {
            return;
        }

        running = false;

        try {
            thread.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

//        SnakeGame.destroy();
    }
}
