/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv;

import DBAccess.NavegacionDAOException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.User;

/**
 * FXML Controller class
 *
 * @author Ana
 */
public class ModificarPerfilController implements Initializable {

    @FXML
    private Button guardar;
    @FXML
    private Button volver;
    @FXML
    private DatePicker DatePicker;
    @FXML
    private ImageView Avatar1;
    @FXML
    private ImageView Avatar2;
    @FXML
    private ImageView MiAvatar;
    @FXML
    private Text nickName;
    @FXML
    private TextField correo;
    @FXML
    private TextField contraseña;
    @FXML
    private AnchorPane myAnchorPane;
    @FXML
    private Label ageLabel;
    @FXML
    private Label mailLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private ImageView Avatar3;
    @FXML
    private ImageView AvatarPC;
    @FXML
    private GridPane gridPane;
    @FXML
    private ImageView avatarDef;
    @FXML
    private Text userHelp;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        nombreUser();
        guardar.setDisable(true);
        correo.setOnKeyPressed(new EventHandler<KeyEvent>()
                {
        @Override
        public void handle(KeyEvent k) {
            guardar.setDisable(false);
        } }
        );
        contraseña.setOnKeyPressed(new EventHandler<KeyEvent>()
                {
        @Override
        public void handle(KeyEvent k) {
            guardar.setDisable(false);
        } }
        );
        DatePicker.setOnAction(new EventHandler<ActionEvent>()
                {
        @Override
        public void handle(ActionEvent k) {
            guardar.setDisable(false);
        } }
        );
        
    }    
    public void nombreUser () {
      nickName.setText(FXMLAutentificarseController.nuevoUser.getNickName());            
      MiAvatar.setImage(FXMLAutentificarseController.nuevoUser.getAvatar());
      correo.setText(FXMLAutentificarseController.nuevoUser.getEmail());
      contraseña.setText(FXMLAutentificarseController.nuevoUser.getPassword());
      DatePicker.setValue(FXMLAutentificarseController.nuevoUser.getBirthdate());
    }
    
    public void cerrarVentana() throws IOException{
        try{
        FXMLLoader miCargador = new FXMLLoader(getClass().getResource("FXMLMenu.fxml"));
         Parent root = miCargador.load();
         
         Menu controlador = miCargador.getController();   
         Scene scene = new Scene(root);
         Stage stage = new Stage();
          stage.setTitle("Menú");
         
         stage.setScene(scene);
         stage.show();
         
         stage.setOnCloseRequest(e -> {                            
                                e.consume();
                                Alert alert = new Alert(AlertType.CONFIRMATION);
                                 alert.initModality(Modality.APPLICATION_MODAL);
                                 
                                 DialogPane aviso = alert.getDialogPane();
                                 aviso.setStyle("-fx-background-color: #cce3f2;"); 
                                 aviso.setHeaderText("¿Desea salir de la aplicación?");                                       
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
        Stage este = (Stage)guardar.getScene().getWindow();
            este.close();
        } catch (IOException e){
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE,null,e);
        }
}

    @FXML
    private void guardaUsuario(ActionEvent event) throws IOException, NavegacionDAOException  {
        boolean edad = true; 
        boolean mail = true;
        boolean password = true;
        LocalDate date = LocalDate.now();
        int d = date.getYear();
        int m = date.getMonthValue();
        int day = date.getDayOfMonth();
        
        if ((d - DatePicker.getValue().getYear()) < 16  
              || (d - DatePicker.getValue().getYear() == 16) && DatePicker.getValue().getMonthValue() > m 
              || (d - DatePicker.getValue().getYear() == 16) && (m == DatePicker.getValue().getMonthValue()) && day < DatePicker.getValue().getDayOfMonth())
             {ageLabel.setText("El usuario tiene que ser mayor de 16 años" );
              edad = false;
             }
        if ( User.checkEmail(correo.getText()) == false ) {
            mailLabel.setText("Email no es válido");
            mail = false; 
        }
        if  ( User.checkPassword(contraseña.getText()) == false) {
            passwordLabel.setText("La contraseña no es válida");
            password = false;
            userHelp.setText("");
        }
        
        if ( edad && mail && password) {
        
        
        FXMLAutentificarseController.nuevoUser.setPassword(contraseña.getText());
        FXMLAutentificarseController.nuevoUser.setBirthdate(DatePicker.getValue());
        FXMLAutentificarseController.nuevoUser.setAvatar(MiAvatar.getImage());
        FXMLAutentificarseController.nuevoUser.setEmail(correo.getText());
        Stage stage = (Stage) myAnchorPane.getScene().getWindow(); 
        
        Alert.AlertType type = Alert.AlertType.INFORMATION;
        Alert alert = new Alert(type, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);
        DialogPane aviso = alert.getDialogPane();
        aviso.setStyle("-fx-background-color: #cce3f2;"); 
        aviso.setHeaderText(null);
        aviso.setGraphic(null);
        ButtonType ini = new ButtonType("OK");
        alert.getButtonTypes().setAll(ini);
        aviso.setContentText("¡Se han guardado las modificaciones!");
        // alert.getDialogPane().setHeaderText("título");
        alert.showAndWait();
        } guardar.setDisable(true);
        userHelp.setText("");
    }
    
    

    @FXML
    private void goback(ActionEvent event) throws IOException {
        if (guardar.isDisable() == false) {
         Alert alert = new Alert(AlertType.CONFIRMATION);
                            
        DialogPane aviso = alert.getDialogPane();
        aviso.setStyle("-fx-background-color: #cce3f2;"); 
        aviso.setHeaderText("¿Desea volver al menú?");               
        aviso.setContentText("Las modificaciones de su perfil no guardadas se perderán.");        
        ButtonType si = new ButtonType("Sí");
        ButtonType no = new ButtonType("No");
        alert.getButtonTypes().setAll(si,no);
        Optional<ButtonType> result = alert.showAndWait();
          if(result.isPresent()) {
                if(result.get() == si){
                   cerrarVentana();
                } 
          }     }       
        else {
            cerrarVentana();
        }
       
    }

    @FXML
    private void setAvatar1(ActionEvent event) {
        MiAvatar.setImage(Avatar1.getImage());
        guardar.setDisable(false);
    }

    @FXML
    private void setAvatar3(ActionEvent event) {
        MiAvatar.setImage(Avatar3.getImage());
        guardar.setDisable(false);
    }

    @FXML
    private void setAvatar2(ActionEvent event) {
        MiAvatar.setImage(Avatar2.getImage());
        guardar.setDisable(false);
    }

    @FXML
    private void chooseAvatar(ActionEvent event) {
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
        guardar.setDisable(false);
    }

    @FXML
    private void clearMail(MouseEvent event) {
        mailLabel.setText(" ");
        userHelp.setText("");
    }

    @FXML
    private void clearPassword(MouseEvent event) {
        passwordLabel.setText(" ");
        userHelp.setText("Introduzca un valor entre 8 y 20 caracteres, con al menos una \n" +
            " letra en mayúsculas y minúsculas, un dígito y un carácter especial");
    }
    @FXML
    private void clearAge(MouseEvent event) {
        ageLabel.setText(" ");
        userHelp.setText("");
    }
    @FXML
    private void setAvatarDef(ActionEvent event) {
        MiAvatar.setImage(avatarDef.getImage());
        guardar.setDisable(false);
    }

    


   
}