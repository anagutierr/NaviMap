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
import java.util.Collections;
import static java.util.Collections.shuffle;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.util.Duration;
import model.Answer;
import model.Navegacion;
import model.Problem;
import static poiupv.ListaProblemasController.navegacion;




/**
 * FXML Controller class
 *
 * @author Ana
 */
public class ProblemaController implements Initializable {

    @FXML
    private Button bFinalizar;
    @FXML
    private Button bVerificar;
    @FXML
    private Button bSiguiente;
    @FXML
    private Label lEnunciado;
    @FXML
    private RadioButton answer1;
    @FXML
    private RadioButton answer2;
    @FXML
    private RadioButton answer3;
    @FXML
    private RadioButton answer4;

     public static Navegacion navegacion = null;
     
     Problem problema;
     List<Answer> respuestas;
    
    @FXML
    private ToggleGroup answers;
    @FXML
    private Label mensajeIn;    
    @FXML
    private VBox vBox;
    
    private List<Problem> listaProblemas;
    
    private int hits = 0;
    private int faults = 0;
    @FXML
    private Slider zoom_slider;
    @FXML
    private Label posicion;
    @FXML
    private ScrollPane map_scrollpane;
    
    @FXML
    private MenuButton map_pin;
     
    boolean primeraPasada = true;
    // hashmap para guardar los puntos de interes POI
    private final HashMap<String, Poi> hm = new HashMap<>();
    // ======================================
    // la variable zoomGroup se utiliza para dar soporte al zoom
    // el escalado se realiza sobre este nodo, al escalar el Group no mueve sus nodos
    private Group zoomGroup;
    @FXML
    private ColorPicker colorP;
    @FXML
    private Slider sliderGrosor;
    @FXML
    private ToggleButton bMarcarPunto;
    @FXML
    private ToggleButton bRecta;
    @FXML
    private ToggleButton bArco;
    @FXML
    private ToggleButton bTexto;
    @FXML
    private ToggleButton bTransportador;
    @FXML
    private ToggleButton bCoordenadas;
    @FXML
    private ToggleButton bLimpiar;
    @FXML
    private ImageView nodo;
    
     TextField texto = new TextField();
     
    private int valor = 0;
    
    Line linePainting;
    Line lineX,lineY;
    
