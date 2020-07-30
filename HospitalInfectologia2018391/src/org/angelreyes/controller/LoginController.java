package org.angelreyes.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;
import org.angelreyes.bean.TipoUsuario;
import org.angelreyes.bean.Usuario;
import org.angelreyes.db.Conexion;
import org.angelreyes.sistema.Principal;


public class LoginController implements Initializable{
    @FXML private Button btnLogin; 
    @FXML private Button btnRegister;
    @FXML private Button btnReporte;
    @FXML private Button btnCancelar;
    private static Principal escenarioPrincipal;
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContraseña;
    private ObservableList <TipoUsuario> listaTipoUsuario;
    @FXML private ComboBox cmbTipo;
    
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmbTipo.setItems(getTiposUsuarios());
    }
    
    /*@FXML
  private void login (ActionEvent event) throws Exception{
      if (event.getSource()==btnLogin){
          MenuPrincipalController.getEscenarioPrincipal().cambiarEscena("MenuPrincipalView.fxml",  600, 400);
      }
          
  }*/
    
    public void login(){
        try{
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarUsuario(?,?)}");
        procedimiento.setString(1, txtUsuario.getText());
        procedimiento.setString(2, txtContraseña.getText());
        ResultSet registro = procedimiento.executeQuery();
         while(registro.next()){
     if(txtUsuario.getText().equals(registro.getString("usuarioLogin")) && txtContraseña.getText().equals(registro.getString("usuarioContraseña"))){
         escenarioPrincipal.cambiarEscena("MenuPrincipalView.fxml", 600, 400);
     }else{ 
         int respuesta = JOptionPane.showConfirmDialog(null,"usuario incorrecto, desea verificar si esta registrado?", "advertencia", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
         if (respuesta == JOptionPane.YES_OPTION){
                escenarioPrincipal.cambiarEscena("UsuariosView.fxml", 600, 400);
            }else{
         }
     }
         }
        }catch(Exception e){
        e.printStackTrace();               
    }
        
        }
    
    @FXML
    private void register (ActionEvent event)throws Exception{
          escenarioPrincipal.cambiarEscena("UsuariosView.fxml", 600, 400);
      }
    
     
 
  public static Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }
     
    public static void setEscenarioPrincipal(Principal EscenarioPrincipal) {
        LoginController.escenarioPrincipal = EscenarioPrincipal;
    }
    
    public ObservableList<TipoUsuario> getTiposUsuarios(){
        ArrayList<TipoUsuario> lista = new ArrayList<TipoUsuario>();
       try{
           PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarTiposUsuarios()}");
           ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new TipoUsuario(resultado.getInt("codigoTipoUsuario"),
                        resultado.getString("descripcion")));
            }
       }catch(Exception e){
           e.printStackTrace();
       }
        return listaTipoUsuario = FXCollections.observableList(lista); 
                }
}
