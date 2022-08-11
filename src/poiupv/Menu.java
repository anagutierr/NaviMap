/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv;

import DBAccess.NavegacionDAOException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Navegacion;
import model.Problem;
import model.Session;
import model.User;
import static poiupv.FXMLResultadosController.navegacion;
import poiupv.Poi;
import static poiupv.PoiUPVApp.navegacion;


/**
 *
 * @author jsoler
 */
public class Menu implements Initializable {

    
    @FXML
    private Button linkProblema;
    
    //public static int hits = 0;
     //public static int faults = 0;

    public static Navegacion navegacion = null;
    
     private static int aciertosAGuardar = 0;
    
    private static int fallosAGuardar = 0;
    @FXML
    private ImageView pic;
    @FXML
    private MenuItem myprofile;
    @FXML
    private MenuItem inicio;
    @FXML
    private Button linkLista;
    @FXML
    private Button linkResultados;
    @FXML
    private Label label;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       try {
            // TODO
                     
            navegacion = Navegacion.getSingletonNavegacion(); 
            label.setText(FXMLAutentificarseController.nuevoUser.getNickName());
            
            
          
            
        } catch (NavegacionDAOException ex) {
            Logger.getLogger(ProblemaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        //linkResultados.setTooltip(new Tooltip("Accesible solo con sesiones guardadas"));
        
        picture();
        

    }
   public void picture () {
   pic.setImage(FXMLAutentificarseController.nuevoUser.getAvatar());
   }
   public static void setaciertosAGuardar(int nuevo){aciertosAGuardar=nuevo;}
     public static void setfallosAGuardar(int nuevo){fallosAGuardar=nuevo;}


   public void cerrarVentana() throws IOException{
        try{
        FXMLLoader miCargador = new FXMLLoader(getClass().getResource("FXMLMenu.fxml"));
         Parent root = miCargador.load();
         
         Menu controlador = miCargador.getController();   
         Scene scene = new Scene(root);
         Stage stage = new Stage();
         stage.setResizable(false);
         
         stage.setScene(scene);
         stage.show();
         
        Stage este = (Stage)linkLista.getScene().getWindow();
            este.close();
        } catch (IOException e){
            Logger.getLogger(AyudaCartaController.class.getName()).log(Level.SEVERE,null,e);
        }
}

    @FXML
    private void abrirProblema(MouseEvent event) throws IOException{
            FXMLLoader miCargador = new FXMLLoader(getClass().getResource("FXMLProblema.fxml"));
            Parent root = miCargador.load();
            
            ProblemaController controlador = miCargador.getController();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
             stage.setTitle("Problema");
             stage.setResizable(false);
             
            List<Problem> problemas = navegacion.getProblems();
            
            //Se ha usado el método nextInt, para obtener una posicion aleatoria de una lista dada, se ha sacado de:
            //https://es.acervolima.com/obtener-elementos-aleatorios-de-arraylist-en-java/
            Random r = new Random();
            int index = r.nextInt(problemas.size());  
            controlador.initProblema(problemas.get(index));
            
                       
            stage.setScene(scene);
            stage.show();
            
            stage.setOnCloseRequest(e -> {
                try {
                    controlador.cerrarVentana();
                } catch (IOException ex) {
                    Logger.getLogger(AyudaCartaController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            Stage este = (Stage)linkProblema.getScene().getWindow();
            este.close();
                         
    }
    
    

    @FXML
    private void abrirLista(MouseEvent event) throws IOException{
        FXMLLoader miCargador = new FXMLLoader(getClass().getResource("FXMLListaProblemas.fxml"));
            Parent root = miCargador.load();
            
            ListaProblemasController controlador = miCargador.getController();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
             stage.setTitle("Mostrar lista de problemas");
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
            Stage este = (Stage)linkProblema.getScene().getWindow();
            este.close();
    }
        
    

    @FXML
    private void abrirResultados(MouseEvent event) throws IOException {
        List<Session> listaSesiones = navegacion.getUser(FXMLAutentificarseController.nuevoUser.getNickName()).getSessions();
            
        if(listaSesiones.isEmpty()){
            Alert alerta = new Alert(AlertType.ERROR);
            DialogPane aviso = alerta.getDialogPane();
           aviso.setStyle("-fx-background-color: #cce3f2;"); 
            aviso.setHeaderText("El usuario no ha resuelto problemas todavía");
            aviso.setContentText("Para acceder a esta opción, primero guarde sesión.");
            ButtonType ini = new ButtonType("OK");
            alerta.getButtonTypes().setAll(ini);
            alerta.showAndWait();
         
        }else{
        FXMLLoader miCargador = new FXMLLoader(getClass().getResource("FXMLResultados.fxml"));
            Parent root = miCargador.load();
            
            FXMLResultadosController controlador = miCargador.getController();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
             stage.setTitle("Mostrar Resultados");
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
            Stage este = (Stage)linkProblema.getScene().getWindow();
            este.close();
        }
    }


    
    @FXML
     private void cerrarSesion(ActionEvent event) throws IOException {
        try {
            
            //navegacion.exitsNickName(FXMLAutentificarseController.nuevoUser.getNickName());
            navegacion.getUser(FXMLAutentificarseController.nuevoUser.getNickName()).addSession(new Session(LocalDateTime.now(),aciertosAGuardar,fallosAGuardar));            
            closeMenu();
        } catch (NavegacionDAOException ex) {
            Logger.getLogger(AyudaCartaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
     
     public void guardarHyF(int h, int f){
        aciertosAGuardar += h;
        fallosAGuardar += f;
    
    } 

    @FXML
    private void goPerfil(ActionEvent event) throws IOException {
        FXMLLoader miCargador = new FXMLLoader(getClass().getResource("FXMLModificarPerfil.fxml"));
            Parent root = miCargador.load();
            ModificarPerfilController controlador = miCargador.getController();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
             stage.setTitle("Mi perfil");
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
            Stage este = (Stage)linkProblema.getScene().getWindow();
            este.close();
    }

    public void closeMenu() throws IOException {
        FXMLLoader miCargador = new FXMLLoader(getClass().getResource("FXMLAutentificarse.fxml"));
            Parent root = miCargador.load();
            FXMLAutentificarseController controlador = miCargador.getController();
            Scene scene = new Scene(root);
            
            Stage stage = new Stage();
            stage.setTitle("Autentificarse");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
            Stage este = (Stage)inicio.getParentPopup().getOwnerWindow();
            
            este.close(); 
    }
}
