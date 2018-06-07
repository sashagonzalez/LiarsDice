package cs121.liarsdice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.view.View;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // Make Lobby Button
        Button make = (Button) findViewById(R.id.createLobbyBtn);
        make.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StartActivity.this, LobbyActivity.class);
                i.putExtra("isHost", true);
                startActivity(i);
            }
        });

        // Join Lobby Button
        Button join = (Button) findViewById(R.id.searchLobbyBtn);
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StartActivity.this, LobbyActivity.class);
                i.putExtra("isHost", false);
                startActivity(i);
            }
        });
    }
}
