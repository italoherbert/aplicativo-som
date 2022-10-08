package grafico;

import java.awt.Color;
import java.awt.Graphics;
import som.SOMCFG;
import som.SOMMatrizU;
import gui.desenho.Grafico;
import gui.desenho.PainelGrafico;

public class PlotaMatrizUReduzida2DGrafico extends MatCoresGrafico implements Grafico<SOMMatrizU> {

    @Override
    public void desenha(Graphics g, SOMMatrizU gc, PainelGrafico painel) {
        int x = super.getX( painel );
        int y = super.getY( painel );
        int w = super.getWidth( painel );
        int h = super.getHeight( painel );
        
        double[][] mat_u = gc.getMatrizU();                
        int nl = gc.getAltura();
        int nc = gc.getLargura();
        
        SOMCFG config = gc.getConfig();
        int gradeQNV = config.getGradeQuantNeuroniosVertical();
        int gradeQNH = config.getGradeQuantNeuroniosHorizontal();        
        
        int qw = (int)Math.floor( (double)w / (double)(gradeQNH) );
        int qh = (int)Math.floor( (double)h / (double)(gradeQNV) );
        
        double minMatUValor = Double.MAX_VALUE;
        double maxMatUValor = Double.MIN_VALUE;
        for( int i = 0; i < nl; i++ ) {
            for( int j = 0; j < nc; j++ ) {
                if ( i % 2 == 0 && j % 2 == 0 ) { 
                    double v = mat_u[i][j];
                    if ( v < minMatUValor )
                        minMatUValor = v;
                    if ( v > maxMatUValor )
                        maxMatUValor = v;
                }
            }
        }
        
        int k = 0;
	for( int i = 0; i < nl; i++ ) {
            for( int j = 0; j < nc; j++ ) {
                if ( i % 2 == 0 && j % 2 == 0 ) { 
                    int gi = (int)Math.floor( (double)k / gradeQNV );
                    int gj = ( k % gradeQNH );
                    int qx = (int)Math.floor( x + gi*qw );
                    int qy = (int)Math.floor( y + gj*qh );                
                    double v = mat_u[i][j];
                    int sinza = (int) Math.round( 255 * ( ( v - minMatUValor ) / ( maxMatUValor - minMatUValor ) ) );

                    g.setColor( new Color( sinza, sinza, sinza ) );                 
                    g.fillRect( qx, qy, qw, qh );
                    if ( super.mostrarGrade ) {
                        g.setColor( super.gradeCor ); 
                        g.drawRect( qx, qy, qw, qh );
                    }
                    k++;
                }
            }
        }	            
    }
    
}
