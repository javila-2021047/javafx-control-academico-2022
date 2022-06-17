package org.in5bm.marvinlarios.juanavila.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import org.in5bm.marvinlarios.juanavila.system.Principal;

/**
 *
 * @author Marvin Larios
 */
public class MenuPrincipalController implements Initializable{
    
    private Principal escenarioPrincipal;

    @FXML
    void clicAlumnos(ActionEvent event) throws IOException {
        escenarioPrincipal.mostrarEscenaAlumnos();
    }
    
    @FXML
    void clicSalones(ActionEvent event) throws IOException {
        escenarioPrincipal.mostrarEscenaSalones();
    }
    
    @FXML
    void clicCarrerasTecnicas(ActionEvent event) throws IOException {
        escenarioPrincipal.mostrarEscenaCarrerasTecnicas();
    }
    
    @FXML
    void clickAsignacion(ActionEvent event) {
        escenarioPrincipal.mostrarEscenaAsignacion();
    }
    
    @FXML
    void clickHorarios(ActionEvent event) {
        escenarioPrincipal.mostrarEscenaHorarios();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    

}
