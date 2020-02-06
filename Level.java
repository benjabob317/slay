import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;


public class Level extends BorderPane
{
    public HexGrid grid;
    public TopBar topBar;
    public boolean troopPickedUp;
    public HexTile heldTroopSource; // tile where the currently picked up troop came from s 
    public int currentPlayer;

    public Level()
    {
        this.topBar = buildTopBar();  
        setTop(topBar);
        this.troopPickedUp = false;
        this.currentPlayer = 1;

        HBox bottomBox = new HBox(10);
        Button endTurn = new Button("End Turn");
        endTurn.setOnAction( e -> {
            switchPlayer();
        });
        bottomBox.getChildren().addAll(endTurn);
        setBottom(bottomBox);

    }
    public void setGrid(HexGrid grid)
    {
        this.grid = grid;
        setCenter(grid.canvas);
    }

    public class TopBar extends HBox
    {
        public Text money;
        public HBox playerDisplay;
        public int turn;
        public ImageView troopSupply;
        public ImageView castleSupply;
        
        public TopBar()
        {
            this.money = new Text();
            this.setAlignment(Pos.CENTER);
            this.setSpacing(10);
            this.turn = 1;

            troopSupply = new ImageView();
            castleSupply = new ImageView();
            troopSupply.setFitWidth(40);
            troopSupply.setFitHeight(40);
            castleSupply.setFitWidth(40);
            castleSupply.setFitHeight(40);

            VBox itemSupply = new VBox();
            itemSupply.setAlignment(Pos.CENTER);
            HBox newItems = new HBox();
            newItems.getChildren().addAll(troopSupply, castleSupply);
            itemSupply.getChildren().addAll(newItems, money);

            VBox playerDisplayHolder = new VBox();
            playerDisplayHolder.setAlignment(Pos.CENTER);
            
            this.playerDisplay = new HBox();
            playerDisplayHolder.getChildren().addAll(playerDisplay, (new Text("Turn " + Integer.toString(turn))));
            playerDisplay.getChildren().addAll((new Rectangle(40, 20, Color.web("0x94CF8A"))), (new Rectangle(40, 20, Color.web("0xDCCA98"))), (new Rectangle(40, 20, Color.web("0x016E1E"))), (new Rectangle(40, 20, Color.web("0xE6E174"))), (new Rectangle(40, 20, Color.web("0x56A319"))), (new Rectangle(40, 20, Color.web("0x786319")))); 
            ((Rectangle) playerDisplay.getChildren().get(0)).setStroke(new Color(0, 0, 0, 1));
            ((Rectangle) playerDisplay.getChildren().get(0)).setStrokeWidth(1);
            this.getChildren().addAll(playerDisplayHolder, itemSupply);
            this.setPadding(new Insets(0, 0, 10, 0));
        }
        public void adjustPlayerDisplay() // adjusts the width of the player bars
        {
            int totalPlayerTiles = tilesOwnedBy(1) + tilesOwnedBy(2) + tilesOwnedBy(3) + tilesOwnedBy(4) + tilesOwnedBy(5) + tilesOwnedBy(6);
            for (int i = 0; i < 6; i++)
            {
                ((Rectangle) playerDisplay.getChildren().get(i)).setWidth(200.0*tilesOwnedBy(i+1)/totalPlayerTiles);
            }
        }

    } 
    public int tilesOwnedBy(int player) // tiles owned by player players
    {
        int val = 0;
        for (ArrayList<HexTile> column: grid)
        {
            for (HexTile tile: column)
            {
                if (tile.getPlayer() == player)
                {
                    val++;
                }
            }
        }
        return val;
    }

    public void switchPlayer()
    {
        ((Rectangle) topBar.playerDisplay.getChildren().get(currentPlayer-1)).setStroke(new Color(0, 0, 0, 0));
        ((Rectangle) topBar.playerDisplay.getChildren().get(currentPlayer-1)).setStrokeWidth(0);
        currentPlayer++;
        if (currentPlayer > 6)
        {
            topBar.turn += 1;
            ((Text) ((VBox) topBar.getChildren().get(0)).getChildren().get(1)).setText("Turn " + Integer.toString(topBar.turn));
            currentPlayer = 1;
        }
        ((Rectangle) topBar.playerDisplay.getChildren().get(currentPlayer-1)).setStroke(new Color(0, 0, 0, 1));
        ((Rectangle) topBar.playerDisplay.getChildren().get(currentPlayer-1)).setStrokeWidth(1);
        grid.deselectCurrentTerritory();
        topBar.money.setText("");
        newTurn(currentPlayer);
    }
    public void newTurn(int player) // updates all territories of player for a new turn
    {
        for (int i = 0; i < grid.territories.size(); i++)
        {
            if ((grid.territories.get(i).getPlayer() == player) && (grid.territories.get(i).size() > 1))
            {
                for (int n = 0; n < grid.territories.get(i).size(); n++)
                {
                    if (grid.territories.get(i).get(n).getContents() instanceof Gravestone)
                        {
                            grid.territories.get(i).get(n).setContents(new Tree(grid.territories.get(i).get(n))); // gravestones turned into trees
                        }
                    if (!(grid.territories.get(i).get(n).getContents() instanceof Tree))
                    {
                        grid.territories.get(i).money += 1; // if contents are not a tree, give the territory 1 money
                        if (grid.territories.get(i).get(n).getContents() instanceof Troop)
                        {
                            ((Troop) grid.territories.get(i).get(n).getContents()).rechargeMove();
                            grid.territories.get(i).money -= (2* ((int) Math.pow(3, grid.territories.get(i).get(n).getContents().getProtectionLevel()-1 ))); // troop feeding costs
                        }
                    }
                }
            }
        }
    }
    private TopBar buildTopBar()
    {
        return new TopBar();
    }

}