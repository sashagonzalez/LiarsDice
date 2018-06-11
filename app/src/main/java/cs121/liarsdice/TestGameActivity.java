package cs121.liarsdice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.io.Serializable;
import java.util.ArrayList;

public class TestGameActivity extends AppCompatActivity implements Serializable {
    TextView playerText;
    TextView totalDieText;
    TextView currentTurnText;
    TextView nextTurnText;
    TextView currentBidText;
    Button bidButton;
    Button bluffButton;
    Button rollButton;
    EditText testBidText1;
    EditText testBidText2;
    Boolean canClickBluff;
    Boolean canClickRoll;
    Boolean canClickBid;
    LinearLayout testDiceLayout;
    ArrayList<ImageView> diceViews = new ArrayList<>(5);
    ImageView testDieView1;
    ImageView testDieView2;
    ImageView testDieView3;
    ImageView testDieView4;
    ImageView testDieView5;
    RelativeLayout bluffTextLayout;
    TextView bluffTextView;
    ImageButton closeBluffLayoutButton;
    Game testGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_game);
        testGame = (Game) getIntent().getSerializableExtra("testGame");
        initViews();
        initOnClicks();
        setFirstText();
        displayDiceEmpty();
        canClickBid = false;
        canClickBluff = false;
        canClickRoll = true;

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

        if(testGame.bidFace==0 || testGame.bidNumber == 0){
            currentBidText.setText("Current Bid: No Bid");
        } else {
            currentBidText.setText("Current Bid: " + testGame.bidNumber + " " +
                    testGame.bidFace + "'s");
        }

        displayDice();


    }

    void initViews()
    {
        //dice
        testDiceLayout = findViewById(R.id.testDiceLayout);
        testDieView1 = findViewById(R.id.testDieView1);
        testDieView2 = findViewById(R.id.testDieView2);
        testDieView3 = findViewById(R.id.testDieView3);
        testDieView4 = findViewById(R.id.testDieView4);
        testDieView5 = findViewById(R.id.testDieView5);
        diceViews.add(testDieView1);
        diceViews.add(testDieView2);
        diceViews.add(testDieView3);
        diceViews.add(testDieView4);
        diceViews.add(testDieView5);

        //text in top left corner
        playerText = findViewById(R.id.testPlayersText);
        totalDieText = findViewById(R.id.testTotDieText);
        currentTurnText = findViewById(R.id.testCurTurnText);
        nextTurnText = findViewById(R.id.testNextTurnText);
        currentBidText = findViewById(R.id.testCurrentBidText);

        //text in middle of screen
        bidButton = findViewById(R.id.testBidButton);
        bluffButton = findViewById(R.id.testBluffButton);
        rollButton = findViewById(R.id.testRollButton);
        testBidText1 = findViewById(R.id.testBidText1);
        testBidText2 = findViewById(R.id.testBidText2);

        //when someone calls bluff
        bluffTextLayout = findViewById(R.id.bluffTextLayout);
        bluffTextView = findViewById(R.id.bluffTextView);
        closeBluffLayoutButton = findViewById(R.id.closeBluffLayoutButton);

    }

    void initOnClicks(){

        closeBluffLayoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bluffTextLayout.setAlpha(0);
            }
        });

        bluffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canClickBluff) {


                    if (testGame.isBidTrue()) {
                        testGame.playerLoseLife(testGame.getCurrentTurnInt());
                        bluffTextLayout.setAlpha(1);
                        bluffTextLayout.bringToFront();
                        bluffTextView.setText("Bid was correct! There are "
                                + testGame.getTotalSums()[testGame.bidFace] + " " + testGame.bidFace + "'s!");

                    } else {
                        testGame.playerLoseLife(testGame.getLastTurnInt());
                        bluffTextLayout.setAlpha(1);
                        bluffTextLayout.bringToFront();
                        bluffTextView.setText("Bid was incorrect! There are "
                                + testGame.getTotalSums()[testGame.bidFace] + " " + testGame.bidFace + "'s!");
                    }

                    canClickBid = false;
                    canClickBluff = false;
                    canClickRoll = true;
                    testGame.bidNumber=0;
                    testGame.bidNumber=0;
                    setTextDuringGame();
                    if(testGame.getNumPlayers() == 1){
                        //testGame.getPlayers().get(0) is the winner
                        canClickBid = false;
                        canClickBluff = false;
                        canClickRoll = false;
                        String winner = testGame.getPlayers().get(0).getName();
                        bluffTextLayout.setAlpha(1);
                        bluffTextLayout.bringToFront();
                        bluffTextView.setText(winner + " wins!");

                    }

                } else {
                    if(canClickRoll){
                        Toast t =Toast.makeText(TestGameActivity.this,"you must roll then bid",Toast.LENGTH_SHORT);
                        t.show();
                    } else {
                        Toast t =Toast.makeText(TestGameActivity.this,"you must bid",Toast.LENGTH_SHORT);
                        t.show();
                    }

                }
            }
        });

        rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canClickRoll)
                {
                    testGame.rollAllDice();
                    displayDice();
                    canClickBid = true;
                    canClickBluff = false;
                    canClickRoll = false;
                } else {
                    Toast t =Toast.makeText(TestGameActivity.this,
                            "you must either bid or call a bluff",Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        });

        bidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(canClickBid) {


                    int one = Integer.parseInt(testBidText1.getText().toString());
                    int two = Integer.parseInt(testBidText2.getText().toString());
                    if (one > testGame.bidNumber || two > testGame.bidFace) {
                        testGame.bidNumber = one;
                        testGame.bidFace = two;//this becomes the new bid
                        testGame.incrementTurn();
                        setTextDuringGame();
                        canClickBid = true;
                        canClickBluff = true;
                    } else {
                        Toast t = Toast.makeText(TestGameActivity.this, "enter a correct bid", Toast.LENGTH_SHORT);
                        t.show();
                    }
                } else {
                    Toast t =Toast.makeText(TestGameActivity.this,"you must roll then bid",Toast.LENGTH_SHORT);
                    t.show();
                }

            }
        });
    }

    void displayDiceEmpty(){
        for(int j = 0; j<5;j++){
            diceViews.get(j).setImageResource(0);
        }
    }


    void displayDice(){
        for(int j = 0; j<5;j++){
            diceViews.get(j).setImageResource(0);
        }
        ArrayList<Die> playersDice = testGame.getPlayers().get(testGame.getCurrentTurnInt()).getDice();
        for (int i = 0;i<playersDice.size();i++){
            diceViews.get(i).setImageResource(this.getResources().getIdentifier(
                    "die"+playersDice.get(i).getValue(),"drawable",getPackageName()));
        }
    }






}
