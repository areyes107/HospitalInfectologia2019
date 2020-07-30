package org.angelreyes.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.angelreyes.bean.Horario;
import org.angelreyes.db.Conexion;
import org.angelreyes.report.GenerarReporte;


public class HorariosController implements Initializable {
    @FXML private Button btnBack20;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private TextField txtInicio;
    @FXML private TextField txtSalida;
    @FXML private CheckBox chkLunes;
    @FXML private CheckBox chkMartes;
    @FXML private CheckBox chkMiercoles;
    @FXML private CheckBox chkJueves;
    @FXML private CheckBox chkViernes;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colInicio;
    @FXML private TableColumn colSalida;
    @FXML private TableColumn colLunes;
    @FXML private TableColumn colMartes;
    @FXML private TableColumn colMiercoles;
    @FXML private TableColumn colJueves;
    @FXML private TableColumn colViernes;
    @FXML private TableView tblHorarios;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Horario> listaHorario;
    
    
    
    @FXML
  private void butRegresar20 (ActionEvent event) throws Exception{
      if (event.getSource()==btnBack20){
          LoginController.getEscenarioPrincipal().cambiarEscena("MenuPrincipalView.fxml", 600, 400);
      }      
  }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
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
        Horario registro = new Horario();
        registro.setHorarioInicio(txtInicio.getText());
        registro.setHorarioSalida(txtSalida.getText());
        registro.setLunes(chkLunes.isSelected());
        registro.setMartes(chkMartes.isSelected());
        registro.setMiercoles(chkMiercoles.isSelected());
        registro.setJueves(chkJueves.isSelected());
        registro.setViernes(chkViernes.isSelected());
        
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarHorario(?,?,?,?,?,?,?)}");
            procedimiento.setString(1, registro.getHorarioInicio());
            procedimiento.setString(2, registro.getHorarioSalida());
            procedimiento.setBoolean(3, registro.getLunes());
            procedimiento.setBoolean(4, registro.getMartes());
            procedimiento.setBoolean(5, registro.getMiercoles());
            procedimiento.setBoolean(6, registro.getJueves());
            procedimiento.setBoolean(7, registro.getViernes());
            procedimiento.execute();
            listaHorario.add(registro);
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
                if(tblHorarios.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro que desea eliminar este registro?", "Eliminar Horarios", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarHorario(?)}");
                            procedimiento.setInt(1, ((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getCodigoHorario());
                            procedimiento.execute();
                            listaHorario.remove(tblHorarios.getSelectionModel().getSelectedIndex());
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
                if(tblHorarios.getSelectionModel().getSelectedItem() != null){
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
             PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarHorario(?,?,?,?,?,?,?,?)}");
             Horario registro = (Horario)tblHorarios.getSelectionModel().getSelectedItem();
             registro.setHorarioInicio(txtInicio.getText());
             registro.setHorarioSalida(txtSalida.getText());
             registro.setLunes(chkLunes.isSelected());
             registro.setMartes(chkMartes.isSelected());
             registro.setMiercoles(chkMiercoles.isSelected());
             registro.setJueves(chkJueves.isSelected());
             registro.setViernes(chkViernes.isSelected());
             procedimiento.setInt(1, registro.getCodigoHorario());
             procedimiento.setString(2, registro.getHorarioInicio());
             procedimiento.setString(3, registro.getHorarioSalida());
             procedimiento.setBoolean(4, registro.getLunes());
             procedimiento.setBoolean(5, registro.getMartes());
             procedimiento.setBoolean(6, registro.getMiercoles());
             procedimiento.setBoolean(7, registro.getJueves());
             procedimiento.setBoolean(8, registro.getViernes());
             procedimiento.execute();
           
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    
    
    
    private void cargarDatos(){
        tblHorarios.setItems(getHorarios());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Horario, Integer>("codigoHorario"));
        colInicio.setCellValueFactory(new PropertyValueFactory<Horario, String>("horarioInicio"));
        colSalida.setCellValueFactory(new PropertyValueFactory<Horario, String>("horarioSalida"));
        colLunes.setCellValueFactory(new PropertyValueFactory<Horario, Boolean>("lunes"));
        colMartes.setCellValueFactory(new PropertyValueFactory<Horario, Boolean>("martes"));
        colMiercoles.setCellValueFactory(new PropertyValueFactory<Horario, Boolean>("miercoles"));
        colJueves.setCellValueFactory(new PropertyValueFactory<Horario, Boolean>("jueves"));
        colViernes.setCellValueFactory(new PropertyValueFactory<Horario, Boolean>("viernes"));
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
    
    
    public void seleccionarElemento(){
        txtInicio.setText(((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getHorarioInicio());   
        txtSalida.setText(((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getHorarioSalida());
        chkLunes.setSelected(((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getLunes());
        chkMartes.setSelected(((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getMartes());
        chkMiercoles.setSelected(((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getMiercoles());
        chkJueves.setSelected(((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getJueves());
        chkViernes.setSelected(((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getViernes());
        
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
        parametros.put("codigoHorario", null);
        GenerarReporte.mostrarReporte("ReporteHorarios.jasper", "Reporte de Horarios", parametros);
        
    }
    
    
    
    
    
    public void activarControles(){
        txtInicio.setEditable(true);
        txtSalida.setEditable(true);
        chkLunes.setDisable(false);
        chkMartes.setDisable(false);
        chkMiercoles.setDisable(false);
        chkJueves.setDisable(false);
        chkViernes.setDisable(false);
        
    }
    
    public void desactivarControles(){
        txtInicio.setEditable(false);
        txtSalida.setEditable(false);
        chkLunes.setDisable(true);
        chkMartes.setDisable(true);
        chkMiercoles.setDisable(true);
        chkJueves.setDisable(true);
        chkViernes.setDisable(true);
    }
    
    public void limpiarControles(){
        txtInicio.setText("");
        txtSalida.setText("");
    }
}
