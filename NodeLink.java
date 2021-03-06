package application;
import java.io.IOException;
import java.util.UUID;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.When;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.CubicCurve;
import javafx.stage.Stage;


public class NodeLink extends AnchorPane {
	 RootLayout rootlayout = new RootLayout();
	@FXML CubicCurve node_link;
	private final DoubleProperty mControlOffsetX = new SimpleDoubleProperty();
	private final DoubleProperty mControlOffsetY = new SimpleDoubleProperty();
	private final DoubleProperty mControlDirectionX1 = new SimpleDoubleProperty();
	private final DoubleProperty mControlDirectionY1 = new SimpleDoubleProperty();
	private final DoubleProperty mControlDirectionX2 = new SimpleDoubleProperty();
	private final DoubleProperty mControlDirectionY2 = new SimpleDoubleProperty();
    public NodeLink() {
    	setId(UUID.randomUUID().toString());
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/NodeLink.fxml")
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
            
        mControlOffsetX.set(50.0);
        mControlOffsetY.set(50.0);

        mControlDirectionX1.bind(new When (
        node_link.startXProperty().greaterThan(node_link.endXProperty()))
            .then(-1.0).otherwise(1.0));
            
        mControlDirectionX2.bind(new When (
        node_link.startXProperty().greaterThan(node_link.endXProperty()))
            .then(1.0).otherwise(-1.0));

            
        node_link.controlX1Property().bind(
            Bindings.add(
                node_link.startXProperty(),
                mControlOffsetX.multiply(mControlDirectionX1)
            )
        );
            
        node_link.controlX2Property().bind(
            Bindings.add(
                node_link.endXProperty(),
                mControlOffsetX.multiply(mControlDirectionX2)
            )
        );
            
        node_link.controlY1Property().bind(
            Bindings.add(
                node_link.startYProperty(),
                mControlOffsetY.multiply(mControlDirectionY1)
            )
        );
            
        node_link.controlY2Property().bind(
            Bindings.add(
                node_link.endYProperty(),
                mControlOffsetY.multiply(mControlDirectionY2)
            )
        );
    } 
    
    public void setStart(Point2D startPoint) {

        node_link.setStartX(startPoint.getX()-140);
        node_link.setStartY(startPoint.getY()-48);     
    }
        
    public void setEnd(Point2D endPoint) {
            
        node_link.setEndX(endPoint.getX()-140);
        node_link.setEndY(endPoint.getY()-48); 
    }
    
    public void bindEnds (DraggableNode source, DraggableNode target) {
        node_link.startXProperty().bind(
            Bindings.add(source.layoutXProperty(), (source.getWidth() / 2.0)-140));
            
        node_link.startYProperty().bind(
            Bindings.add(source.layoutYProperty(), (source.getWidth() / 2.0)-48));
            
        node_link.endXProperty().bind(
            Bindings.add(target.layoutXProperty(), (target.getWidth() / 2.0)-140));
            
        node_link.endYProperty().bind(
            Bindings.add(target.layoutYProperty(), (target.getWidth() / 2.0)-48));
        
        source.registerLink (getId());
        target.registerLink (getId());
        
    }
   
  @FXML
  public void calculate() throws IOException {
		rootlayout.count();
	  FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("OPBC.fxml"));
      Parent root1 = fxmlLoader.load();
      Stage stage = new Stage();
      stage.setTitle("Optical Power Budget Calculator");
      stage.setScene(new Scene(root1));
      stage.show();
  }
  
  
}