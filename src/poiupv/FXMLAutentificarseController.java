/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package poiupv;

import DBAccess.NavegacionDAOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import model.Navegacion;
import static poiupv.AyudaCartaController.navegacion;
import static poiupv.PoiUPVApp.navegacion;
import model.User;

/**
 * FXML Controller class
 *
 * @author aleks
 */
public class FXMLAutentificarseController implements Initializable {

    @FXML
    private TextField nombre_usuario;
    @FXML
    private PasswordField contraseña;
    @FXML
    private Hyperlink registro;
    @FXML
    private Button bIniciar;
    @FXML
    private Text error;
    public static Navegacion navegacion = null;
    public static User nuevoUser;
/*void newpage (ActionEvent e) throws IOException {
    Parent root1 = FXMLLoader.load(getClass().getResource("Registrarse.fxml"));
    Scene scene = new Scene(root1);
    Stage stage = new Stage();
    stage.setScene(scene);
    stage.show(); */
    @FXML
    private CheckBox checkVer;
    @FXML
    private TextField contraseñaVer;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
        navegacion = Navegacion.getSingletonNavegacion(); 
        
                 
        bIniciar.setDisable(true);
        contraseñaVer.setVisible(false);
        contraseña.textProperty().bindBidirectional(contraseñaVer.textProperty());
        
      
        
       checkVer.selectedProperty().addListener((ob,n,o)->{           
        if(checkVer.isSelected()){
           contraseña.setVisible(false);
            contraseñaVer.setVisible(true);
        } else {
            contraseña.setVisible(true);
            contraseñaVer.setVisible(false);
        }
        
        });
      
        contraseña.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if(!(nombre_usuario.getText().isBlank() && contraseña.getText().isBlank())){
                bIniciar.setDisable(false);
                error.setVisible(false);
            } else {
                bIniciar.setDisable(true);
            }
        
        });
       
                              
        } catch (NavegacionDAOException ex) {
            Logger.getLogger(ProblemaController.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }    

  
    @FXML
    private void newpage(javafx.event.ActionEvent event) throws IOException {        
    FXMLLoader miCargador = new FXMLLoader(getClass().getResource("FXMLRegistrarse.fxml"));
            Parent root = miCargador.load();

            RegistrarseController controlador = miCargador.getController();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
             stage.setTitle("Registrarse");
             stage.setResizable(false);

            stage.setScene(scene);
            stage.show();

            stage.setOnCloseRequest(e -> {
                try {
                    controlador.cerrarVentana();
                } catch (IOException ex) {
                    Logger.getLogger(AyudaCartaController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
           Stage este = (Stage)registro.getScene().getWindow();
            este.close();    
    }

    @FXML
    private void iniciarMenu(javafx.event.ActionEvent event) throws IOException {
        //navegacion.existsNickName(nombre_usuario.getText());
        //bIniciar.setDisable(true);
        
        if((nombre_usuario.getText().isBlank() || contraseña.getText().isBlank())){
                error.setVisible(true);
                error.setText("Por favor, rellene todos los campos");
                error.setFill(Color.RED);
        } else {
            
            if(!(nombre_usuario.getText().isBlank() && contraseña.getText().isBlank())){
                if(navegacion.exitsNickName(nombre_usuario.getText())){
                    if(navegacion.getUser(nombre_usuario.getText()).checkCredentials(nombre_usuario.getText(), contraseña.getText())){
                        
                        nuevoUser = navegacion.loginUser(nombre_usuario.getText(), contraseña.getText());
                        FXMLLoader miCargador = new FXMLLoader(getClass().getResource("FXMLMenu.fxml"));
                        Parent root = miCargador.load();

                        Menu controlador = miCargador.getController();
                        Scene scene = new Scene(root);
                        Stage stage = new Stage();
                        stage.setTitle("Menu");
                        stage.setResizable(false);

                        stage.setScene(scene);
                        stage.show();
                        stage.setOnCloseRequest(e -> {                            
                                e.consume();
                                Alert alert = new Alert(AlertType.CONFIRMATION);
                            
                                 DialogPane aviso = alert.getDialogPane();
                                 aviso.setStyle("-fx-background-color: #cce3f2;"); 
                                 aviso.setHeaderText("¿Desea salir de la aplicación?");
                                 //aviso.setGraphic(null);        
                                 aviso.setContentText("Si no cierra sesión antes, se perderá el progreso.");
                                 ButtonType si = new ButtonType("Sí");
                                 ButtonType no = new ButtonType("No");
                                 alert.getButtonTypes().setAll(si,no);
                                 Optional<ButtonType> result = alert.showAndWait();
                                  if(result.isPresent()) {
                                      if(result.get() == si){
                                          System.exit(0);
                                      } 
                                  }                                  
                                
                        });
                         
                        Stage este = (Stage)registro.getScene().getWindow();
                        este.close();
                        
                    } else {  
                        error.setVisible(true);
                    error.setText("Contraseña incorrecta");
                    error.setFill(Color.RED);
                    }
                } else {
                    error.setVisible(true);
                    error.setText("Usuario no existe");
                    error.setFill(Color.RED);
                    
                }}
         
       
            }
        } }  

   
    

    


    

    
    

