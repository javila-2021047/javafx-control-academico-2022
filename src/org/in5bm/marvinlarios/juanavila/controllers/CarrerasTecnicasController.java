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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import org.in5bm.marvinlarios.juanavila.models.CarrerasTecnicas;
import org.in5bm.marvinlarios.juanavila.system.Principal;

/**
 *
 * @author Marvin Larios
 *
 */
public class CarrerasTecnicasController implements Initializable {

    // Tipo de dato   nombre de la variable
    private enum Operacion {
        NINGUNO, GUARDAR, ACTUALIZAR
    }

    private Operacion operacion = Operacion.NINGUNO;

    private Principal escenarioPrincipal;

    @FXML
    private TextField txtCodigoTecnico;

    @FXML
    private TextField txtCarrera;

    @FXML
    private TextField txtGrado;

    @FXML
    private TextField txtSeccion;

    @FXML
    private TextField txtJornada;

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
    private TableView tblCarrerasTecnicas;

    @FXML
    private TableColumn colCodigoTecnico;

    @FXML
    private TableColumn colCarrera;

    @FXML
    private TableColumn colGrado;

    @FXML
    private TableColumn colSeccion;

    @FXML
    private TableColumn colJornada;

    @FXML
    private ObservableList<CarrerasTecnicas> listaCarrerasTecnicas;

    @FXML
    public void clicRegresar(MouseEvent event) {
        escenarioPrincipal.mostrarEscenaPrincipal();
    }

