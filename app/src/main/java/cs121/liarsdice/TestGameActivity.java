package cs121.liarsdice;

import android.content.Intent;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.Serializable;

public class TestGameActivity extends AppCompatActivity implements Serializable {
    TextView playerText;
    TextView totalDieText;
    TextView currentTurnText;
    TextView nextTurnText;
    TextView currentBidText;
    Button bidButton;
    Button bluffButton;
    EditText testBidText1;
    EditText testBidText2;

    Game testGame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_game);
        testGame = (Game) getIntent().getSerializableExtra("testGame");
        initViews();
        initOnClicks();
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

    void setTextDuringGame(){
        playerText.setText("Players: " + testGame.getNumPlayers());
        totalDieText.setText("Die in Game: " + testGame.getNumDie());
        currentTurnText.setText("Player's Turn: " + testGame.getCurrentTurnString());
        nextTurnText.setText("Next Player: " + testGame.getNextTurnString());

        currentBidText.setText("Current Bid: " + testGame.bidNumber + " " +
                testGame.bidFace + "'s");


    }

    void initViews()
    {
        playerText = findViewById(R.id.testPlayersText);
        totalDieText = findViewById(R.id.testTotDieText);
        currentTurnText = findViewById(R.id.testCurTurnText);
        nextTurnText = findViewById(R.id.testNextTurnText);
        currentBidText = findViewById(R.id.testCurrentBidText);

        bidButton = findViewById(R.id.testBidButton);
        bluffButton = findViewById(R.id.testBluffButton);
        testBidText1 = findViewById(R.id.testBidText1);
        testBidText2 = findViewById(R.id.testBidText2);

    }

    void initOnClicks(){

        bidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int one = Integer.parseInt(testBidText1.getText().toString());
                int two = Integer.parseInt(testBidText2.getText().toString());
                if(one > testGame.bidNumber || two > testGame.bidFace ){
                    testGame.bidNumber = one;
                    testGame.bidFace = two;//this becomes the new bid
                    testGame.incrementTurn();
                    testGame.rollAllDice();
                    setTextDuringGame();
                } else {
                    Toast t =Toast.makeText(TestGameActivity.this,"enter a correct bid",Toast.LENGTH_SHORT);
                    t.show();
                }

            }
        });

        bluffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                    if (testGame.isBidTrue()){
                        testGame.playerLoseLife(testGame.getCurrentTurnInt());
                    } else {
                        testGame.playerLoseLife(testGame.getLastTurnInt());
                    }
            }
        });


    }






}
