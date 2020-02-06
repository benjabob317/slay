import javafx.scene.image.ImageView;

public class Gravestone extends TileContent
{
    public Gravestone(HexTile tile)
    {
        super(tile);
        this.protectionLevel = 0;
    }
    public ImageView getImage()
    {
        return new ImageView("./assets/graveyard.png");
    }
}