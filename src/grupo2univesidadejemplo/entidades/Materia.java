/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ulpejemplo.entidades;

/**
 *
 * @author marti
 */
public class Materia {
    
    private int id_materia;
    private int codigoMateria;
    private String nombre;
    private int anio;
    private boolean activa;

    public Materia() {
    }

    public Materia(int codigoMateria, String nombre, int anio, boolean activa) {
        this.codigoMateria = codigoMateria;
        this.nombre = nombre;
        this.anio = anio;
        this.activa = activa;
    }

    public Materia(int id_materia, int codigoMateria, String nombre, int anio, boolean activa) {
        this.id_materia = id_materia;
        this.codigoMateria = codigoMateria;
        this.nombre = nombre;
        this.anio = anio;
        this.activa = activa;
    }

    public int getId_materia() {
        return id_materia;
    }

    public void setId_materia(int id_materia) {
        this.id_materia = id_materia;
    }

    public int getCodigoMateria() {
        return codigoMateria;
    }

    public void setCodigoMateria(int codigoMateria) {
        this.codigoMateria = codigoMateria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    @Override
    public String toString() {
        return "Materia{" + "id_materia=" + id_materia + ", codigoMateria=" + codigoMateria + ", nombre=" + nombre + ", anio=" + anio + ", activa=" + activa + '}';
    }
}

