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
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Navegacion;
import model.Session;
import static poiupv.ListaProblemasController.navegacion;

/**
 * FXML Controller class
 *
 * @author Ana
 */
public class FXMLResultadosController implements Initializable {

    @FXML
    private Button bVolver;

    public static Navegacion navegacion = null;
    @FXML
    private DatePicker calendario;
    @FXML
    private Label estadisticas;
    @FXML
    private TableView<Session> tabla;
    @FXML
    private Label valoracion;
    @FXML
    private TableColumn<Session, Integer> tAciertos;
    @FXML
    private TableColumn<Session, Integer> tFallos;
    @FXML
    private TableColumn<Session, String> TFecha;
    
    private ObservableList<Session> datos = null;
    
    private ObservableList<Session> filtro = null;
    
    private LocalDateTime timeStamp;
    @FXML
    private PieChart grafico;
    @FXML
    private BorderPane bp;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            
            navegacion = Navegacion.getSingletonNavegacion();
            List<Session> listaSesiones = navegacion.getUser(FXMLAutentificarseController.nuevoUser.getNickName()).getSessions();
            
                
              
            datos = FXCollections.observableArrayList(listaSesiones);
            filtro = FXCollections.observableArrayList(listaSesiones);
            tabla.setItems(datos);
            
            
            calendario.setPromptText(listaSesiones.get(0).getLocalDate().toString());
            
            
            calendario.setDayCellFactory((DatePicker calendario) -> {//coger la primera sesion del usuario para que los dias q no tenia la app salen en gris
                    return new DateCell(){
                    @Override
                    public void updateItem(LocalDate date, boolean empty){
                        try {
                            navegacion = Navegacion.getSingletonNavegacion();
                        } catch (NavegacionDAOException ex) {
                            Logger.getLogger(FXMLResultadosController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                         List<Session> listaSesiones = navegacion.getUser(FXMLAutentificarseController.nuevoUser.getNickName()).getSessions();
                    super.updateItem(date, empty);
                    
                    LocalDate primera = listaSesiones.get(0).getLocalDate();
                    LocalDate hoy = LocalDate.now();
                    setDisable(empty || date.compareTo(primera) < 0 || date.compareTo(hoy) > 0);
                    if(date == null || empty) setText(null);                   
                    }
                    };
                    } );
            
           tAciertos.textProperty().set("Nº ACIERTOS");
            
            tAciertos.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getHits()).asObject());
            
            tFallos.textProperty().set("Nº FALLOS");
            tFallos.setCellValueFactory(dataCell2 -> new SimpleIntegerProperty(dataCell2.getValue().getFaults()).asObject());
           
            
            TFecha.textProperty().set("FECHA");
            TFecha.setCellValueFactory(dataCell3 -> new SimpleStringProperty(dataCell3.getValue().getLocalDate().toString()));
                      
            inicializarEstadísticas(datos);
            
          
        } catch (NavegacionDAOException ex) {
            Logger.getLogger(FXMLResultadosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    private void inicializarEstadísticas(ObservableList<Session> list){
         estadisticas.setText(calculoPorcentajeAciertos(list));
            valoracion.setVisible(true);
            if((calculoPorcentajeAciertosGrafico(list)<= 50)){
                valoracion.setText("-> Deberías esforzarte más...");
            } else if(calculoPorcentajeAciertosGrafico(list)> 50 && calculoPorcentajeAciertosGrafico(list)<= 70){
                valoracion.setText("-> ¡Sigue así!");
            } else if (calculoPorcentajeAciertosGrafico(list)> 70){
                valoracion.setText("-> ¡Enhorabuena!");
            } else {
                valoracion.setText("-> No ha resuelto problemas");
            }
            ObservableList<PieChart.Data> datosGrafico = FXCollections.observableArrayList();
            datosGrafico.add(new PieChart.Data("Aciertos", calculoPorcentajeAciertosGrafico(list)));
            datosGrafico.add(new PieChart.Data("Fallos", 100-calculoPorcentajeAciertosGrafico(list)));
            grafico.setData(datosGrafico);
             grafico.getData().forEach(this::tooltip);
            grafico.setLegendVisible(false); 
            grafico.setLegendSide(Side.LEFT);
            
           
            grafico.setLabelLineLength(5.0f);
            grafico.setLabelsVisible(true);
            
           
    
    }

    public void tooltip(PieChart.Data d) {

    String msg = String.format("%s", d.getPieValue()) + " %";

    Tooltip tt = new Tooltip(msg);
    tt.setStyle("-fx-background-color: gray; -fx-text-fill: whitesmoke;");
    
    Tooltip.install(d.getNode(), tt);
    }


    private double calculoPorcentajeAciertosGrafico(ObservableList<Session> list){
        
        double porcentaje =0;
        int aciertos = 0;
        int fallos = 0;
        
        for(int i = 0; i < list.size();i++){
            aciertos += list.get(i).getHits();
            fallos += list.get(i).getFaults();
        }
        porcentaje =((double)aciertos/(aciertos+fallos))*100;
        
        return porcentaje;    
    }
    
    private String calculoPorcentajeAciertos(ObservableList<Session> list){
        double porcentaje =0;
        int aciertos = 0;
        int fallos = 0;
       
        for(int i = 0; i < list.size();i++){
            aciertos += list.get(i).getHits();
            fallos += list.get(i).getFaults();
        }
        porcentaje =((double)aciertos/(aciertos+fallos))*100;
        
        String val = String.format ("%.2f", porcentaje);
        
        return val + "% correctas";    
    }
    
    @FXML
    private void volver(ActionEvent event) throws IOException {
        cerrarVentana();
    }
    
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
        Stage este = (Stage)bVolver.getScene().getWindow();
            este.close();
        } catch (IOException e){
            Logger.getLogger(AyudaCartaController.class.getName()).log(Level.SEVERE,null,e);
        }
}

    @FXML
    private void filtrar(ActionEvent event) {
        
        valoracion.setVisible(true);
        
        LocalDate calFecha = calendario.getValue();
        filtro.clear();
        for(int i = 0; i< datos.size();i++){
               if(calFecha.compareTo(datos.get(i).getLocalDate())<= 0 ){ 
                   filtro.add(datos.get(i));
                   
               }
        }
        
        
        tabla.setItems(filtro);
      
         inicializarEstadísticas(filtro);}
    
}
