package exemplos.somNd;

import italo.iplot.thread.PCOperThread;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import italo.iplot.plot3d.g3d.Objeto3D;
import italo.iplot.plot3d.g3d.GrafoObjeto3D;
import italo.iplot.plot3d.g3d.SuperficieObjeto3D;
import italo.iplot.plot3d.planocartesiano.PlanoCartesianoPlot3D;
import italo.iplot.plot3d.planocartesiano.PlanoCartesianoPlot3DDriver;
import italo.iplot.plot3d.planocartesiano.g3d.PlanoCartesianoObjeto3D;
import som.Neuronio;
import som.SOM;
import som.SOMCFG;
import som.SOMEvent;
import som.SOMMatrizU;
import som.SOMMatrizUSuperficie;
import aplic.SOMAplicContainer;
import gui.desenho.Grafico;
import italo.iplot.gui.plot.Plot3DGUI;
import italo.iplot.thread.plot3d.PCPlot3DOperManagerThread;
import italo.iplot.thread.plot3d.controller.ThreadPCGUIDesenhoControlador;
import javax.swing.SwingUtilities;

public class SOM3DAplic implements SOMAplic2, PlanoCartesianoPlot3DDriver {
                
    private final PlanoCartesianoPlot3D plot3D = new PlanoCartesianoPlot3D();
    private final PCPlot3DOperManagerThread operManagerThread = new PCPlot3DOperManagerThread( plot3D );    
    
    private GrafoObjeto3D grafoObj3d;
    private GrafoObjeto3D pontosObj3d;
        
    private final SOMAplicContainer aplicContainer;
        
    public SOM3DAplic( SOMAplicContainer container ) {         
        this.aplicContainer = container;                        
    }

    @Override
    public void configura(PlanoCartesianoObjeto3D pc) {
        pc.setTitulo( "Treinamento de rede SOM 3D" ); 
        pc.setArestasCor( Color.BLACK );
        
        grafoObj3d = new GrafoObjeto3D();
        grafoObj3d.setVerticeRaio( 0.01 );
        grafoObj3d.setVerticesCor( Color.RED );
        grafoObj3d.setArestasCor( Color.BLACK ); 
                
        pontosObj3d = new GrafoObjeto3D();
        pontosObj3d.setVerticeObjTipo( Objeto3D.VCIRCULO ); 
        pontosObj3d.setVerticeRaio( 0.01 );
        pontosObj3d.setPintarVertices( true ); 
        pontosObj3d.setPintarArestas( false );        
        pontosObj3d.setVerticesCor( Color.BLUE );         
        
        pc.setAltura2D( 1.95d );
        
        pc.addComponenteObj3D( pontosObj3d );
        pc.addComponenteObj3D( grafoObj3d );
    }
        
    @Override
    public void inicializaSOM( String aplicNome ) {  
        Plot3DGUI pgui = plot3D.novoPlotGUI( new ThreadPCGUIDesenhoControlador( operManagerThread ) );
        aplicContainer.getSOMGUI().addDesenhoComponent( pgui, aplicNome );            

        SwingUtilities.invokeLater( () -> { 
            aplicContainer.getSOMGUI().setVisible( true ); 

            int w = plot3D.getDesenhoComponent().getWidth();
            int h = plot3D.getDesenhoComponent().getHeight();

            plot3D.constroi( this, w, h );  

            operManagerThread.startThread();        
        } );
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
        double minZ = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;
        double maxZ = Double.MIN_VALUE;                
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
                if ( peso[2] < minZ )
                    minZ = peso[2];
                if ( peso[2] > maxZ )
                    maxZ = peso[2];
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
            if ( amostra[2] < minZ )
                minZ = amostra[2];
            if ( amostra[2] > maxZ )
                maxZ = amostra[2];
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
        
        pontosObj3d.setGrafoNos( amostrasPontos );
        grafoObj3d.setGrafoNos( nos ); 
        grafoObj3d.setGrafoArestas( grafoArestas );                       
    }

    @Override
    public void primeiraVezGrafoGerado() {
        plot3D.getPlanoCartesiano().constroi( plot3D ); 
    }
    
    @Override
    public void repintaGraficoSOM() {
        if ( operManagerThread.getOper() == PCOperThread.NENHUM )
            operManagerThread.semConfigConstroi( false );                
    }

    @Override
    public void aposTreinoConcluido() { 
        SOM som = aplicContainer.getSOM();
        SOMCFG cfg = aplicContainer.getSOMCFG();
        if ( som.getIteracao() < cfg.getQuantIteracoes() )
            return;
                    
        PlanoCartesianoPlot3D muPlot3D = new PlanoCartesianoPlot3D();

        JFrame matUFrame = new JFrame();
        matUFrame.setTitle( "Matriz U" ); 
        matUFrame.setContentPane( muPlot3D.getDesenhoComponent() );
        matUFrame.setSize( 400, 400 );
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
            superficieObj3d.setPintarArestas( false );
            superficieObj3d.setDesenharFaces( false ); 

            pc.setTitulo( "Matriz U" );
            pc.setAltura2D( 1.95d ); 
            pc.addComponenteObj3D( superficieObj3d ); 
        }; 
            
        SwingUtilities.invokeLater( () -> { 
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

}
