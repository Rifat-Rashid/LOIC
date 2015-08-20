package com.genius.rifatrashid.loworbitioncannon;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.net.InetAddress;
import java.net.URL;
import java.util.regex.Pattern;

public class MainActivity extends Activity implements CompoundButton.OnCheckedChangeListener {

    public static long numberOfPacketsSent = 0;
    private Button getIPButton;
    private EditText portText;
    private EditText urlText;
    private TextView urlTextView;
    private EditText ipTextBox;
    private boolean usingURL = false;
    private Integer PORT = null;
    private Integer THREADS = null;
    private InetAddress address = null;
    private RadioButton UDPOption, TCPOption, HTTPOption, manualIP, httpIP;
    public static TextView numberOfPacketSentText;
    private TextView packetsPerSecondText;
    private Button fireButton;
    private byte[] sendingBytes = new byte[65100];
    private managerClass services;
    private int methodType = 1;
    private TextView elapsedTimeText;
    private EditText numberOfThreadsText;
    private boolean gotIP = false;
    protected PowerManager.WakeLock wakeLock;
    private String attackIP;
    private static final int PAUSE_TIME = 50;
    private AdView mAdView;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //User Agreement
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (preferences.getBoolean("startMessage", true)) {
            new AlertDialog.Builder(this).setTitle("Terms of Use").setMessage("Low Orbit Ion Cannon (LOIC) is a tool that was designed purley for stress testing networks. The developer assumes no responsibility for any illegal or unintended use of this tool. Enjoy :)").setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    preferences.edit().putBoolean("startMessage", false).commit();
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    preferences.edit().putBoolean("startMessage", true).commit();
                }
            }).show();

        }
        //

        Runnable r = new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdView = (AdView) findViewById(R.id.adView);
                        AdRequest adRequest = new AdRequest.Builder().build();
                        mAdView.loadAd(adRequest);
                    }
                });
            }
        };
        new Thread(r).start();
        //
        urlText = (EditText) findViewById(R.id.url_textbox);
        portText = (EditText) findViewById(R.id.port_textbox);
        getIPButton = (Button) findViewById(R.id.getIp);
        urlTextView = (TextView) findViewById(R.id.ip_text);
        ipTextBox = (EditText) findViewById(R.id.ip_textbox);
        UDPOption = (RadioButton) findViewById(R.id.udp_radio);
        HTTPOption = (RadioButton) findViewById(R.id.httpButton);
        TCPOption = (RadioButton) findViewById(R.id.tcp_radio);
        HTTPOption.setOnCheckedChangeListener(this);
        UDPOption.setOnCheckedChangeListener(this);
        TCPOption.setOnCheckedChangeListener(this);
        services = new managerClass();
        numberOfPacketSentText = (TextView) findViewById(R.id.packetSentText);
        numberOfPacketSentText.setText(String.valueOf(numberOfPacketsSent));
        packetsPerSecondText = (TextView) findViewById(R.id.packetsPerSecondText);
        packetsPerSecondText.setText("0");
        elapsedTimeText = (TextView) findViewById(R.id.elapsedTimeText);
        fireButton = (Button) findViewById(R.id.fireButton);
        manualIP = (RadioButton) findViewById(R.id.ipRadio);
        manualIP.setOnCheckedChangeListener(this);
        httpIP = (RadioButton) findViewById(R.id.httpRadio);
        httpIP.setOnCheckedChangeListener(this);
        numberOfThreadsText = (EditText) findViewById(R.id.threadText);
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "Lock");
        this.wakeLock.acquire();
        getIPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempURL = urlText.getText().toString();
                if (manualIP.isChecked()) {
                    //Manualy typed ip\
                    try {
                        tempURL = ipTextBox.getText().toString();
                        urlTextView.setText(tempURL);
                        gotIP = true;
                    }catch (Exception e){
                        e.printStackTrace();
                        Context context = getApplicationContext();
                        CharSequence errorMessage = "Error with manually typed IP";
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(context, errorMessage, duration);
                        toast.show();
                    }
                } else {
                    //used http url
                    try {
                        getWebIP(tempURL);
                        address = InetAddress.getByName(new URL(tempURL).getHost());
                    } catch (Exception e) {
                        //Error
                        e.printStackTrace();
                        Context context = getApplicationContext();
                        CharSequence errorMessage = "Error getting IP from URL";
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(context, errorMessage, duration);
                        toast.show();
                    }
                }
                //usingURL = (tempURL.contains("www") || tempURL.contains("WWW")) ? true : false;
            }
        });

        fireButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!managerClass.firing) {
                    if (gotIP) {
                        if (portText.getText() != null) {
                            if (Pattern.matches("[a-zA-Z]+", portText.getText()) == false) {
                                try {
                                    PORT = Integer.parseInt(String.valueOf(portText.getText()));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (numberOfThreadsText.getText() != null) {
                                    if (Pattern.matches("[a-zA-Z]+", numberOfThreadsText.getText()) == false) {
                                        try {
                                            THREADS = Integer.parseInt(String.valueOf(numberOfThreadsText.getText()));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        //Basic number of threads
                                        THREADS = 10;
                                    }
                                    //Insert code block here !
                                    try {
                                        InetAddress realAddress = InetAddress.getByName(address.getHostAddress());
                                        services.start(realAddress, PORT, THREADS, sendingBytes, methodType, PAUSE_TIME);
                                        fireButton.setText("Stop");
                                        Runnable r = new Runnable() {
                                            public void run() {
                                                while (managerClass.firing) {
                                                    switch (methodType) {
                                                        case 1:
                                                            //UDP
                                                            MainActivity.this.runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    try {
                                                                        numberOfPacketSentText.setText(String.valueOf(UDPpacket.count));
                                                                        packetsPerSecondText.setText(String.valueOf(Math.round(UDPpacket.count / ((System.currentTimeMillis() - UDPpacket.sTime) / 1000.0))));
                                                                        elapsedTimeText.setText("Elapsed Time: " + (System.currentTimeMillis() - UDPpacket.sTime) / 1000.0 + "s");
                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            });
                                                            break;
                                                        case 2:
                                                            //HTTP
                                                            MainActivity.this.runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    try {
                                                                        numberOfPacketSentText.setText(String.valueOf(HTTPpacket.count));
                                                                        packetsPerSecondText.setText(String.valueOf(Math.round(HTTPpacket.count / ((System.currentTimeMillis() - HTTPpacket.sTime) / 1000.0))));
                                                                        elapsedTimeText.setText("Elapsed Time: " + (System.currentTimeMillis() - HTTPpacket.sTime) / 1000.0 + "s");
                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            });
                                                            break;
                                                        case 3:
                                                            //TCP
                                                            MainActivity.this.runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    try {
                                                                        numberOfPacketSentText.setText(String.valueOf(TCPpacket.count));
                                                                        packetsPerSecondText.setText(String.valueOf(Math.round(TCPpacket.count / ((System.currentTimeMillis() - TCPpacket.sTime) / 1000.0))));
                                                                        elapsedTimeText.setText("Elapsed Time: " + (System.currentTimeMillis() - TCPpacket.sTime) / 1000.0 + "s");
                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            });
                                                            break;
                                                    }
                                                    try {
                                                        Thread.sleep(50);
                                                    } catch (Exception io) {
                                                        io.printStackTrace();
                                                    }
                                                }
                                            }
                                        };
                                        new Thread(r).start();
                                    } catch (Exception io) {
                                        io.printStackTrace();
                                    }
                                }

                            }
                        }
                    } else {
                        try {
                            Context context = getApplicationContext();
                            CharSequence errorMessage = "IP Not Selected";
                            int duration = Toast.LENGTH_LONG;
                            Toast toast = Toast.makeText(context, errorMessage, duration);
                            toast.show();
                        } catch (Exception e) {
                            //Something happened here!
                            e.printStackTrace();
                        }
                    }
                } else {
                    services.stop();
                    fireButton.setText("Start");
                }
            }
        });
    }

    public void getWebIP(final String address1) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    String webAddress = address1.replace("http://", "").replace("www.", "");
                    if (webAddress.length() > 0) {
                        attackIP = InetAddress.getByName(webAddress).getHostAddress();
                        Runnable r = new Runnable() {
                            @Override
                            public void run() {
                                urlTextView.setText(attackIP);
                                gotIP = true;
                            }
                        };
                        MainActivity.this.runOnUiThread(r);
                    }
                } catch (Exception e) {
                    Context context = getApplicationContext();
                    CharSequence errorMessage = "Unable to get IP, try adding www. to the web address";
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, errorMessage, duration);
                    toast.show();
                    e.printStackTrace();
                }
            }
        };
        new Thread(r).start();
    }

    @Override
    public void onBackPressed() {
        if (managerClass.firing) {
            services.stop();
            fireButton.setText("Fire");
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
        services.stop();
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //MethodType keeps track of which attack option the user has selected
        if (isChecked) {
            if (buttonView.getId() == R.id.tcp_radio) {
                TCPOption.setChecked(true);
                UDPOption.setChecked(false);
                HTTPOption.setChecked(false);
                methodType = 3;
            }
            if (buttonView.getId() == R.id.udp_radio) {
                TCPOption.setChecked(false);
                UDPOption.setChecked(true);
                HTTPOption.setChecked(false);
                methodType = 1;
            }
            if (buttonView.getId() == R.id.httpButton) {
                TCPOption.setChecked(false);
                HTTPOption.setChecked(true);
                UDPOption.setChecked(false);
                methodType = 2;
            }
            if (buttonView.getId() == R.id.httpRadio) {
                httpIP.setChecked(true);
                manualIP.setChecked(false);
            }
            if (buttonView.getId() == R.id.ipRadio) {
                httpIP.setChecked(false);
                manualIP.setChecked(true);
            }
        }
    }
}






