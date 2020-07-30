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
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.angelreyes.bean.Especialidad;
import org.angelreyes.bean.Horario;
import org.angelreyes.bean.Medico;
import org.angelreyes.bean.MedicoEspecialidad;
import org.angelreyes.db.Conexion;


public class MedicoEspecialidadController implements Initializable{
    @FXML Button btnBack21;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoMedico.setItems(getMedicos());
        cmbCodigoEspecialidad.setItems(getEspecialidades());
        cmbCodigoHorario.setItems(getHorarios());
    }
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Medico> listaMedico;
    private ObservableList<Especialidad> listaEspecialidad;
    private ObservableList<Horario> listaHorario;
    private ObservableList<MedicoEspecialidad> listaMedicoEspecialidad;
    @FXML private ComboBox cmbCodigoMedico;
    @FXML private ComboBox cmbCodigoEspecialidad;
    @FXML private ComboBox cmbCodigoHorario;
    @FXML private TableView tblMedicoEspecialidad;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colCodigoMedico;
    @FXML private TableColumn colCodigoEspecialidad;
    @FXML private TableColumn colCodigoHorario;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    
    @FXML
  private void butRegresar21 (ActionEvent event) throws Exception{
      if (event.getSource()==btnBack21){
          LoginController.getEscenarioPrincipal().cambiarEscena("MenuPrincipalView.fxml",  600, 400);
      }
    }
  
  
  
  public ObservableList<Medico> getMedicos(){
        ArrayList<Medico> lista = new ArrayList<Medico>();
       try{
           PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarMedicos()}");
           ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Medico(resultado.getInt("codigoMedico"),
                        resultado.getInt("licenciaMedica"),
                        resultado.getString("nombres"), 
                        resultado.getString("apellidos"),
                        resultado.getString("horaEntrada"), 
                        resultado.getString("horaSalida"), 
                        resultado.getInt("turnoMaximo"),
                        resultado.getString("sexo")));
            }
       }catch(Exception e){
           e.printStackTrace();
       }
        return listaMedico = FXCollections.observableList(lista); 
                }
  
  
  
  public ObservableList<Especialidad> getEspecialidades(){
        ArrayList<Especialidad> lista = new ArrayList<Especialidad>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarEspecialidades()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Especialidad(resultado.getInt("codigoEspecialidad"),
                resultado.getString("nombreEspecialidad")));
            }
        }catch(Exception e){
            e.printStackTrace();
            
        }
        return listaEspecialidad = FXCollections.observableList(lista);
    }
  
  
  
  public ObservableList<Horario>getHorarios(){
        ArrayList<Horario> lista = new ArrayList<Horario>();
       try{
           PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarHorarios()}");
           ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Horario(resultado.getInt("codigoHorario"),
                        resultado.getString("horarioInicio"),
                        resultado.getString("horarioSalida"),
                        resultado.getBoolean("lunes"), 
                        resultado.getBoolean("martes"),
                        resultado.getBoolean("miercoles"), 
                        resultado.getBoolean("jueves"),
                        resultado.getBoolean("viernes")));
            }
       }catch(Exception e){
           e.printStackTrace();
       }
       return listaHorario = FXCollections.observableList(lista);
    }
  
  
  
  public ObservableList<MedicoEspecialidad>getMedicosEspecialidades(){
      ArrayList<MedicoEspecialidad> lista = new ArrayList<MedicoEspecialidad>();
      try{
          PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarMedicosEspecialidades()}");
           ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new MedicoEspecialidad(resultado.getInt("codigoMedicoEspecialidad"),
                        resultado.getInt("codigoMedico"),
                        resultado.getInt("codigoEspecialidad"),
                        resultado.getInt("codigoHorario")));
            }
      }catch(Exception e){
          e.printStackTrace();
      }
      return listaMedicoEspecialidad = FXCollections.observableList(lista);
  }
  
  private void cargarDatos() {
        tblMedicoEspecialidad.setItems(getMedicosEspecialidades());
        colCodigo.setCellValueFactory(new PropertyValueFactory<MedicoEspecialidad, Integer> ("codigoMedicoEspecialidad"));
        colCodigoMedico.setCellValueFactory(new PropertyValueFactory<MedicoEspecialidad, Integer>("codigoMedico"));
        colCodigoEspecialidad.setCellValueFactory(new PropertyValueFactory<MedicoEspecialidad, Integer>("codigoEspecialidad"));
        colCodigoHorario.setCellValueFactory(new PropertyValueFactory<MedicoEspecialidad, Integer>("codigoHorario"));
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
        MedicoEspecialidad registro = new MedicoEspecialidad();
        registro.setCodigoMedico(((Medico)cmbCodigoMedico.getSelectionModel().getSelectedItem()).getCodigoMedico());
        registro.setCodigoEspecialidad(((Especialidad)cmbCodigoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoEspecialidad());
        registro.setCodigoHorario(((Horario)cmbCodigoHorario.getSelectionModel().getSelectedItem()).getCodigoHorario());
        
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarMedicoEspecialidad(?,?,?)}");
            procedimiento.setInt(1, registro.getCodigoMedico());
            procedimiento.setInt(2, registro.getCodigoEspecialidad());
            procedimiento.setInt(3, registro.getCodigoHorario());
            procedimiento.execute();
            listaMedicoEspecialidad.add(registro);
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
                if(tblMedicoEspecialidad.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro que desea eliminar este registro?", "Eliminar Medico-Especialidad", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarMedicoEspecialidad(?)}");
                            procedimiento.setInt(1, ((MedicoEspecialidad)tblMedicoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoMedico());
                            procedimiento.execute();
                            listaMedico.remove(tblMedicoEspecialidad.getSelectionModel().getSelectedIndex());
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
                if(tblMedicoEspecialidad.getSelectionModel().getSelectedItem() != null){
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
             PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarMedicoEspecialidad(?,?,?,?)}");
             MedicoEspecialidad registro = (MedicoEspecialidad)tblMedicoEspecialidad.getSelectionModel().getSelectedItem();
             registro.setCodigoMedico(((Medico)cmbCodigoMedico.getSelectionModel().getSelectedItem()).getCodigoMedico());
             registro.setCodigoEspecialidad(((Especialidad)cmbCodigoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoEspecialidad());
             registro.setCodigoHorario(((Horario)cmbCodigoHorario.getSelectionModel().getSelectedItem()).getCodigoHorario());
             procedimiento.setInt(1, registro.getCodigoMedicoEspecialidad());
             procedimiento.setInt(2, registro.getCodigoMedico());
             procedimiento.setInt(3, registro.getCodigoEspecialidad());
             procedimiento.setInt(4, registro.getCodigoHorario());
             procedimiento.execute();
           
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
  
  
  public void activarControles(){
      cmbCodigoMedico.setEditable(false);
      cmbCodigoEspecialidad.setEditable(false);
      cmbCodigoHorario.setEditable(false);
  }
  
  public void desactivarControles(){
      cmbCodigoMedico.setEditable(false);
      cmbCodigoEspecialidad.setEditable(false);
      cmbCodigoHorario.setEditable(false);
  }
  
  public void limpiarControles(){
      cmbCodigoMedico.getSelectionModel().clearSelection();
      cmbCodigoMedico.setPromptText("");
      cmbCodigoEspecialidad.getSelectionModel().clearSelection();
      cmbCodigoEspecialidad.setPromptText("");
      cmbCodigoHorario.getSelectionModel().clearSelection();
      cmbCodigoHorario.setPromptText("");
  }
  
  public void seleccionarElemento(){;
        cmbCodigoMedico.setPromptText(String.valueOf(((MedicoEspecialidad) tblMedicoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoMedico()));
        cmbCodigoEspecialidad.setPromptText(String.valueOf(((MedicoEspecialidad) tblMedicoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoEspecialidad()));
        cmbCodigoHorario.setPromptText(String.valueOf(((MedicoEspecialidad) tblMedicoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoHorario()));
    }
  
  
}
