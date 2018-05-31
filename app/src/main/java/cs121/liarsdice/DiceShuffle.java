package cs121.liarsdice;

import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Random;

public class DiceShuffle extends AppCompatActivity {
    private Random ran = new Random();
    private ImageView dice1;
    private ImageView dice2;
    private ImageView dice3;
    private ImageView dice4;
    private ImageView dice5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dice);
        dice1 = findViewById(R.id.imageView6);
        dice2 = findViewById(R.id.imageView5);
        dice3 = findViewById(R.id.imageView4);
        dice4 = findViewById(R.id.imageView3);
        dice5 = findViewById(R.id.imageView2);

        Button btn = (Button) findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rollDice();
            }
        });
    }
    private void rollDice(){
        int randomnumber = ran.nextInt(6) + 1;

        switch(randomnumber){
            case 1:
                dice1.setImageResource(R.drawable.die1);
                break;
            case 2:
                dice2.setImageResource(R.drawable.die1);
                break;
            case 3:
                dice3.setImageResource(R.drawable.die1);
                break;
            case 4:
                dice4.setImageResource(R.drawable.die1);
                break;
            case 5:
                dice5.setImageResource(R.drawable.die1);
                break;\k
        }

    }
}
