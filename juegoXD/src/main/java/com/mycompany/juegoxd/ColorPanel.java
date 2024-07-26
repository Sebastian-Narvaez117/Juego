/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.juegoxd;

import javax.swing.*;
import java.awt.*;

public class ColorPanel extends JPanel {
    public ColorPanel() {
        setPreferredSize(new Dimension(200, 200));
        setBackground(generateRandomColor());
    }
    
    public void setColor(Color color) {
        setBackground(color);
    }
    
    public Color getColor() {
        return getBackground();
    }
    
    private Color generateRandomColor() {
        return new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256));
    }
}
