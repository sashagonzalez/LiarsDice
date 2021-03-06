package cs121.liarsdice;

import java.io.Serializable;
import java.util.Random;

public class Die implements Serializable{

    private int value;
    private Random random;

    public Die(){
        random = new Random();
        value =  random.nextInt(6) + 1;
    }

    public void roll(){
        value =  random.nextInt(6) + 1;
    } //values 1 thru 6

    public int getValue() {
        return value;
    }
}
