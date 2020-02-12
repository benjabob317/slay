import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class Capital extends TileContent
{
    public Capital(HexTile tile)
    {
        super(tile);
        this.protectionLevel = 1;

    }
    public ImageView getImage()
    {
        ImageView iv = new ImageView("./assets/capital.png");
        if ((tile.getTerritory().money >= 10) && (tile.getPlayer() == tile.container.level.currentPlayer))
        {
            iv = new ImageView("./assets/flag_capital.gif");
        }
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
            }


        });
        return iv;
    }
}