/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.juegoxd;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameTimer {
    private int timeLimit;
    private Timer timer;
    private ActionListener timeListener;
    
    public GameTimer(int initialTime, ActionListener timeListener) {
        this.timeLimit = initialTime;
        this.timeListener = timeListener;
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLimit--;
                timeListener.actionPerformed(e);
                if (timeLimit <= 0) {
                    timer.stop();
                }
            }
        });
    }
    
    public void start() {
        timer.start();
    }
    
    public int getTimeLimit() {
        return timeLimit;
    }
    
    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }
}