    private final String PAQUETE_IMAGES = "org/in5bm/marvinlarios/juanavila/resources/images/";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        
        
    }

    public void cargarDatos() {
        tblCarrerasTecnicas.setItems(getCarrerasTecnicas());
        colCodigoTecnico.setCellValueFactory(new PropertyValueFactory<CarrerasTecnicas, String>("codigoTecnico"));
        colCarrera.setCellValueFactory(new PropertyValueFactory<CarrerasTecnicas, String>("carrera"));
        colGrado.setCellValueFactory(new PropertyValueFactory<CarrerasTecnicas, String>("grado"));
        colSeccion.setCellValueFactory(new PropertyValueFactory<CarrerasTecnicas, Character>("seccion"));
        colJornada.setCellValueFactory(new PropertyValueFactory<CarrerasTecnicas, String>("jornada"));
    }

    public boolean existeElementoSeleccionado() {
        return (tblCarrerasTecnicas.getSelectionModel().getSelectedItem() != null);
    }

    @FXML
    public void seleccionarElemento() {
        if (existeElementoSeleccionado()) {
            txtCodigoTecnico.setText(((CarrerasTecnicas) tblCarrerasTecnicas.getSelectionModel().getSelectedItem()).getCodigoTecnico());

            txtCarrera.setText(((CarrerasTecnicas) tblCarrerasTecnicas.getSelectionModel().getSelectedItem()).getCarrera());

            txtGrado.setText(((CarrerasTecnicas) tblCarrerasTecnicas.getSelectionModel().getSelectedItem()).getGrado());

            txtSeccion.setText(String.valueOf(((CarrerasTecnicas) tblCarrerasTecnicas.getSelectionModel().getSelectedItem()).getSeccion()));

            txtJornada.setText(((CarrerasTecnicas) tblCarrerasTecnicas.getSelectionModel().getSelectedItem()).getJornada());
        }

    }

    private boolean agregarCarrera() {
        CarrerasTecnicas carrera = new CarrerasTecnicas();
        carrera.setCodigoTecnico(txtCodigoTecnico.getText());
        carrera.setCarrera(txtCarrera.getText());
        carrera.setGrado(txtGrado.getText());
        carrera.setSeccion(txtSeccion.getText().charAt(0));
        carrera.setJornada(txtJornada.getText());

        PreparedStatement pstmt = null;

        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{call sp_carreras_tecnicas_create(?, ?, ?, ?, ?)}");
            pstmt.setString(1, carrera.getCodigoTecnico());
            pstmt.setString(2, carrera.getCarrera());
            pstmt.setString(3, carrera.getGrado());
            pstmt.setString(4, (String.valueOf(carrera.getSeccion())));
            pstmt.setString(5, carrera.getJornada());

            System.out.println(pstmt.toString());
            pstmt.execute();
            listaCarrerasTecnicas.add(carrera);

            return true;

        } catch (SQLException e) {
            System.err.println("\n Se produjo un error al intentar insertar la siguiente carrera: " + carrera.toString());
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

    private boolean actualizarCarrera() {
        CarrerasTecnicas carrera = new CarrerasTecnicas();
        carrera.setCodigoTecnico(txtCodigoTecnico.getText());
        carrera.setCarrera(txtCarrera.getText());
        carrera.setGrado(txtGrado.getText());
        carrera.setSeccion(txtSeccion.getText().charAt(0));
        carrera.setJornada(txtJornada.getText());

        PreparedStatement pstmt = null;

        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("call sp_carreras_tecnicas_update(?, ?, ?, ?, ?)");
            pstmt.setString(1, carrera.getCodigoTecnico());
            pstmt.setString(2, carrera.getCarrera());
            pstmt.setString(3, carrera.getGrado());
            pstmt.setString(4, String.valueOf(carrera.getSeccion()));
            pstmt.setString(5, carrera.getJornada());

            System.out.println(pstmt.toString());
            pstmt.execute();

            return true;

        } catch (SQLException e) {
            System.err.println("\nSe produjo un error al intentar actualizar el siguiente registro" + carrera.toString());
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

    public boolean eliminarCarrera() {
        if (existeElementoSeleccionado()) {
            CarrerasTecnicas carrera = (CarrerasTecnicas) tblCarrerasTecnicas.getSelectionModel().getSelectedItem();
            System.out.println(carrera.toString());

            PreparedStatement pstmt = null;
            try {
                pstmt = Conexion.getInstance().getConexion().prepareCall("call sp_carreras_tecnicas_delete(?)");
                pstmt.setString(1, carrera.getCodigoTecnico());
                System.out.println(pstmt);

                pstmt.execute();
                return true;

            } catch (SQLException e) {
                System.err.println("\nSe produjo un error al intentar eliminar la carrera: " + carrera.toString());
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return false;
    }

    public ObservableList getCarrerasTecnicas() {

        ArrayList<CarrerasTecnicas> lista = new ArrayList<>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("call sp_carreras_tecnicas_read()");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                CarrerasTecnicas carrerasTecnicas = new CarrerasTecnicas();

                carrerasTecnicas.setCodigoTecnico(rs.getString(1));
                carrerasTecnicas.setCarrera(rs.getString(2));
                carrerasTecnicas.setGrado(rs.getString(3));
                carrerasTecnicas.setSeccion(rs.getString(4).charAt(0));
                carrerasTecnicas.setJornada(rs.getString(5));

                lista.add(carrerasTecnicas);

                System.out.println(carrerasTecnicas.toString());
            }

            listaCarrerasTecnicas = FXCollections.observableArrayList(lista);

        } catch (SQLException e) {
            System.err.println("\n Se produjo un error al intentar consultar la lista de Carreras Tecnicas");
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

        return listaCarrerasTecnicas;
    }

    private void habilitarCampos() {
        txtCodigoTecnico.setEditable(true);
        txtCarrera.setEditable(true);
        txtGrado.setEditable(true);
        txtSeccion.setEditable(true);
        txtJornada.setEditable(true);

        txtCodigoTecnico.setDisable(false);
        txtCarrera.setDisable(false);
        txtGrado.setDisable(false);
        txtSeccion.setDisable(false);
        txtJornada.setDisable(false);
    }

    private void deshabilitarCampos() {
        txtCodigoTecnico.setEditable(false);
        txtCarrera.setEditable(false);
        txtGrado.setEditable(false);
        txtSeccion.setEditable(false);
        txtJornada.setEditable(false);

        txtCodigoTecnico.setDisable(true);
        txtCarrera.setDisable(true);
        txtGrado.setDisable(true);
        txtSeccion.setDisable(true);
        txtJornada.setDisable(true);
    }

    private void limpiarCampos() {
        txtCodigoTecnico.setText("");
        txtCarrera.setText("");
        txtGrado.setText("");
        txtSeccion.setText("");
        txtJornada.clear();

    }

    @FXML
    private void clicNuevo() {
        switch (operacion) {
            case NINGUNO:
                habilitarCampos();

                txtCodigoTecnico.setEditable(true);
                txtCodigoTecnico.setDisable(false);
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
                if (txtCodigoTecnico.getText().isEmpty() || txtCarrera.getText().isEmpty()
                        || txtGrado.getText().isEmpty() || txtSeccion.getText().isEmpty() || txtSeccion.getText().contentEquals("")
                        || txtJornada.getText().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Control Académico Kinal");
                    alert.setHeaderText(null);
                    alert.setContentText("Todos los campos son obligatorios");
                    Stage dialog = new Stage();
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image(PAQUETE_IMAGES + "logo.png"));
                    alert.show();
                } else {
                    if (agregarCarrera()) {
                        cargarDatos();
                        limpiarCampos();
                        deshabilitarCampos();
                        tblCarrerasTecnicas.setDisable(false);

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
                
                tblCarrerasTecnicas.setDisable(false);

                operacion = Operacion.NINGUNO;
                break;

            case ACTUALIZAR:
                if (existeElementoSeleccionado()) {
                    if (actualizarCarrera()) {
                        cargarDatos();
                        limpiarCampos();
                        deshabilitarCampos();
                        tblCarrerasTecnicas.setDisable(false);
                        tblCarrerasTecnicas.getSelectionModel().clearSelection();
                        
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
                        if (eliminarCarrera()) {
                            listaCarrerasTecnicas.remove(tblCarrerasTecnicas.getSelectionModel().getFocusedIndex());
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
                        tblCarrerasTecnicas.getSelectionModel().clearSelection();
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
