/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ulpejemplo.entidades;

import java.time.LocalDate;

/**
 *
 * @author marti
 */
public class Estudiante {
    
    private int id_estudiante;
    private int dni;
    private String apellido;
    private String nombre;
    private LocalDate fechaNac;
    //chequear si en SQL es activo o estado
    private boolean activo;

    public Estudiante() {
    }

    public Estudiante(int dni, String apellido, String nombre, LocalDate fechaNac, boolean activo) {
        this.dni = dni;
        this.apellido = apellido;
        this.nombre = nombre;
        this.fechaNac = fechaNac;
        this.activo = activo;
    }

    public Estudiante(int id_estudiante, int dni, String apellido, String nombre, LocalDate fechaNac, boolean activo) {
        this.id_estudiante = id_estudiante;
        this.dni = dni;
        this.apellido = apellido;
        this.nombre = nombre;
        this.fechaNac = fechaNac;
        this.activo = activo;
    }

    public int getId_estudiante() {
        return id_estudiante;
    }

    public void setId_estudiante(int id_estudiante) {
        this.id_estudiante = id_estudiante;
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(LocalDate fechaNac) {
        this.fechaNac = fechaNac;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "Estudiante{" + "id_estudiante=" + id_estudiante + ", dni=" + dni + ", apellido=" + apellido + ", nombre=" + nombre + ", fechaNac=" + fechaNac + ", activo=" + activo + '}';
    }
    
}