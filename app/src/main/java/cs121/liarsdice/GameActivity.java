package cs121.liarsdice;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class GameActivity extends AppCompatActivity implements Serializable {

//    WifiP2pManager myWifi;
  //  WifiP2pManager.Channel myChannel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Button make = (Button) findViewById(R.id.testBidButton);
        make.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegistration();
            }
        });


        Button get = (Button) findViewById(R.id.button2);
        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discoverService();
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
        DiceWifi.myWifi.addLocalService(DiceWifi.myChannel, serviceInfo, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                // Command successful! Code isn't necessarily needed here,
                // Unless you want to update the UI or add logging statements.
                Toast.makeText(GameActivity.this, "Added service successfully.",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int arg0) {
                // Command failed.  Check for P2P_UNSUPPORTED, ERROR, or BUSY
                Toast.makeText(GameActivity.this, "Fail to add service.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Move this to game activity later to receive messages to other phones
    final HashMap<String, String> messages = new HashMap<String, String>();

    // Call method to look for services/messages from other devices
    private void discoverService() {
        WifiP2pManager.DnsSdTxtRecordListener txtListener = new WifiP2pManager.DnsSdTxtRecordListener() {


            public void onDnsSdTxtRecordAvailable(String fullDomain, Map record, WifiP2pDevice device) {
                messages.put(device.deviceAddress, (String) record.get("curBid"));
                System.out.println("bacon" + (String) record.get("curBid"));
            }
        };

        WifiP2pManager.DnsSdServiceResponseListener servListener = new WifiP2pManager.DnsSdServiceResponseListener() {
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

        DiceWifi.myWifi.setDnsSdResponseListeners(DiceWifi.myChannel, servListener, txtListener);

        WifiP2pDnsSdServiceRequest serviceRequest = WifiP2pDnsSdServiceRequest.newInstance();
        DiceWifi.myWifi.addServiceRequest(DiceWifi.myChannel,
                serviceRequest,
                new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        System.out.println("yes1");
                    }

                    @Override
                    public void onFailure(int code) {
                        System.out.println("no1");
                    }
                });

        DiceWifi.myWifi.discoverServices(DiceWifi.myChannel, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                System.out.println("size" + messages.size() + messages.get("curPlayer"));

                for (Map.Entry<String,String> entry : messages.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    System.out.println("assdas" + key + " " + value);
                }
            }

            @Override
            public void onFailure(int code) {
                System.out.println("no2");
            }
        });
    }
}
