import java.io.IOException;
import java.net.MalformedURLException;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class Adventure extends Application
{
	public static Stage primaryStage;
	
	private Map map;
	private char[][] gridMap;
	private GameChar game;
	
	private TextArea textArea;
	private TextField textField;
	
	private FlowPane flow;
	
	@Override
	public void start(Stage primaryStage)
	{
		
		
//		System.out.println("Parameters at start method:" + this.getParameters());
//		
//		for(String s : this.getParameters().getRaw())
//		{
//			System.out.println("Parameters at start method:" + s);
//		}
		
		try
		{
			Adventure.primaryStage = primaryStage;
			
			map = new Map("map.txt");
			gridMap = map.getMap();
			
			
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,735,500);
			
			root.setTop(addHTopBox());
			root.setBottom(addHBottomBox());
			root.setLeft(addHLeftBox());
			root.setRight(addVRightBox());
			
			game = new GameChar(primaryStage, gridMap, map.getRows(), 
					map.getColumns(), map.getHeight(), 
					map.getWidth(), map.getFileLinesCharacters(), 
					map.getFileLinesItems(), textArea, flow);
			
		
			primaryStage.setScene(scene);
			primaryStage.show();
			
			this.printMap();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void printMap()
    {
    	textArea.appendText("***************************MAP***************************\n");
    	
    	for( int r = 0; r < map.getRows(); r++ )
        {
    		for( int c = 0; c < map.getColumns(); c++ )
            {
    			textArea.appendText(gridMap[r][c] + "\t");
            }
    		textArea.appendText("\n");
        }
    	
    	textArea.appendText("\n");
    }
	
	private Node addHLeftBox() throws MalformedURLException
	{
		flow = new FlowPane();
	    flow.setPadding(new Insets(15, 12, 15, 12));
	    flow.setPrefWrapLength(340); 
	    flow.setStyle("-fx-background-color: DAE6F3;");

	    return flow;
	}

	private Node addVRightBox()
	{
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(15, 12, 15, 12));
		vbox.setSpacing(10);
		vbox.setStyle("-fx-background-color: gray;");
	    
	    textArea = new TextArea();
	    textArea.setPrefColumnCount(25);
	    textArea.setPrefRowCount(25);
	    
	    vbox.getChildren().addAll(textArea);
	    
	    return vbox;
	}

	private Node addHBottomBox()
	{
		HBox hbox = new HBox();
	    hbox.setPadding(new Insets(15, 12, 15, 12));
	    hbox.setSpacing(10);
	    hbox.setStyle("-fx-background-color: #336699;");
	    
	    textField = new TextField();
	    textField.setPrefColumnCount(100);
	    
	    textField.setOnAction(e -> {	    	
			String str = textField.getText();
			textField.clear();
			
			// Send commands to the Game and execute them
			try
			{
				game.go(str);
			}
			catch (MalformedURLException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
	    
	    hbox.getChildren().addAll(textField);
	    
	    return hbox;
	}

	private Node addHTopBox()
	{
		HBox hbox = new HBox();
	    hbox.setPadding(new Insets(15, 12, 15, 12));
	    hbox.setSpacing(10);
	    hbox.setStyle("-fx-background-color: #336699;");

	    Button buttonSave = addButtonSave(new Button("Save"));
	    Button buttonOpen = addButtonOpen(new Button("Open"));
	    Button buttonQuit = addButtonQuit(new Button("Quit"));
	    
	    hbox.getChildren().addAll(buttonSave, buttonOpen, buttonQuit);
	    
	    return hbox;
	}

	private Button addButtonQuit(Button button)
	{
		button.setPrefSize(100, 20);
	    
		button.setOnAction(value ->  {
			primaryStage.close();
        });
	    
		return button;
	}

	private Button addButtonOpen(Button button)
	{
		button.setPrefSize(100, 20);
	    
		button.setOnAction(value ->  {
			textArea.appendText("Clicking button Open!!\n");
			game.openSavedGame();
        });
	    
		return button;
	}

	private Button addButtonSave(Button button)
	{
		button.setPrefSize(100, 20);
	    
		button.setOnAction(value ->  {
			textArea.appendText("Clicking button Save!!\n");
			game.saveGame();
        });
	    
		return button;
	}

	public static void main(String[] args) throws IOException
	{
		if (args.length < 1) 
		{
		    throw new IOException("No Parameters" );
		} 
		else 
		{
		    System.out.print("\n" + args.length + " Parameters: ");
		    int i = 0;
		    		
		    for (i = 0; i < args.length; i++)
		    {
		        System.out.print(args[i] + "\n");
		    }
		    
		    launch(args);
		}
		
	}
}
