import java.util.ArrayList;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;


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
        if (territory.money >= 10)
        {
            level.topBar.troopSupply.setOpacity(1);
        } else {
            level.topBar.troopSupply.setOpacity(0);
        }


        if (territory.money >= 15)
        {
            level.topBar.castleSupply.setOpacity(1);
        } else {
            level.topBar.castleSupply.setOpacity(0);
        }

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

    public void adjustTerritory(Territory territory) // splits up, removes money from, and adds a capital to the territory if necessary
    {
        ArrayList<HexTile> tiles = new ArrayList<HexTile>();
        for (HexTile tile: territory)
        {
            tiles.add(tile);
        }
        HexTile capitalTile = tiles.get(0);
        boolean hasCapital = false;
        for (HexTile tile: tiles)
        {
            tile.setTerritory(new Territory(this, tile.getTerritory().getPlayer()));
            tile.getTerritory().add(tile);
            if (tile.getContents() instanceof Capital)
            {
                hasCapital = true;
                capitalTile = tile;
            }
        }

        for (HexTile tile: tiles)
        {
            Territory newTerritory = new Territory(this, tile.getPlayer());
            ArrayList<Territory> eligibleTerritories = new ArrayList<Territory>();
            for (HexTile tile2: tile.getNeighbors())
            {
                if (tile2.getPlayer() == tile.getPlayer())
                {
                    eligibleTerritories.add(tile2.getTerritory());
                }
            }
            for (int i = 0; i < eligibleTerritories.size(); i++)
            {
                newTerritory.absorbTerritory(eligibleTerritories.get(i));
            }
            newTerritory.addTile(tile);
        }

        ArrayList<Territory> relevantTerritories = new ArrayList<Territory>();

        for (HexTile tile: tiles)
        {
            if (hasCapital)
            {
                if (tile == capitalTile)
                {
                    tile.setContents(new Capital(tile));
                    tile.getTerritory().money = territory.money;
                }
            }
            if (!(relevantTerritories.contains(tile.getTerritory())))
            {
                relevantTerritories.add(tile.getTerritory());
            }
            if (tile.getContents() instanceof Capital)
            {
                tile.getTerritory().hasCapital = true;
            }
        }


        for (Territory terr: relevantTerritories)
        {
            if (terr.hasCapital)
            {
                if (terr.size() == 1)
                {
                    terr.get(0).setContents(new Tree(terr.get(0)));
                    terr.hasCapital = false;
                }
                
            }
            if (!(terr.hasCapital))
            {
                if (terr.size() > 1)
                {
                    terr.generateNewCapital();
                    capitals.add(terr.getCapital());
                }
                terr.money = 0;
            }
            territories.add(terr);
        }
        territories.remove(territory);

        this.draw();
    }

}