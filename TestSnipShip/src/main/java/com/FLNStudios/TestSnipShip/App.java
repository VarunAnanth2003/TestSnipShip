package com.FLNStudios.TestSnipShip;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import java.awt.Toolkit;

public class App {
    public static void main(String[] args) {
        final JFrame startFrame = new JFrame("Snip and Ship");
        JPanel startPanel = new JPanel();
        JButton startButton = new JButton("Start");
        JButton exitButton = new JButton("Exit");
        final JTextField secondsBeforeSnip = new JTextField(10);
        startPanel.add(secondsBeforeSnip);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SnipTool currThread = new SnipTool();
                try {
                    int sec = Integer.parseInt(secondsBeforeSnip.getText());
                    if (sec > 10) {
                        sec = 10;
                    }
                    currThread.setSeconds(sec);
                } catch (NumberFormatException ne) {
                }
                currThread.start();
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startFrame.setVisible(false);
            }
        });
        startPanel.add(new JLabel("Input seconds before snip."));
        startPanel.add(new JLabel("Max 10 seconds, default 3 seconds."));
        startPanel.add(startButton);
        startPanel.add(exitButton);
        startFrame.add(startPanel);
        startFrame.setBounds(Toolkit.getDefaultToolkit().getScreenSize().width / 2,
                Toolkit.getDefaultToolkit().getScreenSize().height / 2, 225, 150);
        startFrame.setResizable(false);
        startFrame.setVisible(true);
    }
}
