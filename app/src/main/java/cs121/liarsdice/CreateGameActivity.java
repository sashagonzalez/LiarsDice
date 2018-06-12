package cs121.liarsdice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class CreateGameActivity extends AppCompatActivity {

    Button enterPlayerBtn;
    EditText enterPlayerText;
    Game game;
    ArrayList<Player> players;
    Button startGameBtn;
    TextView numPlayersText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);
        enterPlayerBtn = findViewById(R.id.enterPlayerButton);
        enterPlayerText = findViewById(R.id.enterPlayerText);
        startGameBtn = findViewById(R.id.startGameBtn);
        numPlayersText = findViewById(R.id.numPlayersText);
        players = new ArrayList<>();
        initOnClick();
    }

    public void initOnClick(){
        enterPlayerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String playerName = enterPlayerText.getText().toString();
                Player p = new Player(playerName);
                players.add(p);
                enterPlayerText.setText("");
                numPlayersText. setText("Number of Players: " + players.size());
            }
        });

        startGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game = new Game(players.size(),players);
                Intent i = new Intent(CreateGameActivity.this, TestGameActivity.class);
                i.putExtra("testGame", game);
                startActivity(i);
            }
        });
    }


}
