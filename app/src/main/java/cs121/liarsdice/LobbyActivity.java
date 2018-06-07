package cs121.liarsdice;

import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LobbyActivity extends AppCompatActivity {

    WifiP2pManager myWifi;
    boolean isHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
    }

    @Override
    public void onResume(){
        super.onResume();

        isHost = getIntent().getExtras().getBoolean("isHost");
        TextView t = findViewById(R.id.textView4);
        if(isHost)
            t.setText("tru");
        else
            t.setText("trun't");
    }

}