    Circle circlePainting;
    Circle pointPainting;
    double inicioXArc;
    @FXML
    private ToggleGroup accionesMap;
   
    
    double inicioXTrans, inicioYTrans,baseX,baseY;
    @FXML
    private ImageView imTransportador;
    @FXML
    private ImageView transportador;
    @FXML
    private ToggleButton bAyuda;
   
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        try {
          
            bAyuda.setDisable(false);
            
            
            bVerificar.setDisable(true);
            bSiguiente.disableProperty().bind(bVerificar.disableProperty());
            
            navegacion = Navegacion.getSingletonNavegacion();
            
            listaProblemas = navegacion.getProblems();
            ToggleGroup tg = answer1.getToggleGroup();
            // add a change listener
            tg.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
                public void changed(ObservableValue<? extends Toggle> ob, Toggle o, Toggle n) {
                    RadioButton rb = (RadioButton)tg.getSelectedToggle();
                    if (rb != null) {  
                        bVerificar.setDisable(false);
                    }
                }
            });
        } catch (NavegacionDAOException ex) {
            Logger.getLogger(ProblemaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // TODO
        //initData();
        //==========================================================
        // inicializamos el slider y enlazamos con el zoom
        zoom_slider.setMin(1.0);
        zoom_slider.setMax(7.0);
        zoom_slider.setValue(1.5);
        zoom_slider.valueProperty().addListener((o, oldVal, newVal) -> zoom((Double) newVal));
        
        //=========================================================================
        //Envuelva el contenido de scrollpane en un grupo para que 
        //ScrollPane vuelva a calcular las barras de desplazamiento tras el escalado
        Group contentGroup = new Group();
        zoomGroup = new Group();
        contentGroup.getChildren().add(zoomGroup);
        zoomGroup.getChildren().add(map_scrollpane.getContent());
        map_scrollpane.setContent(contentGroup);
        
        
        //Comportamiento del teextField al escribir texto:
        texto.setOnAction(e -> {
            
            Text textoT = new Text(texto.getText());
            textoT.setX(texto.getLayoutX());
            textoT.setY(texto.getLayoutY());
            
            texto.setStyle("-fx-font-family: Gafata; -fx-font-size: 18");
            zoomGroup.getChildren().add(textoT);
            texto.setText("  ");
            zoomGroup.getChildren().remove(texto);
            e.consume();      
            
             //Para borrar el texto:
                 textoT.setOnContextMenuRequested(eve -> {
                     ContextMenu menuContext = new ContextMenu();
                     MenuItem borrarItem = new MenuItem("eliminar");
                     menuContext.getItems().add(borrarItem);
                     borrarItem.setOnAction(ev -> {
                        zoomGroup.getChildren().remove((Node)eve.getSource());
                        ev.consume();
                
                 });
                 menuContext.show(textoT, eve.getScreenX(),eve.getScreenY());
                eve.consume();
                 });
        });
        
        zoom_slider.setTooltip(new Tooltip("Ampliar/desampliar carta"));
        bArco.setTooltip(new Tooltip("Dibujar arcos"));
        bCoordenadas.setTooltip(new Tooltip("Marcar un punto de extremo a extremo de la carta"));
        bLimpiar.setTooltip(new Tooltip("Borrar todos los elementos de la carta"));
        bMarcarPunto.setTooltip(new Tooltip("Dibujar puntos"));
        bRecta.setTooltip(new Tooltip("Dibujar rectas"));
        bTexto.setTooltip(new Tooltip("Insertar texto"));
        bTransportador.setTooltip(new Tooltip("Mostrar/guardar transportador"));
        colorP.setTooltip(new Tooltip("Cambiar color del trazo"));
        sliderGrosor.setTooltip(new Tooltip("Cambiar grossor del trazo"));
        bAyuda.setTooltip(new Tooltip("Mostrar ayuda"));
        
    }    


    @FXML
    private void finalizar(ActionEvent event) throws IOException {
        cerrarVentana();       
    }

    @FXML
    private void verificarSol(ActionEvent event) {
        Answer correcta = null;
        String s = "";
        for(Answer a: respuestas){
            if(a.getValidity()){correcta = a;}
        }
        
         ToggleGroup tg = answer1.getToggleGroup();         
         RadioButton rb = (RadioButton)tg.getSelectedToggle();  
                if (rb != null) {
                    s = rb.getText();  
                }
               
        if(s.equals(correcta.getText())){
             hits++;
            
            mensajeIn.setText("¡Correcto!");
            mensajeIn.setTextFill(Color.color(0, 0.8, 0));
            mensajeIn.setVisible(true);
            
        } else {
            faults++;
            mensajeIn.setText("Incorrecto");
            mensajeIn.setTextFill(Color.color(1, 0, 0));
            mensajeIn.setVisible(true);
            
        }
        //guardarHyF(hits,faults);
        answer1.setDisable(true);
        answer2.setDisable(true);
        answer3.setDisable(true);
        answer4.setDisable(true);
        bVerificar.setVisible(false);
          
    }

    @FXML
    private void pasarSig(ActionEvent event) {
        answers.getSelectedToggle().setSelected(false);
        mensajeIn.setVisible(false);
        answer1.setDisable(false);
        answer2.setDisable(false);
        answer3.setDisable(false);
        answer4.setDisable(false);
        bVerificar.setVisible(true);
        bVerificar.setDisable(true);
         List<Problem> aux = new ArrayList<Problem>();
         
          aux.addAll(listaProblemas);
          
           
           boolean rep = true;
           while(rep){
               Collections.shuffle(aux);
               if(!(problema.getText().equals(aux.get(0).getText()))){
                   rep = false;
                   initProblema(aux.get(0));
               }
           }
           
    }
    
    public void initProblema(Problem p){
        problema = p;
        lEnunciado.setText(p.getText());
        respuestas = p.getAnswers(); 
        List<Answer> aux = new ArrayList<Answer>();
        
        //método addAll usado para copiar los elementos de una lista en otra, sacado de:
        //https://www.delftstack.com/es/howto/java/copy-arraylist-java/#copiar-un-arraylist-a-otro-usando-el-m%25C3%25A9todo-clone
        aux.addAll(respuestas);
        
          
        //método shuffle usado de stackOverflow, la lista respuestas no se podía desordenar, por eso se ha creado una lista auxiliar con los mismos datos: 
        //https://es.stackoverflow.com/questions/144011/el-metodo-collection-shuffle-sirve-para-desordenar-solo-listas
        Collections.shuffle(aux);
        
        answer1.setText(aux.get(0).getText());
        answer2.setText(aux.get(1).getText());
        answer3.setText(aux.get(2).getText());
        answer4.setText(aux.get(3).getText());
        
    }
    
    
 
    public void cerrarVentana() throws IOException{
        try{
        FXMLLoader miCargador = new FXMLLoader(getClass().getResource("FXMLMenu.fxml"));
         Parent root = miCargador.load();
         
         Menu controlador = miCargador.getController();   
         Scene scene = new Scene(root);
         Stage stage = new Stage();
          stage.setTitle("Menú");
         controlador.guardarHyF(hits, faults);
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
         
        Stage este = (Stage)bVerificar.getScene().getWindow();
            este.close();
        } catch (IOException e){
            Logger.getLogger(AyudaCartaController.class.getName()).log(Level.SEVERE,null,e);
        }
    
    }

    
    
    
    
    //CARTA DE NAVEGACION:
    
    
    @FXML
     void zoomIn(ActionEvent event) {
        //================================================
        // el incremento del zoom dependerá de los parametros del 
        // slider y del resultado esperado
        double sliderVal = zoom_slider.getValue();
        zoom_slider.setValue(sliderVal += 0.1);
    }

    @FXML
    void zoomOut(ActionEvent event) {
        double sliderVal = zoom_slider.getValue();
        zoom_slider.setValue(sliderVal + -0.1);
    }
    
    // esta funcion es invocada al cambiar el value del slider zoom_slider
    private void zoom(double scaleValue) {
        //===================================================
        //guardamos los valores del scroll antes del escalado
        double scrollH = map_scrollpane.getHvalue();
        double scrollV = map_scrollpane.getVvalue();
        //===================================================
        // escalamos el zoomGroup en X e Y con el valor de entrada
        zoomGroup.setScaleX(scaleValue);
        zoomGroup.setScaleY(scaleValue);
        //===================================================
        // recuperamos el valor del scroll antes del escalado
        map_scrollpane.setHvalue(scrollH);
        map_scrollpane.setVvalue(scrollV);
    }
   
    
    @FXML
    private void muestraPosicion(MouseEvent event) {
        
        posicion.setText("         X: " + (int) event.getX() + ",          Y: " + (int) event.getY());
    }
    

    private void acercaDe(ActionEvent event) {
        Alert mensaje= new Alert(Alert.AlertType.INFORMATION);
        mensaje.setTitle("Acerca de");
        mensaje.setHeaderText("IPC - 2022");
        mensaje.showAndWait();
    }

    @FXML
    private void marcarPunto(ActionEvent event) {
        valor = 1;
    }

    @FXML
    private void trazarRecta(ActionEvent event) {
        valor = 2;
    }

    @FXML
    private void trazarArco(ActionEvent event) {
        valor = 3;
    }

    @FXML
    private void escribirTexto(ActionEvent event) {
        valor = 4;
        
    }
    

    private void borrarMarca(ActionEvent event) {
        valor = 5;
    }

    @FXML
    private void coordenadasPunto(ActionEvent event) {
       valor = 6;
        
    }

    @FXML
    private void limpiarCarta(ActionEvent event) {
        
        ObservableList<Node> nodos = zoomGroup.getChildren();
         primeraPasada=true;
        for(int i = nodos.size()-1; i >= 1;i--){
            nodos.remove(nodos.size()-1);
        }        
    }

    @FXML
    private void guardarPosicion(MouseEvent event) {
        switch(valor){
            case 1:
                pointPainting = new Circle(1);
                
                pointPainting.setStroke(colorP.getValue());
                 pointPainting.setFill(colorP.getValue());
                pointPainting.setRadius(sliderGrosor.getValue());
                zoomGroup.getChildren().add(pointPainting);
               
                pointPainting.setCenterX(event.getX());
               pointPainting.setCenterY(event.getY());
               inicioXArc = event.getX();
//               //Para borrar y cambiar color del ultimo punto hecho:
                 pointPainting.setOnContextMenuRequested(e -> {
                     ContextMenu menuContext = new ContextMenu();
                     MenuItem cambiarItem = new MenuItem("Cambiar color");
                     MenuItem borrarItem = new MenuItem("Eliminar");
                     menuContext.getItems().add(cambiarItem);
                     menuContext.getItems().add(borrarItem);
                     borrarItem.setOnAction(ev -> {
                        zoomGroup.getChildren().remove((Node)e.getSource());
                        ev.consume();
                
                     });
                      cambiarItem.setOnAction(ev -> {
                          Circle n = (Circle)e.getSource();
                           n.setStroke(colorP.getValue());
                           n.setFill(colorP.getValue());                        
                        ev.consume();                
                     });
                 menuContext.show(pointPainting, e.getScreenX(),e.getScreenY());
                e.consume();
                 });
                 
                break;
            case 2:
                linePainting = new Line(event.getX(),event.getY(),event.getX(),event.getY());
                              
                linePainting.setStroke(colorP.getValue());                
                 linePainting.setFill(colorP.getValue());
                 linePainting.setStrokeWidth(sliderGrosor.getValue());
                zoomGroup.getChildren().add(linePainting);
                //Para borrar y cambiar color del ultimo punto hecho:
                 linePainting.setOnContextMenuRequested(e -> {
                     ContextMenu menuContext = new ContextMenu();
                     MenuItem cambiarItem = new MenuItem("Cambiar color");
                     MenuItem borrarItem = new MenuItem("Eliminar");
                     menuContext.getItems().add(cambiarItem);
                     menuContext.getItems().add(borrarItem);
                     borrarItem.setOnAction(ev -> {
                        zoomGroup.getChildren().remove((Node)e.getSource());
                        ev.consume();
                
                     });
                      cambiarItem.setOnAction(ev -> {
                          Line n = (Line)e.getSource();                          
                           n.setStroke(colorP.getValue());
                           n.setFill(colorP.getValue());                        
                        ev.consume();                
                     });
                 menuContext.show(linePainting, e.getScreenX(),e.getScreenY());
                e.consume();
                 });
                break;
            case 3:
                circlePainting = new Circle(1);
                 circlePainting.setStroke(colorP.getValue());
                 circlePainting.setFill(Color.TRANSPARENT);
                
                circlePainting.setStrokeWidth(sliderGrosor.getValue());
                zoomGroup.getChildren().add(circlePainting);
                circlePainting.setCenterX(event.getX());
               circlePainting.setCenterY(event.getY());
               inicioXArc = event.getX();
               //Para borrar la ultima circunferecnia hecha:
                circlePainting.setOnContextMenuRequested(e -> {
                     ContextMenu menuContext = new ContextMenu();
                     MenuItem cambiarItem = new MenuItem("Cambiar color");
                     MenuItem borrarItem = new MenuItem("Eliminar");
                     menuContext.getItems().add(cambiarItem);
                     menuContext.getItems().add(borrarItem);
                     borrarItem.setOnAction(ev -> {
                        zoomGroup.getChildren().remove((Node)e.getSource());
                        ev.consume();
                
                     });
                      cambiarItem.setOnAction(ev -> {
                          Circle n = (Circle)e.getSource();
                           n.setStroke(colorP.getValue());
                           ev.consume();                
                     });
                 menuContext.show(circlePainting, e.getScreenX(),e.getScreenY());
                e.consume();
                 }); 
               
               break;
                
            case 4:
                 zoomGroup.getChildren().add(texto);
                 //nodos.add(texto);
                texto.setLayoutX(event.getX());
                texto.setLayoutY(event.getY());
                texto.requestFocus();               
               break;
            case 6:
                 lineX = new Line(0,event.getY(),812,event.getY());                              
                lineX.setStroke(colorP.getValue());
                
                 lineX.setStrokeWidth(1);                
                zoomGroup.getChildren().add(lineX);
                
                lineY = new Line(event.getX(),0,event.getX(),538);                              
                lineY.setStroke(colorP.getValue());                   
                 lineY.setStrokeWidth(1);                
                zoomGroup.getChildren().add(lineY);
                
                lineX.setOnContextMenuRequested(e -> {
                     ContextMenu menuContext = new ContextMenu();
                     MenuItem cambiarItem = new MenuItem("Cambiar color");
                     MenuItem borrarItem = new MenuItem("Eliminar");
                     menuContext.getItems().add(cambiarItem);
                     menuContext.getItems().add(borrarItem);
                     borrarItem.setOnAction(ev -> {
                        zoomGroup.getChildren().remove((Node)e.getSource());
                        ev.consume();
                
                     });
                      cambiarItem.setOnAction(ev -> {
                          Line n = (Line)e.getSource();
                           n.setStroke(colorP.getValue());
                           ev.consume();                
                     });
                 menuContext.show(lineX, e.getScreenX(),e.getScreenY());
                e.consume();
                 }); 
                
                 lineY.setOnContextMenuRequested(e -> {
                     ContextMenu menuContext = new ContextMenu();
                     MenuItem cambiarItem = new MenuItem("Cambiar color");
                     MenuItem borrarItem = new MenuItem("Eliminar");
                     menuContext.getItems().add(cambiarItem);
                     menuContext.getItems().add(borrarItem);
                     borrarItem.setOnAction(ev -> {
                        zoomGroup.getChildren().remove((Node)e.getSource());
                        ev.consume();
                
                     });
                      cambiarItem.setOnAction(ev -> {
                          Line n = (Line)e.getSource();
                           n.setStroke(colorP.getValue());
                                                 
                        ev.consume();                
                     });
                 menuContext.show(lineY, e.getScreenX(),e.getScreenY());
                e.consume();
                 
                 });
          }
    }

    @FXML
    private void guardarPosicion2(MouseEvent event) {
          switch(valor){
            case 1:
                break;
            case 2:
                linePainting.setEndX(event.getX());
                linePainting.setEndY(event.getY());
                event.consume();
                break;
            case 3:
                double radio = Math.abs(event.getX()-inicioXArc);
                circlePainting.setRadius(radio);
                event.consume();
                break;
                
            case 4:
                break;
           }
    }

   
     
    @FXML
    private void mostrarTransportador(ActionEvent event) {
        
        if(primeraPasada){
            imTransportador.setVisible(true); 
            //nodos.add(imTransportador);
            zoomGroup.getChildren().add(imTransportador);
            primeraPasada = false;
        } else{
            zoomGroup.getChildren().remove(imTransportador);
            primeraPasada=true;
        }
        valor = 7;
        
        imTransportador.setOnContextMenuRequested(e -> {
                     ContextMenu menuContext = new ContextMenu();
                     MenuItem borrarItem = new MenuItem("Ocultar");
                     menuContext.getItems().add(borrarItem);
                     borrarItem.setOnAction(ev -> {
                        zoomGroup.getChildren().remove((Node)e.getSource());
                        primeraPasada = false;
                        ev.consume();
                
                 });
                 menuContext.show(imTransportador, e.getScreenX(),e.getScreenY());
                e.consume();
                 });
    }

    @FXML
     private void guardarPosicionTransportador0(MouseEvent event) {
        
                imTransportador.setTranslateX(baseX + event.getSceneX() - inicioXTrans);
                imTransportador.setTranslateY(baseY + event.getSceneY() - inicioYTrans);
                event.consume();
    }
     
    @FXML
    private void guardarPosicionTransportador2(MouseEvent event) {
        double despX = event.getSceneX() - inicioXTrans;
                double despY = event.getSceneY() - inicioYTrans;
                imTransportador.setTranslateX(baseX + despX);
                imTransportador.setTranslateY(baseY + despY);
                event.consume();
    }

    @FXML
    private void guardarPosicionTransportador(MouseEvent event) {
          inicioXTrans = event.getSceneX();
                inicioYTrans = event.getSceneY();
                baseX= imTransportador.getTranslateX();
                baseY = imTransportador.getTranslateY();
                event.consume();
    }

    @FXML
    private void mostrarAyuda(ActionEvent event) throws IOException {
         FXMLLoader miCargador = new FXMLLoader(getClass().getResource("FXMLAyudaCarta.fxml"));
            Parent root = miCargador.load();           
            
            Scene scene = new Scene(root);
            Stage stage = new Stage();
             stage.setTitle("Ayuda de la carta de navegación");
             stage.setResizable(false);
             stage.initModality(Modality.APPLICATION_MODAL);
              stage.setScene(scene);
            stage.showAndWait();
            
            
             
    }
    

     

}
