package SCC.model;

import java.util.Map;
import java.util.Queue;

import SCC.model.Usuario;
import SCC.model.Usuario.Gustos;
import SCC.model.Preferencias.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.math.*;

public class Cupido{
    public static class HopcroftKarpPonderadoBinario {
        private double[][] pesos;
        private int nHombres;
        private int nMujeres;
        
        // Datos de Hopcroft-Karp
        private int[] parejaHombres;
        private int[] parejaMujeres;
        private int[] distancia;
        private List<List<Integer>> grafo;
        
        public HopcroftKarpPonderadoBinario(double[][] pesos) {
            this.pesos = pesos;
            this.nHombres = pesos.length;
            this.nMujeres = pesos[0].length;
            this.parejaHombres = new int[nHombres];
            this.parejaMujeres = new int[nMujeres];
            this.distancia = new int[nHombres];
        }
        
        // ============ BÚSQUEDA BINARIA ============
        
        public double encontrarUmbralMaximo() {
            double low = 0;
            double high = 100; 
            
            // Definimos un límite mínimo aceptable de compatibilidad
            double umbralMinimoPermitido = 40.0; 

            for (int i = 0; i < 50; i++) { 
                double mid = (low + high) / 2; 
                
                if (existeMatchingConUmbral(mid)) { 
                    low = mid; 
                } else {
                    high = mid; 
                }
            }
            
            // Si la búsqueda binaria se degradó por debajo del límite mínimo,
            // retornamos el umbral mínimo para que el filtro limpie a los incompatibles.
            return Math.max(low, umbralMinimoPermitido);
        }
        
        // Verificar si existe matching perfecto con un umbral dado
        private boolean existeMatchingConUmbral(double umbral) {
            // 1. Construir grafo filtrado
            construirGrafoFiltrado(umbral);
            
            // 2. Aplicar Hopcroft-Karp
            int matchingSize = hopcroftKarp();
            
            // 3. Verificar mejor matching global
            int MaximoParejas = Math.max(nHombres,nMujeres);
            return matchingSize == MaximoParejas;
        }
        
        // ============ CONSTRUCCIÓN DEL GRAFO FILTRADO ============
        
        private void construirGrafoFiltrado(double umbral) {
            grafo = new ArrayList<>(nHombres);
            
            for (int h = 0; h < nHombres; h++) {
                List<Integer> adyacentes = new ArrayList<>();
                for (int m = 0; m < nMujeres; m++) {
                    // Solo aristas con peso >= umbral
                    if (pesos[h][m] >= umbral) {
                        adyacentes.add(m);
                    }
                }
                grafo.add(adyacentes);
            }
        }
        
        // ============ HOPCROFT-KARP ESTÁNDAR ============
        
        private int hopcroftKarp() {
            // Inicializar arrays
            Arrays.fill(parejaHombres, -1);
            Arrays.fill(parejaMujeres, -1);
            int matching = 0;
            
            // Mientras haya caminos de aumento
            while (bfs()) {
                // Buscar matches con DFS
                for (int u = 0; u < nHombres; u++) {
                    if (parejaHombres[u] == -1) {
                        if (dfs(u)) {
                            matching++;
                        }
                    }
                }
            }
            
            return matching;
        }
        
        // BFS: encuentra todos los caminos de aumento más cortos
        private boolean bfs() {
            Queue<Integer> cola = new LinkedList<>();
            
            // Inicializar distancias
            for (int u = 0; u < nHombres; u++) {
                if (parejaHombres[u] == -1) {
                    distancia[u] = 0;
                    cola.add(u);
                } else {
                    distancia[u] = Integer.MAX_VALUE;
                }
            }
            
            int distanciaNIL = Integer.MAX_VALUE;
            
            while (!cola.isEmpty()) {
                int u = cola.poll();
                
                if (distancia[u] < distanciaNIL) {
                    for (int v : grafo.get(u)) {
                        int uSiguiente = parejaMujeres[v];
                        
                        if (uSiguiente == -1) {
                            distanciaNIL = distancia[u] + 1;
                        } else if (distancia[uSiguiente] == Integer.MAX_VALUE) {
                            distancia[uSiguiente] = distancia[u] + 1;
                            cola.add(uSiguiente);
                        }
                    }
                }
            }
            
            return distanciaNIL != Integer.MAX_VALUE;
        }
        
