package SCC.model;

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

import SCC.model.Usuario.Gustos;
import SCC.model.Usuario.Sexo;

public class GCC implements Serializable{
    private static final long serialVersionUID = 1L;
    public GrafoBipartito grafo;
    private Map<String,Usuario> Usuarios;
    transient Usuario UIngresado;

    public GCC(){
        grafo = new GrafoBipartito();
        Usuarios = new HashMap<>();
        UIngresado = null;
    }

    public boolean CrearCuenta(String Nombre,String Apellido,Sexo G,int Edad,String Clave){
        Usuario nuevo = new Usuario(Nombre, Apellido, G, Edad);
        if(Usuarios.containsKey(Clave)){
            return false;
        }
        Usuarios.put(Clave,nuevo);
        grafo.IngresarUsuario(nuevo);
        return true;
    }

    public void ElimCuenta(String Clave){
        if(Usuarios.containsKey(Clave)){
            Usuario a = Usuarios.get(Clave);
            if(a == UIngresado){
                UIngresado = null;
            }
            Usuarios.remove(Clave);
            grafo.ElimUsuario(a);
        }
    }

    public void ElimCuenta(Usuario EU){
        String Clave = new String();
        for(Map.Entry<String,Usuario> U : Usuarios.entrySet()){
            if(U.getValue().equals(EU)){
                Clave = U.getKey();
            }
        }
        Usuarios.remove(Clave);
        UIngresado = null;
    }

    public void modifCuenta(String Nnombre,String Napellido,int Nedad){
        UIngresado.setNombre(Nnombre);
        UIngresado.setApellido(Napellido);
        UIngresado.setEdad(Nedad);
    }

    public void modifCredencial(String NClave){
        String Clave = new String();
        for(Map.Entry<String,Usuario> U : Usuarios.entrySet()){
            if(U.getValue().equals(UIngresado)){
                Clave = U.getKey();
                break;
            }
        }
        Usuarios.remove(Clave);
        Usuarios.put(NClave,UIngresado);
    }

    public void modifGustos(Map<Gustos,Integer> Gnuevos){
        if(UIngresado != null){
            UIngresado.setGustos(Gnuevos);
        }
    }

    public void modifPref(Preferencias PrefNuevos){
        if(UIngresado != null){
            UIngresado.setPreferencias(PrefNuevos);
        }
    }

    public Usuario getUIngresado() {
        return this.UIngresado;
    }

    public Usuario getPareja(){
        grafo.ConstruirMatching();
        Usuario Pareja = grafo.getPareja(UIngresado);
        return Pareja;
    }

    public void Login(String Clave,String Nombre,String Apellido){
        if(Usuarios.containsKey(Clave)){
            Usuario U = Usuarios.get(Clave);
            if(U.getNombre().equals(Nombre) && U.getApellido().equals(Apellido)){
                UIngresado = U;
            }
        }
    }
    
    public void Logout(){
        UIngresado = null;
    }

    public Map<String, Usuario> getUsuarios() {
        return this.Usuarios;
    }
}
