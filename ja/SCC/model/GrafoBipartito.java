package SCC.model;

import java.util.*;

import SCC.model.Usuario.Sexo;
import java.io.Serializable;

public class GrafoBipartito implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Usuario> X;    //Para el conjunto de caballeros
    private List<Usuario> Y;    //Para el conjunto de damas
    private transient Map<Usuario,List<Aristas>> Vertices;
    private transient Map<Usuario,Usuario> Matches;

    public GrafoBipartito(){
        this.X = new ArrayList<>();
        this.Y = new ArrayList<>();
        this.Vertices = new HashMap<>();
        this.Matches = new HashMap<>();
    }

    public void RearmarGrafoTrasCarga(){
        this.Vertices = new HashMap<>();
        this.Matches = new HashMap<>();
        ArmarGrafo();
    }

    public void IngresarUsuario(Usuario Unuevo){
        switch (Unuevo.getSexo()) {
            case Hombre:
                X.add(Unuevo);
                break;
            case Mujer:
                Y.add(Unuevo);
                break;
        }
        Vertices.put(Unuevo,new ArrayList<>());
        ConstruirMatching();
    }

    public void ElimUsuario(Usuario UElim){
        if(!X.contains(UElim) && !Y.contains(UElim)){
            return;
        }

        if(UElim.getSexo() == Sexo.Hombre){
            X.remove(UElim);
        }else{
            Y.remove(UElim);
        }

        Vertices.remove(UElim);
    }

    public void ArmarGrafo(){
        for(Usuario V : X){
            List<Aristas> Conexiones = new ArrayList<>();
            for(Usuario F : Y){
                Aristas a = new Aristas(F,Cupido.Compatibilidad(V, F));
                Conexiones.add(a);
            }
            Vertices.put(V,Conexiones);
        }
    }

    private void Matching(){
        if(X.isEmpty() || Y.isEmpty()){
            System.out.print("No hay suficientes personas para poder hacer el emparejamiento");
            return;
        }

        double[][] pesos = new double[X.size()][Y.size()];
        int i = 0;
        int j = 0;
        for(Usuario V : X){
            j = 0;
            List<Aristas> Adyacentes = Vertices.get(V);
            for(Usuario F : Y){
                    pesos[i][j] =  Adyacentes.get(j).getValor();
                    j++;
            }
            i++;
        }
        
        Cupido.HopcroftKarpPonderadoBinario hk = new Cupido.HopcroftKarpPonderadoBinario(pesos);

        double umbral = hk.encontrarUmbralMaximo();

        Matches = hk.obtenerMatchingConUmbral(umbral,X,Y);
    }

    public void ConstruirMatching(){
        ArmarGrafo();
        Matching();
    }

    public Usuario getPareja(Usuario Par1){
        if(Matches.containsKey(Par1)){
            return Matches.get(Par1);
        }

        for(Map.Entry<Usuario, Usuario> asignacion : Matches.entrySet()){
            if(asignacion.getValue().equals(Par1)){
                return asignacion.getKey();
            }
        }

        return null;
    }

    public void MostrarRelacion(){
        for(Usuario V : X){
            List<Aristas> Adyacentes = Vertices.get(V);
            if(Adyacentes == null){
                System.out.println("Sin relacion");
                continue;
            }
            for(Aristas a : Adyacentes){
                String b = V.getNombre() + " " + V.getApellido() + " -> " + a.getUsuario().getNombre() + " " + a.getUsuario().getApellido() + " Compatibilidad : " + a.getValor();
                System.out.println(b);
            }            
        }
    }

    public void VerUsuarios(){
        System.out.println("Hombres: ");
        for (Usuario usuario : X) {
            System.out.println(usuario);
        }

        System.out.println("Mujeres: ");
        
        for( Usuario u : Y){
            System.out.println(u);
        }
    }

    public Map<Usuario,List<Aristas>> getVertices(){
        return this.Vertices;
    }
}