package com.wjholden.nodemonitor;

import java.net.UnknownHostException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 *
 * @author William John Holden (wjholden@gmail.com)
 */
public class NodeMonitor {
    
    static final List<Node> nodes = new ArrayList<>();
    static final Map<String, Consumer<String>> parse = new HashMap<>();
    static int pollInterval = 10;
    static int pollTimeout = 2;
    static {
        parse.put("interval", s -> pollInterval = Integer.valueOf(s));
        parse.put("timeout", s -> pollTimeout = Integer.valueOf(s));
        parse.put("node", s -> {
            s = s.trim();
            try {
                if (s.contains(" ")) {
                    nodes.add(new Node(s.substring(0, s.indexOf(" ")), s.substring(s.indexOf(" ") + 1), pollInterval, pollTimeout));
                } else {
                    nodes.add(new Node(s, null, pollInterval, pollTimeout));
                }
            } catch (UnknownHostException ex) {
                System.err.println(ex);
                System.exit(1);
            }
        });
    }

    public static void main(String[] args) throws UnknownHostException {
        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                parse.get(scanner.next()).accept(scanner.nextLine());
            }
        }
        Collections.sort(nodes);
        MonitorFrame jf = new MonitorFrame(nodes.toArray(new Node[nodes.size()]));
        jf.addKeyListener(jf);
    }
    
}
