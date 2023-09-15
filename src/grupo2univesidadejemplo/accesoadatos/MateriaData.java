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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import ulpejemplo.entidades.Materia;


public class MateriaData {
    
    private Connection con; // para ser más explícito se puede inicializar = null;
    
    public MateriaData()
    {
        //al iniciarse el constructor, asigno conexion para cargar drivers
        con = Conexion.getConexion();
    }
    
    //métodos de MateriaData
    
    public void guardarMateria (Materia materia)
    {
        //necesito agregar una fila a la tabla materia
        //el id se agrega solo
        String sql = "INSERT INTO materia (codigo_materia, nombre, año, estado) VALUES (?, ?, ?, ?)";
        
        try {
            //preparo el statement y pido que me devuelva la clave primaria autogenerada
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            //seteo los placeholders (comodines ?)
            //1 corresponde al primer ? de codigo_materia, 2 a nombre, etc
            ps.setInt(1, materia.getCodigoMateria());
            ps.setString(2, materia.getNombre());
            ps.setInt(3, materia.getAnio());
            ps.setBoolean(4, materia.isActiva());
            
            ps.executeUpdate();
            
            //getGeneratedKeys devuelve un result set
            ResultSet rs = ps.getGeneratedKeys();
            
            //si pudo agregar la materia, er result set tendra un componenete, al que le seteamos el id
            if(rs.next())
            {
                // 1 es el numero de columna
                materia.setId_materia(rs.getInt(1));
                JOptionPane.showMessageDialog(null, "Materia agregada existosamente");
            }
            //cierro el objeto para liberar recursos
            ps.close();
            
        } 
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "No se pudo acceder a la tabla materia");
        }
    }
    
    public Materia buscarMateriaPorID(int id)
    {
        String sql = "SELECT codigo_materia, nombre, año FROM materia WHERE id_materia = ? AND estado = true"; //se puede poner estado = 1
        
        //declaro atributo que recibirá parametros del result set
        Materia mate = null;
        
        try 
        {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            
            //executeQuery devuleve un result set
            ResultSet rs = ps.executeQuery();
            
            if(rs.next())
            {
                // construyo materia con datos de tabla
                mate = new Materia();
                //debo incluir el id de vuelta
                mate.setId_materia(id);
                mate.setCodigoMateria(rs.getInt("codigo_materia"));
                mate.setNombre(rs.getString("nombre"));
                mate.setAnio(rs.getInt("año"));
                mate.setActiva(true);
            }
            else
            {
                JOptionPane.showMessageDialog(null, "No se enontró materia con id: " + id);
            }
            //cierro el objeto para liberar recursos
            ps.close();
            
        } 
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "No se pudo acceder a la tabla materia");
        }
        return mate;
    }
    
    public void modificarMateria (Materia materia)
    {
        //hago update de tala materia
        String sqlUp = "UPDATE materia SET codigo_materia = ?, nombre = ?, año = ? WHERE id_materia = ?";
        
        try 
        {
            PreparedStatement ps = con.prepareStatement(sqlUp);
            
            ps.setInt(1, materia.getCodigoMateria());
            ps.setString(2, materia.getNombre());
            ps.setInt(3, materia.getAnio());
            
            //finalemnte el id que tambien paso por parametro
            ps.setInt(4, materia.getId_materia());
            
            int exito = ps.executeUpdate();
            //estoy modificando una sola materia, por lo tanto si es correcto, devuelve 1
            if (exito == 1)
            {
                JOptionPane.showMessageDialog(null, "Materia modificada existosamente");
            }
            // debería agregar ps.close() ???
        } 
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "No se pudo acceder a la tabla materia");
        }
    }
    
    public void eliminarMateria(int id)
    {
        String sql = "UPDATE materia SET estado = false WHERE id_materia = ?";
        
        try 
        {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int exito = ps.executeUpdate();
            //estoy modificando una sola materia, por lo tanto si es correcto, devuelve 1
            if (exito == 1)
            {
                JOptionPane.showMessageDialog(null, "Mataria dada de baja");
            }
        } 
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "No se pudo acceder a la tabla materia");
        }
        
        // deberia agregar ps.close() ??
    }
    
    public List<Materia> listarMaterias()
    {
        String sql = "SELECT id_materia, codigo_materia, nombre, año FROM materia WHERE estado = true"; //se puede poner estado = 1
        
        //declaro atributo que recibirá parametros del result set
        List<Materia> materias = new ArrayList<>();
        
        try 
        {
            PreparedStatement ps = con.prepareStatement(sql);
            
            //executeQuery devuleve un result set
            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                // construyo estudiante con datos de tabla
                Materia mate = new Materia();
                //debo incluir el id de vuelta
                mate.setId_materia(rs.getInt("id_materia"));
                mate.setCodigoMateria(rs.getInt("codigo_materia"));
                mate.setNombre(rs.getString("nombre"));
                mate.setAnio(rs.getInt("año"));
                mate.setActiva(true);
                
                //agrego a la lista
                materias.add(mate);
            }

            //cierro el objeto para liberar recursos
            ps.close();
            
        } 
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "No se pudo acceder a la tabla materia");
        }
        return materias;
    }
}
