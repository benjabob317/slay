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
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MenuBar;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.lang.ClassNotFoundException;
import java.io.File;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class LevelEditor extends BorderPane
{
    public EditableGrid grid;
    public TopBar topBar;
    public int selectedPlayer;
    public StackPane stack;
    public ScrollPane scroll;
    public double levelWidth;
    public double levelHeight;

    public LevelEditor(double levelWidth, double levelHeight, Stage primary)
    {
        this.topBar = new TopBar();

        VBox topRow = new VBox();
        topRow.getChildren().addAll(buildMenus(primary), topBar);
        setTop(topRow);
        
        this.levelWidth = levelWidth;
        this.levelHeight = levelHeight;

        this.selectedPlayer = 1;

        this.scroll = new ScrollPane();
        scroll.setMinHeight(levelHeight);
        scroll.setPrefHeight(levelHeight);
        scroll.setMaxHeight(levelHeight);
        scroll.setMinWidth(levelWidth);
        scroll.setPrefWidth(levelWidth);
        scroll.setMaxWidth(levelWidth);

        setCenter(scroll);

        HBox bottomBar = new HBox();
        bottomBar.setAlignment(Pos.CENTER);


        Button trim = new Button("Trim");
        trim.setOnAction(e -> {
            trimLevel();
        });
        Button addBottomRow = new Button(" | \n \\/ ");
        addBottomRow.setOnAction( e -> {
            addRowOnBottom();
        });
        VBox boxOne = new VBox();
        boxOne.getChildren().addAll(trim, addBottomRow);

        Button addRightColumn = new Button("->");
        addRightColumn.setOnAction(e -> {
            addColumnOnRight();
        });
        
        bottomBar.getChildren().addAll(boxOne, addRightColumn);
        setBottom(bottomBar);


    }
    public void setGrid(EditableGrid grid)
    {
        this.grid = grid;
        scroll.setContent(grid.canvas);
    }


    public class TopBar extends HBox
    {
        public Hexagon waterTileSupply;
        public TileSupply playerOneTileSupply;
        public Hexagon playerTwoTileSupply;
        public Hexagon playerThreeTileSupply;
        public Hexagon playerFourTileSupply;
        public Hexagon playerFiveTileSupply;
        public Hexagon playerSixTileSupply;
        public ImageView treeSupply;
        public ImageView treeDeletion;
        public Button zoomIn;
        public Button zoomOut;
        public int treeState;
        
        public TopBar()
        {
            this.setAlignment(Pos.CENTER);
            //this.setSpacing(5);
            this.treeState = 0;
            
            this.waterTileSupply = new TileSupply(0, Color.web("0x0000FF"));
            this.playerOneTileSupply = new TileSupply(1, Color.web("0x94CF8A"));
            this.playerTwoTileSupply = new TileSupply(2, Color.web("0xDCCA98"));
            this.playerThreeTileSupply = new TileSupply(3, Color.web("0x016E1E"));
            this.playerFourTileSupply = new TileSupply(4, Color.web("0xE6E174"));
            this.playerFiveTileSupply = new TileSupply(5, Color.web("0x56A319"));
            this.playerSixTileSupply = new TileSupply(6, Color.web("0x786319"));

            
            this.treeSupply = new ImageView("./assets/tree.png");
            treeSupply.setFitWidth(32);
            treeSupply.setFitHeight(50);
            treeSupply.setOnMouseClicked( e -> {
                treeState = 1;
            });

            this.treeDeletion = new ImageView("./assets/remove_tree.png");
            treeDeletion.setFitWidth(50);
            treeDeletion.setFitHeight(50);
            treeDeletion.setOnMouseClicked( e -> {
                treeState = -1;
            });

            getChildren().addAll(waterTileSupply, playerOneTileSupply, playerTwoTileSupply, playerThreeTileSupply, playerFourTileSupply, playerFiveTileSupply, playerSixTileSupply, treeSupply, treeDeletion);
            setPadding(new Insets(0, 0, 10, 0));
        }

    }

    public class TileSupply extends Hexagon
    {
        public TileSupply(int player, Color color)
        {
            super(0, 0, 30, color, Color.BLACK);
            setOnMouseClicked( e -> {
                selectedPlayer = player;
                topBar.treeState = 0;
            });
        }
    } 
    public void trimLevel()
    {
        boolean doneTrimming = false;
        while (!(doneTrimming)) // left side 
        {
            for (int y = 0; y < grid.get(0).size(); y++)
            {
                for (int i = 0; i <= 1; i++)
                {
                    if (!(grid.get(0+i).get(y).isWater()))
                    {
                        doneTrimming = true;
                    }
                }
            }
            if (!(doneTrimming))
            {
                for (int y = 0; y < grid.get(0).size(); y++)
                {
                    grid.canvas.getChildren().remove(grid.get(0).get(y).hex);
                }
                grid.remove(grid.get(0));
            }
        }
        doneTrimming = false;
        while (!(doneTrimming)) // right side 
        {
            for (int y = 0; y < grid.get(grid.size() - 1).size(); y++)
            {
                for (int i = 0; i <= 1; i++)
                {
                    if (!(grid.get(grid.size() - 1 - i).get(y).isWater()))
                    {
                        doneTrimming = true;
                    }
                }
            }
            if (!(doneTrimming))
            {
                for (int y = 0; y < grid.get(0).size(); y++)
                {
                    grid.canvas.getChildren().remove(grid.get(grid.size() - 1).get(y).hex);
                }
                grid.remove(grid.get(grid.size() - 1));
            }
        }
        doneTrimming = false;
        while (!(doneTrimming)) // top
        {
            for (int x = 0; x < grid.size(); x++)
            {
                if (!(grid.get(x).get(0).isWater()))
                {
                    doneTrimming = true;
                }
            }
            if (!(doneTrimming))
            {
                for (int x = 0; x < grid.size(); x++)
                {
                    grid.canvas.getChildren().remove(grid.get(x).get(0).hex);
                    grid.get(x).remove(grid.get(x).get(0));
                }
            }
        }
        doneTrimming = false;
        while (!(doneTrimming)) // bottom
        {
            for (int x = 0; x < grid.size(); x++)
            {
                if (!(grid.get(x).get(grid.get(x).size()-1).isWater()))
                {
                    doneTrimming = true;
                }
            }
            if (!(doneTrimming))
            {
                for (int x = 0; x < grid.size(); x++)
                {
                    grid.canvas.getChildren().remove(grid.get(x).get(grid.get(x).size()-1).hex);
                    grid.get(x).remove(grid.get(x).get(grid.get(x).size()-1));
                }
            }
        }

        for (int x = 0; x < grid.size(); x++) // adjusts x and y positions of tiles
        {
            for (int y = 0; y < grid.get(x).size(); y++)
            {
                grid.get(x).get(y).x = x;
                grid.get(x).get(y).y = y;
            }
        }
        grid.draw();
    }

    public void addColumnOnRight()
    {
        for (int i = 0; i < 2; i++)
        {
            grid.add(new ArrayList<EditableTile>());
            for (int y = 0; y < grid.get(grid.size()-2).size(); y++)
            {
                grid.get(grid.size()-1).add(new EditableTile(grid, grid.size()-1, y, 0, false));
            }
        }
        grid.canvas.setPrefSize(grid.hexSize*2*.75*(grid.size()+(1.0/3.0)), grid.hexSize*Math.sqrt(3)*(grid.get(0).size()+0.5));
        grid.canvas.setMinSize(grid.hexSize*2*.75*(grid.size()+(1.0/3.0)), grid.hexSize*Math.sqrt(3)*(grid.get(0).size()+0.5));
        grid.canvas.setMaxSize(grid.hexSize*2*.75*(grid.size()+(1.0/3.0)), grid.hexSize*Math.sqrt(3)*(grid.get(0).size()+0.5));
        grid.draw();
    }
    public void addRowOnBottom()
    {
        for (int x = 0; x < grid.size(); x++)
        {
            grid.get(x).add(new EditableTile(grid, x, grid.get(x).size(), 0, false));
        }
        grid.canvas.setPrefSize(grid.hexSize*2*.75*(grid.size()+(1.0/3.0)), grid.hexSize*Math.sqrt(3)*(grid.get(0).size()+0.5));
        grid.canvas.setMinSize(grid.hexSize*2*.75*(grid.size()+(1.0/3.0)), grid.hexSize*Math.sqrt(3)*(grid.get(0).size()+0.5));
        grid.canvas.setMaxSize(grid.hexSize*2*.75*(grid.size()+(1.0/3.0)), grid.hexSize*Math.sqrt(3)*(grid.get(0).size()+0.5));
        grid.draw();
    }

    public void saveLevel(Stage primary)
    {
        try
        {
            FileChooser fc = new FileChooser();
            fc.setTitle("Open a File");
            File selected = fc.showSaveDialog(primary);
            if (selected != null) {
                FileOutputStream fos = new FileOutputStream(selected.toString());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                
                int[][] playerData = new int[grid.size()][grid.get(0).size()];
                for (int x = 0; x < grid.size(); x++)
                {
                    for (int y = 0; y < grid.get(x).size(); y++)
                    {
                        playerData[x][y] = grid.get(x).get(y).player;
                    }
                }

                oos.writeObject(new LevelData(playerData));
                oos.close();
            }
        }
        catch(FileNotFoundException ex)
        {
            ex.printStackTrace();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    } 

    public MenuBar buildMenus(Stage primary)
    {
        MenuBar mbar = new MenuBar();
        mbar.getMenus().addAll(buildOpenMenu(), buildSaveMenu(primary), returnToMenu());
        return mbar;
    }
    public Menu buildOpenMenu()
    {
        Menu openMenu = new Menu("Open");
        return openMenu;
    }
    public Menu buildSaveMenu(Stage primary)
    {
        Menu saveMenu = new Menu("Save");

        MenuItem saveMenuItem = new MenuItem("Save");
        saveMenuItem.setOnAction( e -> {
            saveLevel(primary);
        });

        saveMenu.getItems().addAll(saveMenuItem);
        return saveMenu;
    }
    public Menu returnToMenu()
    {
        Menu returnMenu = new Menu("Return to Menu");
        return returnMenu;
    }
}