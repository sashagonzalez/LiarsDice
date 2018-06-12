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
    //Button testGameBtn;
    Button ruleBtn;
    Button createGameBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        createGameBtn = (Button)findViewById(R.id.createGameBtn);
        setOnClicks();
        ruleBtn = (Button)findViewById(R.id.rulesBtn);
        ruleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StartActivity.this, RuleActivity.class);
                startActivity(i);
            }
        });
    }



    void setOnClicks()
    {
        createGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StartActivity.this, CreateGameActivity.class);
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

