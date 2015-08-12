package com.example.rifatrashid.loworbitioncannon;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

/**
 * Created by Rifat Rashid on 8/12/2015.
 */
public class HTTPpacket implements Runnable {

    private InetAddress IPAddress;
    private byte[] sendData;
    private DatagramPacket sendPacket;
    private int port;
    private boolean isRunning = false;
    private boolean isReachable = true;
    public static int count = 0;
    public static long sTime = 0;

    public HTTPpacket(InetAddress IPAddress, byte[] sendData, int port)throws IOException{
        this.IPAddress = IPAddress;
        this.sendData = sendData;
        this.port = port;
    }
    @Override
    public void run() {
        count = 0;
        sTime = System.currentTimeMillis();
    }
}
