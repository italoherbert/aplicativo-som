package exemplos.somNd;

import italo.iplot.thread.PCOperThread;
import italo.iplot.gui.plot.Plot2DGUI;
import italo.iplot.plot2d.Plot2D;
import italo.iplot.plot2d.g2d.GrafoObjeto2D;
import javax.swing.JFrame;
import italo.iplot.plot2d.planocartesiano.PlanoCartesianoPlot2D;
import italo.iplot.plot2d.planocartesiano.PlanoCartesianoPlot2DDriver;
import italo.iplot.plot2d.planocartesiano.g2d.PlanoCartesianoObjeto2D;
import italo.iplot.plot3d.g3d.Objeto3D;
import italo.iplot.plot3d.g3d.SuperficieObjeto3D;
import italo.iplot.plot3d.planocartesiano.PlanoCartesianoPlot3D;
import italo.iplot.plot3d.planocartesiano.PlanoCartesianoPlot3DDriver;
import italo.iplot.plot3d.planocartesiano.g3d.PlanoCartesianoObjeto3D;
import italo.iplot.thread.plot2d.PCPlot2DOperManagerThread;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import som.Neuronio;
import som.SOM;
import som.SOMCFG;
import som.SOMEvent;
import som.SOMMatrizU;
import som.SOMMatrizUSuperficie;
import aplic.SOMAplicContainer;
import gui.desenho.Grafico;
import italo.iplot.thread.plot2d.controller.ThreadPCGUI2DDesenhoControlador;

public class SOM2DAplic implements SOMAplic2, PlanoCartesianoPlot2DDriver {

    private final SOMAplicContainer aplicContainer;
    private final PlanoCartesianoPlot2D plot2D = new PlanoCartesianoPlot2D();  
    private final PCPlot2DOperManagerThread operManagerThread = new PCPlot2DOperManagerThread( plot2D );
    
    private GrafoObjeto2D pontosObj2d;
    private GrafoObjeto2D redeObj2d;

    public SOM2DAplic( SOMAplicContainer manager ) {
        this.aplicContainer = manager;
    }

    @Override
    public void configura(Plot2D plot2D, PlanoCartesianoObjeto2D pc) {
        pc.setTitulo( "Treinamento de rede SOM 2D" ); 
        pc.setArestasCor( Color.BLACK );
        pc.getPlotObj2DManager().setXYNumRotulos( 9 ); 
        
        redeObj2d = new GrafoObjeto2D();
        redeObj2d.setVerticeRaio( 0.005 );
        redeObj2d.setVerticesCor( Color.RED );
        redeObj2d.setArestasCor( Color.BLACK ); 
                
        pontosObj2d = new GrafoObjeto2D();
        pontosObj2d.setVerticeRaio( 0.01 );
        pontosObj2d.setPintarVertices( true ); 
        pontosObj2d.setPintarArestas( false );        
        pontosObj2d.setVerticesCor( Color.BLUE );         
                
        pc.addComponenteObj2D( pontosObj2d );
        pc.addComponenteObj2D( redeObj2d );
    }
    
    @Override
    public void inicializaSOM( String aplicNome ) {                
        Plot2DGUI pgui = plot2D.novaPlot2DGUI( new ThreadPCGUI2DDesenhoControlador( operManagerThread ) );
        aplicContainer.getSOMGUI().addDesenhoComponent( pgui, aplicNome );

        SwingUtilities.invokeLater( () -> {           
            aplicContainer.getSOMGUI().setVisible( true ); 

            int w = plot2D.getDesenhoComponent().getWidth();
            int h = plot2D.getDesenhoComponent().getHeight();

            plot2D.constroi( this, w, h );

            operManagerThread.startThread();
        } );
    }
    
    @Override
    public void primeiraVezGrafoGerado() {
        plot2D.getPlanoCartesiano().constroi( plot2D ); 
    }
    