        // DFS: busca caminos de aumento
        private boolean dfs(int u) {
            for (int v : grafo.get(u)) {
                int uSiguiente = parejaMujeres[v];
                
                if (uSiguiente == -1 || 
                    (distancia[uSiguiente] == distancia[u] + 1 && dfs(uSiguiente))) {
                    // Encontramos camino de aumento
                    parejaHombres[u] = v;
                    parejaMujeres[v] = u;
                    return true;
                }
            }
            
            distancia[u] = Integer.MAX_VALUE;
            return false;
        }
        
        // ============ OBTENER EL MATCHING RESULTANTE ============
        
        public Map<Usuario,Usuario> obtenerMatchingConUmbral(double umbral,List<Usuario> X,List<Usuario> Y) {
            // Construir grafo con el umbral
            construirGrafoFiltrado(umbral);
            
            // Ejecutar Hopcroft-Karp
            hopcroftKarp();
            
            // Construir lista de emparejamientos
            Map<Usuario,Usuario> matches = new HashMap<>();
            for (int h = 0; h < nHombres; h++) {
                if (parejaHombres[h] != -1) {
                    int m = parejaHombres[h];
                    Usuario Pibe = X.get(h);
                    Usuario Piba = Y.get(m);
                    matches.put(Pibe, Piba);
                }
            }
            
            return matches;
        }
    }        

    private static boolean Verif(Usuario V,Usuario F){
        if(V.getPreferencias() == null || V.getGustos() == null){
            return false;
        }

        if(F.getPreferencias() == null || F.getGustos() == null){
            return false;
        }

        return true;
    }

    public static int Compatibilidad(Usuario V,Usuario F){
        int compatibilidad = 100;
        if(!Verif(V, F)){
            return 0;
        }
        Preferencias pref1 = V.getPreferencias();
        Preferencias pref2 = F.getPreferencias();

        
        
        if(pref1.getPrefRelacion() != TipodeRelacion.Seria && pref2.getPrefRelacion() == TipodeRelacion.Seria || pref2.getPrefRelacion() != TipodeRelacion.Seria && pref1.getPrefRelacion() == TipodeRelacion.Seria ){
            return 0;
        }
        
        Map<Gustos,Integer> gustos1 = V.getGustos();
        Map<Gustos,Integer> gustos2 = F.getGustos();

        for(Gustos g : Gustos.values()){
            int v1 = gustos1.getOrDefault(g, 0);
            int v2 = gustos2.getOrDefault(g, 0);
            compatibilidad -= Math.sqrt(Math.pow(v1 - v2,2));
        }

        if(pref1.getPrefRelacion() != pref2.getPrefRelacion()){
            compatibilidad -= 10;
        }

        if(pref1.getPrefMascotas() != pref2.getPrefMascotas()){
            if(pref1.getPrefMascotas() == AceptacionDeMascotas.Intolerable || pref2.getPrefMascotas() == AceptacionDeMascotas.Intolerable){
                compatibilidad -= 10;
            }else{
                compatibilidad -= 5;
            }
        }

        if(pref1.getPrefConsumo() != pref2.getPrefConsumo()){
            if(pref1.getPrefConsumo() == ConsumoDeSustancias.BebedorRegular && pref2.getPrefConsumo() == ConsumoDeSustancias.BebedorSocial ){
                compatibilidad -= 2;
            }else if(pref2.getPrefConsumo() == ConsumoDeSustancias.BebedorRegular && pref1.getPrefConsumo() == ConsumoDeSustancias.BebedorSocial ){
                compatibilidad -= 2;
            }else{
                compatibilidad -= 8;
            }
        }

        if(pref1.getPrefHorarios() != pref2.getPrefHorarios()){
            compatibilidad -= 10;
        }

        return compatibilidad;
    }
}
