/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author hiroshi
 */

public class Compilador extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

            Scene scene = new Scene(root);
            scene.getStylesheets().add("Tools/estilos.css");
            stage.setScene(scene);
            stage.show();
        }
        catch(Exception e)
        {System.out.println(""+e);}

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
        //System.out.println(rec("",5));
    }
    
    public static String rec(String e, int pos){
        if(pos == e.length())
            return e;
        return rec(e+"e",pos);
    }
    
}
