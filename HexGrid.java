import java.util.ArrayList;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.image.ImageView;


public class HexGrid extends ArrayList<ArrayList<HexTile>>
{
    public double topLeftX;
    public double topLeftY;
    public double hexSize;
    public Pane canvas;
    public int width;
    public int height;
    public Territory currentTerritory;
    public ArrayList<Line> currentTerritoryBounds;
    public ArrayList<Territory> territories;
    public ArrayList<HexTile> capitals;
    public Level level;

    public HexGrid(double topLeftX, double topLeftY, int width, int height, double hexSize, Pane canvas, Level level)
    {
        this.topLeftX = topLeftX;
        this.topLeftY = topLeftY;
        this.hexSize = hexSize;
        this.canvas = canvas;
        this.width = width;
        this.height = height;
        this.currentTerritory = new Territory(this, 0);
        this.currentTerritoryBounds = new ArrayList<Line>();
        this.territories = new ArrayList<Territory>();
        this.capitals = new ArrayList<HexTile>();
        this.level = level;
        level.setGrid(this);

        canvas.setPrefSize(hexSize*2*.75*(width+(1.0/3.0)), hexSize*Math.sqrt(3)*(height+0.5));
        canvas.setMinSize(hexSize*2*.75*(width+(1.0/3.0)), hexSize*Math.sqrt(3)*(height+0.5));
        canvas.setMaxSize(hexSize*2*.75*(width+(1.0/3.0)), hexSize*Math.sqrt(3)*(height+0.5));

        for (int i = 0; i < width; i++)
        {
            add(new ArrayList<HexTile>());
        }
    }
    public void setPlayers(int[][] data) // quick way to initialize a grid with players and water tiles (water player = 0)
    {
        for (int x = 0; x < width; x++) 
        {
            for (int y = 0; y < height; y++)
            {
                if (get(x).size() > y)
                {
                    get(x).get(y).setPlayer(data[x][y]);
                } else {
                    get(x).add(new HexTile(this, x, y, data[x][y]));
                }
            }
        }
    }
    public void setCurrentTerritory(Territory territory) // selects the current territory
    {
        currentTerritory = territory;
        for (Line line: currentTerritoryBounds)
        {
            this.canvas.getChildren().remove(line);
        }
        currentTerritoryBounds = territory.getBounds();
        for (Line line: currentTerritoryBounds)
        {
            this.canvas.getChildren().add(line);
        }
        level.topBar.money.setText(Integer.toString(territory.money));

    }
    public void deselectCurrentTerritory()
    {
        for (Line line: currentTerritoryBounds)
        {
            this.canvas.getChildren().remove(line);
        }
        currentTerritory = new Territory(this, 7);
    } 
    public void draw()
    {
        for (ArrayList<HexTile> column: this) {
            for (HexTile tile: column) {
                tile.draw();
            }
        }

    }  
    public void initializeTerritories() // merges adjacent tiles together to form larger territories.
    {
        for (int x = 0; x < width; x++) // builds all territories
        {
            for (int y = 0; y < height; y++) // iterates through the grid
            {
                if (this.get(x).get(y).getPlayer() != 0) {
                    Territory newTerritory = new Territory(this, this.get(x).get(y).getPlayer());
                    ArrayList<Territory> eligibleTerritories = new ArrayList<Territory>();
                    for (HexTile tile: this.get(x).get(y).getNeighbors())
                    {
                        if ((tile.getPlayer() == this.get(x).get(y).getPlayer()) && (tile.getTerritory().size() > 0))
                        {
                            eligibleTerritories.add(tile.getTerritory());
                        }
                    }
                    for (int i = 0; i < eligibleTerritories.size(); i++)
                    {
                        newTerritory.absorbTerritory(eligibleTerritories.get(i));
                    }
                    newTerritory.addTile(this.get(x).get(y));
                    if (this.get(x).get(y).getTerritory().size() == 0)
                    {
                        this.get(x).get(y).getTerritory().addTile(this.get(x).get(y));
                    }
                }
            }
        }

        territories.clear();
        for (int x = 0; x < width; x++) // fills in the list of territories
        {
            for (int y = 0; y < height; y++)
            {
                if (!(territories.contains(this.get(x).get(y).getTerritory())))
                {
                    territories.add(this.get(x).get(y).getTerritory());
                }
            }
        }
    }

    public void initializeCapitals() // generates new capitals for all valid territories
    {
        for (Territory territory: this.territories)
        {
            if ((!(territory.hasCapital)) && (territory.size() > 1))
            {
                territory.generateNewCapital();
                capitals.add(territory.getCapital());
            }
        }
    }
}