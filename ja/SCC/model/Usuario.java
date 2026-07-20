package SCC.model;

import java.util.Map;

import SCC.model.Usuario.Gustos;
import SCC.model.Usuario.Sexo;

import java.util.HashMap;
import java.io.Serializable;

public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum Sexo{Hombre, Mujer;};

    public enum Gustos{Deportes,Literatura,Musica,Recreacion,Artes,Jardineria,Cocina};

    private String Nombre;
    private String Apellido;
    private Sexo Genero;
    private int Edad;
    private Map<Gustos,Integer> ListaDeGustos;
    private Preferencias PrefPersonales;

    public Usuario(String Nombre,String Apellido,Sexo Genero,int Edad){
        this.Nombre = Nombre;
        this.Apellido = Apellido;
        this.Genero = Genero;
        this.Edad = Edad;
        ListaDeGustos = new HashMap<>();
        PrefPersonales = new Preferencias(null,null,null,null);
    }

    public String toString(){
        return "Nombre: " + Nombre + " Apellido: " + Apellido + " Genero: "+ Genero + " Edad: " + Edad; 
    }

    public void setGustos(Map<Gustos,Integer> G){
        this.ListaDeGustos = G;
    }

    public void setPreferencias(Preferencias P){
        this.PrefPersonales = P;
    }

    public void setNombre(String N) { this.Nombre = N;}
    public void setApellido(String A) { this.Apellido = A;}
    public void setEdad(int Edad){ this.Edad = Edad;}

    public String getNombre(){ return Nombre;}
    public String getApellido(){ return Apellido;}
    public int getEdad() { return Edad;}
    public Sexo getSexo(){ return Genero; }
    public Map<Gustos,Integer> getGustos(){ return ListaDeGustos;}
    public Preferencias getPreferencias(){ return PrefPersonales;}
}
