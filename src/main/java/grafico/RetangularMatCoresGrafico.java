package grafico;

import java.awt.Graphics;
import gui.desenho.Grafico;
import gui.desenho.PainelGrafico;

public class RetangularMatCoresGrafico extends MatCoresGrafico implements Grafico<CMat> {
            
    @Override
    public void desenha(Graphics g, CMat gc, PainelGrafico painel) {
        int x = super.getX( painel );
        int y = super.getY( painel );
        int w = super.getWidth( painel );
        int h = super.getHeight( painel );
        
        int nl = gc.getMatAltura();
        int nc = gc.getMatLargura();
        
        int qw = (int)Math.floor( (double)w / (double)nc );
        int qh = (int)Math.floor( (double)h / (double)nl );
        
        for( int i = 0; i < nl; i++ ) {
            for( int j = 0; j < nc; j++ ) {
                int qx = (int)Math.floor( x + j*qw );
                int qy = (int)Math.floor( y + i*qh );                
                
                g.setColor( gc.getCor( j, i ) );                 
                g.fillRect( qx, qy, qw, qh );
                if ( super.mostrarGrade ) {
                    g.setColor( super.gradeCor ); 
                    g.drawRect( qx, qy, qw, qh );
                }
            }
        }
        
    }
    
}
