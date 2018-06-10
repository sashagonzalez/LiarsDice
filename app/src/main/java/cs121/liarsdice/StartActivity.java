package cs121.liarsdice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.io.Serializable;
import java.util.ArrayList;
import android.content.Intent;
import android.widget.Button;
import android.view.View;


public class StartActivity extends AppCompatActivity implements Serializable
{
    Button testGameBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        testGameBtn = (Button)findViewById(R.id.testBtn);
        setOnClicks();
    }



    void setOnClicks()
    {
        testGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Player Paul = new Player("Paul");
                Player Steve = new Player("Steve");
                Player Kratos = new Player("Kratos");
                ArrayList<Player> testPlayers = new ArrayList<Player>(4);
                testPlayers.add(null);
                testPlayers.add(Paul);
                testPlayers.add(Steve);
                testPlayers.add(Kratos);
                Game testGame = new Game(3, testPlayers);
                Intent i = new Intent(StartActivity.this, TestGameActivity.class);
                i.putExtra("testGame", testGame);
                startActivity(i);
                }
                });


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

