import java.net.MalformedURLException;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ImageDriver
{
	Stage primaryStage;
	private Image mainImage;
	private ImageView imageToShow = new ImageView();
	private double height;
    private double width;
    private String[] character;
//    private int characterPos = 0;
//    private int descriptionPos = 1;
    private int filePathPos = 2;
	
	public ImageDriver(Stage primaryStage, double height, double width, String[] character)
	{
		this.primaryStage = primaryStage;
		this.height = height;
		this.width = width;
		this.character = character;
	}
	
	public ImageDriver(double height, double width, String[] character)
	{
		this.height = height;
		this.width = width;
		this.character = character;
	}
	
	public void open() throws MalformedURLException
	{
        mainImage = new Image(character[filePathPos]);
        setImage(mainImage);
	}
	
	public void setImage(Image img)
	{
        imageToShow.setImage(img);
        imageToShow.setFitHeight(height);
        imageToShow.setFitWidth(width);
    }
	
	public ImageView getImage()
	{
		 return imageToShow;
	}
}
