package cs121.liarsdice;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {

    private int numLives;
    private String name;
    private ArrayList<Die> dice;
    private int[] sums; //array with the number of a player's die at that spot in array.
                        //ex if a player has 3 5's then sums[5]=3.

    public Player(String Name){
        name = Name;
        numLives = 5;
        sums = new int[7];
        for (int j = 1; j<7;j++){
            sums[j]=0;
        }
        dice = new ArrayList<Die>(5);
        for (int i = 0; i<dice.size(); i++){
            Die d = new Die();
            dice.add(d);
        }
    }

    public void rollDice(){
        for (int i=0; i < numLives; i++){
            dice.get(i).roll();
        } //randomizes all dice
        for (int j = 1; j<7;j++){
            sums[j]=0;
        }//sets all values in sums[] to 0
        for (int i=0; i< dice.size();i++){
            sums[dice.get(i).getValue()]++;
        }//creates sums[]
    }

    public int loseLife(){
        numLives--;
        dice.remove(numLives);
        dice.trimToSize();
        return numLives;
    }

    //getters
    public int[] getSums(){
        return sums;
    }

    public int getNumLives() {
        return numLives;
    }

    public ArrayList<Die> getDice(){
        return dice;
    }

    public String getName(){
        return name;
    }
}

