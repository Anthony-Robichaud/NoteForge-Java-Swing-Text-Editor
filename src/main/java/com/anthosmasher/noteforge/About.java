package com.anthosmasher.noteforge;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class About extends JFrame implements ActionListener {
    JButton b1;
    About() {
        setBounds(600, 200, 700, 600);
        setBackground(Color.darkGray);
        setTitle("About NoteForge");
        setLayout(null);

        URL i1url = getClass().getResource("/windowsxp.png");
        ImageIcon i1 = new ImageIcon(i1url);
        Image i2 = i1.getImage().getScaledInstance(400, 80, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel l1 = new JLabel(i3);
        l1.setBounds(150, 40, 400, 80);
        add(l1);

        URL i4url = getClass().getResource("/noteforge.png");
        ImageIcon i4 = new ImageIcon(i4url);
        this.setIconImage(i4.getImage());
        Image i5 = i4.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT);
        ImageIcon i6 = new ImageIcon(i5);
        JLabel l2 = new JLabel(i6);
        l2.setBounds(50, 180, 100, 100);
        add(l2);

        JLabel l3 = new JLabel("<html>This is a random text editor made by AnthoSmasher." +
                " <br>It has some cool features like auto-closing and smart indentation"+
                " <br><br>(Not actually for Windows XP)<html>" );
        l3.setFont(new Font("SAN_SERIF", Font.PLAIN, 18));
        l3.setBounds(150, 130, 500, 300);
        add(l3);

        b1 = new JButton("OK");
        b1.setBounds(580, 500, 80, 25);
        b1.addActionListener(this);
        add(b1);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.setVisible(false);
    }
}
