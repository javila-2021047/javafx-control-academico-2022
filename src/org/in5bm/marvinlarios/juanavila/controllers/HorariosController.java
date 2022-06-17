package org.in5bm.marvinlarios.juanavila.controllers;

import com.jfoenix.controls.JFXTimePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import java.sql.ResultSet;
import java.time.LocalTime;
import java.time.format.FormatStyle;
import java.util.Locale;
import javafx.collections.FXCollections;
import javafx.css.converter.StringConverter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.converter.LocalTimeStringConverter;

import org.in5bm.marvinlarios.juanavila.db.Conexion;
import org.in5bm.marvinlarios.juanavila.models.Horarios;
import org.in5bm.marvinlarios.juanavila.system.Principal;

/**
 *
 * @author juana
 */
public class HorariosController implements Initializable {

    @FXML
    private Button btnNuevo;

    @FXML
    private ImageView imgNuevo;

    @FXML
    private Button btnModificar;

    @FXML
    private ImageView imgModificar;

    @FXML
    private Button btnEliminar;

    @FXML
    private ImageView imgEliminar;

    @FXML
    private Button btnReporte;

    @FXML
    private ImageView imgReporte;

    @FXML
    private JFXTimePicker tmpHorarioInicio;

    @FXML
    private JFXTimePicker tmpHorarioFinal;

    @FXML
    private TextField txtId;

    @FXML
    private TableView<Horarios> tblHorarios;

    @FXML
    private TableColumn<Horarios, Integer> colId;

    @FXML
    private TableColumn<Horarios, LocalTime> colHorarioInicio;

    @FXML
    private TableColumn<Horarios, LocalTime> colHorarioFinal;

    @FXML
    private TableColumn<Horarios, Boolean> colLunes;

    @FXML
    private TableColumn<Horarios, Boolean> colMartes;

    @FXML
    private TableColumn<Horarios, Boolean> colMiercoles;

    @FXML
    private TableColumn<Horarios, Boolean> colJueves;

    @FXML
    private TableColumn<Horarios, Boolean> colViernes;

    @FXML
    private CheckBox chkLunes;

    @FXML
    private CheckBox chkMartes;

    @FXML
    private CheckBox chkMiercoles;

    @FXML
    private CheckBox chkJueves;

    @FXML
    private CheckBox chkViernes;

    private ObservableList<Horarios> listaObservableHorarios;

    private enum Operacion {
        NINGUNO, GUARDAR, ACTUALIZAR
    }

    private Operacion operacion = Operacion.NINGUNO;

    private Principal escenarioPrincipal;

    private final String PAQUETE_IMAGES = "org/in5bm/marvinlarios/juanavila/resources/images/";

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        tmpHorarioInicio.set24HourView(true);
        tmpHorarioFinal.set24HourView(true);

        var defaultConverter
                = new LocalTimeStringConverter(FormatStyle.SHORT, Locale.US);
        tmpHorarioInicio.setConverter(defaultConverter);

        var defaultConverter2
                = new LocalTimeStringConverter(FormatStyle.SHORT, Locale.UK);
        tmpHorarioFinal.setConverter(defaultConverter2);

