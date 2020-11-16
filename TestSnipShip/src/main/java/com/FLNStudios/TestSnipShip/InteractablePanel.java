package com.FLNStudios.TestSnipShip;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.*;

public class InteractablePanel extends JPanel implements MouseMotionListener, MouseListener {
    /**
     * Serial number
     */
    private static final long serialVersionUID = 1L;
    private BufferedImage m_img;

    public InteractablePanel(BufferedImage img) {
        m_img = img;
        addMouseListener(this);
        addMouseMotionListener(this);
        repaint();
    }

    private Point lastClicked = new Point(0, 0);
    private Point currentPos = new Point(0, 0);
    private Rectangle currR = new Rectangle(0, 0, 0, 0);
    private boolean complete = false;

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(m_img, 0, 0, this);
        g.setColor(Color.red);
        g.drawRect(lastClicked.x, lastClicked.y, currentPos.x, currentPos.y);
        g.drawRect(currR.x, currR.y, currR.width, currR.height);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("Clicked");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        lastClicked = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        currR = new Rectangle(lastClicked.x, lastClicked.y, currentPos.x, currentPos.y);
        lastClicked = new Point(0, 0);
        currentPos = new Point(0, 0);
        if (currR.width * currR.height >= 1000) {
            confirmSelection();
        } else {
            currR = new Rectangle();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        currentPos = new Point(e.getX() - lastClicked.x, e.getY() - lastClicked.y);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    public boolean isComplete() {
        return complete;
    }

    public Rectangle getSubRectangle() {
        return currR;
    }

    private void confirmSelection() {
        final JFrame confirmationFrame = new JFrame("Confirmation");
        JPanel jp = new JPanel();
        confirmationFrame.setBounds(Toolkit.getDefaultToolkit().getScreenSize().width / 2,
                Toolkit.getDefaultToolkit().getScreenSize().height / 2, 200, 100);
        JButton yesButton = new JButton("Yes");
        JButton noButton = new JButton("No");
        yesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmationFrame.setVisible(false);
                complete = true;
            }
        });
        noButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmationFrame.setVisible(false);
                currR = new Rectangle();
            }
        });
        jp.add(new JLabel("Is this your selection?"));
        jp.add(yesButton);
        jp.add(noButton);
        confirmationFrame.setResizable(false);
        confirmationFrame.add(jp);
        confirmationFrame.setVisible(true);
    }
}
