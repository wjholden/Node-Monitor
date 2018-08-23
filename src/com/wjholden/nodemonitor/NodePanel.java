/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wjholden.nodemonitor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.swing.JPanel;

/**
 *
 * @author William John Holden (wjholden@gmail.com)
 */
public class NodePanel extends JPanel {

    protected static int xoffset = 20;
    protected static int yoffset = 50;
    protected static float fontSize = 14;
    private final Node a[];
    private final Color c[];

    public NodePanel(Node a[]) {
        this.a = a;
        c = new Color[a.length];
        ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(10);
        for (int i = 0; i < a.length; i++) {
            final int x = i;
            executor.scheduleAtFixedRate(
                () -> {
                    try {
                        c[x] = a[x].ip.isReachable(a[x].timeout * 1000) ? Color.GREEN : Color.RED;
                    } catch (IOException ex) {
                        c[x] = Color.BLUE;
                    }
                },
                0, a[x].interval, TimeUnit.SECONDS);
        }
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500, 500);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int r = (int) Math.ceil(Math.sqrt(a.length));
        int index = 0;
        int width = this.getWidth();
        int height = this.getHeight();
        Font currentFont = g.getFont();
        Font newFont = currentFont.deriveFont(fontSize);
        g.setFont(newFont);
        
        for (int y = 0 ; y < r ; y++) {
            final int py = y * height / r;
            for (int x = 0 ; x < r ; x++) {
                final int px = x * width / r;
                
                if (index < a.length) {
                    g.setColor(c[index]);
                    g.fillRect(px, py, width / r, height / r);
                    g.setColor(Color.BLACK);
                    int lineOffset = 0;
                    for (String s : a[index].toString().split("\n")) {
                        g.drawString(s, px + xoffset, py + yoffset + lineOffset);
                        lineOffset += g.getFontMetrics().getHeight();
                    }
                } else {
                    g.fillRect(px, py, width / r, height / r);
                }
                g.drawRect(px, py, width / r, height / r);
                index++;
            }
        }
    }
}
