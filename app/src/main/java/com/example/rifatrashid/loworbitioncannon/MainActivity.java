package com.example.rifatrashid.loworbitioncannon;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

public class MainActivity extends ActionBarActivity {

    public static UDPpacket udpPacket;
    public static long numberOfPacketsSent = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0);
    }

    public void initialize() throws MalformedURLException, UnknownHostException {
        InetAddress address = InetAddress.getByName(new URL("xxxx").getHost());
    }
}
