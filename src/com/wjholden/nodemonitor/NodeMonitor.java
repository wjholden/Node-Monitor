package com.wjholden.nodemonitor;

import java.awt.event.KeyListener;
import java.net.UnknownHostException;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author William John Holden (wjholden@gmail.com)
 */
public class NodeMonitor {

    public static void main(String[] args) throws UnknownHostException {
        final List<Node> nodes = new ArrayList<>();
        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.length() == 0) break;
                if (line.contains(" ")) {
                    nodes.add(new Node(line.substring(0, line.indexOf(" ")),
                    line.substring(line.indexOf(" ") + 1)));
                } else {
                    nodes.add(new Node(line, null));
                }
            }
        }
        MonitorFrame jf = new MonitorFrame(nodes.toArray(new Node[nodes.size()]));
        jf.addKeyListener(jf);
    }
    
}
