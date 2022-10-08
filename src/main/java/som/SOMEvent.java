package som;


public class SOMEvent {
    
    public double taxaAprendizado;    
    public double raioVizinhanca;    
    public int iteracao;
    
    public SOMCFG somConfig;
    
    public SOMEvent( SOMCFG scfg, int it, double ta, double rv ) {
        this.somConfig = scfg;
        this.iteracao = it;
        this.taxaAprendizado = ta;
        this.raioVizinhanca = rv;
    }

    public double getTaxaAprendizado() {
        return taxaAprendizado;
    }

    public double getRaioVizinhanca() {
        return raioVizinhanca;
    }

    public int getIteracao() {
        return iteracao;
    }

    public SOMCFG getSOMConfig() {
        return somConfig;
    }
    
}
