package grafico;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import gui.desenho.Grafico;
import gui.desenho.GraficoConfig;
import gui.desenho.PainelGrafico;

public class GrupoGrafico implements Grafico {

    private List<Grafico> graficos = new ArrayList();
    
    @Override
    public void desenha(Graphics g, GraficoConfig gc, PainelGrafico painel) {
        for( Grafico gf : graficos )
            gf.desenha( g, gc, painel ); 
    }        
    
    public void addGrafico( Grafico grafico ) {
        graficos.add( grafico );
    }
        
}
