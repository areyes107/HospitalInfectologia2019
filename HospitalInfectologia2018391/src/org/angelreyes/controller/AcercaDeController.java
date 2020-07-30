package org.angelreyes.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.angelreyes.sistema.Principal;


public class AcercaDeController implements Initializable {
  @FXML Button btnBack;
  
  @FXML
  private void butRegresar (ActionEvent event) throws Exception{
      if (event.getSource()==btnBack){
          LoginController.getEscenarioPrincipal().cambiarEscena("MenuPrincipalView.fxml",  600, 400);
      }
          
  }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
    
}
