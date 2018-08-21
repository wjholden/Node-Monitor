/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wjholden.nodemonitor;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

/**
 *
 * @author William John Holden (wjholden@gmail.com)
 */
final public class Node {
    protected final InetAddress ip;
    private final String description;
    
    public Node(String address, String description) throws UnknownHostException {
        ip = InetAddress.getByName(address);
        this.description = description;
    }
    
    @Override
    public String toString() {
        if (description == null) return ip.getHostAddress();
        else return description + "\n" + ip.getHostAddress();
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
    
    
}
