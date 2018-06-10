package cs121.liarsdice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.Serializable;

public class TestGameActivity extends AppCompatActivity implements Serializable {
    TextView playerText;
    TextView totalDieText;
    TextView currentTurnText;
    TextView nextTurnText;

    Game testGame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_game);
        testGame = (Game) getIntent().getSerializableExtra("testGame");
        initTextViews();

        //loop that runs game?
        //while(testGame.getNumPlayers()>0)
        //{
            playerText.setText("Players: " + testGame.getNumPlayers());
            totalDieText.setText("Die in Game: " + testGame.getNumDie());
            currentTurnText.setText("Player's Turn: " + testGame.getCurrentTurnString());
            nextTurnText.setText("Next Player: " + testGame.getNextTurnString());
        //}

    }

    void initTextViews()
    {
        playerText = findViewById(R.id.testPlayersText);
        totalDieText = findViewById(R.id.testTotDieText);
        currentTurnText = findViewById(R.id.testCurTurnText);
        nextTurnText = findViewById(R.id.testNextTurnText);

    }
}
