package com.genius.rifatrashid.loworbitioncannon;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

public class UDPpacket implements Runnable {
    private InetAddress IPAddress;
    private DatagramSocket clientSocket;
    private byte[] sendData;
    private DatagramPacket sendPacket;
    private int port;
    private boolean isRunning = false;
    private boolean isReachable = true;
    private Socket sock = null;
    private String stringIP;
    public static int count = 0;
    public static long sTime = 0;


    public UDPpacket(InetAddress IPAddress, byte[] sendData, int port, int pause) throws SocketException {
        this.IPAddress = IPAddress;
        this.sendData = sendData;
        this.port = port;
        clientSocket = new DatagramSocket();
        sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
    }

    public void setStringIP(String ip) {
        this.stringIP = ip;
    }

    public void setIsRunning(boolean run) {
        this.isRunning = run;
    }

    @Override
    public void run() {
        count = 0;
        sTime = System.currentTimeMillis();
        while(managerClass.firing) {
            try {
                clientSocket.send(sendPacket);
                count++;
                Thread.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}