    @Override
    public void geraNosEGrafoRede() {
        Neuronio[][] neuronios = aplicContainer.getSOMCFG().getNeuronios();
        double[][] amostras = aplicContainer.getSOMCFG().getAmostras();
        
        double[][] amostrasPontos = new double[ amostras.length ][];
        for( int i = 0; i < amostras.length; i++ )
            amostrasPontos[i] = amostras[i].clone();                                                        
                
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
        
        int nl = neuronios.length;
        int nc = ( nl <= 0 ? 0 : neuronios[0].length );            
        
        double[][] nos = new double[ nl*nc ][];                
        List<int[]> arestas = new ArrayList();

        int k = 0;
        for( int i = 0; i < nl; i++ )
            for( int j = 0; j < nc; j++ )
                nos[k++] = neuronios[i][j].getPeso().clone();                
                    
        for( int i = 0; i < nl; i++ ) {
            for( int j = 0; j < nc; j++ ) {     
                int k1 = i * nc + j;
                                
                if ( i < nl-1 ) {
                    int k2 = (i+1) * nc + j;
                    arestas.add( new int[] { k1, k2 } );
                }
                
                if ( j < nc-1 ) {
                    int k3 = i * nc + j + 1;
                    arestas.add( new int[] { k1, k3 } );                
                }
            }
        } 
        
        int[][] grafoArestas = new int[ arestas.size() ][];
        grafoArestas = arestas.toArray( grafoArestas );
        
        pontosObj2d.setGrafoNos( amostrasPontos );
        redeObj2d.setGrafoNos( nos ); 
        redeObj2d.setGrafoArestas( grafoArestas );
    }

    @Override
    public void repintaGraficoSOM() {     
        if ( operManagerThread.getOper() == PCOperThread.NENHUM )            
            operManagerThread.semConfigConstroi();                 
    }
    
    @Override
    public void execIT(SOMEvent e) {        
        aplicContainer.setIteracaoInfo( e ); 
        if ( e.getIteracao() % 10 != 0 )
            return;                                                                           
        
        this.geraNosEGrafoRede();
        aplicContainer.repintaGrafico();
        
        try {
            Thread.sleep( aplicContainer.getSOMGUI().getDelay() );
        } catch (InterruptedException ex) {
            
        }
    }
    
    @Override
    public void aposTreinoConcluido() {  
        SOM som = aplicContainer.getSOM();
        SOMCFG cfg = aplicContainer.getSOMCFG();
        if ( som.getIteracao() < cfg.getQuantIteracoes() )
            return;
                    
        PlanoCartesianoPlot3D muPlot3D = new PlanoCartesianoPlot3D();

        SwingUtilities.invokeLater( () -> {
            JFrame matUFrame = new JFrame();
            matUFrame.setTitle( "Matriz U" ); 
            matUFrame.setContentPane( muPlot3D.getDesenhoComponent() );
            matUFrame.setSize( 500, 500 );
            matUFrame.setDefaultCloseOperation( JFrame.HIDE_ON_CLOSE );
            matUFrame.setLocationRelativeTo( matUFrame );                        

            SOMMatrizU matU = som.geraMatrizU( cfg );
            SOMMatrizUSuperficie superficie = som.geraMatrizUSuperficie( matU );
            double[] vx = superficie.getVetorX();
            double[] vz = superficie.getVetorZ();
            double[][] my = superficie.getMatrizY();

            PlanoCartesianoPlot3DDriver drv = (PlanoCartesianoObjeto3D pc) -> {
                SuperficieObjeto3D superficieObj3d = new SuperficieObjeto3D( vx, vz, my );
                superficieObj3d.setPreenchimento( Objeto3D.Preenchimento.GRADIENTE ); 

                pc.setAltura2D( 1.4 ); 
                pc.setTitulo( "Matriz U" ); 
                pc.addComponenteObj3D( superficieObj3d ); 

            }; 

            matUFrame.setVisible( true );                 

            int largura = muPlot3D.getDesenhoComponent().getWidth();
            int altura = muPlot3D.getDesenhoComponent().getHeight();

            muPlot3D.constroi( drv, largura, altura );            
            muPlot3D.addPCRotacaoDesenhoGUIListener();
        } );
    }

    @Override
    public Grafico getGrafico() {
        return null;
    }
    
}
