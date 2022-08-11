/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv;

import DBAccess.NavegacionDAOException;
import com.sun.javafx.logging.PlatformLogger.Level;
import java.io.File;
import java.io.IOException;
import java.lang.System.Logger;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Navegacion;
import static poiupv.PoiUPVApp.navegacion;
import model.User;
import poiupv.FXMLAutentificarseController;
import static model.User.checkEmail;
/**
 * FXML Controller class
 *
 * @author Ana
 */
public class RegistrarseController implements Initializable {

    @FXML
    private Button guardar;
    @FXML
    private Button cancelar;
    @FXML
    private AnchorPane myAnchorPane;
    @FXML
    private TextField username;
    @FXML
    private TextField email;
    @FXML
    private DatePicker age;
    @FXML
    private PasswordField password;
    @FXML
    private Label ageLabel;
    @FXML
    private SplitMenuButton foto;
    @FXML
    private ImageView MiAvatar;
    @FXML
    private ImageView Avatar1;
    @FXML
    private ImageView Avatar2;
    @FXML
    private ImageView Avatar3;
    public static Navegacion navegacion = null;
    @FXML
    private Label userLabel;
    @FXML
    private ImageView AvatarPC;
    @FXML
    private Label emailLabel;
    @FXML
    private Label passwordLabel;
    
