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
public class Inscripcion {
    
    private int id_inscripcion;
    private float nota;
    private Estudiante estudiante;
    private Materia materia;
    
    private int idEstudiante;
    private int idMateria;
    
    public Inscripcion ()
    {
        
    }

    public Inscripcion(float nota, Estudiante estudiante, Materia materia) {
        this.nota = nota;
        this.estudiante = estudiante;
        this.materia = materia;
    }

    public Inscripcion(int id_inscripcion, float nota, Estudiante estudiante, Materia materia) {
        this.id_inscripcion = id_inscripcion;
        this.nota = nota;
        this.estudiante = estudiante;
        this.materia = materia;
    }

    public int getId_inscripcion() {
        return id_inscripcion;
    }

    public void setId_inscripcion(int id_inscripcion) {
        this.id_inscripcion = id_inscripcion;
    }

    public float getNota() {
        return nota;
    }

    public void setNota(float nota) {
        this.nota = nota;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }
    
    public int getEstudianteID()
    {
        return estudiante.getId_estudiante();
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }
    
    public void setEstudianteID(int IdEstudiante)
    {
        this.idEstudiante = idEstudiante;
    }
    
    public Materia getMateria() {
        return materia;
    }
    
    public int getMateriaID()
    {
        return materia.getId_materia();
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }
    
    public void setMateriaID(int idMateria)
    {
        this.idMateria = idMateria;
    }

    @Override
    public String toString() {
        String inscripcion = id_inscripcion + " " + estudiante.getApellido() + ", " + estudiante.getNombre() + " " + materia.getNombre();
        return inscripcion;
    }
    
}
