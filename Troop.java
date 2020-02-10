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
    public Troop(HexTile tile, int protectionLevel, boolean moveRemaining) // useful for spawning in new troops
    {
        super(tile);
        this.protectionLevel = protectionLevel;
        if (moveRemaining == false)
        {
            this.moves = 0;
        } else {
            this.moves = 1;
        }
    }
    public ImageView getImage()
    {
        String file = "";
        
        if (protectionLevel == 1) {
            if (moves > 0) {
                file = "./assets/animated_peasant.gif";
            } else {
                file = "./assets/peasant.png";
            }
        } else if (protectionLevel == 2) {
            if (moves > 0) {
                file = "./assets/animated_spearman.gif";
            } else {
                file = "./assets/spearman.png";
            }
        } else if (protectionLevel == 3) {
            if (moves > 0) {
                file = "./assets/animated_knight.gif";
            } else {
                file = "./assets/knight.png";
            }
        } else if (protectionLevel == 4) {
            if (moves > 0) {
                file = "./assets/animated_baron.gif";
            } else {
                file = "./assets/baron.png";
            }
        }

        ImageView iv = new ImageView(file);
        iv.setOnMouseEntered( e -> {
            tile.hex.setFill(Color.color(tile.hex.red*.9, tile.hex.green*.9, tile.hex.blue*.9));
        });
        iv.setOnMouseExited( e -> {
            tile.hex.setFill(tile.hex.getColor());
        });
        iv.setOnMouseClicked( e -> {
            if (tile.container.level.troopPickedUp)
            {
                ((Troop) tile.container.level.heldTroopSource.getContents()).moveToTile(tile);
                this.tile.draw();
                tile.container.level.hideHand();
                tile.container.level.troopPickedUp = false;
            }
            else if (tile.container.level.troopSpawnedIn)
            {
                tile.container.currentTerritory.newTroopAtTile(tile, 1);
                tile.container.level.hideHand();
                tile.container.level.troopSpawnedIn = false;

            }
            else if (this.moves > 0)
            {
                tile.container.level.troopPickedUp = true;
                tile.container.level.heldTroopSource = tile;
                tile.container.level.stack.getChildren().add(new ImageView("./assets/hand.png"));
                iv.setOpacity(0);
                
                if (this.protectionLevel == 1) {
                    tile.container.level.stack.getChildren().add(new ImageView("./assets/peasant.png"));
                } else if (this.protectionLevel == 2) {
                    tile.container.level.stack.getChildren().add(new ImageView("./assets/spearman.png"));
                } else if (this.protectionLevel == 3) {
                    tile.container.level.stack.getChildren().add(new ImageView("./assets/knight.png"));
                } else if (this.protectionLevel == 4) {
                    tile.container.level.stack.getChildren().add(new ImageView("./assets/baron.png"));
                }

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
                this.tile.container.level.hideHand();
            }
            else if (newTile.getContents() instanceof Tree) //if the tile has a tree
            {
                this.tile.setContents(new EmptyTile(this.tile));
                this.tile.draw(); // clears old tile
                
                setTile(newTile); // moves troop to new tile
                newTile.setContents(this);
                newTile.draw();
                moves = 0;
                this.tile.container.level.hideHand();
            }
            else if (newTile.getContents() instanceof EmptyTile) // if the tile is empty
            {
                this.tile.setContents(new EmptyTile(this.tile));
                this.tile.draw(); // clears old tile
                
                setTile(newTile); // moves troop to new tile
                newTile.setContents(this);
                newTile.draw();
                this.tile.container.level.hideHand();
            }
        }
        else if ((newTile.isWater == false) && (newTile.getProtectionLevel() < this.protectionLevel) && (this.tile.getTerritory().getHostileTileNeighbors().contains(newTile))) // if a hostile land tile and the troop is strong enough
        {
            if (newTile.getContents() instanceof Capital)
            {
                newTile.getTerritory().hasCapital = false;
            }
            newTile.getTerritory().remove(newTile);
            if (newTile.getTerritory().size() > 0)
            {
                tile.container.adjustTerritory(newTile.getTerritory());
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
            this.tile.container.level.hideHand();
            newTile.container.level.topBar.adjustPlayerDisplay();

        }
    }
    public void rechargeMove()
    {
        this.moves = 1;
    }
}