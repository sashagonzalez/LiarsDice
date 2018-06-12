package cs121.liarsdice;

import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

public class GameActivity extends AppCompatActivity implements Serializable {

//    WifiP2pManager myWifi;
  //  WifiP2pManager.Channel myChannel;

    Game myGame;
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
    ArrayList<Player> players;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        Button start = (Button) findViewById(R.id.buttonG);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discoverService();
                setTextDuringGame();
            }
        });

        players = new ArrayList<>(DiceWifi.receiver.peers.size()+1);
        Player me = new Player(DiceWifi.myName);
        players.add(me);
        for(int i = 0; i < DiceWifi.receiver.peers.size(); i++){
            Player p = new Player(DiceWifi.receiver.peers.get(i).deviceName);
            players.add(p);
        }


        int playerAmt = DiceWifi.receiver.peers.size()+1;
        while (playerAmt < 0){
            int newNum = 0;
            for(int i = 1; i < playerAmt; i++){
                if(players.get(i-1).getName().compareTo(players.get(i).getName()) >= 0){
                    Collections.swap(players, i-1, i);
                    newNum = i;
                }
            }
            playerAmt = newNum;
        }

        myGame = new Game(DiceWifi.receiver.peers.size() + 1, players);

        //init everything
        initViews();
        initOnClicks();
        setFirstText();
        displayDiceEmpty();
        canClickBid = false;
        canClickBluff = false;
        canClickRoll = true;
    }

    @Override
    protected void onResume(){
        super.onResume();
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(myGame.getCurrentTurnString() != DiceWifi.myName) {
           //         discoverService();
                   // setTextDuringGame();
                }

            }
        }, 0, 1000);//put here time 1000 milliseconds=1 second
    }


    // Move this to game activity later to send messages to other phones
    // Curplayer will call this method to send data to everyone else
    private void startRegistration() {
        //  Create a string map containing information about your service.
        Map record = new HashMap();
        record.put("curBid", "three two's");
        // Send game instance
        int gameTurn = myGame.getTotalTurns() + 1;
        byte[] theGameB = serializeGame(myGame).getBytes();
Log.d("aaa", Integer.toString(theGameB.length));
        byte[][] gameParts = new byte[12][128];
  //      byte[] game3 = new byte[theGameB.length - 255*2];

        for(int i = 0; i < theGameB.length; i++){
            for(int j = 0; j < 12; j++) {
                if (i / 128 == j)
                    gameParts[j][i] = theGameB[i];
                else
                    break;
            }
        }

     //   String newString = new String(game1) + new String(game2) + new String(game3);

        for(int j = 0; j < 12; j++) {
            record.put(Integer.toString(j) + Integer.toString(gameTurn), new String(gameParts[j]));
        }

        // Service information.  Pass it an instance name, service type
        // _protocol._transportlayer , and the map containing
        // information other devices will want once they connect to this one.
        WifiP2pDnsSdServiceInfo serviceInfo =
                WifiP2pDnsSdServiceInfo.newInstance("_test", "_presence._tcp", record);

        // Add the local service, sending the service info, network channel,
        // and listener that will be used to indicate success or failure of
        // the request.
        DiceWifi.myWifi.addLocalService(DiceWifi.myChannel, serviceInfo, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                // Command successful! Code isn't necessarily needed here,
                // Unless you want to update the UI or add logging statements.
                Toast.makeText(GameActivity.this, "Added service successfully.",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int arg0) {
                // Command failed.  Check for P2P_UNSUPPORTED, ERROR, or BUSY
                Toast.makeText(GameActivity.this, "Fail to add service.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Move this to game activity later to receive messages to other phones
    final HashMap<String, String> messages = new HashMap<String, String>();

    // Call method to look for services/messages from other devices
    private void discoverService() {
        WifiP2pManager.DnsSdTxtRecordListener txtListener = new WifiP2pManager.DnsSdTxtRecordListener() {


            public void onDnsSdTxtRecordAvailable(String fullDomain, Map record, WifiP2pDevice device) {
                int gameTurn = myGame.getTotalTurns() + 1;
                messages.put(device.deviceAddress, "k");

                messages.put(device.deviceAddress, (String) record.get("curBid"));
                System.out.println("bacon" + (String) record.get("curBid"));


                String updatedGame = "";
                for(int j = 0; j < 12; j++) {
                    updatedGame += record.get(Integer.toString(j) + Integer.toString(gameTurn));
                }

                myGame = (Game) deserializeGame(updatedGame);
                Toast.makeText(GameActivity.this, "REEEEEE",
                        Toast.LENGTH_SHORT).show();
                setTextDuringGame();
            }
        };

        WifiP2pManager.DnsSdServiceResponseListener servListener = new WifiP2pManager.DnsSdServiceResponseListener() {
            @Override
            public void onDnsSdServiceAvailable(String instanceName, String registrationType,
                                                WifiP2pDevice resourceType) {
                // Update the device name with the human-friendly version from
                // the DnsTxtRecord, assuming one arrived.
                resourceType.deviceName = messages
                        .containsKey(resourceType.deviceAddress) ? messages
                        .get(resourceType.deviceAddress) : resourceType.deviceName;
            }
        };

        DiceWifi.myWifi.setDnsSdResponseListeners(DiceWifi.myChannel, servListener, txtListener);

        WifiP2pDnsSdServiceRequest serviceRequest = WifiP2pDnsSdServiceRequest.newInstance();
        DiceWifi.myWifi.addServiceRequest(DiceWifi.myChannel,
                serviceRequest,
                new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                     //   System.out.println("yes1");
                    }

                    @Override
                    public void onFailure(int code) {
                        System.out.println("no1");
                    }
                });

        DiceWifi.myWifi.discoverServices(DiceWifi.myChannel, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                System.out.println("size" + messages.size() + messages.get("k"));

                for (Map.Entry<String,String> entry : messages.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    System.out.println("assdas" + key + " " + value);
                }

                Toast.makeText(GameActivity.this, "REeEEEEE",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int code) {
                System.out.println("no2");
            }
        });
    }

    //String serializedObject = "";

    // serialize the object
    public String serializeGame(Object obj) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);
            so.writeObject(obj);
            so.flush();
            return bo.toString();
        } catch (Exception e) {
            System.out.println(e);
            return "";
        }
    }

    // deserialize the object
    public Object deserializeGame(String s) {
        try {
            byte b[] = s.getBytes();
            ByteArrayInputStream bi = new ByteArrayInputStream(b);
            ObjectInputStream si = new ObjectInputStream(bi);
            Object game = (Object) si.readObject();
            return game;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    void setFirstText(){
        playerText.setText("Players: " + myGame.getNumPlayers());
        totalDieText.setText("Die in Game: " + myGame.getNumDie());
        currentTurnText.setText("Player's Turn: " + myGame.getCurrentTurnString());
        nextTurnText.setText("Next Player: " + myGame.getNextTurnString());

        //currentBidText.setText("Current Bid: " + myGame.currentBid.first.toString() +
        //        myGame.currentBid.second.toString() + "'s");


    }

    void setTextDuringGame(){
        playerText.setText("Players: " + myGame.getNumPlayers());
        totalDieText.setText("Die in Game: " + myGame.getNumDie());
        currentTurnText.setText("Player's Turn: " + myGame.getCurrentTurnString());
        nextTurnText.setText("Next Player: " + myGame.getNextTurnString());

        if(myGame.bidFace==0 || myGame.bidNumber == 0){
            currentBidText.setText("Current Bid: No Bid");
        } else {
            currentBidText.setText("Current Bid: " + myGame.bidNumber + " " +
                    myGame.bidFace + "'s");
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
                if (canClickBluff && myGame.getCurrentTurnString()== DiceWifi.myName ) {

                    if (myGame.isBidTrue()) {
                        myGame.playerLoseLife(myGame.getCurrentTurnInt());
                        bluffTextLayout.setAlpha(1);
                        bluffTextLayout.bringToFront();
                        bluffTextView.setText("Bid was correct! There are "
                                + myGame.getTotalSums()[myGame.bidFace] + " " + myGame.bidFace + "'s!");

                    } else {
                        myGame.playerLoseLife(myGame.getLastTurnInt());
                        bluffTextLayout.setAlpha(1);
                        bluffTextLayout.bringToFront();
                        bluffTextView.setText("Bid was incorrect! There are "
                                + myGame.getTotalSums()[myGame.bidFace] + " " + myGame.bidFace + "'s!");
                    }

                    canClickBid = false;
                    canClickBluff = false;
                    canClickRoll = true;
                    myGame.bidNumber=0;
                    myGame.bidNumber=0;
                    setTextDuringGame();
                    if(myGame.getNumPlayers() == 1){
                        //myGame.getPlayers().get(0) is the winner
                        canClickBid = false;
                        canClickBluff = false;
                        canClickRoll = false;
                        String winner = myGame.getPlayers().get(0).getName();
                        bluffTextLayout.setAlpha(1);
                        bluffTextLayout.bringToFront();
                        bluffTextView.setText(winner + " wins!");

                    }

                    startRegistration();//send data here

                } else {
                    if(canClickRoll){
                        Toast t =Toast.makeText(GameActivity.this,"you must roll then bid",Toast.LENGTH_SHORT);
                        t.show();
                    } else {
                        Toast t =Toast.makeText(GameActivity.this,"you must bid",Toast.LENGTH_SHORT);
                        t.show();
                    }

                }
            }
        });

        rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canClickRoll && myGame.getCurrentTurnString() == DiceWifi.myName)
                {
                    myGame.rollAllDice();
                    displayDice();
                    canClickBid = true;
                    canClickBluff = false;
                    canClickRoll = false;
                } else {
                    Toast t =Toast.makeText(GameActivity.this,
                            "you must either bid or call a bluff",Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        });

        bidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(canClickBid && myGame.getCurrentTurnString()== DiceWifi.myName )
                {


                    int one = Integer.parseInt(testBidText1.getText().toString());
                    int two = Integer.parseInt(testBidText2.getText().toString());
                    if (one > myGame.bidNumber || two > myGame.bidFace)
                    {

                        myGame.bidNumber = one;
                        myGame.bidFace = two;//this becomes the new bid
                        myGame.incrementTurn();
                        setTextDuringGame();
                        canClickBid = true;
                        canClickBluff = true;
                        startRegistration();//send data to update

                    } else {
                        Toast t = Toast.makeText(GameActivity.this, "enter a correct bid", Toast.LENGTH_SHORT);
                        t.show();
                    }

                } else {
                    Toast t =Toast.makeText(GameActivity.this,"you must roll then bid",Toast.LENGTH_SHORT);
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
        ArrayList<Die> playersDice = myGame.getPlayers().get(myGame.getCurrentTurnInt()).getDice();
        for (int i = 0;i<playersDice.size();i++){
            diceViews.get(i).setImageResource(this.getResources().getIdentifier(
                    "die"+playersDice.get(i).getValue(),"drawable",getPackageName()));
        }
    }

}
