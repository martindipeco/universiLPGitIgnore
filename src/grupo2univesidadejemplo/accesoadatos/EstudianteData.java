/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ulpejemplo.accesoDatos;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import ulpejemplo.entidades.Estudiante;

/**
 *
 * @author marti
 */
public class EstudianteData {
    
    private Connection con = null;
    
    public EstudianteData()
    {
        con = Conexion.getConexion();
    }
    
    public void guardarEstudiante(Estudiante estudiante)
    {
        //agrego una fila a estudiante
        String sql = "INSERT INTO estudiante (dni, apellido, nombre, fechaNac, estado)VALUES (?, ?, ?, ?, ?)";

        try 
        {
            //pido que devuelva la clave primaria generada
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, estudiante.getDni());
            ps.setString(2, estudiante.getApellido());
            ps.setString(3, estudiante.getNombre());
            ps.setDate(4, Date.valueOf(estudiante.getFechaNac()));
            ps.setBoolean(5, estudiante.isActivo());
            
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            //si pudo agregar el estudiante, le seteamos el id
            if(rs.next())
            {
                // 1 es el numero de columna
                estudiante.setId_estudiante(rs.getInt(1));
                JOptionPane.showMessageDialog(null, "Alumno agregado existosamente");
            }
            //cierro el objeto para liberar recursos
            ps.close();
        } 
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "No se pudo acceder a la tabla alumnos");
        }
    }
    
    public void modificarEstudiante (Estudiante estudiante)
    {
        String sqlUp = "UPDATE estudiante SET dni = ?, apellido = ?, nombre = ?, fechaNac = ? WHERE id_estudiante = ?";
        
        try 
        {
            PreparedStatement ps = con.prepareStatement(sqlUp);
            
            ps.setInt(1, estudiante.getDni());
            ps.setString(2, estudiante.getApellido());
            ps.setString(3, estudiante.getNombre());
            ps.setDate(4, Date.valueOf(estudiante.getFechaNac()));
            
            //finalemnte el id que tambien paso por parametro
            ps.setInt(5, estudiante.getId_estudiante());
            
            int exito = ps.executeUpdate();
            //estoy modificando un solo alumno, por lo tanto si es correcto, devuelve 1
            if (exito == 1)
            {
                JOptionPane.showMessageDialog(null, "Estudiante modificado existosamente");
            }
            // debería agregar ps.close() ???
        } 
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "No se pudo acceder a la tabla estudiante");
        }
    }
    
    public void eliminarEstudiante(int id)
    {
        String sqlUp = "UPDATE estudiante SET estado = false WHERE id_estudiante = ?";
        
        try 
        {
            PreparedStatement ps = con.prepareStatement(sqlUp);
            ps.setInt(1, id);
            int exito = ps.executeUpdate();
            //estoy modificando un solo alumno, por lo tanto si es correcto, devuelve 1
            if (exito == 1)
            {
                JOptionPane.showMessageDialog(null, "Estudiante dado de baja");
            }
        } 
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "No se pudo acceder a la tabla estudiante");
        }
        
        // deberia agregar ps.close() ??
    }
    
    public Estudiante buscarEstudiantePorID(int id)
    {
        String sql = "SELECT dni, apellido, nombre, fechaNac FROM estudiante WHERE id_estudiante = ? AND estado = true"; //se puede poner estado = 1
        
        //declaro atributo que recibirá parametros del result set
        Estudiante est = null;
        
        try 
        {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            
            //executeQuery devuleve un result set
            ResultSet rs = ps.executeQuery();
            
            if(rs.next())
            {
                // construyo estudiante con datos de tabla
                est = new Estudiante();
                //debo incluir el id de vuelta
                est.setId_estudiante(id);
                est.setDni(rs.getInt("dni"));
                est.setApellido(rs.getString("apellido"));
                est.setNombre(rs.getString("nombre"));
                est.setFechaNac(rs.getDate("fechaNac").toLocalDate());
                est.setActivo(true);
            }
            else
            {
                JOptionPane.showMessageDialog(null, "No se enontró estudiante con id: " + id);
            }
            //cierro el objeto para liberar recursos
            ps.close();
            
        } 
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "No se pudo acceder a la tabla estudiante");
        }
        return est;
    }
    
    public Estudiante buscarEstudiantePorDNI(int dni)
    {
        String sql = "SELECT id_estudiante, dni, apellido, nombre, fechaNac FROM estudiante WHERE dni = ? AND estado = true"; //se puede poner estado = 1
        
        //declaro atributo que recibirá parametros del result set
        Estudiante est = null;
        
        try 
        {
            PreparedStatement ps = con.prepareStatement(sql);
            //asigno valor a ? placeholder
            ps.setInt(1, dni);
            
            //executeQuery devuleve un result set
            ResultSet rs = ps.executeQuery();
            
            if(rs.next())
            {
                // construyo estudiante con datos de tabla
                est = new Estudiante();
                //debo incluir el id de vuelta
                est.setId_estudiante(rs.getInt("id_estudiante"));
                est.setDni(rs.getInt("dni"));
                est.setApellido(rs.getString("apellido"));
                est.setNombre(rs.getString("nombre"));
                est.setFechaNac(rs.getDate("fechaNac").toLocalDate());
                est.setActivo(true);
            }
            else
            {
                JOptionPane.showMessageDialog(null, "No se enontró estudiante con DNI: " + dni);
            }
            //cierro el objeto para liberar recursos
            ps.close();
            
        } 
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "No se pudo acceder a la tabla estudiante");
        }
        return est;
    }
    
    
    public List<Estudiante> listarEstudiantesActivos()
    {
        String sql = "SELECT id_estudiante, dni, apellido, nombre, fechaNac FROM estudiante WHERE estado = true"; //se puede poner estado = 1
        
        //declaro atributo que recibirá parametros del result set
        List<Estudiante> estudiantes = new ArrayList<>();
        
        try 
        {
            PreparedStatement ps = con.prepareStatement(sql);
            
            //executeQuery devuleve un result set
            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                // construyo estudiante con datos de tabla
                Estudiante est = new Estudiante();
                //debo incluir el id de vuelta
                est.setId_estudiante(rs.getInt("id_estudiante"));
                est.setDni(rs.getInt("dni"));
                est.setApellido(rs.getString("apellido"));
                est.setNombre(rs.getString("nombre"));
                est.setFechaNac(rs.getDate("fechaNac").toLocalDate());
                est.setActivo(true);
                //agrego estudiante a la lista
                estudiantes.add(est);
            }

            //cierro el objeto para liberar recursos
            ps.close();
            
        } 
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "No se pudo acceder a la tabla estudiante");
        }
        return estudiantes;
    }
}
