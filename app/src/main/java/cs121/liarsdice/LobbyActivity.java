package cs121.liarsdice;

import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.DnsSdTxtRecordListener;
import android.widget.Toast;
import android.content.Context;



import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class LobbyActivity extends AppCompatActivity implements Serializable {

    IntentFilter intentFilter = new IntentFilter();
    WifiP2pManager myWifi;
    Channel myChannel; // Use to connect to P2P framework
    DiceReceiver receiver;
    boolean isHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

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
            receiver = new DiceReceiver(myWifi, myChannel, this);
            t.setVisibility(View.INVISIBLE);

            if(isHost)
                MakeLobby();
            else
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
    }

    @Override
    public void onResume(){
        super.onResume();

        // Register the broadcast receiver
        receiver = new DiceReceiver(myWifi, myChannel, this);
        registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();

        // Unregister the broadcast receiver
        unregisterReceiver(receiver);
    }

    void MakeLobby(){

        myWifi.createGroup(myChannel, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                // Device is ready to accept incoming connections from peers.
                Toast.makeText(LobbyActivity.this, "Connection has been set up.",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(LobbyActivity.this, "P2P group creation failed. Retry.",
                        Toast.LENGTH_SHORT).show();
            }

        });
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
        record.put("curPlayer", "1");

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
            }

            @Override
            public void onFailure(int arg0) {
                // Command failed.  Check for P2P_UNSUPPORTED, ERROR, or BUSY
            }
        });
    }

    // Move this to game activity later to receive messages to other phones
    final HashMap<String, String> messages = new HashMap<String, String>();

    private void discoverService() {
        DnsSdTxtRecordListener txtListener = new DnsSdTxtRecordListener() {
            @Override
            /* Callback includes:
             * fullDomain: full domain name: e.g "printer._ipp._tcp.local."
             * record: TXT record dta as a map of key/value pairs.
             * device: The device running the advertised service.
             */

            public void onDnsSdTxtRecordAvailable(String fullDomain, Map record, WifiP2pDevice device) {
                messages.put(device.deviceAddress, (String) record.get("curPlayer"));
            }
        };
    }

}