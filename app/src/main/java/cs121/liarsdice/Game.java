package cs121.liarsdice;

import java.util.ArrayList;


//the idea is the lobby activity will create a Game object and fill it with players.
//then it will pass this object to GameActivity and it will use this to run the game
public class Game {

    private int numPlayers;
    private ArrayList<Player> players; //will use this arrayList starting from index 1
    private int turn;
    private int nextTurn;
    private int[] totalSums; //sums[] but for the whole game. will use this to check bluffs

    public Game(int nPlayers, ArrayList<Player> p){
        numPlayers = nPlayers;
        turn = 1;
        nextTurn = 2;
        players = p;
    }


    public void incrementTurn(){
        turn = (turn+1)%numPlayers;
        nextTurn = (turn+1)%numPlayers;
    }

    //TODO:this
    public void deletePlayer(String s){

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

    public int getTurn() {
        return turn;
    }

    public int[] getTotalSums() {
        return totalSums;
    }


}
