package org.in5bm.marvinlarios.juanavila.controllers;


import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import org.in5bm.marvinlarios.juanavila.db.Conexion;
import org.in5bm.marvinlarios.juanavila.models.Alumnos;
import org.in5bm.marvinlarios.juanavila.system.Principal;

/**
 *
 * @author Marvin Larios
 *
 */
public class AlumnosController implements Initializable {

    private enum Operacion {
        NINGUNO, GUARDAR, ACTUALIZAR
    }

    private Operacion operacion = Operacion.NINGUNO;

    private Principal escenarioPrincipal;

    @FXML
    private TextField txtCarnet;

    @FXML
    private TextField txtNombre1;

    @FXML
    private TextField txtNombre2;

    @FXML
    private TextField txtNombre3;

    @FXML
    private TextField txtApellido1;

    @FXML
    private TextField txtApellido2;

    @FXML
    private Button btnNuevo;

    @FXML
    private Button btnModificar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnReporte;

    @FXML
    private ImageView imgNuevo;

    @FXML
    private ImageView imgModificar;

    @FXML
    private ImageView imgEliminar;

    @FXML
    private ImageView imgReporte;

    @FXML
    private TableView tblAlumnos;

    @FXML
    private TableColumn colCarnet;

    @FXML
    private TableColumn colNombre1;

    @FXML
    private TableColumn colNombre2;

    @FXML
    private TableColumn colNombre3;

    @FXML
    private TableColumn colApellido1;

    @FXML
    private TableColumn colApellido2;

    @FXML
    private ObservableList<Alumnos> listaAlumnos;
    
    @FXML
    public void clicRegresar(MouseEvent event){
        escenarioPrincipal.mostrarEscenaPrincipal();
    }