        cargarDatos();
    }

    public void cargarDatos() {
        tblHorarios.setItems(getHorarios());
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colHorarioInicio.setCellValueFactory(new PropertyValueFactory<>("horarioInicio"));
        colHorarioFinal.setCellValueFactory(new PropertyValueFactory<>("horarioFinal"));
        colLunes.setCellValueFactory(new PropertyValueFactory<>("lunes"));
        colMartes.setCellValueFactory(new PropertyValueFactory<>("martes"));
        colMiercoles.setCellValueFactory(new PropertyValueFactory<>("miercoles"));
        colJueves.setCellValueFactory(new PropertyValueFactory<>("jueves"));
        colViernes.setCellValueFactory(new PropertyValueFactory<>("viernes"));
    }

    public boolean existeElementoSeleccionado() {
        return (tblHorarios.getSelectionModel().getSelectedItem() != null);

    }

    public boolean campoCompletado() {
        return (tmpHorarioInicio.getValue() == null || tmpHorarioFinal.getValue() == null);
    }

    @FXML
    public void seleccionarElemento() {

        if (existeElementoSeleccionado()) {

            String id = String.valueOf(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).getId());

            txtId.setText(id);

            tmpHorarioInicio.setValue(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).getHorarioInicio());

            tmpHorarioFinal.setValue(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).getHorarioFinal());

            chkLunes.setSelected(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).isLunes());

            chkMartes.setSelected(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).isMartes());

            chkMiercoles.setSelected(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).isMiercoles());

            chkJueves.setSelected(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).isJueves());

            chkViernes.setSelected(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).isViernes());
        }
    }

    private ObservableList getHorarios() {
        ArrayList<Horarios> arrayListHorarios = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_horarios_read()}");
            System.out.println(pstmt.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Horarios horario = new Horarios();
                horario.setId(rs.getInt(1));
                horario.setHorarioInicio(rs.getTime(2).toLocalTime());
                horario.setHorarioFinal(rs.getTime(3).toLocalTime());
                horario.setLunes(rs.getBoolean(4));
                horario.setMartes(rs.getBoolean(5));
                horario.setMiercoles(rs.getBoolean(6));
                horario.setJueves(rs.getBoolean(7));
                horario.setViernes(rs.getBoolean(8));
                System.out.println(horario.toString());
                arrayListHorarios.add(horario);
            }
            listaObservableHorarios = FXCollections.observableArrayList(arrayListHorarios);
        } catch (SQLException e) {
            System.err.println("\nSe produjo un error al consultar la tabla de Horarios");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return listaObservableHorarios;
    }

    //Agregar Horarios
    private boolean agregarHorario() {
        Horarios horarios = new Horarios();

        horarios.setHorarioInicio(tmpHorarioInicio.getValue());
        horarios.setHorarioFinal(tmpHorarioFinal.getValue());
        horarios.setLunes(chkLunes.isSelected());
        horarios.setMartes(chkMartes.isSelected());
        horarios.setMiercoles(chkMiercoles.isSelected());
        horarios.setJueves(chkJueves.isSelected());
        horarios.setViernes(chkViernes.isSelected());

        PreparedStatement pstmt = null;

        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_horarios_create(?, ?, ?, ?, ?, ?, ?)}");

            pstmt.setObject(1, horarios.getHorarioInicio());
            pstmt.setObject(2, horarios.getHorarioFinal());
            pstmt.setBoolean(3, horarios.isLunes());
            pstmt.setBoolean(4, horarios.isMartes());
            pstmt.setBoolean(5, horarios.isMiercoles());
            pstmt.setBoolean(6, horarios.isJueves());
            pstmt.setBoolean(7, horarios.isViernes());

            System.out.println(pstmt.toString());

            pstmt.execute();

            listaObservableHorarios.add(horarios);

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("\nSE PRODUJO UN ERROR AL INTENTAR"
                    + " INGRESAR EL SIGUIENTE REGISTRO: " + horarios.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    //Agregar Horarios
    private boolean actualizarHorario() {
        Horarios horarios = new Horarios();

        horarios.setId(Integer.parseInt(txtId.getText()));
        horarios.setHorarioInicio(tmpHorarioInicio.getValue());
        horarios.setHorarioFinal(tmpHorarioFinal.getValue());
        horarios.setLunes(chkLunes.isSelected());
        horarios.setMartes(chkMartes.isSelected());
        horarios.setMiercoles(chkMiercoles.isSelected());
        horarios.setJueves(chkJueves.isSelected());
        horarios.setViernes(chkViernes.isSelected());

        PreparedStatement pstmt = null;

        try {
            pstmt = Conexion.getInstance().getConexion()
                    .prepareCall("{CALL sp_horarios_update(? ,?, ?, ?, ?, ?, ?, ?)}");

            pstmt.setInt(1, horarios.getId());
            pstmt.setObject(2, horarios.getHorarioInicio());
            pstmt.setObject(3, horarios.getHorarioFinal());
            pstmt.setBoolean(4, horarios.isLunes());
            pstmt.setBoolean(5, horarios.isMartes());
            pstmt.setBoolean(6, horarios.isMiercoles());
            pstmt.setBoolean(7, horarios.isJueves());
            pstmt.setBoolean(8, horarios.isViernes());

            System.out.println(pstmt.toString());

            pstmt.execute();

            listaObservableHorarios.add(horarios);

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("\nSE PRODUJO UN ERROR AL INTENTAR"
                    + " INGRESAR EL SIGUIENTE REGISTRO: " + horarios.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    //Eliminar Instructor
    public boolean eliminarHorario() {
        if (existeElementoSeleccionado()) {
            Horarios horarios = (Horarios) tblHorarios.getSelectionModel().getSelectedItem();
            System.out.println(horarios.toString());

            PreparedStatement pstmt = null;

            try {
                pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_horarios_delete(?)}");
                pstmt.setInt(1, horarios.getId());
                System.out.println(pstmt);

                pstmt.execute();

                return true;

            } catch (SQLException e) {
                System.err.println("\nSE produjo un error al intentar eliminar el alumno con el siguiente registro" + horarios.toString());
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void deshabilitarCampos() {
        txtId.setEditable(false);
        txtId.setDisable(true);

        tmpHorarioInicio.setDisable(true);
        tmpHorarioFinal.setDisable(true);

        chkLunes.setDisable(true);
        chkMartes.setDisable(true);
        chkMiercoles.setDisable(true);
        chkJueves.setDisable(true);
        chkViernes.setDisable(true);
    }

    private void habilitarCampos() {
        txtId.setEditable(false);
        tmpHorarioInicio.setEditable(true);
        tmpHorarioFinal.setEditable(true);

        txtId.setDisable(true);

        tmpHorarioInicio.setDisable(false);
        tmpHorarioFinal.setDisable(false);

        chkLunes.setDisable(false);
        chkMartes.setDisable(false);
        chkMiercoles.setDisable(false);
        chkJueves.setDisable(false);
        chkViernes.setDisable(false);
    }

    private void limpiarCampos() {
        txtId.clear();
        tmpHorarioInicio.getEditor().clear();
        tmpHorarioFinal.getEditor().clear();
    }

    

    @FXML
    void clicMenuPrincipal(MouseEvent event) {
        escenarioPrincipal.mostrarEscenaPrincipal();
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }


    @FXML
    private void clicNuevo() {
        switch (operacion) {
            case NINGUNO:
                habilitarCampos();
                limpiarCampos();

                btnNuevo.setText("Guardar");
                imgNuevo.setImage(new Image(PAQUETE_IMAGES + "guardar.png"));
                btnModificar.setText("Cancelar");
                imgModificar.setImage(new Image(PAQUETE_IMAGES + "cancelar.png"));

                btnEliminar.setDisable(true);
                imgEliminar.setDisable(true);
                btnEliminar.setVisible(false);
                imgEliminar.setVisible(false);

                btnReporte.setDisable(true);
                imgReporte.setDisable(true);
                btnReporte.setVisible(false);
                imgReporte.setVisible(false);

                operacion = Operacion.GUARDAR;
                break;

            case GUARDAR:
                
                    if (agregarHorario()) {
                        cargarDatos();
                        limpiarCampos();
                        deshabilitarCampos();
                        tblHorarios.setDisable(false);

                        btnNuevo.setText("Nuevo");
                        imgNuevo.setImage(new Image(PAQUETE_IMAGES + "agregar.png"));

                        btnModificar.setText("Modificar");
                        imgModificar.setImage(new Image(PAQUETE_IMAGES + "editar.png"));

                        btnEliminar.setDisable(false);
                        imgEliminar.setDisable(false);

                        btnEliminar.setVisible(true);
                        imgEliminar.setVisible(true);

                        btnReporte.setDisable(false);
                        btnReporte.setDisable(false);

                        btnReporte.setVisible(true);
                        imgReporte.setVisible(true);

                        operacion = Operacion.NINGUNO;
                    } 

                break;
        }
    }

    @FXML
    private void clicModificar() {
        switch (operacion) {
            case NINGUNO:
                if (existeElementoSeleccionado()) {
                    habilitarCampos();
                    btnNuevo.setDisable(true);
                    imgNuevo.setDisable(true);

                    btnNuevo.setVisible(false);
                    imgNuevo.setVisible(false);

                    btnModificar.setText("Guardar");
                    imgModificar.setImage(new Image(PAQUETE_IMAGES + "guardar.png"));

                    btnEliminar.setText("Cancelar");
                    imgEliminar.setImage(new Image(PAQUETE_IMAGES + "cancelar.png"));

                    btnReporte.setDisable(true);
                    imgReporte.setDisable(true);

                    btnReporte.setVisible(false);
                    imgReporte.setVisible(false);

                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Control Académico Kinal");
                    alert.setHeaderText(null);
                    alert.setContentText("Antes de continuar seleccione un registro");
                    alert.show();
                }

                operacion = Operacion.ACTUALIZAR;
                break;

            case GUARDAR:
                btnNuevo.setText("Nuevo");
                imgNuevo.setImage(new Image(PAQUETE_IMAGES + "agregar.png"));

                btnModificar.setText("Modificar");
                imgModificar.setImage(new Image(PAQUETE_IMAGES + "editar.png"));

                btnEliminar.setDisable(false);
                imgEliminar.setDisable(false);

                btnEliminar.setVisible(true);
                imgEliminar.setVisible(true);

                btnReporte.setDisable(false);
                imgReporte.setDisable(false);

                btnReporte.setVisible(true);
                imgReporte.setVisible(true);

                limpiarCampos();
                deshabilitarCampos();
                
                tblHorarios.setDisable(false);

                operacion = Operacion.NINGUNO;
                break;

            case ACTUALIZAR:
                if (existeElementoSeleccionado()) {
                    if (actualizarHorario()) {
                        cargarDatos();
                        limpiarCampos();
                        deshabilitarCampos();
                        tblHorarios.setDisable(false);
                        tblHorarios.getSelectionModel().clearSelection();
                        
                        btnNuevo.setVisible(true);
                        imgNuevo.setVisible(true);
                        
                        btnNuevo.setDisable(false);
                        imgNuevo.setDisable(false);

                        btnNuevo.setText("Nuevo");
                        imgNuevo.setImage(new Image(PAQUETE_IMAGES + "agregar.png"));

                        btnModificar.setText("Modificar");
                        imgModificar.setImage(new Image(PAQUETE_IMAGES + "editar.png"));
                        
                        btnEliminar.setText("Eliminar");
                        imgEliminar.setImage(new Image(PAQUETE_IMAGES + "eliminar.png"));

                        btnEliminar.setDisable(false);
                        imgEliminar.setDisable(false);

                        btnEliminar.setVisible(true);
                        imgEliminar.setVisible(true);

                        btnReporte.setDisable(false);
                        btnReporte.setDisable(false);

                        btnReporte.setVisible(true);
                        imgReporte.setVisible(true);

                        operacion = Operacion.NINGUNO;
                    }
                }
                break;

        }
    }

    @FXML
    private void clicEliminar() {
        switch (operacion) {
            case ACTUALIZAR: //CANCELAR LA ACTUALIZACIÓN
                btnNuevo.setDisable(false);
                imgNuevo.setDisable(false);

                btnNuevo.setVisible(true);
                imgNuevo.setVisible(true);

                btnModificar.setText("Modificar");
                imgModificar.setImage(new Image(PAQUETE_IMAGES + "editar.png"));

                btnEliminar.setText("Eliminar");
                imgEliminar.setImage(new Image(PAQUETE_IMAGES + "eliminar.png"));

                btnReporte.setDisable(false);
                imgReporte.setDisable(false);

                btnReporte.setVisible(true);
                imgReporte.setVisible(true);

                limpiarCampos();
                deshabilitarCampos();

                operacion = Operacion.NINGUNO;

                break;

            case NINGUNO:
                if (existeElementoSeleccionado()) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Control Académico Kinal");
                    alert.setHeaderText(null);
                    Stage dialog = new Stage();
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image(PAQUETE_IMAGES + "logo.png"));
                    alert.setContentText("¿Desea eliminar el registro seleccionado?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        if (eliminarHorario()) {
                            listaObservableHorarios.remove(tblHorarios.getSelectionModel().getFocusedIndex());
                            cargarDatos();
                            alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Control Académico Kinal");
                            alert.setHeaderText(null);
                            alert.setContentText("Registro eliminado exitosamente");
                            dialog = new Stage();
                            stage = (Stage) alert.getDialogPane().getScene().getWindow();
                            stage.getIcons().add(new Image(PAQUETE_IMAGES + "logo.png"));
                            alert.show();
                        }

                    } else if (result.get() == ButtonType.CANCEL) {
                        alert.close();
                        tblHorarios.getSelectionModel().clearSelection();
                        limpiarCampos();
                    }

                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Control Académico Kinal");
                    alert.setHeaderText(null);
                    alert.setContentText("Antes de continuar seleccione un registro");
                    Stage dialog = new Stage();
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image(PAQUETE_IMAGES + "logo.png"));
                    alert.show();
                }
                break;
        }
    }

    @FXML
    private void clicReporte() {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("¡AVISO!");
        alerta.setHeaderText(null);
        alerta.setContentText("Esta opcion solo esta disponible en la versión PRO");
        Stage dialog = new Stage();
        Stage stage = (Stage) alerta.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(PAQUETE_IMAGES + "editar.png"));
        alerta.show();
    }
}