    @FXML
    private Label userHelp;
    @FXML
    private Text ups;
    @FXML
    private Label helpPas;
    @FXML
    private GridPane gridPane;
    @FXML
    private VBox vBox;
    @FXML
    private Text nomtext;
    @FXML
    private Text title;
    @FXML
    private ImageView AvatarDef;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try {
        navegacion = Navegacion.getSingletonNavegacion();}
        catch (NavegacionDAOException ex) {};
        
    }    

    @FXML
    private void guardaUsuario(ActionEvent event) throws IOException, NavegacionDAOException, InterruptedException {
           
        boolean datos = true;
        boolean edad = true;
        boolean nick = true;
        boolean mail = true;
        boolean contraseña = true;
        if ( username.getText().isBlank() || email.getText().isBlank() || password.getText().isBlank() || age.getValue() == null){
            ups.setText("Por favor, rellene todos los campos obligatorios");
            datos = false;
            } else {
         ups.setText("");
         LocalDate date = LocalDate.now();
         
         
         int d = date.getYear();
         int m = date.getMonthValue();
         int day = date.getDayOfMonth();
         if ((d - age.getValue().getYear()) < 16  
              || (d - age.getValue().getYear() == 16) && age.getValue().getMonthValue() > m 
              || (d - age.getValue().getYear() == 16) && (m == age.getValue().getMonthValue()) && day < age.getValue().getDayOfMonth())
             {ageLabel.setText("Solo los usuarios de 16 años o más pueden crear una cuenta" );
              ageLabel.setTextFill(Color.color(1, 0, 0));
              edad = false;
              
             }
        
        if (navegacion.exitsNickName(username.getText()) == true) { 
            userLabel.setText("Nombre de usuario ya existe"); 
            userHelp.setText(" ");   
            nick = false;
            userLabel.setTextFill(Color.color(1, 0, 0));
            
        } 
        if ( User.checkNickName(username.getText()) == false){
            userLabel.setText("El nombre de usuario no es válido");
            username.getStyleClass().add("custom");
            userHelp.setText(" ");        
            nick = false;
            
            
        } 
        if ( User.checkEmail(email.getText()) == false ) {
            emailLabel.setText("Email no es válido");            
            mail = false;
            emailLabel.setTextFill(Color.color(1, 0, 0));
        } 
        if  ( User.checkPassword(password.getText()) == false) {
            helpPas.setText(" ");
            passwordLabel.setText("La contraseña no es válida");            
            contraseña = false;
            
            
        }
            
        }
        
        if ( edad && nick && mail && contraseña && datos) {
            
         navegacion.registerUser(username.getText(), email.getText(), password.getText(), MiAvatar.getImage(), age.getValue());
            
        Stage stage = (Stage) myAnchorPane.getScene().getWindow();
        
        AlertType type = AlertType.INFORMATION;
        Alert alert = new Alert(type, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);
        DialogPane aviso = alert.getDialogPane();
        aviso.setId("aviso");
        aviso.getStyleClass().add("custom");
        aviso.setStyle("-fx-background-color: #cce3f2;"); // esto sí que funciona
        aviso.setHeaderText(null);
        aviso.setGraphic(null);
        ButtonType ini = new ButtonType("Iniciar sesión");
        
        alert.getButtonTypes().setAll(ini);
        aviso.setContentText("¡Registrado con éxito!  :D");        
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent()){
            if(result.get()== ini){
            FXMLLoader miCargador = new FXMLLoader(getClass().getResource("FXMLAutentificarse.fxml"));
            Parent root = miCargador.load();
            FXMLAutentificarseController controlador = miCargador.getController();
            Scene scene = new Scene(root);
            Stage escena = new Stage();
            stage.setResizable(false);
             escena.setTitle("Autentificarse");
            escena.setScene(scene);
            escena.show();
            Stage este = (Stage)cancelar.getScene().getWindow();
            este.close(); 
            }
        } 
        } 
        }

    
        
    @FXML
    private void goback(ActionEvent event) throws IOException {
       FXMLLoader miCargador = new FXMLLoader(getClass().getResource("FXMLAutentificarse.fxml"));
            Parent root = miCargador.load();
            FXMLAutentificarseController controlador = miCargador.getController();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
             stage.setTitle("Autentificarse");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            Stage este = (Stage)cancelar.getScene().getWindow();
            este.close(); 
    }

    

    @FXML
    private void setAvatar1(ActionEvent event) {
        MiAvatar.setImage(Avatar1.getImage());
    }

    @FXML
    private void setAvatar2(ActionEvent event) {
        MiAvatar.setImage(Avatar2.getImage());
    }

    @FXML
    private void setAvatar3(ActionEvent event) {
        MiAvatar.setImage(Avatar3.getImage());
    }

    @FXML
    private void choseAvatar(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Buscar imagen");
        fileChooser.getExtensionFilters().addAll(
          new FileChooser.ExtensionFilter("All Images", "*.*"),
          new FileChooser.ExtensionFilter("JPG", ".jpg"),
          new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        File imgFile = fileChooser.showOpenDialog(null);
        if (imgFile != null){
            Image image = new Image("file:" + imgFile.getAbsolutePath());
            MiAvatar.setImage(image);
        }
        }

    @FXML
    private void clearAge(MouseEvent event) {
        ageLabel.setText(" ");
        helpPas.setText(" ");
        userHelp.setText("");
        
    }

    @FXML
    private void clearPassword(MouseEvent event) {
        passwordLabel.setText(" ");
        helpPas.setText("Introduzca un valor entre 8 y 20 caracteres, con al menos una \n" +
            " letra en mayúsculas y minúsculas, un dígito y un carácter especial");
        userHelp.setText("");
    }

    @FXML
    private void clearMail(MouseEvent event) {
        emailLabel.setText(" ");
        helpPas.setText(" ");
        userHelp.setText("");
    }

    @FXML
    private void clearNick(MouseEvent event) {        
        userHelp.setText("Introduzca un valor entre 6 y 15 caracteres o dígitos "
                    + "sin espacios,\npuede usar guiones o sub-guiones  ");
        userLabel.setText(" ");
        helpPas.setText(" ");
        
    }

    @FXML
    private void setAvatarDef(ActionEvent event) {
         MiAvatar.setImage(AvatarDef.getImage());
    }

     
   public void cerrarVentana() throws IOException{
        try{
        FXMLLoader miCargador = new FXMLLoader(getClass().getResource("FXMLAutentificarse.fxml"));
         Parent root = miCargador.load();
         
         FXMLAutentificarseController controlador = miCargador.getController();   
         Scene scene = new Scene(root);
         Stage stage = new Stage();
         stage.setResizable(false);
         
         stage.setScene(scene);
         stage.show();
         
         
        Stage este = (Stage)MiAvatar.getScene().getWindow();
            este.close();
        } catch (IOException e){
            java.util.logging.Logger.getLogger(AyudaCartaController.class.getName()).log(java.util.logging.Level.SEVERE,null,e);
        }
}
    
        
    
} 

    

    

