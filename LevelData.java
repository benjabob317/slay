import java.io.Serializable;
import java.util.ArrayList;

public class LevelData implements Serializable
{
    public int[][] playerData; // players of tiles
    public ArrayList<int[]> treeData; // List of trees defined by [x, y, spreadValue]
    public ArrayList<int[]> capitalData; // List of capitals defined by [x, y] values
    public int turn;
    public ArrayList<ArrayList<int[]>> territoryData; // list of territories defined as lists of [x, y] values
    public ArrayList<Integer> territoryMoneyData; // money should have the same index as its territory 
    public ArrayList<int[]> troopData; // List of troops defined by [x, y, protectionLevel]

    public LevelData(int[][] playerData) // only player data requiured
    {
        this.playerData = playerData;
        this.treeData = new ArrayList<int[]>();
        this.capitalData = new ArrayList<int[]>();
        this.territoryData = new ArrayList<ArrayList<int[]>>();
        this.territoryMoneyData = new ArrayList<Integer>();
        this.troopData = new ArrayList<int[]>();

        this.turn = 1;
    }
    public LevelData(int[][] playerData, ArrayList<int[]> treeData)  // player and tree data required
    {
        this.playerData = playerData;
        this.treeData = treeData;
        this.capitalData = new ArrayList<int[]>();
        this.territoryData = new ArrayList<ArrayList<int[]>>();
        this.territoryMoneyData = new ArrayList<Integer>();
        this.troopData = new ArrayList<int[]>();

        this.turn = 1;
    }
    public LevelData(int[][] playerData, ArrayList<int[]> treeData, ArrayList<int[]> capitalData) // player, tree, and capital data required
    {
        this.playerData = playerData;
        this.treeData = treeData;
        this.capitalData = capitalData;
        this.territoryData = new ArrayList<ArrayList<int[]>>();
        this.territoryMoneyData = new ArrayList<Integer>();
        this.troopData = new ArrayList<int[]>();

        this.turn = 1;
    }
    public LevelData(int[][] playerData, ArrayList<int[]> treeData, ArrayList<int[]> capitalData, int turn, ArrayList<ArrayList<int[]>> territoryData, ArrayList<Integer> territoryMoneyData, ArrayList<int[]> troopData) // everything required - for saved progress
    {
        this.playerData = playerData;
        this.treeData = treeData;
        this.capitalData = capitalData;
        this.territoryData = territoryData;
        this.territoryMoneyData = territoryMoneyData;
        this.troopData = troopData;

        this.turn = 1;
    }


}