package com.example.rifatrashid.loworbitioncannon;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

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


    public UDPpacket(InetAddress IPAddress, byte[] sendData, int port) throws SocketException {
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

    public void sendClient2() throws UnknownHostException, IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (MainActivity.isFiring) {
                    try {
                        clientSocket.send(sendPacket);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public void run() {
        count = 0;
        sTime = System.currentTimeMillis();
        try {
            clientSocket.send(sendPacket);
            count++;
            Thread.sleep(20);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}