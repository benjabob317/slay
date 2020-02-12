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
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.Scene;



public class Level extends BorderPane
{
    public HexGrid grid;
    public TopBar topBar;
    public boolean troopPickedUp;
    public boolean troopSpawnedIn;
    public boolean castleSpawnedIn;
    public HexTile heldTroopSource; // tile where the currently picked up troop came from
    public int currentPlayer;
    public StackPane stack;
    public ScrollPane scroll;
    public double levelWidth;
    public double levelHeight;

    public Level(double levelWidth, double levelHeight)
    {
        this.topBar = buildTopBar();  
        setTop(topBar);
        this.troopPickedUp = false;
        this.currentPlayer = 1;

        this.levelWidth = levelWidth;
        this.levelHeight = levelHeight;

        HBox bottomBox = new HBox(10);
        Button endTurn = new Button("End Turn");
        endTurn.setOnAction( e -> {
            switchPlayer();
        });
        bottomBox.getChildren().addAll(endTurn);
        setBottom(bottomBox);

        this.stack = new StackPane();
        stack.setMinHeight(levelHeight);
        stack.setPrefHeight(levelHeight);
        stack.setMaxHeight(levelHeight);
        stack.setMinWidth(levelWidth);
        stack.setPrefWidth(levelWidth);
        stack.setMaxWidth(levelWidth);
        stack.setAlignment(Pos.TOP_CENTER);

        this.scroll = new ScrollPane();
        scroll.setMinHeight(levelHeight);
        scroll.setPrefHeight(levelHeight);
        scroll.setMaxHeight(levelHeight);
        scroll.setMinWidth(levelWidth);
        scroll.setPrefWidth(levelWidth);
        scroll.setMaxWidth(levelWidth);

        


        setCenter(stack);
        stack.getChildren().add(scroll);
    }

    public Level(double levelWidth, double levelHeight, LevelData data, Stage primary, Scene mainMenu) // initialize level from save data
    {
        this.topBar = buildTopBar();  
        topBar.turn = data.turn;
        setTop(topBar);
        this.troopPickedUp = false;
        this.currentPlayer = 1;

        this.stack = new StackPane();
        stack.setMinHeight(levelHeight);
        stack.setPrefHeight(levelHeight);
        stack.setMaxHeight(levelHeight);
        stack.setMinWidth(levelWidth);
        stack.setPrefWidth(levelWidth);
        stack.setMaxWidth(levelWidth);
        stack.setAlignment(Pos.TOP_CENTER);

        this.scroll = new ScrollPane();
        scroll.setMinHeight(levelHeight);
        scroll.setPrefHeight(levelHeight);
        scroll.setMaxHeight(levelHeight);
        scroll.setMinWidth(levelWidth);
        scroll.setPrefWidth(levelWidth);
        scroll.setMaxWidth(levelWidth);

        
        setCenter(stack);
        stack.getChildren().add(scroll);

        this.setGrid(new HexGrid(0, 0, data.playerData.length, data.playerData[0].length, 40, new Pane(), this));
        grid.setPlayers(data.playerData);
        grid.setTrees(data.treeData);
        this.levelWidth = levelWidth;
        this.levelHeight = levelHeight;
        grid.initializeTerritories();
        grid.initializeCapitals();
        grid.draw();

        HBox bottomBox = new HBox(10);
        Button endTurn = new Button("End Turn");
        endTurn.setOnAction( e -> {
            switchPlayer();
        });
        Button returnToMenu = new Button("Return to main menu");
        returnToMenu.setOnAction( e -> {
            primary.setScene(mainMenu);
        });
        Button zoomOut = new Button("Zoom out");
        zoomOut.setOnAction( e -> {
            grid.hexSize *= .9;
            grid.draw();
        });
        Button zoomIn = new Button("Zoom in");
        zoomIn.setOnAction( e -> {
            grid.hexSize *= (10.0/9.0);
            grid.draw();
        });

        bottomBox.getChildren().addAll(endTurn, returnToMenu, zoomOut, zoomIn);
        setBottom(bottomBox);
        
        newTurn(1);
        grid.draw();
        topBar.money.setText("");
        topBar.adjustPlayerDisplay();


    }  


