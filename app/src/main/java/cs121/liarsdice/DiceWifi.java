package cs121.liarsdice;

import android.app.Application;
import android.net.wifi.p2p.WifiP2pManager;

public class DiceWifi extends Application {
    public static WifiP2pManager myWifi;
    public static WifiP2pManager.Channel myChannel; // Use to connect to P2P framework
    public static DiceReceiver receiver;
    /*
    public WifiP2pManager getWifi (){
        return this.myWifi;
    }
    public void setWifi(){

    }
    public WifiP2pManager.Channel getChannel(){
        return this.myChannel;
    }
*/

}