    private final String PAQUETE_IMAGES = "org/in5bm/marvinlarios/juanavila/resources/images/";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }

    public void cargarDatos() {
        tblAlumnos.setItems(getAlumnos());
        colCarnet.setCellValueFactory(new PropertyValueFactory<Alumnos, String>("carne"));
        colNombre1.setCellValueFactory(new PropertyValueFactory<Alumnos, String>("nombre1"));
        colNombre2.setCellValueFactory(new PropertyValueFactory<Alumnos, String>("nombre2"));
        colNombre3.setCellValueFactory(new PropertyValueFactory<Alumnos, String>("nombre3"));
        colApellido1.setCellValueFactory(new PropertyValueFactory<Alumnos, String>("apellido1"));
        colApellido2.setCellValueFactory(new PropertyValueFactory<Alumnos, String>("apellido2"));
    }

    public boolean existeElementoSeleccionado() {
        return (tblAlumnos.getSelectionModel().getSelectedItem() != null);
    }

    @FXML
    public void seleccionarElemento() {
        if (existeElementoSeleccionado()) {
            txtCarnet.setText(((Alumnos) tblAlumnos.getSelectionModel().getSelectedItem()).getCarne());

            txtNombre1.setText(((Alumnos) tblAlumnos.getSelectionModel().getSelectedItem()).getNombre1());

            txtNombre2.setText(((Alumnos) tblAlumnos.getSelectionModel().getSelectedItem()).getNombre2());

            txtNombre3.setText(((Alumnos) tblAlumnos.getSelectionModel().getSelectedItem()).getNombre3());

            txtApellido1.setText(((Alumnos) tblAlumnos.getSelectionModel().getSelectedItem()).getApellido1());

            txtApellido2.setText(((Alumnos) tblAlumnos.getSelectionModel().getSelectedItem()).getApellido2());
        }

    }

    private boolean agregarAlumno() {
        Alumnos alumno = new Alumnos();
        alumno.setCarne(txtCarnet.getText());
        alumno.setNombre1(txtNombre1.getText());
        alumno.setNombre2(txtNombre2.getText());
        alumno.setNombre3(txtNombre3.getText());
        alumno.setApellido1(txtApellido1.getText());
        alumno.setApellido2(txtApellido2.getText());

        PreparedStatement pstmt = null;

        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_alumnos_create(?, ?, ?, ?, ?, ?)}");

            pstmt.setString(1, alumno.getCarne());
            pstmt.setString(2, alumno.getNombre1());
            pstmt.setString(3, alumno.getNombre2());
            pstmt.setString(4, alumno.getNombre3());
            pstmt.setString(5, alumno.getApellido1());
            pstmt.setString(6, alumno.getApellido2());

            System.out.println(pstmt.toString());
            pstmt.execute();
            listaAlumnos.add(alumno);

            return true;

        } catch (SQLException e) {
            System.err.println("\n Se produjo un error al intentar insertar el siguiente alumno: " + alumno.toString());
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

    private boolean actualizarAlumno() {
        Alumnos alumno = new Alumnos();
        alumno.setCarne(txtCarnet.getText());
        alumno.setNombre1(txtNombre1.getText());
        alumno.setNombre2(txtNombre2.getText());
        alumno.setNombre3(txtNombre3.getText());
        alumno.setApellido1(txtApellido1.getText());
        alumno.setApellido2(txtApellido2.getText());

        PreparedStatement pstmt = null;

        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_alumnos_update(?, ?, ?, ?, ?, ?)}");
            pstmt.setString(1, alumno.getCarne());
            pstmt.setString(2, alumno.getNombre1());
            pstmt.setString(3, alumno.getNombre2());
            pstmt.setString(4, alumno.getNombre3());
            pstmt.setString(5, alumno.getApellido1());
            pstmt.setString(6, alumno.getApellido2());

            System.out.println(pstmt.toString());
            pstmt.execute();

            return true;

        } catch (SQLException e) {
            System.err.println("\nSe produjo un error al intentar actualizar el siguiente registro");
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

    private boolean eliminarAlumno() {
        if (existeElementoSeleccionado()) {
            Alumnos alumno = (Alumnos) tblAlumnos.getSelectionModel().getSelectedItem();
            System.out.println(alumno.toString());

            PreparedStatement pstmt = null;
            try {
                pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_alumnos_delete(?)}");
                pstmt.setString(1, alumno.getCarne());
                System.out.println(pstmt);

                pstmt.execute();
                return true;

            } catch (SQLException e) {
                System.err.println("\nSe produjo un error al intentar eliminar el alumno: " + alumno.toString());
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

        }
        return false;
    }

    public ObservableList getAlumnos() {

        ArrayList<Alumnos> lista = new ArrayList<>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("call sp_alumnos_read()");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Alumnos alumno = new Alumnos();

                alumno.setCarne(rs.getString(1));
                alumno.setNombre1(rs.getString(2));
                alumno.setNombre2(rs.getString(3));
                alumno.setNombre3(rs.getString(4));
                alumno.setApellido1(rs.getString(5));
                alumno.setApellido2(rs.getString(6));

                lista.add(alumno);

                System.out.println(alumno.toString());
            }

            listaAlumnos = FXCollections.observableArrayList(lista);

        } catch (SQLException e) {
            System.err.println("\n Se produjo un error al intentar consultar la lista de Alumnos");
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

        return listaAlumnos;
    }

    private void habilitarCampos() {
        txtCarnet.setEditable(true);
        txtNombre1.setEditable(true);
        txtNombre2.setEditable(true);
        txtNombre3.setEditable(true);
        txtApellido1.setEditable(true);
        txtApellido2.setEditable(true);

        txtCarnet.setDisable(false);
        txtNombre1.setDisable(false);
        txtNombre2.setDisable(false);
        txtNombre3.setDisable(false);
        txtApellido1.setDisable(false);
        txtApellido2.setDisable(false);
    }

    private void deshabilitarCampos() {
        txtCarnet.setEditable(false);
        txtNombre1.setEditable(false);
        txtNombre2.setEditable(false);
        txtNombre3.setEditable(false);
        txtApellido1.setEditable(false);
        txtApellido2.setEditable(false);

        txtCarnet.setDisable(true);
        txtNombre1.setDisable(true);
        txtNombre2.setDisable(true);
        txtNombre3.setDisable(true);
        txtApellido1.setDisable(true);
        txtApellido2.setDisable(true);
    }

    private void limpiarCampos() {
        txtCarnet.setText("");
        txtNombre1.setText("");
        txtNombre2.setText("");
        txtNombre3.setText("");
        txtApellido1.clear();
        txtApellido2.clear();

    }

    @FXML
    private void clicNuevo() {

        switch (operacion) {
            case NINGUNO:
                habilitarCampos();
                tblAlumnos.setDisable(true);

                txtCarnet.setEditable(true);
                txtCarnet.setDisable(false);

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
                if (txtCarnet.getText().isEmpty()||txtNombre1.getText().isEmpty()||
                        txtApellido1.getText().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Control Académico Kinal");
                    alert.setHeaderText(null);
                    alert.setContentText("El campo Carnet, Primer Nombre y Primer Apellido es obligatorio");
                    Stage dialog = new Stage();
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image(PAQUETE_IMAGES + "logo.png"));
                    alert.show();
                } else {
                    if (agregarAlumno()) {
                        cargarDatos();
                        limpiarCampos();
                        deshabilitarCampos();
                        tblAlumnos.setDisable(false);

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

                    operacion = Operacion.ACTUALIZAR;

                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Control Académico Kinal");
                    alert.setHeaderText(null);
                    alert.setContentText("Antes de continuar seleccione un registro");
                    alert.show();
                }

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

                tblAlumnos.setDisable(false);

                operacion = Operacion.NINGUNO;
                break;

            case ACTUALIZAR:
                if (existeElementoSeleccionado()) {
                    if (actualizarAlumno()) {
                        cargarDatos();
                        limpiarCampos();
                        deshabilitarCampos();
                        tblAlumnos.setDisable(false);

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
                
                tblAlumnos.getSelectionModel().clearSelection();
                
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
                        if (eliminarAlumno()) {
                            listaAlumnos.remove(tblAlumnos.getSelectionModel().getFocusedIndex());
                            cargarDatos();
                            alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Control Académico Kinal");
                            alert.setHeaderText(null);
                            alert.setContentText("Registro eliminado exitosamente");
                            dialog = new Stage();
                            stage = (Stage) alert.getDialogPane().getScene().getWindow();
                            stage.getIcons().add(new Image(PAQUETE_IMAGES + "logo.png"));
                            alert.show();

                            limpiarCampos();
                        }

                    } else if (result.get() == ButtonType.CANCEL) {
                        alert.close();
                        tblAlumnos.getSelectionModel().clearSelection();
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

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

}