    public void setGrid(HexGrid grid)
    {
        this.grid = grid;
        this.scroll.setContent(grid.canvas);

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

            troopSupply = new ImageView("./assets/peasant.png");
            castleSupply = new ImageView("./assets/castle.png");
            troopSupply.setFitWidth(17.5);
            troopSupply.setFitHeight(31);
            troopSupply.setOpacity(0);
            castleSupply.setFitWidth(20);
            castleSupply.setFitHeight(31);
            castleSupply.setOpacity(0);

            castleSupply.setOnMouseClicked( e -> {
                if (grid.currentTerritory.money >= 15)
                {
                    castleSpawnedIn = true;
                    stack.getChildren().add(new ImageView("./assets/hand.png"));
                    stack.getChildren().add(new ImageView("./assets/castle.png"));
                }
            });

            troopSupply.setOnMouseClicked( e -> {
                if (grid.currentTerritory.money >= 10)
                {
                    troopSpawnedIn = true;
                    stack.getChildren().add(new ImageView("./assets/hand.png"));
                    stack.getChildren().add(new ImageView("./assets/peasant.png"));
                    grid.currentTerritory.highlightConquerableTiles(1);
                }
            });


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

    public void switchPlayer() // runs at the end of a turn/beginning of a new turn
    {
        for (int i = 0; i < grid.territories.size(); i++)
        {
            if (grid.territories.get(i).getPlayer() == currentPlayer) // prevent troops from being to move when it is not their turn
            {
                for (int n = 0; n < grid.territories.get(i).size(); n++)
                {
                    if (grid.territories.get(i).get(n).getContents() instanceof Troop)
                    {
                        ((Troop) grid.territories.get(i).get(n).getContents()).moves = 0;
                        grid.territories.get(i).get(n).draw();
                    }  
                }
            }
        }

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
                    if (!(grid.territories.get(i).get(n).getContents() instanceof Tree)) // if NOT a tree
                    {
                        if (this.topBar.turn == 1)
                        {
                            grid.territories.get(i).money += 4;
                        }
                        grid.territories.get(i).money += 1; // if contents are not a tree, give the territory 1 money
                        if (grid.territories.get(i).get(n).getContents() instanceof Troop)
                        {
                            ((Troop) grid.territories.get(i).get(n).getContents()).rechargeMove();
                            grid.territories.get(i).money -= (2* ((int) Math.pow(3, grid.territories.get(i).get(n).getContents().getProtectionLevel()-1 ))); // troop feeding costs
                        }
                    }
                    grid.territories.get(i).get(n).draw();
                }
            }
            if (grid.territories.get(i).money < 0)
            {
                for (int n = 0; n < grid.territories.get(i).size(); n++)
                {
                    if (grid.territories.get(i).get(n).getContents() instanceof Troop)
                    {
                        grid.territories.get(i).get(n).setContents(new Gravestone(grid.territories.get(i).get(n))); 
                        grid.territories.get(i).get(n).draw();
                    }
                }
                grid.territories.get(i).money = 0;

            }
            if ((grid.territories.get(i).size() == 1) && (grid.territories.get(i).get(0).getContents() instanceof Troop) && (grid.territories.get(i).getPlayer() == currentPlayer)) // kills troops stranded without a capital
            {
                grid.territories.get(i).get(0).setContents(new Tree(grid.territories.get(i).get(0)));
                grid.territories.get(i).get(0).draw();
            }
        }
        if (tilesOwnedBy(player) == 0)
        {
            switchPlayer();
        } 
    }
    private TopBar buildTopBar()
    {
        return new TopBar();
    }
    public void hideHand()
    {
        ArrayList<ImageView> targets = new ArrayList<ImageView>();
        for (Node node: stack.getChildren())
        {
            if (node instanceof ImageView)
            {
                targets.add((ImageView) node);
            }
        }
        for (ImageView img: targets)
        {
            stack.getChildren().remove(img);
        }
    }

}