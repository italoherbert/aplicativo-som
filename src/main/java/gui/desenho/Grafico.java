package gui.desenho;

import java.awt.Graphics;

public interface Grafico<T extends GraficoConfig> {
    
    public void desenha( Graphics g, T gc, PainelGrafico painel );
    
}
