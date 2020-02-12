import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;


public class Tree extends TileContent
{
    public Tree(HexTile tile)
    {
        super(tile);
        this.protectionLevel = 0;
    }
    public ImageView getImage()
    {
        ImageView iv = new ImageView("./assets/tree.png");
        iv.setOnMouseEntered( e -> {
                if (tile.container.level.currentPlayer == tile.getPlayer())
                {
                    tile.hex.setFill(Color.color(tile.hex.red*.9, tile.hex.green*.9, tile.hex.blue*.9));
                }
            });
        iv.setOnMouseExited( e -> {
            if (tile.container.level.currentPlayer == tile.getPlayer())
            {
                tile.hex.setFill(tile.hex.getColor());
            }
        });
        iv.setOnMouseClicked( e -> {
            if (tile.container.level.currentPlayer == tile.getPlayer())
            {
                if (tile.getTerritory().size() > 1) {
                    tile.container.setCurrentTerritory(tile.getTerritory());
                }
            }
            else if (tile.container.level.troopPickedUp == true)
            {
                ((Troop) tile.container.level.heldTroopSource.getContents()).moveToTile(tile);
                tile.draw();
                tile.container.level.troopPickedUp = false;
            } else if ((tile.container.level.troopSpawnedIn) && (tile.container.currentTerritory.getHostileTileNeighbors().contains(tile)) && (tile.getProtectionLevel() == 0))
            {
                tile.container.currentTerritory.newTroopAtTile(tile, 1);
                tile.container.level.hideHand();
                tile.container.level.troopSpawnedIn = false;
            }

        });
        return iv;
    }
    public String getType()
    {
        return "Tree";
    }
}