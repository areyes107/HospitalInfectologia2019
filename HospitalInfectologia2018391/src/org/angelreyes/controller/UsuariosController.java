package org.angelreyes.controller;

import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.angelreyes.bean.TipoUsuario;
import org.angelreyes.bean.Usuario;
import org.angelreyes.db.Conexion;

public class UsuariosController implements Initializable{
    @FXML private Button btnBack30;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnExtra;
    @FXML private Button btnTipoUsuario;
    @FXML private TableView tblUsuario;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colUsuario;
    @FXML private TableColumn colContraseña;
    @FXML private TableColumn colEstado;
    @FXML private TableColumn colFecha;
    @FXML private TableColumn colHora;
    @FXML private TableColumn colTipo;
    @FXML private TextField txtUsuario;
    @FXML private TextField txtContraseña;
    @FXML private ComboBox cmbTipoUsuario;
    private ObservableList <Usuario> listaUsuario;
    private ObservableList <TipoUsuario> listaTipoUsuario;
    private enum operaciones{AGREGAR, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbTipoUsuario.setItems(getTiposUsuarios());
    }
    
    @FXML
    private void butRegresar30(ActionEvent event) throws Exception{
    if (event.getSource() ==btnBack30){
          LoginController.getEscenarioPrincipal().cambiarEscena("LoginView.fxml",  448, 316);
      }
}
    
    @FXML 
    private void tipoUsuario (ActionEvent event)throws Exception{
        if (event.getSource() == btnTipoUsuario){
            LoginController.getEscenarioPrincipal().cambiarEscena("TipoUsuarioView.fxml", 600, 316);
        }
    }
    
    public void agregar(){
        switch (tipoDeOperacion){
        case NINGUNO:
            limpiarControles();
            activarControles();
            btnAgregar.setText("Guardar");
            btnEliminar.setText("Cancelar");
            btnEditar.setDisable(true);
            tipoDeOperacion = operaciones.GUARDAR;
            break;
            
        case GUARDAR:
            if(txtUsuario.getText().isEmpty() != true &&
                    txtContraseña.getText().isEmpty() != true ){
            guardar();
            limpiarControles();
            btnAgregar.setText("Agregar");
            btnEliminar.setText("Eliminar");
            btnEditar.setDisable(false);
            tipoDeOperacion = operaciones.NINGUNO;
            cargarDatos();
            desactivarControles();
            }else{
                JOptionPane.showMessageDialog(null, "Debe de escribir datos");
            }
            break;
        }
    }
    
    public void guardar(){
        Usuario registro = new Usuario();
        registro.setUsuarioLogin(txtUsuario.getText());
        registro.setUsuarioContraseña(txtContraseña.getText());
        registro.setCodigoTipoUsuario(((TipoUsuario)cmbTipoUsuario.getSelectionModel().getSelectedItem()).getCodigoTipoUsuario());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarUsuario(?,?,?)}");
            procedimiento.setString(1, registro.getUsuarioLogin());
            procedimiento.setString(2, registro.getUsuarioContraseña());
            procedimiento.setInt(3, registro.getCodigoTipoUsuario());
            procedimiento.execute();
            listaUsuario.add(registro);
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        
    }
    
    public void eliminar(){
    switch(tipoDeOperacion){
        case GUARDAR:
            desactivarControles();
            limpiarControles();
            btnAgregar.setText("Agregar");
            btnEliminar.setText("Eliminar");
            btnEditar.setDisable(false);
            tipoDeOperacion = operaciones.NINGUNO;
            break;
        default:
            if(tblUsuario.getSelectionModel().getSelectedItem() != null){
                int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?", "Eliminar Usuario", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(respuesta == JOptionPane.YES_OPTION){
                    try{
                        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarUsuario(?)}");
                        procedimiento.setInt(1, ((Usuario)tblUsuario.getSelectionModel().getSelectedItem()).getCodigoUsuario());
                        procedimiento.execute();
                        listaUsuario.remove(tblUsuario.getSelectionModel().getSelectedIndex());
                        limpiarControles();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }else{
                JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
            }
    }    
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblUsuario.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnExtra.setText("Cancelar");
                    btnExtra.setDisable(false);
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar un elemento");
                    
                }
                break;
            case ACTUALIZAR:
                actualizar();
                desactivarControles();
                limpiarControles();
                btnEditar.setText("Editar");
                btnExtra.setText("");
                btnExtra.setDisable(true);
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarUsuario(?,?,?)}");
            Usuario registro = (Usuario)tblUsuario.getSelectionModel().getSelectedItem();
            registro.setUsuarioLogin(txtUsuario.getText());
            registro.setUsuarioContraseña(txtContraseña.getText());
            procedimiento.setInt(1, registro.getCodigoUsuario());
            procedimiento.setString(2, registro.getUsuarioLogin());
            procedimiento.setString(3, registro.getUsuarioContraseña());
            procedimiento.execute();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    public void cargarDatos(){
        tblUsuario.setItems(getUsuarios());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Usuario, Integer>("codigoUsuario"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<Usuario, String>("usuarioLogin"));
        colContraseña.setCellValueFactory(new PropertyValueFactory<Usuario, String>("usuarioContraseña"));
        colEstado.setCellValueFactory(new PropertyValueFactory<Usuario, Boolean>("usuarioEstado"));
        colFecha.setCellValueFactory(new PropertyValueFactory<Usuario, Date>("usuarioFecha"));
        colHora.setCellValueFactory(new PropertyValueFactory<Usuario, String>("usuarioHora"));
        colTipo.setCellValueFactory(new PropertyValueFactory<Usuario, Integer>("codigoTipoUsuario"));
    }
    
    public void seleccionarElemento(){
        txtUsuario.setText(((Usuario)tblUsuario.getSelectionModel().getSelectedItem()).getUsuarioLogin());
        txtContraseña.setText(((Usuario)tblUsuario.getSelectionModel().getSelectedItem()).getUsuarioContraseña());
        cmbTipoUsuario.setPromptText(String.valueOf(((Usuario) tblUsuario.getSelectionModel().getSelectedItem()).getCodigoTipoUsuario()));
    }
    
    public ObservableList<Usuario> getUsuarios(){
        ArrayList<Usuario> lista = new ArrayList<Usuario>();
       try{
           PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarUsuarios()}");
           ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Usuario(resultado.getInt("codigoUsuario"),
                        resultado.getString("usuarioLogin"),
                        resultado.getString("usuarioContraseña"), 
                        resultado.getBoolean("usuarioEstado"),
                        resultado.getDate("usuarioFecha"),
                        resultado.getString("usuarioHora"),
                        resultado.getInt("codigoTipoUsuario")));
            }
       }catch(Exception e){
           e.printStackTrace();
       }
        return listaUsuario = FXCollections.observableList(lista); 
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
    
    public void activarControles(){
        txtUsuario.setEditable(true);
        txtContraseña.setEditable(true);
    }
    
    public void desactivarControles(){
        txtUsuario.setEditable(false);
        txtContraseña.setEditable(false);
    }
    
    public void limpiarControles(){
        txtUsuario.setText("");
        txtContraseña.setText("");
    }
    
}
                                                                               