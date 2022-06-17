package org.in5bm.marvinlarios.juanavila.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.in5bm.marvinlarios.juanavila.models.Alumnos;
import org.in5bm.marvinlarios.juanavila.models.AsignacionesAlumnos;
import org.in5bm.marvinlarios.juanavila.models.Cursos;

/**
 *
 * @author jUAN Carlos Avila Flores
 * Carnet:2021047
 */
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.in5bm.marvinlarios.juanavila.db.Conexion;

import org.in5bm.marvinlarios.juanavila.models.AsignacionesAlumnos;
import org.in5bm.marvinlarios.juanavila.models.Alumnos;
import org.in5bm.marvinlarios.juanavila.models.Cursos;

import org.in5bm.marvinlarios.juanavila.system.Principal;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author Juan Avila
 *
 *
 *
 */
public class AsignacionesAlumnosController implements Initializable {

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
    private TextField txtId;
    @FXML
    private ImageView imgRegresar;
    @FXML
    private TableView<AsignacionesAlumnos> tblAsignacionesAlumnos; // New
    @FXML
    private TableColumn<AsignacionesAlumnos, Integer> colId;
    @FXML
    private TableColumn<AsignacionesAlumnos, String> colCarne;
    @FXML
    private TableColumn<AsignacionesAlumnos, Integer> colCursoId;
    @FXML
    private TableColumn<AsignacionesAlumnos, LocalDateTime> colFechaAsignacion;

    @FXML
    private ComboBox<Alumnos> cmbAlumnos;
    @FXML
    private ComboBox<Cursos> cmbCurso;

    private Principal escenarioPrincipal;
    @FXML
    private DatePicker dpkFechaAsignacion;

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    @FXML
    private void clicRegresar(MouseEvent event) {
        escenarioPrincipal.mostrarEscenaPrincipal();
    }

    private enum Operacion {
        NINGUNO, GUARDAR, ACTUALIZAR
    }

    private Operacion operacion = Operacion.NINGUNO;

    private final String PAQUETE_IMAGES = "org/in5bm/marvinlarios/juanavila/resources/images/";

