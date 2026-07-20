package SCC.model;

import java.io.Serializable;

import SCC.model.Preferencias.AceptacionDeMascotas;
import SCC.model.Preferencias.ConsumoDeSustancias;
import SCC.model.Preferencias.Disponibilidad;
import SCC.model.Preferencias.TipodeRelacion;

public class Preferencias implements Serializable {
    private static final long serialVersionUID = 1L;
    public enum TipodeRelacion{Seria,Casual,Fluida};
    public enum AceptacionDeMascotas{Encanto,Tolerable,Intolerable};
    public enum ConsumoDeSustancias{Ninguno,Fumador,BebedorSocial,BebedorRegular};
    public enum Disponibilidad{TotalmenteLibre,EntreSemanaTardes,EntreSemanaMañanas,FinesTarde,FinesMañana};

    //filtro de quiebre
    private TipodeRelacion PreferenciaDeRelacion;

    //filtro duros
    private AceptacionDeMascotas PreferenciaDeMascotas;
    private ConsumoDeSustancias PreferenciaDeConsumSus;
    private Disponibilidad PreferenciaDeHorarios;

    public Preferencias(TipodeRelacion R,AceptacionDeMascotas M,ConsumoDeSustancias S,Disponibilidad D){
        this.PreferenciaDeRelacion = R;
        this.PreferenciaDeMascotas = M;
        this.PreferenciaDeConsumSus = S;
        this.PreferenciaDeHorarios = D;
    }

    public TipodeRelacion getPrefRelacion() {return PreferenciaDeRelacion;}
    public AceptacionDeMascotas getPrefMascotas() {return PreferenciaDeMascotas;}
    public ConsumoDeSustancias getPrefConsumo() {return PreferenciaDeConsumSus;}
    public Disponibilidad getPrefHorarios() {return PreferenciaDeHorarios;}
}
