package cs121.liarsdice;

import android.content.ContentResolver;
import android.content.Intent;
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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class LobbyActivity extends AppCompatActivity implements Serializable {

    IntentFilter intentFilter = new IntentFilter();
 //   WifiP2pManager myWifi;
 //   Channel myChannel; // Use to connect to P2P framework
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
        DiceWifi.myWifi = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        try {
            DiceWifi.myChannel = DiceWifi.myWifi.initialize(this, getMainLooper(), null);
            receiver = new DiceReceiver(DiceWifi.myWifi, DiceWifi.myChannel, this, myList, adapter);
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

        /*
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
*/

        Button start = (Button) findViewById(R.id.startGame);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LobbyActivity.this, GameActivity.class);
           //     i.putExtra("wifi", (Serializable) myWifi);
             //   i.putExtra("channel", (Serializable) myChannel);
                startActivity(i);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();

        // Register the broadcast receiver
        receiver = new DiceReceiver(DiceWifi.myWifi, DiceWifi.myChannel, this, myList, adapter);
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
        DiceWifi.myWifi.discoverPeers(DiceWifi.myChannel, new WifiP2pManager.ActionListener() {

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

}