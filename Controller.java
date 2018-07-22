package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller implements Initializable {

    @FXML
    private Label labelReceiver;

    @FXML
    private TextField textBR;

    @FXML
    private TextArea textCable;

    @FXML
    private Label labelPowerBudget;

    @FXML
    private TextField textLength;

    @FXML
   private ChoiceBox <String> dropdownReceiver;

    @FXML
    private Label labelTransmitter;

    @FXML
    private TextArea textPowerBudget;

    @FXML
    private Label labelConnectors;

    @FXML
    private Label labelCable;

    @FXML
   private ChoiceBox <String> dropdownTransmitter;

    @FXML
    private Label labelLength;

    @FXML
    private TextField textConnectors;

    @FXML
    private Label labelBR;




    //private opticalCable cable = new opticalCable();
    //private opticalPowerBudget budget = new opticalPowerBudget();
    String B;
    String L;
    String C;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textBR.setPromptText("Enter the required Bandwidth");

        textLength.setPromptText("Enter the required cable length");

        textConnectors.setPromptText("Enter the required number of connectors");



    }



    public void showResult() {
        B = textBR.getText();
        System.out.println(B);

        L = textLength.getText();
        System.out.println(L);

        C = textConnectors.getText();
        System.out.println(C);



        //getting Bit-rate(B) and cable-length (L)
        Double BL = Double.parseDouble(B) * Double.parseDouble(L);
        Double B2L = Double.parseDouble(B) * BL; // Bˆ2L


        //Optical cable details
        if (Double.parseDouble(L) <= 10 && Double.parseDouble(B) <= 0.2 && BL < 0.01) {
            textCable.setText("Multimode Fibre(step-index) at 0.85um wavelength\n" +
                    "Multimode Fibre(graded-index) at 0.85um wavelength\n" + "single mode Fibre at 1.31um wavelength\n"
                    + "singlemode fibre at 1.5um wavelength\n");
        } else if (Double.parseDouble(B) <= 0.1 && BL <= 4) {
            textCable.setText("Multimode Fibre(graded-index) at 0.85um wavelength\n" + "single mode Fibre at 1.31um wavelength\n"
                    + "singlemode fibre at 1.5um wavelength\n");
        } else if (BL <= 125 ) {
            textCable.setText("single mode Fibre at 1.31um wavelength\n"
                    + "singlemode fibre at 1.5um wavelength\n");
        } else if (BL < 150 && B2L < 4000) {
            textCable.setText("singlemode fibre at 1.5um wavelength\n");
        } else {
            textCable.setText("No optical fibre available");
        }



        // getting transmitter ans receiver choices
        String T = dropdownTransmitter.getValue();
       // System.out.println(T);

        String R = dropdownReceiver.getValue();
       // System.out.println(R);

        //assigning and transmitter and receiver powers (in dBm)
        Double Tp = 0.0;
        if (T.equals("LED")) {
            Tp = -10.0;
          //  System.out.println(Tp);
        } else if (T.equals("Laser")) {
            Tp = 20.0;
           // System.out.println(Tp);
        }

        Double Rp = 0.0;
        if (R.equals("PIN")) {
            Rp = 1.0;
           // System.out.println(Tp);
        } else if (R.equals("Avalanche")) {
            Rp = -7.0;
           // System.out.println(Tp);
        }


        //assigning and transmitter and receiver powers for transmission loss calculation (in W)
        Double Tx = 0.0;
        if (T.equals("LED")) {
            Tx = 0.0001;
           // System.out.println(Tx);
        } else if (T.equals("Laser")) {
            Tx = 0.1;
           // System.out.println(Tx);
        }

        Double Rx = 0.0;
        if (R.equals("PIN")) {
            Rx = 0.0012589254118;
         //   System.out.println(Tp);
        } else if (R.equals("Avalanche")) {
            Rx = 0.0001995262315;
          //  System.out.println(Tp);
        }

        //Calculating cable attenuation loss
        Double cableattloss = 0.0;
        if (Double.parseDouble(L) < 10 && Double.parseDouble(B) < 0.2 && BL < 0.01) {
            cableattloss = 3.5;
        } else if (Double.parseDouble(B) < 0.1 && BL < 4) {
            cableattloss = 3.5;
        } else if (BL < 125 ) {
            cableattloss = 0.4;
        } else if (BL < 150 && B2L < 4000) {
            cableattloss = 0.3;
        } else {
            cableattloss = 0.0;
        }




        //Calculating transmission loss (in dB/m)
        Double alpha = 10*java.lang.Math.log10( Tx / Rx);

        //Calculating connector loss (in dBm)
        Double connloss= 0.75;

        //calculating Splice loss (in dBm)
        Double sloss = 0.1; //considered 2 splices by default

        //loss due to unforeseen factors (in dBm)
        Double unforloss = 3.0;

        //Calculating power budget (in dB)
        
        RootLayout rootlayout = new RootLayout();
        int p = rootlayout.count();
        System.out.println(p);
        //int n = p*(p-1)/2;
        System.out.println(p-1);
        String[] arr = new String[p-1];
        for(int m = 1; m<=p-1; m++) {
        	double q = m*(Tp - Rp - cableattloss - (sloss*2.0)- (connloss*Double.parseDouble(C)- unforloss));
        	System.out.println(q);
        	arr[m-1] = ("Between R"+ 1 +" and R"+ p +" Cost of path with "+ m +" cables is "+ q);
        	//Arrays.sort(arr);
        textPowerBudget.setText(Arrays.toString(arr));
        }

        }
   
   


}



