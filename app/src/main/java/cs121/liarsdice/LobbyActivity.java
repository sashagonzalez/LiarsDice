package cs121.liarsdice;

import android.content.ContentResolver;
import android.net.Uri;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.DnsSdTxtRecordListener;
import android.net.wifi.p2p.WifiP2pManager.DnsSdServiceResponseListener;
import android.widget.Toast;
import android.content.Context;


import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class LobbyActivity extends AppCompatActivity implements Serializable {

    IntentFilter intentFilter = new IntentFilter();
    WifiP2pManager myWifi;
    Channel myChannel; // Use to connect to P2P framework
    DiceReceiver receiver;
    boolean isHost;
    ListView myList;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        myList = (ListView) findViewById(R.id.myList);
        // Show the list view with the each list item an element from listItems
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);

        isHost = (boolean) getIntent().getBooleanExtra("isHost", false);

        // Indicates a change in the Wi-Fi P2P status.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        // Indicates a change in the list of available peers.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        // Indicates the state of Wi-Fi P2P connectivity has changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        // Indicates this device's details have changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        TextView t = findViewById(R.id.textView4);
        myWifi = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        try {
            myChannel = myWifi.initialize(this, getMainLooper(), null);
            receiver = new DiceReceiver(myWifi, myChannel, this, myList, adapter);
            t.setVisibility(View.INVISIBLE);

         //   if(isHost)
           //     MakeLobby();
         //   else
                JoinLobby();
        }
        catch(Exception e) {
            // Wifi direct wasn't enabled(and cannot be enabled by emulators)
            t.setText("Please enable WifiDirect and try again.");

            if(isHost)
                Toast.makeText(LobbyActivity.this, "Host",
                        Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(LobbyActivity.this, "Not Host",
                        Toast.LENGTH_SHORT).show();
        }

        Button make = (Button) findViewById(R.id.sendMsg);
        make.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(receiver.peers.size());
                   startRegistration();
           //     sendData();
            }
        });


        Button get = (Button) findViewById(R.id.getMsg);
        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          //      System.out.println(receiver.peers.size());
                //   startRegistration();
          //      catchData();
                discoverService();
            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();

        // Register the broadcast receiver
        receiver = new DiceReceiver(myWifi, myChannel, this, myList, adapter);
        registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();

        // Unregister the broadcast receiver
        unregisterReceiver(receiver);
    }

    void JoinLobby(){

        // Start finding peers
        myWifi.discoverPeers(myChannel, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                // Code for when the discovery initiation is successful goes here.
                // No services have actually been discovered yet, so this method
                // can often be left blank. Code for peer discovery goes in the
                // onReceive method, detailed below.
            }

            @Override
            public void onFailure(int reasonCode) {
                // Code for when the discovery initiation fails goes here.
                // Alert the user that something went wrong.

                Toast.makeText(LobbyActivity.this, "Failed to find peers.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Move this to game activity later to send messages to other phones
    // Curplayer will call this method to send data to everyone else
    private void startRegistration() {
        //  Create a string map containing information about your service.
        Map record = new HashMap();
        // Sample messages, adjust later
        record.put("callBid", "true");
        record.put("curBid", "three two's");
        record.put("curPlayer", "Dustin");

        // Service information.  Pass it an instance name, service type
        // _protocol._transportlayer , and the map containing
        // information other devices will want once they connect to this one.
        WifiP2pDnsSdServiceInfo serviceInfo =
                WifiP2pDnsSdServiceInfo.newInstance("_test", "_presence._tcp", record);

        // Add the local service, sending the service info, network channel,
        // and listener that will be used to indicate success or failure of
        // the request.
        myWifi.addLocalService(myChannel, serviceInfo, new ActionListener() {
            @Override
            public void onSuccess() {
                // Command successful! Code isn't necessarily needed here,
                // Unless you want to update the UI or add logging statements.
                Toast.makeText(LobbyActivity.this, "Added service successfully.",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int arg0) {
                // Command failed.  Check for P2P_UNSUPPORTED, ERROR, or BUSY
                Toast.makeText(LobbyActivity.this, "Fail to add service.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Move this to game activity later to receive messages to other phones
    final HashMap<String, String> messages = new HashMap<String, String>();

    // Call method to look for services/messages from other devices
    private void discoverService() {
        DnsSdTxtRecordListener txtListener = new DnsSdTxtRecordListener() {


            public void onDnsSdTxtRecordAvailable(String fullDomain, Map record, WifiP2pDevice device) {
                messages.put(device.deviceAddress, (String) record.get("curBid"));
                System.out.println("bacon" + (String) record.get("curBid"));
            }
        };

        DnsSdServiceResponseListener servListener = new DnsSdServiceResponseListener() {
            @Override
            public void onDnsSdServiceAvailable(String instanceName, String registrationType,
                                                WifiP2pDevice resourceType) {
                // Update the device name with the human-friendly version from
                // the DnsTxtRecord, assuming one arrived.
                resourceType.deviceName = messages
                        .containsKey(resourceType.deviceAddress) ? messages
                        .get(resourceType.deviceAddress) : resourceType.deviceName;
            }
        };

        myWifi.setDnsSdResponseListeners(myChannel, servListener, txtListener);

        WifiP2pDnsSdServiceRequest serviceRequest = WifiP2pDnsSdServiceRequest.newInstance();
        myWifi.addServiceRequest(myChannel,
                serviceRequest,
                new ActionListener() {
                    @Override
                    public void onSuccess() {
                        System.out.println("yes1");
                    }

                    @Override
                    public void onFailure(int code) {
                        System.out.println("no1");
                    }
                });

        myWifi.discoverServices(myChannel, new ActionListener() {

                    @Override
                    public void onSuccess() {
                        System.out.println("size" + messages.size() + messages.get("curPlayer"));

                        for (Map.Entry<String,String> entry : messages.entrySet()) {
                            String key = entry.getKey();
                            String value = entry.getValue();
                            System.out.println(key + " " + value);
                        }
                    }

                    @Override
                    public void onFailure(int code) {
                        System.out.println("no2");
                    }
        });
    }


}