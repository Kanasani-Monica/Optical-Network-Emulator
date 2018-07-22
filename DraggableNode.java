package application;

import java.awt.MouseInfo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.geometry.*;

public class DraggableNode extends AnchorPane{
	@FXML AnchorPane root_pane;
	@FXML AnchorPane left_link_handle;
	@FXML AnchorPane right_link_handle;
	private NodeLink mDragLink = null;
	private AnchorPane right_pane = null;
	        
	private EventHandler <MouseEvent> mLinkHandleDragDetected;
	private EventHandler <DragEvent> mLinkHandleDragDropped;
	private EventHandler <DragEvent> mContextLinkDragOver;
	private EventHandler <DragEvent> mContextLinkDragDrop;
	private EventHandler <DragEvent> mContextLinkDragDropped;

    private EventHandler<DragEvent>  mContextDragOver;
    private EventHandler<DragEvent>  mContextDragDropped;
        
    private DragIconType mType = null;
        
    private Point2D mDragOffset = new Point2D(0.0, 0.0);
    @FXML
    private TextField routerText;
    @FXML private Label title_bar;
    @FXML private Label close_button;  
    private final DraggableNode self;
    private final List<String>  mLinkIds = new ArrayList<String>  ();
    
    public void setTitle(String r) {
		routerText.setText(r);
	}
    
