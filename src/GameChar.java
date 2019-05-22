import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class GameChar
{
	Stage primaryStage;
	private char[][] map;
	int[][] deltas = {{0,1}, {0,-1}, {-1,0}, {1,0}, {0,0}};
	private int rows;
	private int columns;
	private int rowPosition = 0;
	private int colPosition = 0;
	
	private double height;
	private double width;
	List<String[]> fileLinesCharacters;
	List<String[]> fileLinesItems;
	private TextArea textArea;
	private FlowPane flow;
	private List<String> inventory;
	
	private File InvFileName;
	private File ItemsFileName;
	
	
	public GameChar(Stage primaryStage, char[][] map, int r, int c, double h, double w, List<String[]> cs, List<String[]> is, TextArea textArea, FlowPane flow) throws MalformedURLException
	{
		this.primaryStage = primaryStage;
		this.map = map;
		this.rows = r;
		this.columns = c;
		this.height = h;
		this.width = w;
		this.fileLinesCharacters = cs;
		this.fileLinesItems = is;
		this.flow = flow;
		this.textArea = textArea;
		this.showTerrain();
		
		inventory = new ArrayList<String>();
		inventory.add("brass lantern");
		inventory.add("rope");
		inventory.add("rations");
		inventory.add("staff");
	}
	
	public void go(String str) throws MalformedURLException
	{
		int[][] currentDelta = new int[1][2];
		String command;
    	String parameter;
    	str = str.toLowerCase();
        String[] arrOfStr = str.split(" +");
	
    	if(arrOfStr.length == 1) {
        	command = arrOfStr[0];
        	
        	if(command.equals("inventory") || command.equals("i"))
        	{
        		showInventory();
        	}
        	else if(command.equals("quit") || command.equals("q")) 
        	{
        	    textArea.appendText("Farewell\n");
        	    textArea.appendText("You are at location " + rowPosition + "," + colPosition + "\n");
	    	}
        	else 
        	{
	        	textArea.appendText("\nInvalid command: " + command + "\n");
	        }
        }
    	else if(arrOfStr.length >= 2) 
        {
    		command = arrOfStr[0];
    		parameter = "";
    		
    		if(arrOfStr.length >= 2)
    		{
    			for(int i = 1; i < arrOfStr.length; i++)
    			{
    				parameter += arrOfStr[i] + " ";
    			}
    			parameter = parameter.trim();
    		}
	        
	        if(command.equals("go") || command.equals("g"))
	        {
		    	currentDelta = getDirection(parameter);
		    	
		    	if(currentDelta[0][0] == 0 && currentDelta[0][1] == 0) 
		    	{
		    		//System.out.println("Do nothing");
		    	}
		    	else 
		    	{
		    		rowPosition = rowPosition + currentDelta[0][0];
			    	colPosition = colPosition + currentDelta[0][1];
			    	
		        	if( (rowPosition >= 0 && rowPosition < rows) && (colPosition >=0 && colPosition < columns))
					{
						textArea.appendText("\nMoving " + parameter + "...\n");
						textArea.appendText("You are at location " + rowPosition + "," + colPosition + " in terrain " + map[rowPosition][colPosition] + "\n");
						showTerrain();
						containItem(rowPosition, colPosition);
					}
		        	else 
		        	{
		        		rowPosition = rowPosition - currentDelta[0][0];
				    	colPosition = colPosition - currentDelta[0][1];
		    			textArea.appendText("\nYou can't go that far " + parameter + ".\n");
		    		}
		    	}
	        }
	        else if(command.equals("take") || command.equals("t"))
	        {
	        	addItemToInventory(rowPosition, colPosition, parameter);
	        	textArea.appendText("You are at location " + rowPosition + "," + colPosition + " in terrain " + map[rowPosition][colPosition] + "\n");
	        }
	        else if(command.equals("drop") || command.equals("d"))
	        {
	        	dropItemToInventory(rowPosition, colPosition, parameter);
	        	textArea.appendText("You are at location " + rowPosition + "," + colPosition + " in terrain " + map[rowPosition][colPosition] + "\n");
	        }
	        else 
	        {
	        	textArea.appendText("\nInvalid command: " + command + "\n");
	        }
        }
	}
	
	public void addItemToInventory(int row, int col, String item)
	{
		for(int index = 0; index < fileLinesItems.size(); index++)
		{
			int itemRow = Integer.parseInt(fileLinesItems.get(index)[0]);
			int itemCol = Integer.parseInt(fileLinesItems.get(index)[1]);
			String itemName = fileLinesItems.get(index)[2];
			
			if(item.equals(itemName) && itemRow == row && itemCol == col)
			{
				textArea.appendText("\nTaking item: " + item + "\n");
				inventory.add(item);
				fileLinesItems.remove(index);
				 
				return;
			}
		}
		
		textArea.appendText("There is no Item in this location: " + item + ".\n");
	}
	
	public void dropItemToInventory(int row, int col, String item)
	{
		for(int index = 0; index < inventory.size(); index++)
		{	
			if(item.equals(inventory.get(index)))
			{
				textArea.appendText("\nDropping item: " + item + "\n");
				String[] newItem = new String[] {Integer.toString(row),Integer.toString(row),item};
				
				fileLinesItems.add(newItem);
				inventory.remove(index);
				textArea.appendText("\nDone: " + item + "\n");
				 
				return;
			}
		}
		
		textArea.appendText("There is no Item in the inventory: " + item + ".\n");
	}
	
	public ImageView getImage(String chrDes) throws MalformedURLException
	{
		int index = getCharacterDetails(chrDes);
		
		ImageDriver image = new ImageDriver(height, width, fileLinesCharacters.get(index));
		image.open();
		
		return image.getImage();
	}
	
	public int getCharacterDetails(String character)
	{
		int index;
		
		for(index = 0; index < fileLinesCharacters.size(); index++)
		{
			if(character.equals(fileLinesCharacters.get(index)[0]))
			{
				 return index;
			}
		}
		return -1;
	}
	
	public void showTerrain() throws MalformedURLException
	{
		int tempRowPos = rowPosition - 2;
		int tempColPos = colPosition - 2;
		flow.getChildren().clear();
		
		for( int r = tempRowPos; r < tempRowPos + 5; r++ )
        {
    		for( int c = tempColPos; c < tempColPos + 5; c++ )
            {
    			if( (r < 0 || r >= rows) || (c < 0 || c >= columns))
    			{
    				// out of bounds
    				flow.getChildren().addAll(getImage("-"));
    				//textArea.appendText("X\t");
    			}
    			else if( (r >= 0 && r < rows) && (c >=0 && c < columns))
    			{
    				flow.getChildren().addAll(getImage(Character.toString(map[r][c])));
    				//textArea.appendText(map[r][c] + "\t");
    			}
            }
    		//textArea.appendText("\n");
        }
		
	}
	
	public void containItem(int row, int col)
	{
		int index;
		
		for(index = 0; index < fileLinesItems.size(); index++)
		{
			int itemRow = Integer.parseInt(fileLinesItems.get(index)[0]);
			int itemCol = Integer.parseInt(fileLinesItems.get(index)[1]);
			String item = fileLinesItems.get(index)[2];
			
			if(itemRow == row && itemCol == col)
			{
				 System.out.println("Item found: " + item);
				 textArea.appendText("Item found: " + item + ".\n");
			}
		}
	}
	
	public int[][] getDirection(String parameter)
	{
		int[][] currentDelta = new int[1][2];
		
		if(parameter.equals("east"))
    	{
			currentDelta[0][0] = deltas[0][0];
			currentDelta[0][1] = deltas[0][1];
			
    		return currentDelta;
    	}
    	else if(parameter.equals("west"))
    	{
    		currentDelta[0][0] = deltas[1][0];
			currentDelta[0][1] = deltas[1][1];
			
    		return currentDelta;
        }
    	else if(parameter.equals("north"))
        {
    		currentDelta[0][0] = deltas[2][0];
			currentDelta[0][1] = deltas[2][1];
			
    		return currentDelta;
        }
    	else if(parameter.equals("south"))
        {
    		currentDelta[0][0] = deltas[3][0];
			currentDelta[0][1] = deltas[3][1];
			
    		return currentDelta;
        }
    	else if(parameter.equals("inside"))
        {
    		currentDelta[0][0] = deltas[4][0];
			currentDelta[0][1] = deltas[4][1];
        	textArea.appendText("You can't go that way.\n");
        }
    	else 
        {
    		currentDelta[0][0] = deltas[4][0];
			currentDelta[0][1] = deltas[4][1];
        	textArea.appendText("\nInvalid parameter: " + parameter + "\n");
        }
		
		return currentDelta;
	}
	
	public void showInventory()
	{
    	textArea.appendText("\nYou are carrying:\n");
    	
    	for(int i = 0; i < inventory.size(); i++)
    	{
    		textArea.appendText("- " + inventory.get(i) + "\n");
    	}
    	
    	textArea.appendText("You are at location " + rowPosition + "," + colPosition + "\n");	
	}
	
	public void showItems()
	{
    	textArea.appendText("\nItems in the map:\n");
    	
    	for(String[] s: fileLinesItems)
    	{
    		textArea.appendText("Row: " + s[0] + " Column: " + s[1] + " Description: " + s[2] + "\n");
    	}
    	
//    	textArea.appendText("You are at location " + rowPosition + "," + colPosition + "\n");	
	}
	
	public void saveGame()
	{
		textArea.appendText("\nSaving game from Game class!\n");	
		// Need to save Inventory, Items, Current Position
		
		try
		{
	        
	        if(InvFileName != null)
	        {
	        	ObjectOutputStream oosInv = new ObjectOutputStream(new FileOutputStream(InvFileName));
				ObjectOutputStream oosItems = new ObjectOutputStream(new FileOutputStream("itemsSaved.txt"));
	        	oosInv.writeObject(inventory);
				oosItems.writeObject(fileLinesItems);
				System.out.println("Writing Inventory to a File!");
	        }
	        else
	        {
	        	FileChooser fileChooser = new FileChooser();
	        	 
	            //Set extension filter for text files
	            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
	            fileChooser.getExtensionFilters().add(extFilter);
	 
	            //Show save file dialog
	            InvFileName = fileChooser.showSaveDialog(primaryStage);
	            InvFileName.getPath();
	            InvFileName.getAbsolutePath();
	            ItemsFileName = InvFileName;
	            ItemsFileName.renameTo(new File("itemsSaved.txt"));
	           
	            ObjectOutputStream oosInv = new ObjectOutputStream(new FileOutputStream(InvFileName));
				ObjectOutputStream oosItems = new ObjectOutputStream(new FileOutputStream("itemsSaved.txt"));
	        	oosInv.writeObject(inventory);
				oosItems.writeObject(fileLinesItems);
				System.out.println("Writing Inventory to a File!");
	 
	            textArea.appendText("\nfile from the dialog: " + InvFileName + "\n");
	            textArea.appendText("\nPath: " + InvFileName.getAbsolutePath() + "\n");
//	            if (file != null) {
//	                saveTextToFile(sampleText, file);
//	            }
	        }
			

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		textArea.appendText("You are at location " + rowPosition + "," + colPosition + "\n");
		
	}
	
	public void openSavedGame()
	{
		textArea.appendText("\nOpenning saved game from A file!\n");
		try
		{
			FileChooser fileChooser = new FileChooser();
       	 
            //Set extension filter for text files
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);
 
            //Show save file dialog
            InvFileName = fileChooser.showOpenDialog(primaryStage);
            
            FileInputStream fileInInv = new FileInputStream(InvFileName);
            ObjectInputStream objectInInv = new ObjectInputStream(fileInInv);
            List<String> objInv = (List<String>) objectInInv.readObject();
            
            inventory.clear();
            inventory = objInv;
            showInventory();
            
            FileInputStream fileInItems = new FileInputStream("itemsSaved.txt");
            ObjectInputStream objectInItems = new ObjectInputStream(fileInItems);
            List<String[]> objItems = (List<String[]>) objectInItems.readObject();
            
            fileLinesItems.clear();
            fileLinesItems = objItems;
            showItems();
 
            System.out.println("The Object has been read from the file");
            objectInInv.close();
            objectInItems.close();
            
 
        } catch (Exception ex) {
            ex.printStackTrace();
            
        }
		textArea.appendText("Saved Game is loaded!\n");
	}
}
