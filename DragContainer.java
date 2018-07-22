package application;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javafx.geometry.Point2D;
import javafx.scene.input.DataFormat;
import javafx.util.Pair;

public class DragContainer<T> implements Serializable{
    
    /**
     * 
     */
	public static final DataFormat AddLink =
		    new DataFormat("application.NodeLink.add");
	public static final DataFormat DragNode = 
		    new DataFormat("application.DraggableNode.drag");
	//public static final DataFormat DragNode = 
	//	    new DataFormat("application.DragIcon.drag");
	 public static final DataFormat AddNode = 
	            new DataFormat("application.DragIcon.add");


    private static final long serialVersionUID = -1458406119115196098L;

    private final List<Pair<String,String>> mDataPairs = new ArrayList <Pair<String, String> > ();
    
    public static final DataFormat Binding = 
            new DataFormat("com.buddyware.treefrog.filesystem.view.FileSystemBinding");
    
    public static final DataFormat Node =
            new DataFormat("com.buddyware.treefrog.filesystem.view.FileSystemNode");


    public DragContainer () {
   
    }
    
    public void addData (Object key, Object point2D) {
        mDataPairs.add((Pair<String, String>) new Pair<String, String>(String.valueOf(key), String.valueOf(point2D)));        
    }
    
    public  Object getValue (Object key) {
 
        for (Pair<String, String> data: mDataPairs) {
 
        if (data.getKey().equals(key))
            return (T) data.getValue();
 
         }
 
        return null;
    }
    
    public List<Pair<String,String>> getData () { return mDataPairs; }




}