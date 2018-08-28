package com.wjholden.nodemonitor;

import java.net.UnknownHostException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
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
                String[] line = scanner.nextLine().split("\\t");
                String ip = line[0].trim();
                int interval = Integer.parseInt(line[1]);
                int timeout = Integer.parseInt(line[2]);
                String description = line[3];
                try {
                    Node n = new Node(ip, description, interval, timeout);
                    nodes.add(n);
                } catch (UnknownHostException e) {
                    System.err.printf("Unable to add %s. This probably indicates that this is an illegal hostname.%n", ip);
                }
            }
        }
        Collections.sort(nodes);
        MonitorFrame jf = new MonitorFrame(nodes.toArray(new Node[nodes.size()]));
        jf.addKeyListener(jf);
    }
    
}
