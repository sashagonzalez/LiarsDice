package cs121.liarsdice;

import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.widget.Toast;
import android.content.Context;



public class LobbyActivity extends AppCompatActivity {

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

                ConnectToPeer();
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

    void ConnectToPeer(){
        // Picking the first device found on the network.
        WifiP2pDevice device = receiver.peers.get(0);

        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        config.wps.setup = WpsInfo.PBC;

        myWifi.connect(myChannel, config, new ActionListener() {
            @Override
            public void onSuccess() {
                // WiFiDirectBroadcastReceiver notifies us. Ignore for now.
                Toast.makeText(LobbyActivity.this, "Connection worked. You should be gook.",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(LobbyActivity.this, "Connect failed. Retry.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }



}