import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class Map 
{
	private Scanner fileIn = null;
	private Scanner fileItemsIn = null;
	private String fileMapName;
	private String itemsFileName;
	List<String> fileLines;
	private char[][] map;
	private int rows;
	private int columns;
	
	private double height;
	private double width;
	List<String[]> fileLinesCharacters;
	List<String[]> fileLinesItems;
	
	public Map(String fileMapName) throws IOException
	{
		this.fileMapName = fileMapName;
		openFile(this.fileMapName);
		readMapSize();
		readMap();
		readImageSize();
		readItemsFileName();
		openItemsFile(this.itemsFileName);
		readItems();
		readCharacters();
		setMapDimensions();
    	createMap();
	}
	
	private void openFile(String fileName)
    {
    	System.out.println("Map File: " + fileName);
    	try {
    		fileIn = new Scanner(new File(fileName));
		} catch (FileNotFoundException x) {
			System.out.println("File open failed.");
			x.printStackTrace();
			System.exit(0);   // TERMINATE THE PROGRAM
		}
    }
	
	private void openItemsFile(String fileName)
    {
    	System.out.println("Items File: " + fileName);
    	try {
    		fileItemsIn = new Scanner(new File(fileName));
		} catch (FileNotFoundException x) {
			System.out.println("File open failed.");
			x.printStackTrace();
			System.exit(0);   // TERMINATE THE PROGRAM
		}
    }
	
	public void readItemsFileName() throws IOException
	{
		if( fileIn.hasNextLine() == false )
            throw new IOException("No lines in map file" );

		itemsFileName = fileIn.nextLine();
		System.out.println("Items File Name: " + itemsFileName);
        
	}
	
	public void readItems()
	{
		String line;
		fileLinesItems = new ArrayList<String[]>();

    	while(fileItemsIn.hasNextLine())
        {
        	line = fileItemsIn.nextLine();
        	String[] itemStr = line.split(";");
        	fileLinesItems.add(itemStr);
        	System.out.println("Row: " + itemStr[0] + " Column: " + itemStr[1] + " Item: " + itemStr[2]);
        }
	}
	
	public void readMapSize() throws IOException
	{
		String line;
		
		if( fileIn.hasNextLine() == false )
            throw new IOException("No lines in map file" );

        line = fileIn.nextLine();
        String[] mapSize = line.split(" +");

    	for (int i = 0; i < mapSize.length; i++) {
    		if(i == 0 && i < 2) 
    		{
    			setNumberRows(Integer.parseInt(mapSize[i]));
    		}
    		else if(i == 1 && i < 2)
    		{
    			setNumberColumns(Integer.parseInt(mapSize[i]));
    		}
    	}
	}
	
	public void readMap()
	{
		String line;
        fileLines = new ArrayList<String>();

        for(int i = 0; i < rows; i++)
        {
        	line = fileIn.nextLine();
        	
            if( line.length( ) == columns && fileLines.size() < rows) 
            {
            	fileLines.add(line);
            }
            else if(line.length() != columns)
            {
            	System.err.println( "Map is not rectangular; skipping row" );
            }
        }
	}
	
	public void readImageSize() throws IOException
	{
		String line;
		
		if( fileIn.hasNextLine() == false )
            throw new IOException("No lines in map file" );

        line = fileIn.nextLine();
        String[] imageSizeStr = line.split(" +");

    	for (int i = 0; i < imageSizeStr.length; i++)
    	{
    		if(i == 0 && i < 2) 
    		{
    			setImageHeight(Integer.parseInt(imageSizeStr[i]));
    		}
    		else if(i == 1 && i < 2)
    		{
    			setImageWidth(Integer.parseInt(imageSizeStr[i]));
    		}
    	}
	}
	
	public void readCharacters()
	{
		String line;
		fileLinesCharacters = new ArrayList<String[]>();

    	while(fileIn.hasNextLine())
        {
        	line = fileIn.nextLine();
        	String[] charaterStr = line.split(";");
        	fileLinesCharacters.add(charaterStr);
        	System.out.println("Character: " + charaterStr[0] + " Description: " + charaterStr[1] + " File: " + charaterStr[2]);
        }
	}
	
	public void createMap()
	{
		Iterator<String> itr = fileLines.iterator();
        
        for( int r = 0; r < rows; r++ )
        {
            String theLine = (String) itr.next();
            map[r] = theLine.toCharArray();
        }
	}
	
	public void setNumberRows(int r)
	{
	       this.rows = r;
	       System.out.println("# ROW " + rows);
	}
	
	public void setNumberColumns(int c)
	{
	       this.columns = c;
	       System.out.println("# COLUMN " + columns);
	}
	
	public void setImageHeight(int h)
	{
	       this.height = h;
	       System.out.println("# Image height size: " + height);
	}
	
	public void setImageWidth(int w)
	{
	       this.width = w;
	       System.out.println("# Image width size: " + width);
	}
	
	public void setMapDimensions()
	{
		map = new char[rows][columns];
	    System.out.println("....Setting Map Dimensions....\n");
	}
	
	public int getRows()
	{
		return rows;
	}
	
	public int getColumns()
	{
		return columns;
	}
	
	public char[][] getMap()
	{
		return map;
	}
	
	public double getHeight()
	{
		return height;
	}
	
	public double getWidth()
	{
		return width;
	}
	
	public List<String[]> getFileLinesCharacters()
	{
		return fileLinesCharacters;
	}
	
	public List<String[]> getFileLinesItems()
	{
		return fileLinesItems;
	}
	
	public void printMap()
    {
    	System.out.println("**********Map**********\n");
    	
    	for( int r = 0; r < rows; r++ )
        {
    		for( int c = 0; c < columns; c++ )
            {
    			System.out.print(map[r][c] + " ");
            }
    		System.out.println("\n");
        }
    }
}
