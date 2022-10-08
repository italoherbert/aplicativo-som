package aplic;

import som.SOMListener;
import gui.desenho.GraficoSource;

public interface SOMAplic extends GraficoSource, SOMListener {
                    
    public void geraNosEGrafoRede();    
                
    public void aposTreinoConcluido();
    
    public void repintaGraficoSOM();
            
}
