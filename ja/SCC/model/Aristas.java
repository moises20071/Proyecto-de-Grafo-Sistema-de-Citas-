package SCC.model;

public class Aristas {
    private Usuario Destino;
    private int valor;

    public Aristas(Usuario Destino,int Valor){
        this.Destino = Destino;
        this.valor = Valor;
    }

    public Usuario getUsuario() { return Destino;}
    public int getValor() { return valor;}
}
