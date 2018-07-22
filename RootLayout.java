package application;


import java.awt.MouseInfo;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
// 


public class RootLayout extends AnchorPane{
	
	static int a = 0;
	@FXML SplitPane base_pane;
    @FXML AnchorPane right_pane;
    @FXML VBox left_pane;
    private EventHandler<DragEvent> mIconDragOverRoot=null;
    private EventHandler<DragEvent> mIconDragDropped=null;
    private EventHandler<DragEvent> mIconDragOverRightPane=null;

    private DragIcon mDragOverIcon = null;
    
    public RootLayout() {

         FXMLLoader fxmlLoader = new FXMLLoader(
            getClass().getResource("/RootLayout.fxml")
        ); 
        
       // FXMLLoader fxmlLoader = new FXMLLoader("/RootLayout.fxml");

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
       
        try {
            fxmlLoader.load();

        } catch(LoadException e) {
        	e.printStackTrace();
        }catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
    @FXML
    private void initialize() {
    	//Add one icon that will be used for the drag-drop process
        //This is added as a child to the root AnchorPane so it can be 
        //visible on both sides of the split pane.
        mDragOverIcon = new DragIcon();
     
        mDragOverIcon.setVisible(false);
        mDragOverIcon.setOpacity(0.65);
        getChildren().add(mDragOverIcon); 
        
        buildDragHandlers();

        //populate left pane with multiple colored icons for testing
        for (int i = 0; i < 7; i++) {
     
        	DragIcon icn = new DragIcon();
            
            addDragDetection(icn);
                   
            icn.setType(DragIconType.values()[i]);
            left_pane.getChildren().add(icn);
            
            
    }
}
    private void addDragDetection(DragIcon dragIcon) {
    
        dragIcon.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                // set the other drag event handles on their respective objects
            base_pane.setOnDragOver(mIconDragOverRoot);
                right_pane.setOnDragOver(mIconDragOverRightPane);
                right_pane.setOnDragDropped(mIconDragDropped);
            
                // get a reference to the clicked DragIcon object
                DragIcon icn = (DragIcon) event.getSource();

                //begin drag ops
                mDragOverIcon.setType(icn.getType());
               mDragOverIcon.relocateToPoint(new Point2D (event.getSceneX(), event.getSceneY()));

                ClipboardContent content = new ClipboardContent();
                DragContainer<?> container = new DragContainer();

               
				container.addData ("type", mDragOverIcon.getType().toString());
                content.put(DragContainer.AddNode, container);

                mDragOverIcon.startDragAndDrop (TransferMode.ANY).setContent(content);
                mDragOverIcon.setVisible(true);
                mDragOverIcon.setMouseTransparent(true);
                event.consume();
            }
        });
    }
    private void buildDragHandlers() {
        
        //drag over transition to move widget form left pane to right pane
        mIconDragOverRoot = new EventHandler <DragEvent>() {

            @Override
            public void handle(DragEvent event) {
     
                Point2D p = right_pane.sceneToLocal(event.getSceneX(), event.getSceneY());

                if (!right_pane.boundsInLocalProperty().get().contains(p)) {
                   mDragOverIcon.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));
                    return;
                }

                event.consume();
            }
        };
     
        mIconDragOverRightPane = new EventHandler <DragEvent> () {

            @Override
            public void handle(DragEvent event) {

                event.acceptTransferModes(TransferMode.ANY);
     
               mDragOverIcon.relocateToPoint(
                       new Point2D(event.getSceneX(), event.getSceneY()));
                event.consume();
            }
        };
     
        mIconDragDropped = new EventHandler <DragEvent> () {

            @Override
            public void handle(DragEvent event) {

                DragContainer<?> container =
                    (DragContainer<?>) event.getDragboard().getContent(DragContainer.AddNode);

                container.addData("scene_coords",
                    new Point2D(event.getSceneX(), event.getSceneY()));

                ClipboardContent content = new ClipboardContent();
                content.put(DragContainer.AddNode, container);

                event.getDragboard().setContent(content);
                event.setDropCompleted(true);
            }
        };
      
        this.setOnDragDone (new EventHandler <DragEvent> (){

            @Override
            public void handle (DragEvent event) {

            right_pane.removeEventHandler(DragEvent.DRAG_OVER, mIconDragOverRightPane);
            right_pane.removeEventHandler(DragEvent.DRAG_DROPPED, mIconDragDropped);
            base_pane.removeEventHandler(DragEvent.DRAG_OVER, mIconDragOverRoot);

            mDragOverIcon.setVisible(false);

            DragContainer<?> container =
               (DragContainer<?>) event.getDragboard().getContent(DragContainer.AddNode);
            
                if (container != null) {
                	
                    if (container.getValue("scene_coords") != null) {
                            
                    	DraggableNode node = new DraggableNode();
                    	a++;
                    	
                    	
                       node.setType(DragIconType.valueOf(String.valueOf(container.getValue("type"))));
                        right_pane.getChildren().add(node);
                        
                        System.out.println(container.getValue("scene_coords"));
                        
                        String s = (String) container.getValue("scene_coords");
                        System.out.println(s);
                        Object c = s;
                        System.out.println(c);
                        java.awt.Point p = MouseInfo.getPointerInfo().getLocation(); 
                        double x = p.x;
                        double y = p.y;
                       
                       node.relocate(x-140, y-48);
                       System.out.println(a);
                       
                       
                       node.setTitle("R"+a);
                           
                    }
                    
                    
                }
              
                
                container =
                		(DragContainer<?>) event.getDragboard().getContent(DragContainer.DragNode);

                		if (container != null) {
                		    if (container.getValue("type") != null)
                		    System.out.println ("Moved node " + container.getValue("type"));
                		}
                		
                		//AddLink drag operation
                		container = (DragContainer<?>) event.getDragboard().getContent(DragContainer.AddLink);
                		                
                		if (container != null) {
                		                    
                		    //bind the ends of our link to the nodes whose id's are stored in the drag container
                		    String sourceId = (String) container.getValue("source");
                		    String targetId = (String) container.getValue("target");

                		    if (sourceId != null && targetId != null) {
                		                        
                		        //System.out.println(container.getData());
                		        NodeLink link = new NodeLink();
                		                        
                		        //add our link at the top of the rendering order so it's rendered first
                		        right_pane.getChildren().add(0,link);
                		                        
                		        DraggableNode source = null;
                		        DraggableNode target = null;
                		                        
                		        for (Node n: right_pane.getChildren()) {
                		                            
                		            if (n.getId() == null)
                		                continue;
                		                            
                		        if (n.getId().equals(sourceId))
                		            source = (DraggableNode) n;
                		                        
                		        if (n.getId().equals(targetId))
                		            target = (DraggableNode) n;
                		                            
                		        }
                		                        
                		        if (source != null && target != null)
                		        link.bindEnds(source, target);
                		    }
                		                        
                		}
           event.consume();
            
           
            }
        }); 
      
        }
    public int count() {
    	int i = a;
    	System.out.println(i);
    	return i;
    }
    
    
    
    
}
