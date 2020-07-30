package org.angelreyes.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
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
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.angelreyes.bean.MedicoEspecialidad;
import org.angelreyes.bean.Paciente;
import org.angelreyes.bean.ResponsableTurno;
import org.angelreyes.bean.Turno;
import org.angelreyes.db.Conexion;


public class TurnoController implements Initializable {
 
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        fechaTurno = new DatePicker(Locale.ENGLISH);
        fechaTurno.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fechaTurno.getCalendarView().todayButtonTextProperty().setValue("Today");
        fechaTurno.getCalendarView().setShowWeeks(false);
        fechaTurno.getStylesheets().add("/org/angelreyes/resources/DatePicker.css");
        grpFechaTurno.add(fechaTurno, 0, 0);
        fechaCita = new DatePicker(Locale.ENGLISH);
        fechaCita.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fechaCita.getCalendarView().todayButtonTextProperty().setValue("Today");
        fechaCita.getCalendarView().setShowWeeks(false);
        fechaCita.getStylesheets().add("/org/angelreyes/resources/DatePicker.css");
        grpFechaCita.add(fechaCita, 0, 0);
        cmbCodigoMedicoEspecialidad.setItems(getMedicosEspecialidades());
        cmbCodigoResponsableTurno.setItems(getResponsableTurnos());
        cmbCodigoPaciente.setItems(getPacientes());
    }
    @FXML private Button btnBack24;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Turno> listaTurno;
    private ObservableList<MedicoEspecialidad> listaMedicoEspecialidad;
    private ObservableList<ResponsableTurno> listaResponsableTurno;
    private ObservableList<Paciente> listaPaciente;
    private DatePicker fechaTurno;
    private DatePicker fechaCita;
    @FXML private TextField txtValorCita;
    @FXML private GridPane grpFechaTurno;
    @FXML private GridPane grpFechaCita;
    @FXML private ComboBox cmbCodigoMedicoEspecialidad;
    @FXML private ComboBox cmbCodigoResponsableTurno;
    @FXML private ComboBox cmbCodigoPaciente;
    @FXML private TableView tblTurno;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colFechaTurno;
    @FXML private TableColumn colFechaCita;
    @FXML private TableColumn colValorCita;
    @FXML private TableColumn colCodigoMedicoEspecialidad;
    @FXML private TableColumn colCodigoResponsableTurno;
    @FXML private TableColumn colCodigoPaciente;
     @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    @FXML
  private void butRegresar24 (ActionEvent event) throws Exception{
      if (event.getSource()==btnBack24){
          LoginController.getEscenarioPrincipal().cambiarEscena("ResponsableTurnoView.fxml", 702, 451);
      }      
  }
  
  private void cargarDatos(){
      tblTurno.setItems(getTurnos());
      colCodigo.setCellValueFactory(new PropertyValueFactory<Turno, Integer>("codigoTurno"));
      colFechaTurno.setCellValueFactory(new PropertyValueFactory<Turno, Date>("fechaTurno"));
      colFechaCita.setCellValueFactory(new PropertyValueFactory<Turno, Date>("fechaCita"));
      colValorCita.setCellValueFactory(new PropertyValueFactory<Turno, Double>("valorCita"));
      colCodigoMedicoEspecialidad.setCellValueFactory(new PropertyValueFactory<Turno, Integer>("codigoMedicoEspecialidad"));
      colCodigoResponsableTurno.setCellValueFactory(new PropertyValueFactory<Turno, Integer>("codigoResponsableTurno"));
      colCodigoPaciente.setCellValueFactory(new PropertyValueFactory<Turno, Integer>("codigoPaciente"));
  }
  
  public ObservableList<Turno>getTurnos(){
        ArrayList<Turno> lista = new ArrayList<Turno>();
       try{
           PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarTurnos()}");
           ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Turno(resultado.getInt("codigoTurno"),
                        resultado.getDate("fechaTurno"),
                        resultado.getDate("fechaCita"), 
                        resultado.getDouble("valorCita"),
                        resultado.getInt("codigoMedicoEspecialidad"),
                        resultado.getInt("codigoResponsableTurno"),
                        resultado.getInt("codigoPaciente")));
            }
       }catch(Exception e){
           e.printStackTrace();
       }
       
                
     return listaTurno = FXCollections.observableList(lista);
    }
  
  public ObservableList<Paciente> getPacientes(){
        ArrayList<Paciente> lista = new ArrayList<Paciente>();
       try{
           PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarPacientes()}");
           ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Paciente(resultado.getInt("codigoPaciente"),
                        resultado.getString("DPI"),
                         resultado.getString("apellidos"),
                        resultado.getString("nombres"),
                        resultado.getDate("fechaNacimiento"),
                        resultado.getInt("edad"), 
                         resultado.getString("direccion"), 
                        resultado.getString("ocupacion"),
                        resultado.getString("sexo")));
            }
       }catch(Exception e){
           e.printStackTrace();
       }
        return listaPaciente = FXCollections.observableList(lista); 
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
        Turno registro = new Turno();
        registro.setFechaTurno(fechaTurno.getSelectedDate());
        registro.setFechaCita(fechaCita.getSelectedDate());
        registro.setValorCita(Double.valueOf(txtValorCita.getText()));
        registro.setCodigoMedicoEspecialidad(((MedicoEspecialidad)cmbCodigoMedicoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoMedicoEspecialidad());
        registro.setCodigoResponsableTurno(((ResponsableTurno)cmbCodigoResponsableTurno.getSelectionModel().getSelectedItem()).getCodigoResponsableTurno());
        registro.setCodigoPaciente(((Paciente)cmbCodigoPaciente.getSelectionModel().getSelectedItem()).getCodigoPaciente());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarTurno(?,?,?,?,?,?)}");
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaTurno().getTime()));
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaCita().getTime()));
            procedimiento.setDouble(3, registro.getValorCita());
            procedimiento.setInt(4, registro.getCodigoMedicoEspecialidad());
            procedimiento.setInt(5, registro.getCodigoResponsableTurno());
            procedimiento.setInt(6, registro.getCodigoPaciente());
            procedimiento.execute();
            listaTurno.add(registro);
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
                if(tblTurno.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro que desea eliminar este registro?", "Eliminar Turno", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarTurno(?)}");
                            procedimiento.setInt(1, ((Turno)tblTurno.getSelectionModel().getSelectedItem()).getCodigoTurno());
                            procedimiento.execute();
                            listaTurno.remove(tblTurno.getSelectionModel().getSelectedIndex());
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
                if(tblTurno.getSelectionModel().getSelectedItem() != null){
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
             PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarTurno(?,?,?,?,?,?,?)}");
             Turno registro = (Turno)tblTurno.getSelectionModel().getSelectedItem();
             registro.setFechaTurno(fechaTurno.getSelectedDate());
             registro.setFechaCita(fechaCita.getSelectedDate());
            registro.setValorCita(Double.valueOf(txtValorCita.getText()));
             registro.setCodigoMedicoEspecialidad(((MedicoEspecialidad)cmbCodigoMedicoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoMedicoEspecialidad());
             registro.setCodigoResponsableTurno(((ResponsableTurno)cmbCodigoResponsableTurno.getSelectionModel().getSelectedItem()).getCodigoResponsableTurno());
             registro.setCodigoPaciente(((Paciente)cmbCodigoPaciente.getSelectionModel().getSelectedItem()).getCodigoPaciente());
             procedimiento.setInt(1, registro.getCodigoTurno());
             procedimiento.setDate(2, new java.sql.Date(registro.getFechaTurno().getTime()));
            procedimiento.setDate(3, new java.sql.Date(registro.getFechaCita().getTime()));
            procedimiento.setDouble(4, registro.getValorCita());
            procedimiento.setInt(5, registro.getCodigoMedicoEspecialidad());
            procedimiento.setInt(6, registro.getCodigoResponsableTurno());
            procedimiento.setInt(7, registro.getCodigoPaciente());
             procedimiento.execute();
           
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
  
  
  public void seleccionarElemento(){
    //    txtValorCita.setText(((Turno)tblTurno.getSelectionModel().getSelectedItem()).getValorCita());
        cmbCodigoMedicoEspecialidad.setPromptText(String.valueOf(((Turno) tblTurno.getSelectionModel().getSelectedItem()).getCodigoMedicoEspecialidad()));
        cmbCodigoResponsableTurno.setPromptText(String.valueOf(((Turno) tblTurno.getSelectionModel().getSelectedItem()).getCodigoResponsableTurno()));
        cmbCodigoPaciente.setPromptText(String.valueOf(((Turno) tblTurno.getSelectionModel().getSelectedItem()).getCodigoPaciente()));
        fechaTurno.setSelectedDate(((Turno)tblTurno.getSelectionModel().getSelectedItem()).getFechaTurno());
        fechaCita.setSelectedDate(((Turno)tblTurno.getSelectionModel().getSelectedItem()).getFechaCita());
    }
    
  public void activarControles(){
      txtValorCita.setEditable(true);
      cmbCodigoMedicoEspecialidad.setEditable(false);
      cmbCodigoResponsableTurno.setEditable(false);
      cmbCodigoPaciente.setEditable(false);
      
  }
  
  public void desactivarControles(){
      txtValorCita.setEditable(false);
      cmbCodigoMedicoEspecialidad.setEditable(false);
      cmbCodigoResponsableTurno.setEditable(false);
      cmbCodigoPaciente.setEditable(false);
      
  }
  
  public void limpiarControles(){
      txtValorCita.setText("");
     cmbCodigoMedicoEspecialidad.getSelectionModel().clearSelection();
     cmbCodigoMedicoEspecialidad.setPromptText("");
     cmbCodigoResponsableTurno.getSelectionModel().clearSelection();
     cmbCodigoResponsableTurno.setPromptText("");
     cmbCodigoPaciente.getSelectionModel().clearSelection();
     cmbCodigoPaciente.setPromptText("");
  }   
    
}
