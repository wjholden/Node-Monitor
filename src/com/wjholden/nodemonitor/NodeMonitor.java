package com.wjholden.nodemonitor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
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

    public static void main(String[] args) throws UnknownHostException, FileNotFoundException {
        final List<Node> nodes = new ArrayList<>();
        final InputStream input = (args.length == 0) ? System.in : new FileInputStream(new File(args[0]));
        
        try (Scanner scanner = new Scanner(input)) {
            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split("\\t"); // just a single tab to allow spaces in node description.
                String ip = line[0].trim(); // apparently there is a weird bug with PowerShell's Get-Content command.
                int interval = Integer.parseInt(line[1]);
                int timeout = Integer.parseInt(line[2]);
                String description = line.length > 3 ? line[3] : "";
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
