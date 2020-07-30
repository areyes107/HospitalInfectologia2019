package org.angelreyes.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import org.angelreyes.sistema.Principal;


public class MenuPrincipalController implements Initializable{
    private static Principal escenarioPrincipal; 
    
    @FXML MenuItem mnItem, mnMedicos, mnPacientes, mnEspecialidades, mnAreas, mnCargos;
    
    @FXML
    private void cuadroAcerca(ActionEvent event)throws Exception{
        LoginController.getEscenarioPrincipal().cambiarEscena("AcercaDeView.fxml", 300, 356 );
    }
    @FXML
    private void cuadroMedicos (ActionEvent event) throws Exception{
        LoginController.getEscenarioPrincipal().cambiarEscena("MedicosView.fxml", 796, 443);
    }
    
    @FXML
    private void cuadroPacientes (ActionEvent event) throws Exception{
        LoginController.getEscenarioPrincipal().cambiarEscena("PacientesView.fxml", 915, 566);
    }
    
     @FXML
    private void cuadroEspecialidades (ActionEvent event) throws Exception{
        LoginController.getEscenarioPrincipal().cambiarEscena("EspecialidadesView.fxml", 600, 531);
    }
    
    @FXML
    private void cuadroAreas (ActionEvent event) throws Exception{
        LoginController.getEscenarioPrincipal().cambiarEscena("AreasView.fxml", 614, 547);
    }
    
    @FXML 
    private void cuadroCargos (ActionEvent event)throws Exception{
        LoginController.getEscenarioPrincipal().cambiarEscena("CargosView.fxml", 601, 556);
    }
    
    @FXML
    private void cuadroHorarios (ActionEvent event)throws Exception{
        LoginController.getEscenarioPrincipal().cambiarEscena("HorarioView.fxml", 600, 400);
    }
    
    @FXML 
    private void cuadroMedicoEspecialidad (ActionEvent event)throws Exception{
        LoginController.getEscenarioPrincipal().cambiarEscena("MedicoEspecialidadView.fxml", 600, 492);
    }
    
    @FXML
    private void login (ActionEvent event)throws Exception{
          LoginController.getEscenarioPrincipal().cambiarEscena("MenuPrincipalView.fxml", 600, 400);
      }
    
  @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }

     public static Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }
     
    public static void setEscenarioPrincipal(Principal EscenarioPrincipal) {
        MenuPrincipalController.escenarioPrincipal = EscenarioPrincipal;
    }
    
}