    public DraggableNode() {
		setId(UUID.randomUUID().toString());
		self= this;
    	FXMLLoader fxmlLoader = new FXMLLoader(
            getClass().getResource("/DraggableNode.fxml")
        );

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }   
    }

    @FXML
    private void initialize() {
    	buildNodeDragHandlers();
    	buildLinkDragHandlers();
    	
    	left_link_handle.setOnDragDetected(mLinkHandleDragDetected);
        right_link_handle.setOnDragDetected(mLinkHandleDragDetected);

        left_link_handle.setOnDragDropped(mLinkHandleDragDropped);
        right_link_handle.setOnDragDropped(mLinkHandleDragDropped);
        mDragLink = new NodeLink();
        mDragLink.setVisible(false);
                    
        parentProperty().addListener((ChangeListener<? super Parent>) (observable, oldValue, newValue) -> right_pane = (AnchorPane) getParent());
    }
   
	public DragIconType getType() { return mType;}

    public void setType(DragIconType type) {

        mType = type;

        getStyleClass().clear();
        getStyleClass().add("dragicon");
        switch (mType) {
            
        case blue:
        getStyleClass().add("icon-blue");
        break;

        case red:
        getStyleClass().add("icon-red");            
        break;

        case green:
        getStyleClass().add("icon-green");
        break;

        case grey:
        getStyleClass().add("icon-grey");
        break;

        case purple:
        getStyleClass().add("icon-purple");
        break;

        case yellow:
        getStyleClass().add("icon-yellow");
        break;

        case black:
        getStyleClass().add("icon-black");
        break;
            
        default:
        break;
        }
    }
   
    private void buildNodeDragHandlers() {
    	//drag detection for node dragging
    	
        title_bar.setOnDragDetected ( new EventHandler <MouseEvent> () {
        	

            @Override
            public void handle(MouseEvent event) {
                    
                getParent().setOnDragOver(null);
                getParent().setOnDragDropped(null);

                getParent().setOnDragOver (mContextDragOver);
                getParent().setOnDragDropped (mContextDragDropped);

            //begin drag ops
                mDragOffset = new Point2D(event.getX(), event.getY());
                        
                relocateToPoint (new Point2D(event.getSceneX(), event.getSceneY()));
                        
            ClipboardContent content = new ClipboardContent();
                DragContainer<?> container = new DragContainer<Object>();
                        
                container.addData ("type", mType.toString());
                content.put(DragContainer.DragNode, container);
                        
            startDragAndDrop (TransferMode.ANY).setContent(content);                  
                        
                event.consume();                    
            }
                    
        });     
        
        mContextDragOver = new EventHandler <DragEvent> () {

            //dragover to handle node dragging in the right pane view
            @Override
            public void handle(DragEvent event) {       

                event.acceptTransferModes(TransferMode.ANY);                
                relocateToPoint(new Point2D( event.getSceneX(), event.getSceneY()));

                event.consume();
            }
        };
                
        //dragdrop for node dragging
        mContextDragDropped = new EventHandler <DragEvent> () {
                
            @Override
            public void handle(DragEvent event) {
                        
            getParent().setOnDragOver(null);
            getParent().setOnDragDropped(null);
                            
            event.setDropCompleted(true);
                            
            event.consume();
            }
        };

        //close button click
        close_button.setOnMouseClicked( new EventHandler <MouseEvent> () {

            @Override
            public void handle(MouseEvent event) {

            AnchorPane parent  = (AnchorPane) self.getParent();
            parent.getChildren().remove(self);
            }
        });
        
        
      
            
	}
    public void relocateToPoint (Point2D p) {

        //relocates the object to a point that has been converted to
        //scene coordinates
        Point2D localCoords = getParent().sceneToLocal(p);

        relocate (
             (localCoords.getX() - mDragOffset.getX()),
             (localCoords.getY() - mDragOffset.getY())
        );
    }	 
    
    private void buildLinkDragHandlers() {
        
    	mLinkHandleDragDetected = new EventHandler <MouseEvent> () {

    	    @Override
    	    public void handle(MouseEvent event) {
    	                
    	        getParent().setOnDragOver(null);
    	    getParent().setOnDragDropped(null);                 
    	    
    	        getParent().setOnDragOver(mContextLinkDragOver);
    	    getParent().setOnDragDropped(mLinkHandleDragDropped);
    	                    
    	  //Set up user-draggable link
            right_pane.getChildren().add(0,mDragLink);                  
                        
        mDragLink.setVisible(true);

       Point2D p = new Point2D(
            getLayoutX() + (getWidth() / 2.0),
            getLayoutY() + (getHeight() / 2.0)
        ); 
       // java.awt.Point p = MouseInfo.getPointerInfo().getLocation(); 
       
            mDragLink.setStart(p);                  
                        
        //Drag content code
        ClipboardContent content = new ClipboardContent();
        DragContainer<?> container = new DragContainer<Object> ();
                        
        container.addData("source", getId());
        content.put(DragContainer.AddLink, container);
                    
            startDragAndDrop (TransferMode.ANY).setContent(content);    

            event.consume();
    	    }
    	};
		        
		    mLinkHandleDragDropped = new EventHandler <DragEvent> () {

		       

				@Override
		        public void handle(DragEvent event) {

		        getParent().setOnDragOver(null);
		        getParent().setOnDragDropped(null);
		                                            
		        //get the drag data.  If it's null, abort.  
		        //This isn't the drag event we're looking for.
		        DragContainer<?> container = 
		            (DragContainer<?>) event.getDragboard().getContent(DragContainer.AddLink);
		                                    
		        if (container == null)
		            return;
		         try {               
		        AnchorPane link_handle = (AnchorPane) event.getSource();
		        Parent parent = link_handle.getParent().getParent().getParent(); } catch (Exception e) {
		        	e.printStackTrace();
		        }
		                        
		        ClipboardContent content = new ClipboardContent();
		                        
		        container.addData("target", getId());
		                        
		        content.put(DragContainer.AddLink, container);
		                        
		        event.getDragboard().setContent(content);
		                        
		        event.setDropCompleted(true);

		        event.consume();  
		      //hide the draggable NodeLink and remove it from the right-hand AnchorPane's children
		        mDragLink.setVisible(false);
		        right_pane.getChildren().remove(0);
		        }
		    };

		            
		    mContextLinkDragOver = new EventHandler <DragEvent> () {

		    	@Override
		        public void handle(DragEvent event) {
		            event.acceptTransferModes(TransferMode.ANY);
		                        
		            //Relocate user-draggable link
		            if (!mDragLink.isVisible())
		                mDragLink.setVisible(true);
		                        
		            mDragLink.setEnd(new Point2D(event.getX(), event.getY()));

		            event.consume();
		    }
		    };

		    mContextLinkDragDropped = new EventHandler <DragEvent> () {
		    	
		    	

		        @Override
		        public void handle(DragEvent event) {
		                
		            getParent().setOnDragOver(null);
		        getParent().setOnDragDropped(null);
		                    
		        event.setDropCompleted(true);
		        event.consume();
		        
		      //hide the draggable NodeLink and remove it from the right-hand AnchorPane's children
		        mDragLink.setVisible(false);
		       right_pane.getChildren().remove(0);
		       }
		                
		    }; 
		    
		  //close button click
		    close_button.setOnMouseClicked( new EventHandler<MouseEvent>  () {

		       @Override
		       public void handle(MouseEvent event) {
		        AnchorPane parent  = (AnchorPane) self.getParent();
		        parent.getChildren().remove(self);

		        //iterate each link id connected to this node
		        //find it's corresponding component in the right-hand
		        //AnchorPane and delete it.
		                        
		        //Note:  other nodes connected to these links are not being
		        //notified that the link has been removed.
		        for (ListIterator<?>  iterId = mLinkIds.listIterator(); 
		                iterId.hasNext();) {
		                            
		            String id = (String) iterId.next();

		            for (ListIterator<?>  iterNode = parent.getChildren().listIterator();
		            iterNode.hasNext();) {
		                
		            Node node = (Node) iterNode.next();
		                                
		            if (node.getId() == null)
		                continue;
		                            
		            if (node.getId().equals(id))
		                iterNode.remove();
		            }
		            iterId.remove();
		        }
		        }
		    });
		    
		    
		}
    
	 public void registerLink(String linkId) {
		    mLinkIds.add(linkId);
		}
	
	
	


}
