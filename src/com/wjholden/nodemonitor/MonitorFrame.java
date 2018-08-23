package com.wjholden.nodemonitor;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.Timer;

/**
 *
 * @author William John Holden (wjholden@gmail.com)
 */
public class MonitorFrame extends JFrame implements KeyListener {
    final NodePanel panel;
    
    public MonitorFrame(Node a[]) {
        super();
        this.setTitle("Node Monitor");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        Container pane = this.getContentPane();
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        
        pane.add(panel = new NodePanel(a));
        
        Timer timer = new Timer(0, this::refresh);
        timer.setRepeats(true);
        timer.setDelay(1000);
        timer.start();
        
        pack();
        setVisible(true);
    }
    
    private void refresh(ActionEvent e) {
        panel.repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        switch (e.getKeyChar()) {
            case 'q': System.exit(0); break;
            case '+': NodePanel.fontSize *= 1.4F; break;
            case '-': NodePanel.fontSize /= 1.4F; break;
            case 'f': setFullscreen(); break;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP: NodePanel.yoffset -= 1; break;
            case KeyEvent.VK_DOWN: NodePanel.yoffset += 1; break;
            case KeyEvent.VK_LEFT: NodePanel.xoffset -= 1; break;
            case KeyEvent.VK_RIGHT: NodePanel.xoffset += 1; break;
        }
        panel.repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }
    
    /**
     * Copied from Route-Monitor program I wrote.
     */
    private void setFullscreen() {
        this.dispose();
        if (isUndecorated()) {
            // clear the maximized bits (4 and 2) if fullscreen -> normal
            setExtendedState(getExtendedState() & (~JFrame.MAXIMIZED_BOTH));
            setUndecorated(false);
        } else {
            // set the maximized bits if normal -> fullscreen
            setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
            setUndecorated(true);
        }
        this.setLocation(0, 0);
        this.pack();
        this.setVisible(true);
    }
}
