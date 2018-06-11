package cs121.liarsdice;

import android.content.BroadcastReceiver;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.net.wifi.WpsInfo;

import java.util.List;
import java.util.ArrayList;

public class DiceReceiver extends BroadcastReceiver {

    private WifiP2pManager mManager;
    private Channel mChannel;
    private LobbyActivity mActivity;

    private ListView mList;
    private ArrayAdapter mArray;

    public List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();

    public DiceReceiver(WifiP2pManager manager, Channel channel,
                        LobbyActivity activity, ListView list, ArrayAdapter array) {
        super();
        this.mManager = manager;
        this.mChannel = channel;
        this.mActivity = activity;
        this.mList = list;
        this.mArray = array;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Check to see if Wi-Fi is enabled and notify appropriate activity
        }
        else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // Call WifiP2pManager.requestPeers() to get a list of current peers
            if (mManager != null) {
                mManager.requestPeers(mChannel, peerListListener);
            }

        }
        else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            // Respond to new connection or disconnections
        }
        else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
        }
    }

    private PeerListListener peerListListener = new PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {
            System.out.println("how many peers:" + peerList.getDeviceList().size()); //new

            List<WifiP2pDevice> refreshedPeers = new ArrayList<WifiP2pDevice>(peerList.getDeviceList());

            if (!refreshedPeers.equals(peers)) {
                Log.d("Dice Reciever","this was called");
                peers.clear();
                peers.addAll(refreshedPeers);

                // Perform any other updates needed based on the new list of
                // peers connected to the Wi-Fi P2P network.
                if(peers.size() > 0)
                    ConnectToPeer();
            }

            System.out.println("Made it to checkpoint.");
        }
    };


    void ConnectToPeer() {
        // Picking the first device found on the network.
        final WifiP2pDevice device = peers.get(0);

        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        config.wps.setup = WpsInfo.PBC;

        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                // WiFiDirectBroadcastReceiver notifies us. Ignore for now.
                System.out.println("Successfully connected to a peer." + device.deviceName + peers.size());

                // Make an array of items to show on the listView
                for(int i = 0; i < peers.size(); i++){
                    mArray.add(peers.get(i).deviceName);
                }
                mList.setAdapter(null);
                mList.setAdapter(mArray);
            }

            @Override
            public void onFailure(int reason) {
                System.out.println("Connect failed. Retry.");
            }
        });
    }
}