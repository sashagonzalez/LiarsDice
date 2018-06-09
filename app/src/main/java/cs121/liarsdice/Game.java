package cs121.liarsdice;

import java.io.Serializable;
import java.util.ArrayList;


//the idea is the lobby activity will create a Game object and fill it with players.
//then it will pass this object to GameActivity and it will use this to run the game
public class Game implements Serializable{

    private int numPlayers;
    private ArrayList<Player> players; //will use this arrayList starting from index 1
    private int currentTurn;
    private int nextTurn;
    private int[] totalSums; //sums[] but for the whole game. will use this to check bluffs

    public Game(int nPlayers, ArrayList<Player> p){
        numPlayers = nPlayers;
        currentTurn = 1;
        nextTurn = 2;
        players = p;
    }


    public void incrementTurn(){
        currentTurn = (currentTurn +1)%numPlayers;
        nextTurn = (currentTurn +1)%numPlayers;
    }


    //this function deletes players by name, meaning that no two players may have the same name
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
    }


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

        for (int i=0;i<numPlayers;i++){
            int[] individualSum = players.get(i).getSums();
            for (int k=1;k<7;k++){
                totalSums[individualSum[k]]++;
            }
        }//gets the sums for all players
    }

    public void setCurrentTurn(int t){
        currentTurn = t%numPlayers;
        nextTurn = (t+1)%numPlayers;
    }

    //getters
    public int getNumPlayers(){
        return numPlayers;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public int getNextTurn() {
        return nextTurn;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public int[] getTotalSums() {
        return totalSums;
    }


}
