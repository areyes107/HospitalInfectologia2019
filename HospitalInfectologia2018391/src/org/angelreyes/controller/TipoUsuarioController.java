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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.angelreyes.bean.TipoUsuario;
import org.angelreyes.db.Conexion;


public class TipoUsuarioController implements Initializable{
    @FXML private Button btnBack40; 
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colDescripcion;
    @FXML private TableView tblTipoUsuario;
    private ObservableList <TipoUsuario> listaTipoUsuario;
        
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       cargarDatos();
    }
    
    @FXML
    private void butRegresar40(ActionEvent event) throws Exception{
    if (event.getSource() ==btnBack40){
          LoginController.getEscenarioPrincipal().cambiarEscena("UsuariosView.fxml",  600, 400);
      }
}
    
    public void cargarDatos(){
        tblTipoUsuario.setItems(getTiposUsuarios());
        colCodigo.setCellValueFactory(new PropertyValueFactory<TipoUsuario, Integer>("codigoTipoUsuario"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<TipoUsuario, String>("descripcion"));

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
