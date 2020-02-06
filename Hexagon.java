import javafx.scene.shape.Polygon;
import javafx.scene.paint.Color;

public class Hexagon extends Polygon 
{
    private double centerX;
    private double centerY;
    private double sideLength;
    public double red;
    public double green;
    public double blue;
    private double strokeRed;
    private double strokeGreen;
    private double strokeBlue;

    public Hexagon(double centerX, double centerY, double sideLength, Color color, Color strokeColor)
    {
        super();

        this.centerX = centerX;
        this.centerY = centerY;
        this.sideLength = sideLength;
        this.red = color.getRed();
        this.green = color.getGreen();
        this.blue = color.getBlue();
        this.strokeRed = strokeColor.getRed();
        this.strokeGreen = strokeColor.getGreen();
        this.strokeBlue = strokeColor.getBlue();

        getPoints().addAll(new Double[]{
            centerX + sideLength*Math.cos(0), centerY - sideLength*Math.sin(0),
            centerX + sideLength*Math.cos(60.0*Math.PI/180.0), centerY - sideLength*Math.sin(60.0*Math.PI/180.0),
            centerX + sideLength*Math.cos(120.0*Math.PI/180.0), centerY - sideLength*Math.sin(120.0*Math.PI/180.0),
            centerX + sideLength*Math.cos(180.0*Math.PI/180.0), centerY - sideLength*Math.sin(180.0*Math.PI/180.0),
            centerX + sideLength*Math.cos(240.0*Math.PI/180.0), centerY - sideLength*Math.sin(240.0*Math.PI/180.0),
            centerX + sideLength*Math.cos(300.0*Math.PI/180.0), centerY - sideLength*Math.sin(300.0*Math.PI/180.0)});
        
        setFill(getColor());
        setStroke(strokeColor);
        setStrokeWidth(1);

    }

    public Color getColor()
    {
        return Color.color(red, green, blue);
    }
    public Color getStrokeColor()
    {
        return Color.color(strokeRed, strokeGreen, strokeBlue);
    }

}