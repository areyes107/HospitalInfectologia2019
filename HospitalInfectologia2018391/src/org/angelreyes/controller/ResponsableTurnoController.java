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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.angelreyes.bean.Area;
import org.angelreyes.bean.Cargo;
import org.angelreyes.bean.ResponsableTurno;
import org.angelreyes.db.Conexion;


public class ResponsableTurnoController implements Initializable{

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoArea.setItems(getAreas());
        cmbCodigoCargo.setItems(getCargos());
    }
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<ResponsableTurno> listaResponsableTurno;
    private ObservableList<Area> listaArea;
    private ObservableList<Cargo> listaCargo;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtTelefono;
    @FXML private TableView tblResponsableTurno;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colNombre;
    @FXML private TableColumn colApellidos;
    @FXML private TableColumn colTelefono;
    @FXML private TableColumn colCodigoArea;
    @FXML private TableColumn colCodigoCargo;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private Button btnBack23;
    @FXML private Button btnTurno;
    @FXML private ComboBox cmbCodigoArea;
    @FXML private ComboBox cmbCodigoCargo;
    
    
    @FXML
  private void butRegresar23 (ActionEvent event) throws Exception{
      if (event.getSource()==btnBack23){
          LoginController.getEscenarioPrincipal().cambiarEscena("AreasView.fxml", 614, 547);
      }      
  }

  private void cargarDatos(){
      tblResponsableTurno.setItems(getResponsableTurnos());
      colCodigo.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, Integer>("codigoResponsableTurno"));
        colNombre.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, String>("nombreResponsable"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, String>("apellidosResponsable"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, String>("telefonoPersonal"));
        colCodigoArea.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, Integer>("codigoArea"));
        colCodigoCargo.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, Integer>("codigoCargo"));
  }
  
  @FXML
    private void Turno (ActionEvent event)throws Exception{
        if (event.getSource()==btnTurno){
          LoginController.getEscenarioPrincipal().cambiarEscena("TurnoView.fxml",  856, 479);
      }
    }
  
  
  public ObservableList<ResponsableTurno>getResponsableTurnos(){
        ArrayList<ResponsableTurno> lista = new ArrayList<ResponsableTurno>();
       try{
           PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarResponsablesTurnos()}");
           ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new ResponsableTurno(resultado.getInt("codigoResponsableTurno"),
                        resultado.getString("nombreResponsable"),
                        resultado.getString("apellidosResponsable"), 
                        resultado.getString("telefonoPersonal"),
                        resultado.getInt("codigoArea"),
                        resultado.getInt("codigoCargo")));
            }
       }catch(Exception e){
           e.printStackTrace();
       }
       
                
     return listaResponsableTurno = FXCollections.observableList(lista);
    }
  
  public ObservableList<Area> getAreas(){
        ArrayList<Area> lista = new ArrayList<Area>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarAreas()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Area(resultado.getInt("codigoArea"),
                resultado.getString("nombreArea")));
            }
        }catch(Exception e){
            e.printStackTrace();
            
        }
        return listaArea = FXCollections.observableList(lista);
    }
  
  public ObservableList<Cargo> getCargos(){
        ArrayList<Cargo> lista = new ArrayList<Cargo>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarCargos()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Cargo(resultado.getInt("codigoCargo"),
                resultado.getString("nombreCargo")));
            }
        }catch(Exception e){
            e.printStackTrace();
            
        }
        return listaCargo = FXCollections.observableList(lista);
    }
  
  public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancerlar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                limpiarControles();
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                agregar();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
  
  public void agregar(){
        ResponsableTurno registro = new ResponsableTurno();
        registro.setNombreResponsable(txtNombre.getText());
        registro.setApellidosResponsable(txtApellidos.getText());
        registro.setTelefonoPersonal(txtTelefono.getText());
        registro.setCodigoArea(((Area)cmbCodigoArea.getSelectionModel().getSelectedItem()).getCodigoArea());
        registro.setCodigoCargo(((Cargo)cmbCodigoCargo.getSelectionModel().getSelectedItem()).getCodigoCargo());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarResponsableTurno(?,?,?,?,?)}");
            procedimiento.setString(1, registro.getNombreResponsable());
            procedimiento.setString(2, registro.getApellidosResponsable());
            procedimiento.setString(3, registro.getTelefonoPersonal());
            procedimiento.setInt(4, registro.getCodigoArea());
            procedimiento.setInt(5, registro.getCodigoCargo());
            procedimiento.execute();
            listaResponsableTurno.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
  
  
  public void eliminar(){
        switch (tipoDeOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
                
            default:
                if(tblResponsableTurno.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro que desea eliminar este registro?", "Eliminar Responsable de Turno", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarResponsableTurno(?)}");
                            procedimiento.setInt(1, ((ResponsableTurno)tblResponsableTurno.getSelectionModel().getSelectedItem()).getCodigoResponsableTurno());
                            procedimiento.execute();
                            listaResponsableTurno.remove(tblResponsableTurno.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            cargarDatos();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar un elemento");
                }
                
        }
    }
  
  
  public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblResponsableTurno.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
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
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
  
  public void actualizar(){
        try{
             PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarResponsableTurno(?,?,?,?,?,?)}");
             ResponsableTurno registro = (ResponsableTurno)tblResponsableTurno.getSelectionModel().getSelectedItem();
             registro.setNombreResponsable(txtNombre.getText());
             registro.setApellidosResponsable(txtApellidos.getText());
         registro.setTelefonoPersonal(txtTelefono.getText());
             registro.setCodigoArea(((Area)cmbCodigoArea.getSelectionModel().getSelectedItem()).getCodigoArea());
             registro.setCodigoCargo(((Cargo)cmbCodigoCargo.getSelectionModel().getSelectedItem()).getCodigoCargo());
             procedimiento.setInt(1, registro.getCodigoResponsableTurno());
             procedimiento.setString(2, registro.getNombreResponsable());
             procedimiento.setString(3, registro.getApellidosResponsable());
             procedimiento.setString(4, registro.getTelefonoPersonal());
             procedimiento.setInt(5, registro.getCodigoArea());
             procedimiento.setInt(6, registro.getCodigoCargo());
             procedimiento.execute();
           
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
  
  
  
  public void seleccionarElemento(){
        txtNombre.setText(((ResponsableTurno)tblResponsableTurno.getSelectionModel().getSelectedItem()).getNombreResponsable());   
        txtApellidos.setText(((ResponsableTurno)tblResponsableTurno.getSelectionModel().getSelectedItem()).getApellidosResponsable());
        txtTelefono.setText(((ResponsableTurno)tblResponsableTurno.getSelectionModel().getSelectedItem()).getTelefonoPersonal());
        cmbCodigoArea.setPromptText(String.valueOf(((ResponsableTurno) tblResponsableTurno.getSelectionModel().getSelectedItem()).getCodigoArea()));
        cmbCodigoCargo.setPromptText(String.valueOf(((ResponsableTurno) tblResponsableTurno.getSelectionModel().getSelectedItem()).getCodigoCargo()));
    }
  
  
  public void activarControles(){
      txtNombre.setEditable(true);
      txtApellidos.setEditable(true);
      txtTelefono.setEditable(true);
      cmbCodigoArea.setEditable(false);
      cmbCodigoCargo.setEditable(false);
  }
  
  public void desactivarControles(){
      txtNombre.setEditable(false);
      txtApellidos.setEditable(false);
      txtTelefono.setEditable(false);
      cmbCodigoArea.setEditable(false);
      cmbCodigoCargo.setEditable(false);
  }
  
  public void limpiarControles(){
      txtNombre.setText("");
      txtApellidos.setText("");
      txtTelefono.setText("");
     cmbCodigoArea.getSelectionModel().clearSelection();
     cmbCodigoArea.setPromptText("");
     cmbCodigoCargo.getSelectionModel().clearSelection();
     cmbCodigoCargo.setPromptText("");
  }    
}
