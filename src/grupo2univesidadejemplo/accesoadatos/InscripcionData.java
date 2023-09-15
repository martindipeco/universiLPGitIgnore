/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ulpejemplo.accesoDatos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import ulpejemplo.entidades.Estudiante;
import ulpejemplo.entidades.Inscripcion;
import ulpejemplo.entidades.Materia;


/**
 *
 * @author marti
 */
public class InscripcionData {
    
    private Connection con;
    
    private EstudianteData estuData;
    
    private MateriaData mateData;
    
    public InscripcionData()
    {
        con = Conexion.getConexion();
    }
    

    
    public void guardarInscripcion(Inscripcion insc)
    {
        //IMPORTANTE! chequear primero que el estudiante no esté ya inscripto a esa materia!!!
        /*
        Sugiere ChatGPT
        To ensure that a student is not enrolled in the same subject multiple times, you can set up a unique constraint or create a composite primary key constraint on the enrollment table that combines the student ID and subject ID. This will enforce the uniqueness of the combination of student and subject in the enrollments table.

        Here's an example of how you can create a composite unique constraint in SQL:
        
        ALTER TABLE enrollments
        ADD CONSTRAINT unique_enrollment
        UNIQUE (student_id, subject_id);

        In this example, enrollments is the name of your enrollment table, and student_id and subject_id are the columns that together form the composite unique key. This means that for each combination of student ID and subject ID, there can only be one entry in the enrollments table.

        Now, when you try to insert a new enrollment record into the table and a duplicate combination of student and subject is detected, the database will raise a constraint violation error, and your Java application should handle this error and prevent the duplicate enrollment.

        In your Java code, before inserting a new enrollment record, you can query the database to check if a record with the same student ID and subject ID already exists. If it does, you can then choose to either update the existing record or display an error message to the user indicating that the student is already enrolled in that subject.

        Here's a simplified example in Java using JDBC for database interaction:
        
        // Assuming you have a Connection object conn and studentId and subjectId values
        // Create a PreparedStatement to check for duplicate enrollments
        String query = "SELECT COUNT(*) FROM enrollments WHERE student_id = ? AND subject_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, subjectId);
            ResultSet resultSet = pstmt.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);

            if (count > 0) {
                // Duplicate enrollment found, handle accordingly
                // You can display an error message or take other appropriate action
            } else {
                // No duplicate enrollment found, proceed with the insertion
                // Insert the new enrollment record into the enrollments table
                // ...
            }
        } catch (SQLException e) {
            // Handle any database-related exceptions
        }

        
        */
        //¿se puede modificar el query para no necesitar el metodo getEstudianteID desde inscripcion???
        // agregar "JOIN estudiante ON idEstudiante = id_Estudiante JOIN materia ON idMateria = id_materia";

        //inserto una fila en tabla inscripcion
        String sql = "INSERT INTO inscripcion (nota, idEstudiante, idMateria) VALUES (?, ?, ?)";
        
        try {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            ps.setFloat(1, insc.getNota());
            ps.setInt(2, insc.getEstudianteID());
            ps.setInt(3, insc.getMateriaID());
            
            ps.executeUpdate();
            
            //getGeneratedKeys devuelve un result set
            ResultSet rs = ps.getGeneratedKeys();
            
            //si pudo agregar la inscripción, er result set tendra un componenete, al que le seteamos el id
            if(rs.next())
            {
                // 1 es el numero de columna
                insc.setId_inscripcion(rs.getInt(1));
                JOptionPane.showMessageDialog(null, "Inscripción agregada existosamente");
            }
            //cierro el objeto para liberar recursos
            ps.close();
            
        } 
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "No se pudo acceder a la tabla inscripcion");
        }
    }
    
    //recibe como parámetro el id de un alumno
    public List<Materia> obtenerMateriasCursadas(int id)
    {
        List<Materia> materias = new ArrayList<Materia>();
        
        /*
        SELECT inscripción.idMateria, nombre, año

        En la cláusula SELECT indicamos que vamos a proyectar los campos de la tabla materia: idMateria, nombre y año; como en la cláusula FROM vamos a reunir dos tablas: INSCRIPCION y MATERIA tendríamos un inconveniente con aquellos campos que tenga el mismo nombre, como ocurre con idMateria, que figura tanto en la tabla INSCIPCION como MATERIA; por lo tanto en el SELECT nos vemos obligados para evitar un error de ambigüedad de nombre cualificar el campo idMateria con alguna de las tablas, en este caso elegía INSCRIPCION, es por ello que figura en el SELECT como inscripción.idMateria, aunque también podría haberlo escrito materia.idMateria.

        FROM inscripción JOIN materia ON (inscripción.idMateria=materia.idMateria)
        Para poder proyectar en la cláusula SELECT los datos de las materias, pero sólo de aquellas en las que está inscripto un determinado alumno, y sabemos que en la tabla inscripción solo tenemos el id del alumno y el id de la materia en el que éste está inscripto, por lo tanto si necesito completar la información con el resto de los datos de la materia que se corresponden con ese id, debo reunir (JOIN) las tablas INSCRIPCION y MATERIA, a través de clave primaria de Materia con la clave foránea idMateria en Inscripcion; es lo que indicamos en la cláusula ON(inscripción.idMateria=materia.idMateria).
        */
        String sql = "SELECT inscripcion.idMateria, nombre, año FROM inscripcion JOIN materia ON (inscripcion.idMateria = materia.id_materia) WHERE inscripcion.idEstudiante = ?";
        
        //String sql2 = "SELECT inscripcion.idMateria, nombre, año FROM inscripcion, " + " materia WHERE inscripcion.idMateria = id_materia\n" + "AND inscripcion.idEstudiante = ?;";
        
        try 
        {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            
            ResultSet rs = ps.executeQuery();
            
            Materia materia;
            
            while(rs.next())
            {
                materia = new Materia();
                materia.setId_materia(rs.getInt("idMateria"));// antes tiraba error con: id_materia
                materia.setNombre(rs.getString("nombre"));
                materia.setAnio(rs.getInt("año"));
                
                materias.add(materia);
                System.out.println("inscripción a materia listada con éxito");
            }
            ps.close();
        } 
        catch (SQLException ex) {
            ex.printStackTrace(); // Print the exception details to the console
            JOptionPane.showMessageDialog(null, "Error al obtener Inscripciones" + ex.getMessage());
        }
        return materias;
    }
    
    //lista las materias no cursadas
    public List<Materia> obtenerMateriasNoCursadas(int id)
    {
        List<Materia> materias = new ArrayList<Materia>();
        
        /*
        To retrieve the subjects where a certain student is NOT enrolled, you can use a SQL statement 
        with a LEFT JOIN and a WHERE clause that filters for rows where there is no match in the inscripcion table 
        (i.e., where the idMateria is NULL). Here's the SQL statement you can use:
        
        SELECT materia.id_materia, nombre, año
        FROM materia
        LEFT JOIN inscripcion ON (materia.id_materia = inscripcion.idMateria AND inscripcion.idEstudiante = ?)
        WHERE inscripcion.idMateria IS NULL;
        
        In this SQL statement:

        We perform a LEFT JOIN between the materia and inscripcion tables on the idMateria column.

        We also include a condition in the JOIN clause that filters for rows where idEstudiante is equal to the specified student's ID 
        (you can replace ? with the actual student's ID).

        In the WHERE clause, we further filter the results to only include rows where inscripcion.idMateria is NULL. 
        This ensures that we retrieve subjects where the student is NOT enrolled.

        This query will return the subjects where the specified student is not enrolled.
        
        */
        
        String sql = "SELECT materia.id_materia, nombre, año\n" +
        "FROM materia\n" +
        "LEFT JOIN inscripcion ON (materia.id_materia = inscripcion.idMateria AND inscripcion.idEstudiante = ?)\n" +
        "WHERE inscripcion.idMateria IS NULL AND materia.estado = true;";
        
        try 
        {
            PreparedStatement ps = con.prepareStatement(sql);
            
            ps.setInt(1, id);
            
            ResultSet rs = ps.executeQuery();
            
            Materia materia;
            
            while(rs.next())
            {
                materia = new Materia();
                materia.setId_materia(rs.getInt("id_materia"));// antes tiraba error con: idMateria
                materia.setNombre(rs.getString("nombre"));
                materia.setAnio(rs.getInt("año"));
                
                materias.add(materia);
                System.out.println("materia NO cursada listada con éxito");
            }
        } 
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al obtener Inscripciones. " + ex.getMessage());
            ex.printStackTrace(); // Print the exception details to the console
        }
        
        return materias;
    }
    
    //lista todas las inscripciones
    public List<Inscripcion> obtenerInscripciones()
    {
        List<Inscripcion> inscripciones = new ArrayList<Inscripcion>();
        
        String sql = "SELECT * FROM inscripcion";
        
        //String sql2 = "SELECT * FROM inscripcion JOIN estudiante ON idEstudiante = id_estudiante JOIN materia ON idMateria = id_materia";
        
        try 
        {
            PreparedStatement ps = con.prepareStatement(sql);
            
            ResultSet rs = ps.executeQuery();
            
            Inscripcion inscripcion;
            
            //Estudiante estud;
            EstudianteData estuDat;
            //Materia mater;
            MateriaData materDat;
            
            while(rs.next())
            {
                inscripcion = new Inscripcion();
                
                estuDat = new EstudianteData();
                
                materDat = new MateriaData();
                
                inscripcion.setId_inscripcion(rs.getInt("id_inscripcion"));
                inscripcion.setNota(rs.getFloat("nota"));
                
                inscripcion.setMateria(materDat.buscarMateriaPorID(rs.getInt("idMateria")));
                inscripcion.setEstudiante(estuDat.buscarEstudiantePorID(rs.getInt("idEstudiante")));
                
                //inscripcion.setEstudianteID(rs.getInt("estudiante.id_estudiante"));
                //inscripcion.setMateriaID(rs.getInt("materia.id_materia"));
                
                inscripciones.add(inscripcion);
                
                System.out.println("inscripción listada con éxito");
            }
            ps.close();
        } 
        catch (SQLException ex) {
            ex.printStackTrace(); // Print the exception details to the console
            JOptionPane.showMessageDialog(null, "Error al obtener Inscripciones" + ex.getMessage());
        }
        return inscripciones;
    }
    
    public List<Inscripcion> obtenerInscripcionesPorEstud(int id)
    {
        List<Inscripcion> inscripciones = new ArrayList<Inscripcion>();
        
        String sql = "SELECT * FROM inscripcion WHERE idEstudiante = ?";
        
        try 
        {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            Inscripcion inscripcion;
            EstudianteData estuDat;
            MateriaData materDat;
            
            while(rs.next())
            {
                inscripcion = new Inscripcion();
                
                estuDat = new EstudianteData();
                
                materDat = new MateriaData();
                
                inscripcion.setId_inscripcion(rs.getInt("id_inscripcion"));
                inscripcion.setNota(rs.getFloat("nota"));
                
                inscripcion.setMateria(materDat.buscarMateriaPorID(rs.getInt("idMateria")));
                inscripcion.setEstudiante(estuDat.buscarEstudiantePorID(rs.getInt("idEstudiante")));
                
                
                inscripciones.add(inscripcion);
                
                System.out.println("inscripción listada con éxito");
            }
            ps.close();
            
        } 
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al obtener Inscripciones" + ex.getMessage());
        }
        
        return inscripciones;
    }
    
    public void borrarInscripcionMateriaEstudiante(int idMateria, int idEstudiante)
    {
        //CONSULTAR: ¿establecer un borrado lògico? ¿UPDATE en vez de DELETE?
        
        JOptionPane.showMessageDialog(null, "Atención, está por intentar borrar una inscripion a la materia ID: " + idMateria + " del Estudiante ID: " + idEstudiante);
        
        String sql = "DELETE FROM inscripcion WHERE idMateria = ? AND idEstudiante = ?";
        
        try 
        {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idMateria);
            ps.setInt(2, idEstudiante);
            
            int exito = ps.executeUpdate();
            //estoy modificando una sola inscripcion, por lo tanto si es correcto, devuelve 1
            //a menos que tengamos duplicada una inscripcion
            if (exito == 1)
            {
                JOptionPane.showMessageDialog(null, "Inscripcion eliminada");
            }
        } 
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al obtener Inscripciones" + ex.getMessage());
        }
    }
    
    public void actualizarNota(int idEstud, int idMate, float nota)
    {
        String sql = "UPDATE inscripcion SET nota = ? WHERE idEstudiante = ? AND idMateria = ?";
        
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            
            ps.setFloat(1, nota);
            ps.setInt(2, idEstud);
            ps.setInt(3, idMate);
            
            
            int exito = ps.executeUpdate();
            
            if (exito > 0)
            {
                JOptionPane.showMessageDialog(null, "Se actualizó la nota del estudiante con ID " + idEstud + " en la materia ID " + idMate + " a " + nota);
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(InscripcionData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<Estudiante> obtenerEstudiantePorMateria(int idMateria)
    {
        Estudiante estud;
        List<Estudiante> estudiantes = new ArrayList<Estudiante>();
        
        String sql = "SELECT * FROM estudiante JOIN inscripcion ON id_estudiante = idEstudiante WHERE idMateria = ?";
        
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            
            ps.setInt(1, idMateria);
            
            ResultSet rs = ps.executeQuery();
            
            while(rs.next())
            {
                estud = new Estudiante();
                estud.setId_estudiante(rs.getInt("id_estudiante"));
                estud.setApellido(rs.getString("apellido"));
                estud.setNombre(rs.getString("nombre"));
                estud.setDni(rs.getInt("dni"));
                estud.setActivo(true);
                
                estudiantes.add(estud);
            }
            ps.close();
        } 
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al obtener Inscripciones" + ex.getMessage());
        }
        return estudiantes;
    }
}

