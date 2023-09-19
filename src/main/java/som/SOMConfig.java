package som;

public class SOMConfig {
    
    private int gradeQuantNeuroniosHorizontal;
    private int gradeQuantNeuroniosVertical;
        
    private double taxaAprendizadoInicial;
    private double taxaAprendizadoFinal;
    
    private double raioVizinhancaInicial;
    private double raioVizinhancaFinal;
    
    private boolean vizinhancaHexagonal;
        
    private int gradeNEsp = 5;
        
    private int quantGrupoITs;
    private int quantITs;
    
    public int getQuantIteracoes() {
        return quantITs;
    }
    
    public void setQuantIteracoes( int quantITs ) {
        this.quantITs = quantITs;
    }
                
    public int getQuantGrupoIteracoes() {
        return quantGrupoITs;
    }

    public void setQuantGrupoIteracoes(int quantITs) {
        this.quantGrupoITs = quantITs;
    }
    
    public int getGradeQuantNeuroniosHorizontal() {
        return gradeQuantNeuroniosHorizontal;
    }

    public void setGradeQuantNeuroniosHorizontal(int gradeQuantNeuroniosHorizontal) {
        this.gradeQuantNeuroniosHorizontal = gradeQuantNeuroniosHorizontal;
    }

    public int getGradeQuantNeuroniosVertical() {
        return gradeQuantNeuroniosVertical;
    }

    public void setGradeQuantNeuroniosVertical(int gradeQuantNeuroniosVertical) {
        this.gradeQuantNeuroniosVertical = gradeQuantNeuroniosVertical;
    }

    public int getGradeNEsp() {
        return gradeNEsp;
    }

    public void setGradeNEsp(int gradeNEsp) {
        this.gradeNEsp = gradeNEsp;
    }

    public double getTaxaAprendizadoInicial() {
        return taxaAprendizadoInicial;
    }

    public void setTaxaAprendizadoInicial(double taxaAprendizadoInicial) {
        this.taxaAprendizadoInicial = taxaAprendizadoInicial;
    }

    public double getTaxaAprendizadoFinal() {
        return taxaAprendizadoFinal;
    }

    public void setTaxaAprendizadoFinal(double taxaAprendizadoFinal) {
        this.taxaAprendizadoFinal = taxaAprendizadoFinal;
    }

    public double getRaioVizinhancaInicial() {
        return raioVizinhancaInicial;
    }

    public void setRaioVizinhancaInicial(double raioVizinhancaInicial) {
        this.raioVizinhancaInicial = raioVizinhancaInicial;
    }

    public double getRaioVizinhancaFinal() {
        return raioVizinhancaFinal;
    }

    public void setRaioVizinhancaFinal(double raioVizinhancaFinal) {
        this.raioVizinhancaFinal = raioVizinhancaFinal;
    }

    public boolean isVizinhancaHexagonal() {
        return vizinhancaHexagonal;
    }

    public void setVizinhancaHexagonal(boolean vizinhancaHexagonal) {
        this.vizinhancaHexagonal = vizinhancaHexagonal;
    }
    
}
