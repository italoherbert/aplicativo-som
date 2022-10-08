package gui.desenho;

import java.awt.Graphics;
import javax.swing.JPanel;

public class DesenhoPNL extends JPanel implements PainelGrafico, GraficoSource {
    
    private Grafico grafico;
    private GraficoConfig gc;
    private GraficoSource gsource;
            
    @Override
    public void paintComponent( Graphics g ) {
        super.paintComponent(g);
        
        Grafico gfc = this.getGrafico();
        if ( gfc != null && gc != null )
            gfc.desenha( g, gc, this );
    }
        
    public void repintaGrafico( GraficoConfig gc ) {
        this.gc = gc; 
        super.repaint();
    }

    public GraficoConfig getGraficoConfig() {
        return gc;
    }

    public void setGraficoConfig(GraficoConfig gc) {
        this.gc = gc;
    }

    public Grafico getGrafico() {
        if ( gsource == null )
            return grafico;
        return gsource.getGrafico();
    }
    
    public void setGrafico( Grafico grafico ) {
        this.grafico = grafico; 
    }
    
    public void setGraficoSource( GraficoSource gsource ) {
        this.gsource = gsource;
    }
    
}
