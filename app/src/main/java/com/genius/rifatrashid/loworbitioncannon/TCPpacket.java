package com.genius.rifatrashid.loworbitioncannon;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Rifat Rashid on 8/12/2015.
 */
public class TCPpacket implements Runnable {

    private InetAddress IPAddress;
    private byte[] sendData;
    private DatagramPacket sendPacket;
    private int port;
    private boolean isRunning = false;
    private boolean isReachable = true;
    public static int count = 0;
    public static long sTime = 0;

    public TCPpacket(InetAddress IPAddress, byte[] sendData, int port) throws IOException {
        this.IPAddress = IPAddress;
        this.sendData = sendData;
        this.port = port;
    }
    @Override
    public void run() {
        count = 0;
        sTime = System.currentTimeMillis();
        Socket sock;
        while(managerClass.firing){
            try{
                sock = new Socket();
                sock.connect(new InetSocketAddress(IPAddress, port), 10);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(sock.getOutputStream());
                outputStreamWriter.write(sendData.toString());
                outputStreamWriter.close();
                sock.close();
                count++;
                Thread.sleep(10);
            }catch (Exception io){
                io.printStackTrace();
            }
        }
    }
}
