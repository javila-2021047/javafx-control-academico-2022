package org.in5bm.marvinlarios.juanavila.models;

/**
 *
 * @author Marvin Larios
 * 
 */
public class CarrerasTecnicas {
    
    private String codigoTecnico;
    private String carrera;
    private String grado;
    private char seccion;
    private String jornada;
    
    public CarrerasTecnicas() {

    }

    public CarrerasTecnicas(String codigoTecnico) {
        this.codigoTecnico = codigoTecnico;
    }

    public CarrerasTecnicas(String codigoTecnico, String carrera, String grado, char seccion, String jornada) {
        this.codigoTecnico = codigoTecnico;
        this.carrera = carrera;
        this.grado = grado;
        this.seccion = seccion;
        this.jornada = jornada;
    }

    public String getCodigoTecnico() {
        return codigoTecnico;
    }

    public void setCodigoTecnico(String codigoTecnico) {
        this.codigoTecnico = codigoTecnico;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getGrado() {
        return grado;
    }

    public void setGrado(String grado) {
        this.grado = grado;
    }

    public char getSeccion() {
        return seccion;
    }

    public void setSeccion(char seccion) {
        this.seccion = seccion;
    }

    public String getJornada() {
        return jornada;
    }

    public void setJornada(String jornada) {
        this.jornada = jornada;
    }
    
    @Override
    public String toString() {
        return "CarrerasTecnicas{" + "codigoTecnico=" + codigoTecnico + ", carrera=" + carrera + ", grado=" + grado + ", seccion=" + seccion + ", jornada=" + jornada + '}';
    }

}
