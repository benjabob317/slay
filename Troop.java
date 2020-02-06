import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class Troop extends TileContent
{
    public int moves;
    public Troop(HexTile tile, int protectionLevel)
    {
        super(tile);
        this.protectionLevel = protectionLevel;
        this.moves = 1;
    }
    public ImageView getImage()
    {
        String file = "";
        
        if (protectionLevel == 1) {
            file = "./assets/peasant.png";
        } else if (protectionLevel == 2) {
            file = "./assets/spearman.png";
        } else if (protectionLevel == 3) {
            file = "./assets/knight.png";
        } else if (protectionLevel == 4) {
            file = "./assets/baron.png";
        }

        ImageView iv = new ImageView(file);
        iv.setOnMouseEntered( e -> {
            tile.hex.setFill(Color.color(tile.hex.red*.9, tile.hex.green*.9, tile.hex.blue*.9));
        });
        iv.setOnMouseExited( e -> {
            tile.hex.setFill(tile.hex.getColor());
        });
        iv.setOnMouseClicked( e -> {
            
            if (this.moves > 0)
            {
                tile.container.level.troopPickedUp = true;
                tile.container.level.heldTroopSource = tile;
            }
        });
        return iv;
    }

    public String getType()
    {
        return "Troop";
    }
    public void moveToTile(HexTile newTile) // moves troop to new tile
    {
        if ((newTile.getPlayer() == this.tile.getPlayer()) && (newTile != this.tile) && (newTile.getTerritory() == this.tile.getTerritory())) // if a friendly tile
        {

            if ((newTile.getContents().getProtectionLevel() + this.protectionLevel < 5) && (newTile.getContents() instanceof Troop) ) // if the tile has a troop of an appropriate level
            {
                newTile.getContents().setProtectionLevel(newTile.getContents().getProtectionLevel() + this.protectionLevel); // merges troops

                this.tile.setContents(new EmptyTile(this.tile));
                this.tile.draw(); // clears old tile
            }
            else if (newTile.getContents() instanceof Tree) //if the tile has a tree
            {
                this.tile.setContents(new EmptyTile(this.tile));
                this.tile.draw(); // clears old tile
                
                setTile(newTile); // moves troop to new tile
                newTile.setContents(this);
                newTile.draw();
                moves = 0;
            }
            else if (newTile.getContents() instanceof EmptyTile) // if the tile is empty
            {
                this.tile.setContents(new EmptyTile(this.tile));
                this.tile.draw(); // clears old tile
                
                setTile(newTile); // moves troop to new tile
                newTile.setContents(this);
                newTile.draw();
            }
        }
        else if ((newTile.isWater == false) && (newTile.getProtectionLevel() < this.protectionLevel) && (this.tile.getTerritory().getHostileTileNeighbors().contains(newTile))) // if a hostile land tile and the troop is strong enough
        {
            if (newTile.getContents() instanceof Capital)
            {
                newTile.getTerritory().hasCapital = false;
            }
            newTile.setPlayer(tile.getPlayer());
            this.tile.getTerritory().addTile(newTile); // conquers tile for the territory
           
            
            this.tile.setContents(new EmptyTile(this.tile));
            this.tile.draw(); // clears old tile

            setTile(newTile);
            newTile.setContents(this);
            newTile.draw();

            newTile.container.setCurrentTerritory(tile.getTerritory());
            newTile.getTerritory().absorbAdjacentFriendlyTerritories();

            moves = 0;

        }
    }
    public void rechargeMove()
    {
        this.moves = 1;
    }
}