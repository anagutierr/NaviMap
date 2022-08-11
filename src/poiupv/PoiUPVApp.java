/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv;

import java.time.LocalDate;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Navegacion;
import model.User;

/**
 *
 * @author jose
 */
public class PoiUPVApp extends Application {
    
    public static Navegacion navegacion = null;
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLAutentificarse.fxml"));
        
        
        Scene scene = new Scene(root);
        stage.setTitle("Menú");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        
//        String nickName = "AnaGutierrez";
//        String email = "ana.gutimandi@gmail.es";
//        String password = "miPassword1@";
//        LocalDate birthdate = LocalDate.now().minusYears(18);
        navegacion = Navegacion.getSingletonNavegacion(); 
        //User result = navegacion.registerUser(nickName, email, password, birthdate);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
