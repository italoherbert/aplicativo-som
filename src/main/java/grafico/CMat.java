package grafico;

import java.awt.Color;
import gui.desenho.GraficoConfig;

public interface CMat extends GraficoConfig {
    
    public int getMatLargura();
    
    public int getMatAltura();
    
    public Color getCor( int x, int y );
    
}
