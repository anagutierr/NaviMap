/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv;

import DBAccess.NavegacionDAOException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Problem;
import model.Navegacion;

/**
 * FXML Controller class
 *
 * @author Ana
 */
public class ListaProblemasController implements Initializable {

    @FXML
    private Button bVolver;
    @FXML
    private Button bOK;
    @FXML
    private ListView<String> listProblemas;
    
    private ObservableList<String> datosProblemas = FXCollections.observableArrayList();
    
    Problem problemaElegido;

    public static Navegacion navegacion = null;
    @FXML
    private TitledPane enunciado;
    
    List<Problem> problemas;
    @FXML
    private Label enunciadoLabel;
    
    private int index;
    @FXML
    private ScrollPane scEnunciado;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try { //Creo que da error porque no se inicializa bien la base de datos
            //int index = listProblemas.getSelectionModel().getSelectedIndex();
            
            /*Stage este = (Stage)bOK.getScene().getWindow();
            este.setResizable(false);*/
            bOK.setDisable(true);
            enunciado.setVisible(true);
            enunciado.setExpanded(false);
            enunciado.setAnimated(true);
            
            navegacion = Navegacion.getSingletonNavegacion();
            problemas = navegacion.getProblems();
                        
            for(int i = 1; i<=problemas.size();i++){
                datosProblemas.add("PROBLEMA " + i);                	
            }
            
            listProblemas.setItems(datosProblemas);  
           
            
            listProblemas.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    if (listProblemas.isFocused()) {
                        bOK.setDisable(false);
                        enunciado.setVisible(true);
                        enunciado.setExpanded(false);
                        
                    }
            });           
          
       } catch (NavegacionDAOException ex) {
            Logger.getLogger(ListaProblemasController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }    

    @FXML
    private void volver(ActionEvent event) throws IOException {
        cerrarVentana();
    }

    @FXML
    private void abrirProblema(ActionEvent event) throws IOException {
        FXMLLoader miCargador = new FXMLLoader(getClass().getResource("FXMLProblema.fxml"));
            Parent root = miCargador.load();
            
            ProblemaController controlador = miCargador.getController();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
             stage.setTitle("Problema");
            
            index = listProblemas.getSelectionModel().getSelectedIndex(); 
            listProblemas.getSelectionModel().clearSelection();
            controlador.initProblema(problemas.get(index));
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            
            stage.setOnCloseRequest(e -> {
                try {
                    controlador.cerrarVentana();
                } catch (IOException ex) {
                    Logger.getLogger(AyudaCartaController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            Stage este = (Stage)bVolver.getScene().getWindow();
            este.close();
        
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
         stage.setResizable(false);
         stage.show();
         
         stage.setOnCloseRequest(e -> {                            
                                e.consume();
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            
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
        Stage este = (Stage)bOK.getScene().getWindow();
            este.close();
        } catch (IOException e){
            Logger.getLogger(AyudaCartaController.class.getName()).log(Level.SEVERE,null,e);
        }
}

    @FXML
    private void mostrarEnunciado(MouseEvent event) {
        index = listProblemas.getSelectionModel().getSelectedIndex();
        enunciadoLabel.setText(problemas.get(index).getText());
        
    }

}
