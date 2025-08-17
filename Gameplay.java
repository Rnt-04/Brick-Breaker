import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Gameplay extends JPanel implements KeyListener, ActionListener {

    private boolean play = false;
    private int score = 0;

    private int totalBricks = 21;

    private Timer timer;
    private int delay = 8;

    private int playerX = 310;
    private int ballPosX = 120;
    private int ballPosY = 350;
    private int ballXdir = -2;
    private int ballYdir = -4;

    private MapGenerator mapG;

    public Gameplay() {
        mapG = new MapGenerator(3, 7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if (play) {
            // Ball - Paddle collision with physics
            Rectangle ballRect = new Rectangle(ballPosX, ballPosY, 20, 20);
            Rectangle paddleRect = new Rectangle(playerX, 550, 100, 8);

            if (ballRect.intersects(paddleRect)) {
                ballYdir = -ballYdir;

                // find hit position relative to paddle
                int hitPos = ballPosX + 10 - playerX; // ball center - paddle start

                if (hitPos < 20) {
                    ballXdir = -3; // sharp left
                } else if (hitPos < 40) {
                    ballXdir = -2; // left-mid
                } else if (hitPos < 60) {
                    ballXdir = 0; // straight up
                } else if (hitPos < 80) {
                    ballXdir = 2; // right-mid
                } else {
                    ballXdir = 3; // sharp right
                }
            }

            // Ball - Brick collision
            A: for (int i = 0; i < mapG.map.length; i++) {
                for (int j = 0; j < mapG.map[0].length; j++) {
                    if (mapG.map[i][j] > 0) {
                        int brickX = j * mapG.brickWidth + 80;
                        int brickY = i * mapG.brickHeight + 50;
                        int brickWidth = mapG.brickWidth;
                        int brickHeight = mapG.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle brickRect = rect;

                        if (ballRect.intersects(brickRect)) {
                            mapG.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;

                            if (ballPosX + 19 <= brickRect.x || ballPosX + 1 >= brickRect.x + brickRect.width) {
                                ballXdir = -ballXdir;
                            } else {
                                ballYdir = -ballYdir;
                            }
                            break A;
                        }
                    }
                }
            }

            // move the ball
            ballPosX += ballXdir;
            ballPosY += ballYdir;

            // left
            if (ballPosX < 0) {
                ballXdir = -ballXdir;
            }
            // right
            if (ballPosX > 670) {
                ballXdir = -ballXdir;
            }
            // top
            if (ballPosY < 0) {
                ballYdir = -ballYdir;
            }
        }

        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) { }
    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerX >= 600) {
                playerX = 600;
            } else {
                moveRight();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerX < 10) {
                playerX = 10;
            } else {
                moveLeft();
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!play) {
                play = true;
                ballPosX = 120;
                ballPosY = 350;
                ballXdir = -2;
                ballYdir = -4;
                playerX = 310;
                score = 0;
                totalBricks = 21;
                mapG = new MapGenerator(3, 7);

                repaint();
            }
        }
    }

    public void paint(Graphics g) {
        // background
        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);

        // draw map
        mapG.draw((Graphics2D) g);

        // borders
        g.setColor(Color.yellow);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);

        // scores
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("Score: " + score, 550, 30);

        // paddle
        g.setColor(Color.green);
        g.fillRect(playerX, 550, 100, 8);

        // ball
        g.setColor(Color.yellow);
        g.fillOval(ballPosX, ballPosY, 20, 20);

        // game won
        if (totalBricks <= 0) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("Serif", Font.BOLD, 30));
            g.drawString("You Won, Score: " + score, 230, 300);

            g.setFont(new Font("Serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 230, 350);
        }

        // game over
        if (ballPosY > 570) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("Serif", Font.BOLD, 30));
            g.drawString("Game Over, Score: " + score, 190, 300);

            g.setFont(new Font("Serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 230, 350);
        }

        g.dispose();
    }

    public void moveRight() {
        play = true;
        playerX += 20;
    }

    public void moveLeft() {
        play = true;
        playerX -= 20;
    }
}
