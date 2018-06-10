package cs121.liarsdice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.Serializable;

public class TestGameActivity extends AppCompatActivity implements Serializable {
    TextView playerText;
    TextView totalDieText;
    TextView currentTurnText;
    TextView nextTurnText;
    TextView currentBidText;

    Game testGame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_game);
        testGame = (Game) getIntent().getSerializableExtra("testGame");
        initTextViews();
        setFirstText();

    }



    void setFirstText(){
        playerText.setText("Players: " + testGame.getNumPlayers());
        totalDieText.setText("Die in Game: " + testGame.getNumDie());
        currentTurnText.setText("Player's Turn: " + testGame.getCurrentTurnString());
        nextTurnText.setText("Next Player: " + testGame.getNextTurnString());

        //currentBidText.setText("Current Bid: " + testGame.currentBid.first.toString() +
        //        testGame.currentBid.second.toString() + "'s");


    }

    void initTextViews()
    {
        playerText = findViewById(R.id.testPlayersText);
        totalDieText = findViewById(R.id.testTotDieText);
        currentTurnText = findViewById(R.id.testCurTurnText);
        nextTurnText = findViewById(R.id.testNextTurnText);
        currentBidText = findViewById(R.id.testCurrentBidText);

    }

    /*Button make = (Button) findViewById(R.id.createLobbyBtn);
        make.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(StartActivity.this, LobbyActivity.class);
            i.putExtra("isHost", true);
            startActivity(i);
        }
    }); */





}
