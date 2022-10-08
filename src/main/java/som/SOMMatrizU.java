package som;

import gui.desenho.GraficoConfig;

public class SOMMatrizU implements GraficoConfig {
    
    private double[][] matrizU;
    private int largura;
    private int altura;
    private SOMCFG config;
    
    public SOMMatrizU() {}
    
    public SOMMatrizU(double[][] matrizU, int largura, int altura, SOMCFG config) {
        this.matrizU = matrizU;
        this.largura = largura;
        this.altura = altura;
        this.config = config;
    }

    public double[][] getMatrizU() {
        return matrizU;
    }

    public void setMatrizU(double[][] matrizU) {
        this.matrizU = matrizU;
    }

    public int getLargura() {
        return largura;
    }

    public void setLargura(int largura) {
        this.largura = largura;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public SOMCFG getConfig() {
        return config;
    }

    public void setConfig(SOMCFG config) {
        this.config = config;
    }
    
}
