package java;

import java.util.*;
import java.util.List;

public class Model {
    private static final int FIELD_WIDTH = 4;
    protected Tile [][] gameTiles;
    int score;
    int maxTile = 0;
    private Stack <Tile[][]> previousStates = new Stack<> ();
    private Stack <Integer> previousScores = new Stack<> ();
    private boolean isSaveNeeded = true;

    public Model() {
        gameTiles =new Tile[FIELD_WIDTH][FIELD_WIDTH];
        resetGameTiles();
    }

    private void saveState(Tile[][] gameTiles){
        Tile[][] tiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i = 0; i < gameTiles.length; i++) {
            for (int j = 0; j < gameTiles[i].length; j++) {
                tiles[i][j]= new Tile(gameTiles[i][j].value);
            }
        }
        previousStates.push(tiles);
        previousScores.push(score);
        isSaveNeeded = false;
    }

    public void rollback (){
        if (!previousStates.isEmpty()&&!previousScores.isEmpty()){
            gameTiles = previousStates.pop();
            score = previousScores.pop();
        }}
    public Tile[][] getGameTiles() {
        return gameTiles;
    }

    public boolean canMove(){
        boolean canMove = false;
        for (int i = 0; i < gameTiles.length-1 ; i++) {
            for (int j = 0; j < gameTiles[i].length-1; j++) {
                if (gameTiles[i][j].value == 0)
                    canMove=true;
                if ( gameTiles[i + 1][j].value == gameTiles[i][j].value)
                    canMove=true;
                if (gameTiles[i][j + 1].value == gameTiles[i][j].value)
                    canMove=true;
            }
        }

        return canMove;
    }

    private List <Tile> getEmptyTiles(){
        List<Tile> emptyTilesList = new ArrayList<>();
        for (int i = 0; i < gameTiles.length; i++) {
            for (int j = 0; j < gameTiles[i].length ; j++) {
                if (gameTiles[i][j].isEmpty()){
                    emptyTilesList.add(gameTiles[i][j]);
                }
            }
        }
        return emptyTilesList;
    }
    public void addTile(){
        List <Tile> emptyTilesList = getEmptyTiles();
        if (emptyTilesList.size()>0){
            emptyTilesList.get((int) (Math.random()*emptyTilesList.size())).value=(Math.random() < 0.9 ? 2 : 4);
        }
    }
    public void resetGameTiles(){
        Arrays.stream(gameTiles).forEach(a -> Arrays.setAll(a, i -> new Tile()));
        addTile();
        addTile();
    }
    private boolean compressTiles(Tile[] tiles){
        boolean change = false;
        for (int i = tiles.length - 1; i > 0; i--) {
            for(int j = 0 ; j < i ; j++){
                if ((tiles[j].value == 0)&&(tiles[j + 1].value != 0)){
                    int tmp = tiles[j].value;
                    tiles[j].value = tiles[j+1].value;
                    tiles[j+1].value = tmp;

                    change = true;
                }
            }
        }
        return  change;
    }
    private boolean mergeTiles(Tile[] tiles){
        boolean change = false;
        for (int i = 0; i < tiles.length-1; i++) {
            if (tiles[i].value==tiles[i+1].value&&tiles[i].value!=0){
                tiles[i].value=2*(tiles[i].value);
                score = score+(tiles[i].value);
                if (maxTile<tiles[i].value){
                    maxTile=tiles[i].value;
                }
                tiles[i+1].value=0;
                compressTiles(tiles);
                change = true;
            }
        }
        return change;
    }
    public void left(){
        if (isSaveNeeded){
            saveState(gameTiles);
        }
        boolean isChange = false;
        for (int i = 0; i < gameTiles.length ; i++) {
            if (compressTiles(gameTiles[i])==true||mergeTiles(gameTiles[i])==true){
                isChange = true;
            }
        }
        if (isChange==true){
            addTile();
            isSaveNeeded=true;
        }
    }
    public void takeover(){
        Tile [][] tiles = gameTiles;
        Tile [][]tilesUp = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i = 0; i<tiles.length ; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                tilesUp[i][j] = tiles[3-j][i];
            }
        }
        gameTiles=tilesUp;
    }

    public void up(){
        saveState(gameTiles);
        takeover();
        takeover();
        takeover();
        left();
        takeover();

    }
    public void right (){
        saveState(gameTiles);
        takeover();
        takeover();
        left();
        takeover();
        takeover();
    }

    public void down (){
        saveState(gameTiles);
        takeover();
        left();
        takeover();
        takeover();
        takeover();
    }
    public void randomMove(){
        int result = ((int) (Math.random() * 100)) % 4;
        if (result==0){
            left();
        } else if (result==1){
            right();
        } else if (result==2){
            up();
        } else if (result==3){
            down();
        }

    }
    public boolean hasBoardChanged (){
        int stackResult = 0;
        int gameTilesResult = 0;
        for (int i = 0; i < previousStates.peek().length; i++) {
            for (int j = 0; j < previousStates.peek()[i].length ; j++) {
                stackResult = stackResult + previousStates.peek()[i][j].value;
            }
        }
        for (int i = 0; i < gameTiles.length; i++) {
            for (int j = 0; j < gameTiles[i].length ; j++) {
                gameTilesResult = gameTilesResult + gameTiles[i][j].value;
            }
        }

        if (stackResult!=gameTilesResult){
            return true;
        } else return false;
    }

    public MoveEfficiency getMoveEfficiency(Move move){
        move.move();
        MoveEfficiency moveEfficiency;

        if (hasBoardChanged()){
            moveEfficiency = new MoveEfficiency(getEmptyTiles().size(), score, move);

        } else {

            moveEfficiency = new MoveEfficiency(-1, 0, move);

        }
        rollback();
        return moveEfficiency;
    }
    public void autoMove(){
        PriorityQueue <MoveEfficiency>queue = new PriorityQueue<>(4, Collections.reverseOrder());
        queue.offer(getMoveEfficiency(this::left));
        queue.offer(getMoveEfficiency(this::right));
        queue.offer(getMoveEfficiency(this::up));
        queue.offer(getMoveEfficiency(this::down));
        queue.peek().getMove().move();

    }
}

