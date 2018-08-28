# Node-Monitor

This program is a basic network monitoring tool. It is inferior to robust commercial and open-source solutions like Nagios, Orion, and SNMPc. Why would I bother writing something like this? Because Node-Monitor is really, really easy to configure. There are no annoying wizards to click through. There are no tiny icons to arrange. You just input a tab-separated list of hostnames/IPs, timeouts, intervals, and descriptions as lines of standard input.

## Input
The input to this program might look like this. The first column is the hostname or IP address of the device. The second column is the timeout, in seconds, that the system waits until considering a ping failed. The third column is the frequency, in seconds, with which we poll the node. It doesn't make much sense to have a timeout greater than or equal to the frequency. The fourth column is a free-form string that will be used to describe the polled device.

```
198.51.100.1	2	10	My default gateway
203.0.113.53	2	10	Our DNS server
192.0.2.25	5	30	The SNMP server
```

Microsoft Excel can export a spreadsheet as a tab-delimited `*.txt` file that is compatible with this program. Do not include headers. The parser will skip lines containing hostnames deemed invalid by the operating system.

## Usage
Start the program by piping the input file as standard input. In PowerShell this looks like `Get-Content input.txt | java -jar .\Node_Monitor.jar`.

Press `q` to quit the program. Press `f` to toggle fullscreen. Press `+` and `-` to resize the text and arrow keys to position the text. Nodes are sorted by IP address before the GUI gets constructed.
