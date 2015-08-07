package com.example.rifatrashid.loworbitioncannon;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by Rifat Rashid on 8/7/2015.
 */
public class UDPpacket {
    private InetAddress IPAddress;
    private DatagramSocket clientSocket;
    private byte[] sendData;
    private DatagramPacket sendPacket;
    private int port;
    private boolean isRunning = false;
    private boolean isReachable = true;
    private Socket sock = null;
    private String stringIP;

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
                while (true) {
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
}
