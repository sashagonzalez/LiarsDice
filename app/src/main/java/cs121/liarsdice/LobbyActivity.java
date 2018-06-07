package cs121.liarsdice;

import android.bluetooth.BluetoothAdapter;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.widget.Toast;
import android.content.Context;



public class LobbyActivity extends AppCompatActivity {

    IntentFilter intentFilter = new IntentFilter();
    WifiP2pManager myWifi;
    Channel myChannel; // Use to connect to P2P framework
    DiceReceiver receiver;
    boolean isHost;


    private BluetoothAdapter BA;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);




        // Indicates a change in the Wi-Fi P2P status.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        // Indicates a change in the list of available peers.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        // Indicates the state of Wi-Fi P2P connectivity has changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        // Indicates this device's details have changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        myWifi = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        myChannel = myWifi.initialize(this, getMainLooper(), null);

        TextView t = findViewById(R.id.textView4);
        if(myChannel == null){
            t.setText("fail");
        }
        else{
            t.setText("good");
        }

        /*
        String asdf = Integer.toString(getIntent().getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1));


        if (getIntent().getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1) == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
            myChannel = myWifi.initialize(this, getMainLooper(), null);
                               t.setText("worked");
            // Wifi P2P is enabled
        } else {
            // Wi-Fi P2P is not enabled
             t.setText("fail");
        }

        
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
*/
    }

    @Override
    public void onResume(){
        super.onResume();

        /*
        isHost = getIntent().getExtras().getBoolean("isHost");
        TextView t = findViewById(R.id.textView4);
        if(isHost)
            t.setText("tru");
        else
            t.setText("trun't");


        // Register the broadcast receiver
        receiver = new DiceReceiver(myWifi, myChannel, this);
        registerReceiver(receiver, intentFilter);
        */
    }

    @Override
    public void onPause() {
        super.onPause();




        // Unregister the broadcast receiver
     //   unregisterReceiver(receiver);
    }

}
