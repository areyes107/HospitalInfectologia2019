package org.angelreyes.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale; 
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.angelreyes.bean.Paciente;
import org.angelreyes.db.Conexion;
import org.angelreyes.report.GenerarReporte;


public class PacientesController implements Initializable{
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
   private ObservableList<Paciente> listaPaciente;
    private DatePicker fecha;
    @FXML private Button btnContactoUrgencia;
    @FXML private TextField txtDPI;
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtEdad;
    @FXML private GridPane grpFecha;
    @FXML private TextField txtSexo;
    @FXML private TextField txtOcupacion;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colDPI;
    @FXML private TableColumn colNombres;
    @FXML private TableColumn colApellidos;
    @FXML private TableColumn colDireccion;
    @FXML private TableColumn colEdad;
    @FXML private TableColumn colNacimiento;
    @FXML private TableColumn colSexo;
    @FXML private TableColumn colOcupacion;
    @FXML private TableView tblPacientes;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private Button btnBack3;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().setValue("Today");
        fecha.getCalendarView().setShowWeeks(false);
        fecha.getStylesheets().add("/org/angelreyes/resources/DatePicker.css");
        grpFecha.add(fecha, 0, 0);
    }
  
  @FXML
  private void butRegresar3 (ActionEvent event) throws Exception{
      if (event.getSource()==btnBack3){
          LoginController.getEscenarioPrincipal().cambiarEscena("MenuPrincipalView.fxml",  600, 400);
      }
          
  }
  
  @FXML
    private void contactoUrgencia (ActionEvent event)throws Exception{
        if (event.getSource()==btnContactoUrgencia){
          LoginController.getEscenarioPrincipal().cambiarEscena("ContactoUrgenciaView.fxml",  686, 540);
      }
    }
  
  public void nuevo(){
      switch(tipoDeOperacion){
          case NINGUNO:
              activarControles();
              limpiarControles();
              btnNuevo.setText("Guardar");
              btnEliminar.setText("Cancelar");
              btnReporte.setDisable(true);
              btnEditar.setDisable(true);
              tipoDeOperacion = operaciones.GUARDAR;
              break;
              
          case GUARDAR:
              if(txtDPI.getText().isEmpty() !=true &&
                      txtApellidos.getText().isEmpty() != true &&
                      txtNombres.getText().isEmpty() != true &&
                      txtDireccion.getText().isEmpty() != true &&
                      txtOcupacion.getText().isEmpty() != true &&
                      txtSexo.getText().isEmpty() != true){
              guardar();
              desactivarControles();
              limpiarControles();
              btnNuevo.setText("Nuevo");
              btnEliminar.setText("Eliminar");
              btnEditar.setDisable(false);
              btnReporte.setDisable(false);
              tipoDeOperacion = operaciones.NINGUNO;
              cargarDatos();
              break;
              }else{
                  JOptionPane.showMessageDialog(null, "Debe de escribir datos");
              }
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
              if(tblPacientes.getSelectionModel().getSelectedItem() != null){
                  int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro que quiere eliminar el registro?", "Eliminar Paciente", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                  if(respuesta == JOptionPane.YES_OPTION){
                      try{
                          PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarPaciente(?)}");
                          procedimiento.setInt(1, ((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getCodigoPaciente());
                        procedimiento.execute();
                        listaPaciente.remove(tblPacientes.getSelectionModel().getSelectedIndex());
                        limpiarControles();
                      }catch(Exception e){
                          e.printStackTrace();
                      }
                  }
              }else{
                  JOptionPane.showMessageDialog(null, "Debe seleccionar un registro");
              }
      }
  }
  
  public void editar(){
      switch(tipoDeOperacion){
          case NINGUNO:
              if (tblPacientes.getSelectionModel().getSelectedItem() != null){
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
          
          
  public void guardar(){
        Paciente registro = new Paciente();
        registro.setDPI(txtDPI.getText());
        registro.setApellidos(txtApellidos.getText());
        registro.setNombres(txtNombres.getText());
        registro.setFechaNacimiento(fecha.getSelectedDate());
        registro.setDireccion(txtDireccion.getText());
        registro.setOcupacion(txtOcupacion.getText());
        registro.setSexo(txtSexo.getText());
        
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarPaciente(?,?,?,?,?,?,?)}");
            procedimiento.setString(1, registro.getDPI());
            procedimiento.setString(2, registro.getApellidos());
            procedimiento.setString(3, registro.getNombres());
            procedimiento.setDate(4, new java.sql.Date(registro.getFechaNacimiento().getTime()));
            procedimiento.setString(5, registro.getDireccion());
            procedimiento.setString(6, registro.getOcupacion());
            procedimiento.setString(7, registro.getSexo());
            procedimiento.execute();
            listaPaciente.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
  
  public void actualizar(){
      try{
          PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarPaciente(?,?,?,?,?,?,?,?)}");
          Paciente registro = (Paciente) tblPacientes.getSelectionModel().getSelectedItem();
          registro.setDPI(txtDPI.getText());
          registro.setApellidos(txtApellidos.getText());
          registro.setNombres(txtNombres.getText());
          registro.setFechaNacimiento(fecha.getSelectedDate());
          registro.setDireccion(txtDireccion.getText());
          registro.setOcupacion(txtOcupacion.getText());
          registro.setSexo(txtSexo.getText());
          procedimiento.setInt(1, registro.getCodigoPaciente());
          procedimiento.setString(2, registro.getDPI());
          procedimiento.setString(3, registro.getApellidos());
          procedimiento.setString(4, registro.getNombres());
          procedimiento.setDate(5, new java.sql.Date(registro.getFechaNacimiento().getTime()));
          procedimiento.setString(6, registro.getDireccion());
          procedimiento.setString(7, registro.getOcupacion());
          procedimiento.setString(8, registro.getSexo());
          procedimiento.execute();
          
          
      }catch(Exception e){
          e.printStackTrace();
      }
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
  
  public Paciente bucarPaciente (int codigoPaciente){
        Paciente resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarPaciente(?)}");
            procedimiento.setInt(1, codigoPaciente);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Paciente( registro.getInt("codigoPaciente"),
                        registro.getString("DPI"),
                         registro.getString("apellidos"),
                        registro.getString("nombres"),
                        registro.getDate("fechaNacimiento"),
                        registro.getInt("edad"), 
                         registro.getString("direccion"), 
                        registro.getString("ocupacion"),
                        registro.getString("sexo"));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
  
  public void cargarDatos(){
        tblPacientes.setItems(getPacientes());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Paciente, Integer>("codigoPaciente"));
        colDPI.setCellValueFactory(new PropertyValueFactory<Paciente, Integer>("DPI"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<Paciente, String>("apellidos"));
        colNombres.setCellValueFactory(new PropertyValueFactory<Paciente, String>("nombres"));
        colNacimiento.setCellValueFactory(new PropertyValueFactory<Paciente, Date>("fechaNacimiento"));
        colEdad.setCellValueFactory(new PropertyValueFactory<Paciente, Integer>("edad"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Paciente, String>("direccion"));
        colOcupacion.setCellValueFactory(new PropertyValueFactory<Paciente, String>("ocupacion"));
        colSexo.setCellValueFactory(new PropertyValueFactory<Paciente, String>("sexo"));

    }
  
  
  public void generarReporte(){
        switch(tipoDeOperacion){
            case NINGUNO:
                imprimirReporte();
                limpiarControles();
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
        }
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("codigoPaciente", null);
        GenerarReporte.mostrarReporte("ReportePacientes.jasper", "Reporte de Pacientes", parametros);
        
    }
  
  public void seleccionarElemento(){
       txtDPI.setText(String.valueOf(((Paciente) tblPacientes.getSelectionModel().getSelectedItem()).getDPI()));
        txtNombres.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getNombres());
        txtApellidos.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getApellidos());
        fecha.setSelectedDate(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getFechaNacimiento());
        txtEdad.setText(String.valueOf(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getEdad()));
        txtDireccion.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getDireccion());
        txtOcupacion.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getOcupacion());
        txtSexo.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getSexo());
  }
  
  
  public void desactivarControles(){
        txtDPI.setEditable(false);
        txtNombres.setEditable(false);
        txtApellidos.setEditable(false);
        grpFecha.setDisable(true);
        txtDireccion.setEditable(false);
        txtEdad.setEditable(false);
        txtSexo.setEditable(false);
        txtOcupacion.setEditable(false);
}
    
    public void activarControles(){
        txtDPI.setEditable(true);
        txtNombres.setEditable(true);
        txtApellidos.setEditable(true);
        grpFecha.setDisable(false);
        fecha.setDisable(false);
        txtDireccion.setEditable(true);
        txtEdad.setEditable(false);
        txtSexo.setEditable(true);
        txtOcupacion.setEditable(true);
    }
    
    public void limpiarControles(){
        txtDPI.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        txtDireccion.setText("");
        txtEdad.setText("");
        txtSexo.setText("");
        txtOcupacion.setText("");
    }
  
  
  
}
