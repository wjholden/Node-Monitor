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
                String inputLine = scanner.nextLine();
                if (inputLine.startsWith("#")) continue; // the "#" character effectively comments out a line. (https://github.com/wjholden/Node-Monitor/issues/3)
                String[] line = inputLine.split("\\t"); // just a single tab to allow spaces in node description.
                final String ip = line[0].trim(); // apparently there is a weird bug with PowerShell's Get-Content command.
                final int interval = line.length > 1 ? Integer.parseInt(line[1]) : 2;
                final int timeout = line.length > 2 ? Integer.parseInt(line[2]) : interval * 3;
                final String description = line.length > 3 ? line[3].replaceAll("^\"", "").replaceAll("\"$", "") : ""; // ignore quotation marks (https://github.com/wjholden/Node-Monitor/issues/2)
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
