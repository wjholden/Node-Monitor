/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wjholden.nodemonitor;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Objects;

/**
 *
 * @author William John Holden (wjholden@gmail.com)
 */
final public class Node implements Comparable<Node> {
    protected final InetAddress ip;
    private final Long ipAsInteger;
    private final String description;
    protected final int interval, timeout;
    
    public Node(String address, String description, int interval, int timeout) throws UnknownHostException {
        ip = InetAddress.getByName(address);
        this.description = description;
        this.interval = interval;
        this.timeout = timeout;
        
        long addr = 0;
        for (byte b : ip.getAddress()) {
            addr |= (long)(0xff & b);
            addr <<= 8;
        }
        ipAsInteger = addr;
    }
    
    @Override
    public String toString() {
        return description + "\n" + ip.getHostAddress() + "\n" + ipAsInteger;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.ip);
        hash = 89 * hash + Objects.hashCode(this.description);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Node other = (Node) obj;
        if (!Objects.equals(this.ip, other.ip)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Node o) {
        return this.ipAsInteger.compareTo(o.ipAsInteger);
    }
    
    
}
