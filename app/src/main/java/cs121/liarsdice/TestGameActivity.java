package cs121.liarsdice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.Serializable;

public class TestGameActivity extends AppCompatActivity implements Serializable {

    Game testGame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_game);
        testGame = (Game) getIntent().getSerializableExtra("testGame");
    }
}
