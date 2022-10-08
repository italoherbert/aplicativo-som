package som;

import gui.desenho.GraficoConfig;

public class SOMCFG extends SOMConfig implements GraficoConfig {
                
    private Neuronio[][] neuronios;
    private double[][] amostras;
    private double[][][] grade;
        
    private SOMListener listener;
    private SOMAlgoritmoDriver somAlgoritmoDrv = null;
    
    public SOMCFG() {}
    
    public boolean isFinalizar() {
        if ( somAlgoritmoDrv != null )
            return somAlgoritmoDrv.isFinalizar();
        return false;
    }
    
    public Neuronio[][] getNeuronios() {
        return neuronios;
    }

    public void setNeuronios(Neuronio[][] neuronios) {
        this.neuronios = neuronios;
    }

    public double[][] getAmostras() {
        return amostras;
    }

    public void setAmostras(double[][] amostras) {
        this.amostras = amostras;
    }

    public double[][][] getGrade() {
        return grade;
    }

    public void setGrade(double[][][] grade) {
        this.grade = grade;
    }

    public SOMListener getSOMListener() {
        return listener;
    }

    public void setSOMListener(SOMListener listener) {
        this.listener = listener;
    }

    public SOMAlgoritmoDriver getSOMAlgoritmoDriver() {
        return somAlgoritmoDrv;
    }

    public void setSOMAlgoritmoDriver(SOMAlgoritmoDriver condicaoParada) {
        this.somAlgoritmoDrv = condicaoParada;
    }    
    
}
