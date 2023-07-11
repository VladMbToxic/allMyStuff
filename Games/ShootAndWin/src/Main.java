// Butkovskiy Vlad 2023a
// 20.05.2022
// Game with mouse traction

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Main {

    /*
    1000 463 или 1280 720
    */

    public static JFrame frame = new JFrame("no title");

    public static Timer t;
    public static double timerDelay = 20.0;


    public static void main(String[] args) {

        frame.setSize(1280, 720);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Panel panel = new Panel();
        frame.add(panel);
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        frame.getContentPane().add(box, BorderLayout.EAST);
        JTextField tf = new JTextField();
        box.add(tf);
        JButton jb = new JButton("Start");
        box.add(jb);

        panel.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && panel.getMousePosition() != null) {
                    Panel.px.add((int) panel.getMousePosition().getX() - Panel.ra);
                    Panel.py.add((int) panel.getMousePosition().getY() - Panel.ra);
                    panel.repaint();
                }
                if (e.getButton() == MouseEvent.BUTTON3 && panel.getMousePosition() != null) {
                    Panel.nx.add((int) panel.getMousePosition().getX() - Panel.ra);
                    Panel.ny.add((int) panel.getMousePosition().getY() - Panel.ra);
                    panel.repaint();
                }
            }

            public void mousePressed(MouseEvent e) {

            }

            public void mouseReleased(MouseEvent e) {

            }

            public void mouseEntered(MouseEvent e) {

            }

            public void mouseExited(MouseEvent e) {

            }
        });

        tf.addActionListener((te) -> {
            double value = Double.parseDouble(tf.getText());
            Panel.vx = Math.cos(Panel.angle) * value;
            Panel.vy = Math.sin(Panel.angle) * value;
        });

        jb.addActionListener((e) -> {
            Panel.x_c = 0;
            Panel.y_c = 0;
            Panel.angleVisible = false;
            t.start();
        });



        frame.setVisible(true);

        t = new Timer((int) timerDelay, (a) -> {
            frame.repaint();
            panel.move();
            panel.collisionTest();
        });
    }
}