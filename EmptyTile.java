import javafx.scene.image.ImageView;

public class EmptyTile extends TileContent
{
    public EmptyTile(HexTile tile)
    {
        super(tile);
        this.protectionLevel = 0;
    }
    public ImageView getImage()
    {
        return new ImageView();
    }
}