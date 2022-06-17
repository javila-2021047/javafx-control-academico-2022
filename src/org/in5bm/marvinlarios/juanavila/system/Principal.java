package org.in5bm.marvinlarios.juanavila.system;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.in5bm.marvinlarios.juanavila.controllers.AlumnosController;
import org.in5bm.marvinlarios.juanavila.controllers.CarrerasTecnicasController;
import org.in5bm.marvinlarios.juanavila.controllers.SalonesController;
import org.in5bm.marvinlarios.juanavila.controllers.MenuPrincipalController;
import org.in5bm.marvinlarios.juanavila.controllers.AsignacionesAlumnosController;
import org.in5bm.marvinlarios.juanavila.controllers.HorariosController;

/**
 *
 * @author Marvin Larios
 */
public class Principal extends Application {

    private final String PAQUETE_VIEW = "../views/";
    private final String PAQUETE_IMAGES = "org/in5bm/marvinlarios/juanavila/resources/images/";
    private Stage escenarioPrincipal;
    private Scene escena;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.escenarioPrincipal = primaryStage;
        this.escenarioPrincipal.setTitle("Control Académico Kinal");
        this.escenarioPrincipal.getIcons().add(new Image(PAQUETE_IMAGES + "logo.png"));
        this.escenarioPrincipal.setResizable(false);
        this.escenarioPrincipal.centerOnScreen();
        mostrarEscenaPrincipal();

    }

    public Initializable cambiarEscena(String vistaFxml) throws IOException {
        Initializable resultado = null;

        FXMLLoader cargadorFXML = new FXMLLoader(getClass().getResource(this.PAQUETE_VIEW + vistaFxml));
        Scene scene = new Scene((AnchorPane) cargadorFXML.load());
        this.escenarioPrincipal.setScene(scene);
        this.escenarioPrincipal.sizeToScene();
        this.escenarioPrincipal.show();
        resultado = (Initializable) cargadorFXML.getController();
        return resultado;
    }

    public void mostrarEscenaHorarios() {
        try {
            HorariosController horariosController = (HorariosController) cambiarEscena("HorariosView.fxml");
            horariosController.setEscenarioPrincipal(this);
        } catch (Exception ex) {
            System.err.println("\nSe produjo un error al intentar mostrar la vista de Alumnos");
            ex.printStackTrace();
        }
    }

    public void mostrarEscenaPrincipal() {
        try {
            MenuPrincipalController menuController = (MenuPrincipalController) cambiarEscena("MenuPrincipalView.fxml");
            menuController.setEscenarioPrincipal(this);
        } catch (Exception ex) {
            System.err.println("\nSe produjo un error al cargar la vista del menu principal");
            ex.printStackTrace();
        }
    }

    public void mostrarEscenaAlumnos() {
        try {
            AlumnosController alumnosController = (AlumnosController) cambiarEscena("AlumnosView.fxml");
            alumnosController.setEscenarioPrincipal(this);
        } catch (Exception ex) {
            System.err.println("\nSe produjo un error al intentar mostrar la vista de Alumnos");
            ex.printStackTrace();
        }
    }

    public void mostrarEscenaSalones() {
        try {
            SalonesController salonesController = (SalonesController) cambiarEscena("SalonesView.fxml");
            salonesController.setEscenarioPrincipal(this);
        } catch (Exception ex) {
            System.err.println("\nSe produjo un error al intentar mostrar la vista de Alumnos");
            ex.printStackTrace();
        }
    }

    public void mostrarEscenaCarrerasTecnicas() {
        try {
            CarrerasTecnicasController carrerasTecnicasController = (CarrerasTecnicasController) cambiarEscena("CarrerasTecnicasView.fxml");
            carrerasTecnicasController.setEscenarioPrincipal(this);
        } catch (Exception ex) {
            System.err.println("\nSe produjo un error al intentar mostrar la vista de Alumnos");
            ex.printStackTrace();
        }

    }

    public void mostrarEscenaAsignacion() {
        try {
            AsignacionesAlumnosController asignacionController = (AsignacionesAlumnosController) cambiarEscena("AsignacionesAlumnosView.fxml");
            asignacionController.setEscenarioPrincipal(this);
        } catch (Exception ex) {
            System.err.println("\nSe produjo un error al intentar mostrar la vista de Alumnos");
            ex.printStackTrace();
        }

    }
}
