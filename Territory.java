import java.util.ArrayList;
import javafx.scene.shape.Line;
import javafx.scene.paint.Color;

public class Territory extends ArrayList<HexTile>
{
    private HexGrid container;
    private int player;
    public int money;
    private HexTile capital;
    public boolean hasCapital;
    private ArrayList<HexTile> highlightedTiles;
    public Territory(HexGrid container, int player)
    {
        this.container = container;
        this.player = player;
        this.money = 0;
        this.hasCapital = false;
        this.highlightedTiles = new ArrayList<HexTile>();
    }
    private boolean closeEnough(double[] m, double[] n) // compares if 2 segments are the same
    {
        if ((Math.abs(m[0] - n[0]) < 1E-6)  && (Math.abs(m[1] - n[1]) < 1E-6) && (Math.abs(m[2] - n[2]) < 1E-6) && (Math.abs(m[3] - n[3]) < 1E-6)) {
            return true;
        } else if ((Math.abs(m[0] - n[2]) < 1E-6)  && (Math.abs(m[1] - n[3]) < 1E-6) && (Math.abs(m[2] - n[0]) < 1E-6) && (Math.abs(m[3] - n[1]) < 1E-6)) {
            return true;
        } else {
            return false;
        }
    }
    public void addTile(HexTile tile)
    {
        if (tile.getPlayer() == player)
        {
            if ((tile.getTerritory() != this))
            {
                tile.setTerritory(this);
            }
            if (!(this.contains(tile))) {
                this.add(tile);
            }
        }
        tile.draw();
    }
    public void absorbTerritory(Territory territory)
    {
        this.money += territory.money;
        ArrayList<int[]> coordList = new ArrayList<int[]>();
        for (int i = 0; i < territory.size(); i++)
        {
            if (territory.get(i).getContents() instanceof Capital)
            {
                territory.get(i).setContents(new EmptyTile(territory.get(i))); // destroys old capital
            }
            coordList.add(new int[] {territory.get(i).getX(), territory.get(i).getY()});
        }

        for (int[] tileCoords: coordList)
        {
            this.addTile(container.get(tileCoords[0]).get(tileCoords[1]));
        }

    }
    public ArrayList<Line> getBounds()
    {
        ArrayList<double[]> segments = new ArrayList<double[]>();
        ArrayList<double[]> otherTileSegments = new ArrayList<double[]>();

        for (HexTile tile: this)
        {

            for (HexTile otherTile: this)
            {
                if ((tile.getY() != otherTile.getY()) || (tile.getX() != otherTile.getX()))
                {
                    for (double[] segment: otherTile.getSegments())
                    {
                        otherTileSegments.add(segment);
                    }
                }
            }

            for (double[] segment: tile.getSegments())
            {
                boolean alreadyUsed = false;
                for (double[] otherSegment: otherTileSegments) 
                {
                    if (closeEnough(segment, otherSegment))
                    {
                        alreadyUsed = true;
                    }
                }
                if (!(alreadyUsed))
                {
                    segments.add(segment);
                } 

            }
            otherTileSegments.clear(); 
        }

        ArrayList<Line> lines = new ArrayList<Line>();
        for (double[] segment: segments)
        {
            Line line = new Line(segment[0], segment[1], segment[2], segment[3]);
            line.setStroke(Color.WHITE);
            line.setStrokeWidth(2);
            lines.add(line);
        }
        return lines;
    }
    public void setCapital(HexTile tile) // manually sets capital in the tile
    {
        if (tile.getPlayer() == player)
        {
            tile.setContents(new Capital(tile));
        }
        this.capital = tile;
        hasCapital = true;
    }
    public HexTile getCapital()
    {
        return capital;
    }
    public void generateNewCapital() // algorithmically creates a new capital in a valid spot
    {  
        ArrayList<HexTile> validTiles = new ArrayList<HexTile>();
        for (HexTile tile: this)
        {
            if (tile.getContents() instanceof EmptyTile)
            {
                validTiles.add(tile);
            }
        }
        if (validTiles.size() > 0) // if there are empty tiles
        {
            setBestCapitalTile(validTiles);
        } else
        {
            for (HexTile tile: this)
            {
                if (tile.getContents() instanceof Tree)
                {
                    validTiles.add(tile);
                }
            }
            
            if (validTiles.size() > 0) // if there are treed tiles
            {
                setBestCapitalTile(validTiles);
            } else 
            {
                for (HexTile tile: this)
                {
                    if (tile.getContents() instanceof Castle)
                    {
                        validTiles.add(tile);
                    }
                }
                
                if (validTiles.size() > 0) // if there are castle tiles
                {
                    setBestCapitalTile(validTiles);
                } else { // there must be only troops ig LOL
                    for (HexTile tile: this)
                    {
                        validTiles.add(tile);
                    }
                    if (validTiles.size() > 0) // if there are castle tiles
                    {
                        setBestCapitalTile(validTiles);
                    }
                }
            }

        }
    }
    private void setBestCapitalTile(ArrayList<HexTile> usableTiles)
    {
        int avgXYSUM = (int) usableTiles.stream().mapToInt(tile -> tile.getX() + tile.getY()).average().getAsDouble(); // optimal tile is that closest to the center
        HexTile bestTile = usableTiles.get(0);
        for (HexTile tile: usableTiles.subList(1, usableTiles.size()))
        {
            if (Math.abs((tile.getX() + tile.getY()) - avgXYSUM) < Math.abs((bestTile.getX() + bestTile.getY())  - avgXYSUM))
            {
                bestTile = tile;
            }
        }
        setCapital(bestTile);
    }

