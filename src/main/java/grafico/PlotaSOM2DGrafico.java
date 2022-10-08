package grafico;

import java.awt.Color;
import java.awt.Graphics;
import som.Neuronio;
import som.SOMCFG;
import gui.desenho.Grafico;
import gui.desenho.PainelGrafico;

public class PlotaSOM2DGrafico extends AbstractGrafico implements Grafico<SOMCFG> {

    @Override
    public void desenha(Graphics g, SOMCFG gc, PainelGrafico painel) {
        int x = super.getX( painel );
        int y = super.getY( painel );
        int w = super.getWidth( painel );
        int h = super.getHeight( painel );
        
        int gradeQNH = gc.getGradeQuantNeuroniosHorizontal();
        int gradeQNV = gc.getGradeQuantNeuroniosVertical();
        
        Neuronio[][] neuronios = gc.getNeuronios();
        double[][] amostras = gc.getAmostras();
        
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;
        for( int i = 0; i < neuronios.length; i++ ) {
            for( int j = 0; j < neuronios[i].length; j++ ) {
                Neuronio n = neuronios[i][j];
                double[] peso = n.getPeso();
                if ( peso[0] < minX )
                    minX = peso[0];
                if ( peso[0] > maxX )
                    maxX = peso[0];
                if ( peso[1] < minY )
                    minY = peso[1];
                if ( peso[1] > maxY )
                    maxY = peso[1];
            }
        }
        
        for( int i = 0; i < amostras.length; i++ ) {
            double[] amostra = amostras[i];
            if ( amostra[0] < minX )
                minX = amostra[0];
            if ( amostra[0] > maxX )
                maxX = amostra[0];
            if ( amostra[1] < minY )
                minY = amostra[1];
            if ( amostra[1] > maxY )
                maxY = amostra[1];            
        }       
                
        for( int i = 0; i < neuronios.length; i++ ) {
            for( int j = 0; j < neuronios[i].length; j++ ) {
                Neuronio n = neuronios[i][j];
                                
                double px = n.getPeso()[0];
                double py = n.getPeso()[1];
                double fnx = ( px - minX ) / ( maxX - minX );
                double fny = ( py - minY ) / ( maxY - minY );
                double nx = x + fnx * w;
                double ny = y + fny * h;
                
                double nx2 = -1;
                double ny2 = -1;
                double nx3 = -1;
                double ny3 = -1;
                
                if ( i < gradeQNV-1 ) {
                    Neuronio n2 = gc.getNeuronios()[i+1][j];
                    double px2 = n2.getPeso()[0];
                    double py2 = n2.getPeso()[1];
                    double fnx2 = ( px2 - minX ) / ( maxX - minX );
                    double fny2 = ( py2 - minY ) / ( maxY - minY );                    
                    nx2 = x + fnx2 * w;
                    ny2 = y + fny2 * h;
                    
                    g.setColor( Color.BLACK ); 
                    g.drawLine( (int)nx, (int)ny, (int)nx2, (int)ny2 );
                }
                
                if ( j < gradeQNH-1 ) {
                    Neuronio n3 = gc.getNeuronios()[i][j+1];
                    double px3 = n3.getPeso()[0];
                    double py3 = n3.getPeso()[1];
                    double fnx3 = ( px3 - minX ) / ( maxX - minX );
                    double fny3 = ( py3 - minY ) / ( maxY - minY );  
                    nx3 = x + fnx3 * w;
                    ny3 = y + fny3 * h;
                    
                    g.setColor( Color.BLACK ); 
                    g.drawLine( (int)nx, (int)ny, (int)nx3, (int)ny3 );                    
                }
                
                g.setColor( Color.RED ); 
                g.fillArc( (int)nx-2, (int)ny-2, 4, 4, 0, 360 ); 
            }
        }
        
        for( int i = 0; i < amostras.length; i++ ) {
            double[] amostra = amostras[i];
            double fnx = ( amostra[0] - minX ) / ( maxX - minX );
            double fny = ( amostra[1] - minY ) / ( maxY - minY );
            double nx = x + fnx * w;
            double ny = y + fny * h;
            
            g.setColor( Color.BLUE );
            g.fillArc( (int)nx-2, (int)ny-2, 4, 4, 0, 360 ); 
        }
    }     
    
}
