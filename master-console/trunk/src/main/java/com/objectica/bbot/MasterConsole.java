package com.objectica.bbot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * @author curly
 */
public class MasterConsole extends JFrame
{
    private static final String EOL = System.getProperty("line.separator");
    private JTextArea displayArea;

    public static void main(String[] args)
    {
        MasterConsole console  = new MasterConsole();
    }

    public MasterConsole()
    {
        setSize(600, 600);
        setTitle("BBot Master Console");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel label = new JLabel("W,S,A,D - move",SwingConstants.CENTER);
        label.setFocusable(true);
        getContentPane().add(label,BorderLayout.NORTH);

        displayArea = new JTextArea();
        displayArea.addKeyListener(new RemoteCommandListener(this));
        displayArea.setAutoscrolls(true);
        displayArea.setEditable(false);                        
        JScrollPane scrollPane = new JScrollPane(displayArea);

        getContentPane().add(scrollPane,BorderLayout.CENTER);

        setVisible(true);
    }

    public void displayInfo(String info)
    {
          displayArea.append(info + EOL);
    }
}