    public ArrayList<HexTile> getHostileTileNeighbors() // returns all hostile tiles adjacent to this territory
    {
        ArrayList<HexTile> neighbors = new ArrayList<HexTile>();
        for (HexTile tile: this)
        {
            for (HexTile neighbor: tile.getNeighbors())
            {
                if ((neighbor.getPlayer() != player) && !(neighbors.contains(neighbor)))
                {
                    neighbors.add(neighbor);
                }
            }
        }
        return neighbors;
    }
    public ArrayList<HexTile> getFriendlyTileNeighbors() // returns all friendly tiles adjacent to this territory but not in this territory
    {
        ArrayList<HexTile> neighbors = new ArrayList<HexTile>();
        for (HexTile tile: this)
        {
            for (HexTile neighbor: tile.getNeighbors())
            {
                if ((neighbor.getPlayer() == player) && !(neighbors.contains(neighbor) && !(this.contains(neighbor))))
                {
                    neighbors.add(neighbor);
                }
            }
        }
        return neighbors;
    }
    public void absorbAdjacentFriendlyTerritories()
    {
        ArrayList<Territory> friendlyAdjacentTerritories = new ArrayList<Territory>();
        for (HexTile tile: getFriendlyTileNeighbors())
        {
            if (!(friendlyAdjacentTerritories.contains(tile.getTerritory())) && (tile.getTerritory() != this))
            {
                friendlyAdjacentTerritories.add(tile.getTerritory());
            }

        }
        for (int i = 0; i < friendlyAdjacentTerritories.size(); i++)
        {
            absorbTerritory(friendlyAdjacentTerritories.get(i));
        }
    }
    public int getPlayer()
    {
        return player;
    }
    public void newTroopAtTile(HexTile tile, int level) // spawning in new troops
    {
        if (this.contains(tile))
        {
            if ((tile.getContents() instanceof Troop) && (tile.getContents().getProtectionLevel()+level < 5))
            {
                tile.getContents().protectionLevel += level;
                this.money -= 10;
                unHighlightConquerableTiles();
            }
            else if (tile.getContents() instanceof Tree)
            {
                tile.setContents(new Troop(tile, level, false));
                this.money -= 10;
                unHighlightConquerableTiles();
            }
            else if (tile.getContents() instanceof EmptyTile)
            {
                tile.setContents(new Troop(tile, level, true));
                this.money -= 10;
                unHighlightConquerableTiles();
            }
        }
        else if ((getHostileTileNeighbors().contains(tile)) && (tile.getContents().getProtectionLevel() < level) && !(tile.isWater)) // hostile tile and significantly strong troop
        {
            tile.getTerritory().remove(tile);
            if (tile.getTerritory().size() > 0)
            {
                container.adjustTerritory(tile.getTerritory());
            }
            tile.setPlayer(player);
            this.addTile(tile);
            tile.setContents(new Troop(tile, level, false));
            this.money -= 10;
            tile.container.setCurrentTerritory(this);
            tile.getTerritory().absorbAdjacentFriendlyTerritories();
            unHighlightConquerableTiles();
        }
        for (HexTile hexTile: this)
        {
            hexTile.draw();
        }
        this.container.level.topBar.adjustPlayerDisplay();
        this.container.level.topBar.adjustLevelPreview();
    }
    public void highlightConquerableTiles(int level)
    {
        for (HexTile tile: getHostileTileNeighbors())
        {
            if (!(tile.isWater) && (tile.getProtectionLevel() < level))
            {
                tile.draw();
                tile.hex.setStroke(Color.RED);
                tile.hex.setStrokeWidth(2);
                highlightedTiles.add(tile);
            }
        }
    }
    public void unHighlightConquerableTiles()
    {
        for (HexTile tile: highlightedTiles)
        {
            tile.hex.setStroke(Color.BLACK);
            tile.hex.setStrokeWidth(1);
            tile.draw();
        }
    }

}