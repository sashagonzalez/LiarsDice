package cs121.liarsdice;

import android.support.v4.util.Pair;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;


//the idea is the lobby activity will create a Game object and fill it with players.
//then it will pass this object to GameActivity and it will use this to run the game
public class Game implements Serializable{

    private int numPlayers;
    private int totalTurns;
    private ArrayList<Player> players; //will use this arrayList starting from index 0
    private int currentTurn;
    private int nextTurn;
    private int lastTurn;
    public int bidNumber;
    public int bidFace;

    private int[] totalSums; //sums[] but for the whole game. will use this to check bluffs

    public Game(int nPlayers, ArrayList<Player> p){
        numPlayers = nPlayers;
        currentTurn = 0;
        totalTurns = 0;
        nextTurn = 1;
        lastTurn = nPlayers - 1;
        players = p;
        bidNumber = 0;
        bidFace = 0;
        totalSums = new int[7];
    }


    public void incrementTurn(){
        currentTurn = (currentTurn +1)%numPlayers;
        nextTurn = (currentTurn +1)%numPlayers;
        lastTurn = (lastTurn +1)%numPlayers;
        totalTurns++;
    }


    /*//this function deletes players by name, meaning that no two players may have the same name
    public Boolean deletePlayerByName(String s){
        for(int i = 0;i<numPlayers;i++)
        {
            if(players.get(i).getName().equals(s) )
            {
                players.remove(i);
                numPlayers--;
                if(i==currentTurn){
                    setCurrentTurn(currentTurn); //the player who's turn it is now is deleted, so
                    //the next player is now on his turn. IF he was the last player, then the next
                    //turn will be Player 1
                } else if(i==nextTurn){
                    setCurrentTurn(currentTurn+1); //the player who's next in line dies. if he
                    //was the last player, then Player 1 is next
                }
                return true;
            }
        }
        return false;
    } */


    public Boolean deletePlayerByIndex(int index){
        if (index >=numPlayers ){
            return false;
        } else {
            players.remove(index);
            numPlayers--;
            return true;
        }
    }

    public void setSums(){
        for(int j=1;j<7;j++){
            totalSums[j]=0;
        } //sets everything to 0

        for (int i=0;i<numPlayers;i++)
        {
            int[] individualSum = players.get(i).getSums();
            for (int k=1;k<7;k++)
            {
                totalSums[k] += individualSum[k];
            }
        }//gets the sums for all players
    }

    public void rollAllDice(){
        for(int i = 0; i<numPlayers; i++){
            players.get(i).rollDice();
        }
        setSums();
        Log.d("game","checking totalSums");
        for (int i = 1; i < totalSums.length; i++) {
            if (i > 1) {
                Log.d("game",", ");
            }
            Log.d("game",""+totalSums[i]);
        }
    }

    /*public void setCurrentTurn(int t){
        currentTurn = t%numPlayers;
        nextTurn = (t+1)%numPlayers;
    } */

    public Boolean isBidTrue(){
        return totalSums[bidFace] >= bidNumber;
    }

    public void playerLoseLife(int index){
        players.get(index).loseLife();
        if (players.get(index).getNumLives()==0){
            if( index == players.size() -1 ){
                incrementTurn();
                deletePlayerByIndex(index); //if player who died is last, turn is now at 0
            }
            deletePlayerByIndex(index); //else we dont actually need to increment the turn
            totalTurns++;
        }

    }

    //getters
    public int getNumPlayers(){
        return numPlayers;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public int getNextTurnInt() {
        return nextTurn;
    }

    public int getCurrentTurnInt() {
        return currentTurn;
    }

    public int getLastTurnInt(){
        return lastTurn;
    }

    public String getCurrentTurnString(){
        return players.get(currentTurn).getName();
    }

    public String getNextTurnString(){
        return players.get(nextTurn).getName();
    }

    public int[] getTotalSums() {
        return totalSums;
    }

    public int getNumDie()
    {
        int numDie = 0;
        for(int i =0; i<numPlayers;i++ ){
            numDie+= players.get(i).getNumLives();
        }
        return numDie;
    }

    public int getTotalTurns(){return totalTurns;}




}
