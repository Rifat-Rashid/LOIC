package com.example.rifatrashid.loworbitioncannon;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

public class MainActivity extends ActionBarActivity implements CompoundButton.OnCheckedChangeListener {

    public static UDPpacket udpPacket;
    public static long numberOfPacketsSent = 0;
    private Button getIPButton;
    private EditText portText;
    private EditText urlText;
    private TextView urlTextView;
    private EditText ipTextBox;
    private boolean usingURL = false;
    private String IPADDRESS = null;
    private Integer PORT = null;
    private InetAddress address = null;
    private RadioButton UDPOption, TCPOption;
    public static TextView numberOfPacketSentText;
    private TextView packetsPerSecondText;
    private Button fireButton;
    private InetAddress trueAddress;
    public static boolean isFiring = false;
    private Integer tempCount = 0;
    private byte[] sendingBytes = new byte[65100];
    private boolean runOnce = false;
    private managerClass services;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2a2a2a")));
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        urlText = (EditText) findViewById(R.id.url_textbox);
        portText = (EditText) findViewById(R.id.port_textbox);
        getIPButton = (Button) findViewById(R.id.getIp);
        urlTextView = (TextView) findViewById(R.id.ip_text);
        ipTextBox = (EditText) findViewById(R.id.ip_textbox);
        UDPOption = (RadioButton) findViewById(R.id.udp_radio);
        TCPOption = (RadioButton) findViewById(R.id.tcp_radio);
        UDPOption.setOnCheckedChangeListener(this);
        TCPOption.setOnCheckedChangeListener(this);
        services = new managerClass();
        numberOfPacketSentText = (TextView) findViewById(R.id.packetSentText);
        numberOfPacketSentText.setText(String.valueOf(numberOfPacketsSent));
        packetsPerSecondText = (TextView) findViewById(R.id.packetsPerSecondText);
        packetsPerSecondText.setText("0");
        fireButton = (Button) findViewById(R.id.fireButton);
        getIPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempURL = urlText.getText().toString();
                usingURL = (tempURL.contains("www") || tempURL.contains("WWW")) ? true : false;
                if (!usingURL) {
                    tempURL = ipTextBox.getText().toString();
                    urlTextView.setText(tempURL);
                } else {
                    try {
                        address = InetAddress.getByName(new URL(tempURL).getHost());
                        urlTextView.setText(address.getHostAddress().toString());
                    } catch (Exception e) {
                        //Error
                        e.printStackTrace();
                    }
                }

            }
        });

        fireButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!managerClass.firing) {
                    if (UDPOption.isChecked() == true) {
                        if (portText.getText() != null) {
                            if (Pattern.matches("[a-zA-Z]+", portText.getText()) == false) {
                                PORT = Integer.parseInt(String.valueOf(portText.getText()));
                                try {
                                    InetAddress realAddress = InetAddress.getByName(address.getHostAddress());
                                    services.start(realAddress, PORT, 35, sendingBytes);
                                    fireButton.setText("Stop");
                                    Runnable r = new Runnable() {
                                        public void run() {
                                            while (managerClass.firing) {
                                                MainActivity.this.runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        numberOfPacketSentText.setText(String.valueOf(UDPpacket.count));
                                                    }
                                                });
                                            }
                                        }
                                    };
                                    new Thread(r).start();
                                } catch (Exception io) {

                                }
                            }
                        }
                    }
                } else {
                    services.stop();
                    fireButton.setText("Start");
                }
                /*
                tempCount++;
                switch (tempCount) {
                    case 0:
                        fireButton.setText("Start");
                        break;
                    case 1:
                        isFiring = true;
                        fireButton.setText("Stop");
                        if (UDPOption.isChecked() == true) {
                            if (portText.getText() != null) {
                                if (Pattern.matches("[a-zA-Z]+", portText.getText()) == false) {
                                    PORT = Integer.parseInt(String.valueOf(portText.getText()));
                                    try {
                                        InetAddress realAddress = InetAddress.getByName(address.getHostAddress());
                                        services.start(realAddress, Integer.parseInt(String.valueOf(portText.getText())), 10, sendingBytes);
                                        managerClass.firing = true;
                                    } catch (UnknownHostException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                        Runnable r = new Runnable() {
                            @Override
                            public void run() {
                                while (managerClass.firing) {
                                    MainActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            numberOfPacketSentText.setText(String.valueOf(UDPpacket.count));
                                        }
                                    });
                                }
                            }
                        };
                        new Thread(r).start();
                        break;
                    case 2:
                        tempCount = 0;
                        services.stop();
                        fireButton.setText("Start");
                        break;
                }
                */
            }
        });

    }

    public void initialize() throws MalformedURLException, UnknownHostException {
        InetAddress address = InetAddress.getByName(new URL("xxxx").getHost());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (buttonView.getId() == R.id.tcp_radio) {
                TCPOption.setChecked(true);
                UDPOption.setChecked(false);
            }
            if (buttonView.getId() == R.id.udp_radio) {
                TCPOption.setChecked(false);
                UDPOption.setChecked(true);
            }
        }
    }

}