    private ObservableList<Alumnos> listaObservableAlumnos;
    private ObservableList<Cursos> listaObservableCursos;
    private ObservableList<AsignacionesAlumnos> listaObservableAsignaciones;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
    }

    private void deshabilitarCampos() {
        txtId.setEditable(false);
        cmbCurso.setEditable(false);
        cmbAlumnos.setEditable(false);
        dpkFechaAsignacion.setEditable(false);

        txtId.setDisable(true);
        cmbCurso.setDisable(true);
        cmbAlumnos.setDisable(true);
        dpkFechaAsignacion.setDisable(true);
    }

    private void habilitarCampos() {
        txtId.setDisable(false);
        dpkFechaAsignacion.setEditable(true);
        cmbCurso.setDisable(false);
        cmbAlumnos.setDisable(false);
        dpkFechaAsignacion.setDisable(false);
    }

    private void limpiarCampos() {
        txtId.clear();

        cmbCurso.valueProperty().set(null);
        cmbAlumnos.valueProperty().set(null);
        dpkFechaAsignacion.getEditor().clear();
    }

    public ObservableList getAlumnos() {
        ArrayList<Alumnos> arrayListAlumnos = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = Conexion.getInstance().getConexion()
                    .prepareCall("{CALL sp_alumnos_read()}");
            System.out.println(pstmt.toString());

            rs = pstmt.executeQuery();

            while (rs.next()) {
                // Opción 1:
                Alumnos alumno = new Alumnos();
                alumno.setCarne(rs.getString(1));
                alumno.setNombre1(rs.getString(2));
                alumno.setNombre2(rs.getString(3));
                alumno.setNombre3(rs.getString(4));
                alumno.setApellido1(rs.getString(5));
                alumno.setApellido2(rs.getString(6));
                System.out.println(alumno.toString());
                arrayListAlumnos.add(alumno);
            }
            listaObservableAlumnos = FXCollections.observableArrayList(arrayListAlumnos);
        } catch (SQLException e) {
            System.err.println("\nSe produjo un error al intentar listar la tabla de Alumnos");
            System.err.println("Message: " + e.getMessage());
            System.err.println("Error code: " + e.getErrorCode());
            System.err.println("SQLState: " + e.getSQLState());
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
        return listaObservableAlumnos;
    }

    // read -> Lista todos los registros
    public ObservableList getAsignacionesAlumnos() {
        ArrayList<AsignacionesAlumnos> arrayListAsignaciones = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = Conexion.getInstance().getConexion()
                    .prepareCall("{CALL sp_asignaciones_alumnos_read()}");

            System.out.println(pstmt.toString());

            rs = pstmt.executeQuery();

            while (rs.next()) {
                AsignacionesAlumnos asignacion = new AsignacionesAlumnos();
                asignacion.setId(rs.getInt("id"));
                asignacion.setAlumnoId(rs.getString("alumno_id"));
                asignacion.setCursoId(rs.getInt("curso_id"));
                asignacion.setFechaAsignacion(rs.getTimestamp("fecha_asignacion").toLocalDateTime());

                System.out.println(asignacion);

                arrayListAsignaciones.add(asignacion);
            }

            listaObservableAsignaciones = FXCollections.observableArrayList(arrayListAsignaciones);

        } catch (SQLException e) {
            System.err.println("\nSe produjo un error al intentar listar la tabla de Alumnos");
            System.err.println("Message: " + e.getMessage());
            System.err.println("Error code: " + e.getErrorCode());
            System.err.println("SQLState: " + e.getSQLState());
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

        return listaObservableAsignaciones;
    }

    public void cargarDatos() {
        tblAsignacionesAlumnos.setItems(getAsignacionesAlumnos());
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCarne.setCellValueFactory(new PropertyValueFactory<>("alumnoId"));
        colCursoId.setCellValueFactory(new PropertyValueFactory<>("cursoId"));
        colFechaAsignacion.setCellValueFactory(new PropertyValueFactory<>("fechaAsignacion"));
        cmbAlumnos.setItems(getAlumnos());
        cmbCurso.setItems(getCursos());
    }

    private boolean existeElementoSeleccionado() {
        return (tblAsignacionesAlumnos.getSelectionModel().getSelectedItem() != null);
    }

    private ObservableList getCursos() {
        ArrayList<Cursos> arrayListCursos = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = Conexion.getInstance().getConexion()
                    .prepareCall("{CALL sp_cursos_read()}");

            System.out.println(pstmt.toString());

            rs = pstmt.executeQuery();

            while (rs.next()) {
                Cursos curso = new Cursos();
                curso.setId(rs.getInt("id"));
                curso.setNombreCurso(rs.getString("nombre_curso"));
                curso.setCiclo(rs.getInt("ciclo"));
                curso.setCupoMaximo(rs.getInt("cupo_maximo"));
                curso.setCupoMinimo(rs.getInt("cupo_minimo"));
                curso.setCarreraTecnicaId(rs.getString("carrera_tecnica_id"));
                curso.setHorarioId(rs.getInt("horario_id"));
                curso.setInstructorId(rs.getInt("instructor_id"));
                curso.setSalonId(rs.getString("salon_id"));

                System.out.println(curso.toString());

                arrayListCursos.add(curso);
            }

            listaObservableCursos = FXCollections.observableArrayList(arrayListCursos);

        } catch (SQLException e) {
            System.err.println("\nSe produjo un error al intentar listar la tabla de Alumnos");
            System.err.println("Message: " + e.getMessage());
            System.err.println("Error code: " + e.getErrorCode());
            System.err.println("SQLState: " + e.getSQLState());
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

        return listaObservableCursos;
    }

    private Cursos buscarCurso(int id) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Cursos curso = null;

        try {
            pstmt = Conexion.getInstance().getConexion()
                    .prepareCall("{CALL sp_cursos_read_by_id(?)}");
            pstmt.setInt(1, id);

            System.out.println(pstmt.toString());

            rs = pstmt.executeQuery();

            while (rs.next()) {
                curso = new Cursos();
                curso.setId(rs.getInt("id"));
                curso.setNombreCurso(rs.getString("nombre_curso"));
                curso.setCiclo(rs.getInt("ciclo"));
                curso.setCupoMaximo(rs.getInt("cupo_maximo"));
                curso.setCupoMinimo(rs.getInt("cupo_minimo"));
                curso.setCarreraTecnicaId(rs.getString("carrera_tecnica_id"));
                curso.setHorarioId(rs.getInt("horario_id"));
                curso.setInstructorId(rs.getInt("instructor_id"));
                curso.setSalonId(rs.getString("salon_id"));

                System.out.println(curso.toString());
            }

        } catch (SQLException e) {
            System.err.println("\nSe produjo un error al intentar listar la tabla de Alumnos");
            System.err.println("Message: " + e.getMessage());
            System.err.println("Error code: " + e.getErrorCode());
            System.err.println("SQLState: " + e.getSQLState());
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

        return curso;
    }

    private Alumnos buscarAlumnos(String id) {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Alumnos alumno = null;

        try {
            pstmt = Conexion.getInstance().getConexion()
                    .prepareCall("{CALL sp_alumnos_read_by_id(?)}");

            pstmt.setString(1, id);
            System.out.println(pstmt.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                alumno = new Alumnos(
                        rs.getString("carne"),
                        rs.getString("nombre1"),
                        rs.getString("nombre2"),
                        rs.getString("nombre3"),
                        rs.getString("apellido1"),
                        rs.getString("apellido2")
                );

                System.out.println(alumno.toString());
            }

        } catch (SQLException e) {
            System.err.println("\nSe produjo un error al intentar buscar al Alumno con el id: " + id);
            System.err.println("Message: " + e.getMessage());
            System.err.println("Error code: " + e.getErrorCode());
            System.err.println("SQLState: " + e.getSQLState());
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

        return alumno;
    }

    @FXML
    public void seleccionarElemento() {
        if (existeElementoSeleccionado()) {

            txtId.setText(
                    String.valueOf(
                            ((AsignacionesAlumnos) tblAsignacionesAlumnos
                                    .getSelectionModel().getSelectedItem()).getId()
                    )
            );

            cmbCurso.getSelectionModel().select(
                    buscarCurso(
                            ((AsignacionesAlumnos) tblAsignacionesAlumnos
                                    .getSelectionModel().getSelectedItem()).getCursoId()
                    )
            );

            cmbAlumnos.getSelectionModel().select(
                    buscarAlumnos(
                            ((AsignacionesAlumnos) tblAsignacionesAlumnos
                                    .getSelectionModel().getSelectedItem()).getAlumnoId()
                    )
            );

            dpkFechaAsignacion.setValue(
                    ((AsignacionesAlumnos) tblAsignacionesAlumnos.getSelectionModel().getSelectedItem())
                            .getFechaAsignacion().toLocalDate()
            );

        }
    }

    private boolean agregarAsignacion() {
        AsignacionesAlumnos asignacion = new AsignacionesAlumnos();

        asignacion.setAlumnoId(((Alumnos) cmbAlumnos.getSelectionModel().getSelectedItem()).getCarne());

        asignacion.setCursoId(((Cursos) cmbCurso.getSelectionModel().getSelectedItem()).getId());

        asignacion.setFechaAsignacion(dpkFechaAsignacion.getValue().atStartOfDay());

        PreparedStatement pstmt = null;

        try {
            pstmt = Conexion.getInstance().getConexion()
                    .prepareCall("{CALL sp_asignaciones_alumnos_create(?, ?, ?)}");

            pstmt.setString(1, asignacion.getAlumnoId());
            pstmt.setInt(2, asignacion.getCursoId());
            pstmt.setTimestamp(3, Timestamp.valueOf(asignacion.getFechaAsignacion()));

            System.out.println(pstmt.toString());

            pstmt.execute();

            listaObservableAsignaciones.add(asignacion);
            return true;
        } catch (SQLException e) {
            System.err.println("\nSe produjo un error al intentar insertar "
                    + "el siguiente registro: " + asignacion.toString());
            e.printStackTrace();
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

    private boolean actualizarAsignacion() {
        AsignacionesAlumnos asignacion = new AsignacionesAlumnos();
        asignacion.setId(Integer.parseInt(txtId.getText()));
        asignacion.setAlumnoId(((Alumnos) cmbAlumnos.getSelectionModel().getSelectedItem()).getCarne());
        asignacion.setCursoId(((Cursos) cmbCurso.getSelectionModel().getSelectedItem()).getId());
        asignacion.setFechaAsignacion(dpkFechaAsignacion.getValue().atStartOfDay());

        PreparedStatement pstmt = null;

        try {
            pstmt = Conexion.getInstance().getConexion()
                    .prepareCall("{CALL sp_asignaciones_alumnos_update(?, ?, ?, ?)}");
            pstmt.setInt(1, asignacion.getId());
            pstmt.setString(2, asignacion.getAlumnoId());
            pstmt.setInt(3, asignacion.getCursoId());
            pstmt.setTimestamp(4, Timestamp.valueOf(asignacion.getFechaAsignacion()));
            System.out.println("\n" + pstmt.toString());
            pstmt.execute();
            return true;

        } catch (SQLException e) {
            System.err.println("A ocurrido un error al momento de intentar actualizar el siguiente registro: "
                    + asignacion.toString());
            System.out.println("Message: " + e.getMessage());
            System.out.println("Error Code: " + e.getErrorCode());
            System.out.println("SQLState: " + e.getSQLState());
            e.printStackTrace();
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

    private boolean eliminarAsignacion() {

        AsignacionesAlumnos asignacion = (AsignacionesAlumnos) tblAsignacionesAlumnos
                .getSelectionModel().getSelectedItem();

        System.out.println(asignacion.toString());

        PreparedStatement pstmt = null;

        try {
            pstmt = Conexion.getInstance().getConexion()
                    .prepareCall("{CALL sp_asignaciones_alumnos_delete(?)}");

            pstmt.setInt(1, asignacion.getId());

            System.out.println(pstmt.toString());

            pstmt.execute();

            listaObservableAsignaciones.remove(tblAsignacionesAlumnos
                    .getSelectionModel().getFocusedIndex()
            );

            return true;

        } catch (SQLException e) {
            System.err.println("\nSe produjo un error al intentar eliminar el siguiente registro: " + asignacion.toString());
            e.printStackTrace();
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

    public boolean validacionDeCampo() {
        return (cmbAlumnos.getSelectionModel() == null || cmbCurso.getSelectionModel() == null || dpkFechaAsignacion.getValue() == null);
    }

    @FXML
    private void clicNuevo(ActionEvent event) {
        switch (operacion) {
            case NINGUNO:
                habilitarCampos();

                tblAsignacionesAlumnos.setDisable(true);


                txtId.setEditable(false);
                txtId.setDisable(true);

                limpiarCampos();

                btnNuevo.setText("Guardar");
                imgNuevo.setImage(new Image(PAQUETE_IMAGES + "guardar.png"));

                btnModificar.setText("Cancelar");
                imgModificar.setImage(new Image(PAQUETE_IMAGES + "cancelar.png"));

                btnEliminar.setDisable(true);
                btnEliminar.setVisible(false);
                imgEliminar.setVisible(false);
                btnReporte.setDisable(true);
                btnReporte.setVisible(false);
                imgReporte.setVisible(false);

                operacion = Operacion.GUARDAR;

                break;
            case GUARDAR:
                if (validacionDeCampo()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setTitle("KINAL \"CONTROL ACÁDEMICO\"");
                    alert.setContentText("Para finalizar el registro de datos debe completar los datos requeridos");
                    Image icon = new Image(PAQUETE_IMAGES + "logo.png");
                    Stage stageAlert = (Stage) alert.getDialogPane().getScene().getWindow();
                    stageAlert.getIcons().add(icon);
                    alert.showAndWait();
                } else {
                    if (agregarAsignacion()) {
                        cargarDatos();
                        limpiarCampos();
                        deshabilitarCampos();
                        tblAsignacionesAlumnos.setDisable(false);
                        btnNuevo.setText("Nuevo");
                        imgNuevo.setImage(new Image(PAQUETE_IMAGES + "agregar.png"));

                        btnModificar.setText("Editar");
                        imgModificar.setImage(new Image(PAQUETE_IMAGES + "editar.png"));

                        btnEliminar.setText("Eliminar");
                        imgEliminar.setImage(new Image(PAQUETE_IMAGES + "eliminar.png"));

                        btnEliminar.setDisable(false);
                        btnEliminar.setVisible(true);
                        imgEliminar.setVisible(true);
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
    private void clicModificar(ActionEvent event) {
        
        switch (operacion) {
            case NINGUNO:
                if (existeElementoSeleccionado()) {
                    habilitarCampos();
                    txtId.setEditable(false);
                    txtId.setDisable(true);

                    
                    btnNuevo.setDisable(true);
                    btnNuevo.setVisible(false);
                    imgNuevo.setVisible(false);

                    btnModificar.setText("Guardar");
                    imgModificar.setImage(new Image(PAQUETE_IMAGES + "guardar.png"));

                    btnEliminar.setText("Cancelar");
                    imgEliminar.setImage(new Image(PAQUETE_IMAGES + "cancelar.png"));

                    btnReporte.setDisable(true);
                    btnReporte.setVisible(false);
                    imgReporte.setVisible(false);

                    
                    operacion = Operacion.ACTUALIZAR;
               
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Control Academico");
                    alert.setHeaderText(null);
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image(PAQUETE_IMAGES + "logo.png"));
                    alert.setContentText("Antes de continuar, seleccione un registro.");
                    alert.show();
                }
                
               
                break;
            case GUARDAR: 
                btnNuevo.setText("Nuevo");
                imgNuevo.setImage(new Image(PAQUETE_IMAGES + "agregar.png"));

                btnModificar.setText("Modificar");
                imgModificar.setImage(new Image(PAQUETE_IMAGES + "editar.png"));

                btnEliminar.setDisable(false);
                btnEliminar.setVisible(true);
                imgEliminar.setVisible(true);
                btnReporte.setDisable(false);
                btnReporte.setVisible(true);
                imgReporte.setVisible(true);

                limpiarCampos();
                deshabilitarCampos();

                
                tblAsignacionesAlumnos.setDisable(false);

                operacion = Operacion.NINGUNO;
                break;
            case ACTUALIZAR:

                if (existeElementoSeleccionado()) {
                    
                    if (validacionDeCampo()) {
                        
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText(null);
                        alert.setTitle("KINAL \"CONTROL ACÁDEMICO\"");
                        alert.setContentText("Para finalizar la actualizacion de datos debe completar los datos requeridos");
                        Image icon = new Image(PAQUETE_IMAGES + "logo.png");
                        Stage stageAlert = (Stage) alert.getDialogPane().getScene().getWindow();
                        stageAlert.getIcons().add(icon);
                        alert.showAndWait();
                    
                    } else {

                        
                        if (actualizarAsignacion()) {
                            cargarDatos();
                            limpiarCampos();

                            tblAsignacionesAlumnos.setDisable(false);
                            tblAsignacionesAlumnos.getSelectionModel().clearSelection();

                            btnNuevo.setText("Nuevo");
                            btnNuevo.setDisable(false);
                            btnNuevo.setVisible(true);
                            imgNuevo.setVisible(true);
                            imgNuevo.setImage(new Image(PAQUETE_IMAGES + "agregar.png"));

                            btnModificar.setText("Editar");
                            imgModificar.setImage(new Image(PAQUETE_IMAGES + "editar.png"));

                            btnEliminar.setText("Eliminar");
                            imgEliminar.setImage(new Image(PAQUETE_IMAGES+ "eliminar.png"));

                            btnEliminar.setDisable(false);
                            btnEliminar.setVisible(true);
                            imgEliminar.setVisible(true);
                            btnReporte.setDisable(false);
                            btnReporte.setVisible(true);
                            imgReporte.setVisible(true);

                            deshabilitarCampos();

                            operacion = Operacion.NINGUNO;
                        }
                    }
                }

                break;

        }
    }

    @FXML
    private void clicEliminar() {
        switch (operacion) {
            case ACTUALIZAR: 
                btnNuevo.setDisable(false);
                btnNuevo.setVisible(true);
                imgNuevo.setVisible(true);
                btnModificar.setText("Modificar");
                imgModificar.setImage(new Image(PAQUETE_IMAGES + "editar.png"));
                btnEliminar.setText("Eliminar");
                imgEliminar.setImage(new Image(PAQUETE_IMAGES + "eliminar.png"));
                btnReporte.setDisable(false);
                btnReporte.setVisible(true);
                imgReporte.setVisible(true);
                limpiarCampos();
                deshabilitarCampos();
                tblAsignacionesAlumnos.getSelectionModel().clearSelection();
                operacion = Operacion.NINGUNO;
                break;
            case NINGUNO:
                if (existeElementoSeleccionado()) {
                    Alert alertNew = new Alert(Alert.AlertType.CONFIRMATION);
                    alertNew.setHeaderText(null);
                    alertNew.setTitle("KINAL \"CONTROL ACÁDEMICO\"");
                    alertNew.setContentText(null);
                    alertNew.setContentText("¿Desea eliminar el registro?");
                    Stage stage = (Stage) alertNew.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image(PAQUETE_IMAGES + "logo.png"));
                    Optional<ButtonType> result = alertNew.showAndWait();
                    if (result.get().equals(ButtonType.OK)) {
                        if (eliminarAsignacion()) {
                            listaObservableAsignaciones.remove(tblAsignacionesAlumnos.getSelectionModel().getFocusedIndex());
                            System.out.println("\n");
                            limpiarCampos();
                            cargarDatos();
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText(null);
                            alert.setTitle("KINAL \"CONTROL ACÁDEMICO\"");
                            alert.setContentText("Registro eliminado exitosamente");
                            Image icon = new Image(PAQUETE_IMAGES + "logo.png");
                            Stage stageAlert = (Stage) alert.getDialogPane().getScene().getWindow();
                            stageAlert.getIcons().add(icon);
                            alert.show();
                        } else if (result.get().equals(ButtonType.CANCEL)) {
                            System.out.println("\nCancelando Operacion");
                            tblAsignacionesAlumnos.getSelectionModel().clearSelection();
                            limpiarCampos();
                        }
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText(null);
                    alert.setTitle("KINAL \"CONTROL ACÁDEMICO\"");
                    alert.setContentText("Antes de seguir selecciona un registro");
                    Image icon = new Image(PAQUETE_IMAGES + "logo.png");
                    Stage stageAlert = (Stage) alert.getDialogPane().getScene().getWindow();
                    stageAlert.getIcons().add(icon);
                    alert.show();
                }
                break;
        }
    }

    @FXML
    private void clicReporte(ActionEvent event) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("AVISO!!!");
        alerta.setHeaderText(null);
        alerta.setContentText("Esta funcionalidad solo está disponible en la versión PRO");
        Stage stageAlert = (Stage) alerta.getDialogPane().getScene().getWindow();
        stageAlert.getIcons().add(new Image(PAQUETE_IMAGES + "colegio.png"));
        alerta.show();
    }

}
