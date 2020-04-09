package com.wjholden.nodemonitor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.swing.JPanel;

/**
 *
 * @author William John Holden (wjholden@gmail.com)
 */
public final class NodePanel extends JPanel {

    protected int xoffset = 20;
    protected int yoffset = 50;
    protected float fontSize = 14;
    private final Node a[];
    private final Color c[];
    private final List<Integer> order; // order in which nodes should be drawn on the screen.
    private float brightness = 0.7f; // brightness is initially 70%

    public NodePanel(Node a[]) {
        this.a = a;
        c = new Color[a.length];
        Arrays.fill(c, Color.BLACK); // initialize color list as all black.
        
        order = IntStream.range(0, a.length).boxed().collect(Collectors.toList());
        
        ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(10);
        for (int i = 0; i < a.length; i++) {
            final int x = i;
            executor.scheduleAtFixedRate(
                () -> {
                    try {
                        boolean reachable = a[x].ip.isReachable(a[x].timeout * 1000);
                        // If the node is reachable then paint the box green, otherwise red.
                        c[x] = new Color(reachable ? 0.0f : brightness * 1.0f,
                                reachable ? brightness * 1.0f : 0.0f,
                                0.0f);
                        //c[x] = a[x].ip.isReachable(a[x].timeout * 1000) ? Color.GREEN : Color.RED;
                    } catch (IOException ex) {
                        c[x] = Color.BLUE;
                    }
                },
                0, a[x].interval, TimeUnit.SECONDS);
        }
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Figure out which rectangle was clicked. Guess I should have
                // made each of those rectangles their own object.
                int r = (int) Math.ceil(Math.sqrt(a.length));
                int row = e.getY() / (getHeight() / r);
                int col = e.getX() / (getWidth() / r);
                int index = col + row * r;
                
                if (e.getClickCount() >= 2) {
                    try {
                        // yes, this is system-specific. Just need something to work for now.
                        // (https://github.com/wjholden/Node-Monitor/issues/1)
                        Runtime.getRuntime().exec(a[index].onDoubleClick + " " + a[index].ip.getHostAddress());
                    } catch (IOException ex) {
                        System.err.println(ex);
                    }
                }
            }
        });
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
                    g.setColor(c[order.get(index)]);
                    g.fillRect(px, py, width / r, height / r);
                    g.setColor(Color.BLACK);
                    int lineOffset = 0;
                    for (String s : a[order.get(index)].toString().split("\n")) {
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
    
    public void increaseBrightness() {
        assert(0.0f <= brightness && brightness <= 1.0f);

        // The constant 0.7 is for compatibility with
        // http://hg.openjdk.java.net/jdk8/jdk8/jdk/file/687fd7c7986d/src/share/classes/java/awt/Color.java#l626
        // I'm not really in love with this constant.
        // 0.7 looks too dark on my machine and 1.0 looks too bright.
        brightness = Math.min(brightness / 0.7f, 1.0f);
        
        // This is only here to make the UI update immediately. These values
        // will be overwritten by the next poll.
        for (int i = 0 ; i < c.length ; i++) {
            c[i] = c[i].brighter();
        }
    }
    
    public void decreaseBrightness() {
        assert(0.0f <= brightness && brightness <= 1.0f);
        brightness = Math.max(brightness * 0.7f, 0.0f);
        
        for (int i = 0 ; i < c.length ; i++) {
            c[i] = c[i].darker();
        }
    }
    
    public void insertionOrder() {
        assert(order.size() == a.length);
        Collections.sort(order);
    }
    
    public void ipAddressOrder() {
        assert(order.size() == a.length);
        Collections.sort(order, (l,r) -> a[l].ipAsInteger.compareTo(a[r].ipAsInteger));
    }
    
    public void descriptionOrder() {
        assert(order.size() == a.length);
        Collections.sort(order, (l,r) -> a[l].description.compareTo(a[r].description));
    }
}
