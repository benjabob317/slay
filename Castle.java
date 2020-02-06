import javafx.scene.image.ImageView;

public class Castle extends TileContent
{
    public Castle(HexTile tile)
    {
        super(tile);
        this.protectionLevel = 2;
    }
    public ImageView getImage()
    {
        return new ImageView("./assets/castle.png");
    }
}