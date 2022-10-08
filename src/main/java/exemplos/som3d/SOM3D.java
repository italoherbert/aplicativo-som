package exemplos.som3d;

import italo.iplot.gui.DesenhoGUIListener;
import italo.iplot.gui.DesenhoUI;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import italo.iplot.plot3d.Plot3D;
import italo.iplot.plot3d.Plot3DDriver;
import italo.iplot.plot3d.Plot3DSimples;
import italo.iplot.plot3d.g3d.CuboObjeto3D;
import italo.iplot.plot3d.g3d.GrafoObjeto3D;
import italo.iplot.plot3d.g3d.Objeto3D;
import italo.iplot.plot3d.g3d.SuperficieObjeto3D;
import italo.iplot.plot3d.g3d.UniversoVirtual3D;
import italo.iplot.thread.plot3d.SimplesPlot3DOperManagerThread;
import javax.swing.SwingUtilities;
import som.Neuronio;
import som.SOMEvent;
import som.SOMMatrizU;
import som.SOMMatrizUSuperficie;
import aplic.SOMAplic;
import aplic.SOMAplicContainer;
import gui.desenho.Grafico;

public class SOM3D extends SOMAplicContainer implements SOMAplic, Plot3DDriver, DesenhoGUIListener {
                    
    private final double alt = 1.2d;   
    private final String DESENHO_COMP_NOME = "SOM3D";
            
    private final Plot3DSimples plot3D = new Plot3DSimples();
    private final SimplesPlot3DOperManagerThread operManagerThread = new SimplesPlot3DOperManagerThread( plot3D ); 
    
    private final GrafoObjeto3D grafoObj3d;
    private final GrafoObjeto3D pontosObj3d;
    private final CuboObjeto3D cuboObj3d;    
        
    public SOM3D() { 
        super( "SOM 3D" );
                
        cuboObj3d = new CuboObjeto3D();
        cuboObj3d.setPintarFaces( false );
        cuboObj3d.setPintarVertices( false );
        cuboObj3d.setPintarArestas( true );
        cuboObj3d.setArestasCor( Color.BLACK );
        
        grafoObj3d = new GrafoObjeto3D();
        grafoObj3d.setVerticeRaio( 0.01d );
        grafoObj3d.setVerticesCor( Color.RED );
        grafoObj3d.setArestasCor( Color.BLACK ); 
                
        pontosObj3d = new GrafoObjeto3D();
        pontosObj3d.setVerticeRaio( 0.01d ); 
        pontosObj3d.setPintarVertices( true );
        pontosObj3d.setPintarArestas( false );
        pontosObj3d.setVerticesCor( Color.BLUE );                          
        
        cuboObj3d.setLado( alt );                  
        
        cuboObj3d.addComponenteObj3D( pontosObj3d );
        cuboObj3d.addComponenteObj3D( grafoObj3d ); 
    }

    @Override
    public void configura(Plot3D plot3D, UniversoVirtual3D uv) {       
        uv.setCorFundo( Color.WHITE ); 
        uv.addObjeto( cuboObj3d );
        uv.inicialRot();
    }
        
    @Override
    public void inicializaSOM() {                                                          
        gui.addDesenhoComponent( plot3D.getDesenhoComponent(), DESENHO_COMP_NOME );                         
        SwingUtilities.invokeLater( () -> {
            gui.setVisible( true ); 
            
            int w = plot3D.getDesenhoComponent().getWidth();
            int h = plot3D.getDesenhoComponent().getHeight();

            plot3D.setRotManager( plot3D.novoIncRotManager() ); 
            plot3D.setGrafico( plot3D.novoJava2DGrafico() ); 
            plot3D.setAplicarPerspectiva( false );

            plot3D.constroi( this, w, h ); 
            plot3D.addRotacaoDesenhoGUIListener( this );

            operManagerThread.startThread();
        } );                
    }

    @Override
    public void geraNosEGrafoRede() {        
        Neuronio[][] neuronios = cfg.getNeuronios();
        double[][] amostras = cfg.getAmostras();
        
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
                Neuronio n = cfg.getNeuronios()[i][j];
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
        plot3D.getUniversoVirtual().constroi( plot3D );
    }
    
    @Override
    public void repintaGraficoSOM() {
        operManagerThread.constroiERepinta( false );         
    }

    @Override
    public void aposTreinoConcluido() {                
        if ( som.getIteracao() < cfg.getQuantIteracoes() )
            return;
                    
        Plot3DSimples muPlot3D = new Plot3DSimples();

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
        
        Plot3DDriver drv = (Plot3D plot3d, UniversoVirtual3D uv) -> {
            SuperficieObjeto3D superficieObj3d = new SuperficieObjeto3D( vx, vz, my );
            superficieObj3d.setPreenchimento( Objeto3D.Preenchimento.GRADIENTE ); 
            
            uv.setCorFundo( Color.WHITE ); 
            uv.inicialRot();
            uv.addComponenteObj3D( superficieObj3d );             
        }; 
        
        matUFrame.setVisible( true );                 
        
        int largura = muPlot3D.getDesenhoComponent().getWidth();
        int altura = muPlot3D.getDesenhoComponent().getHeight();

        muPlot3D.setAplicarPerspectiva( false ); 
        muPlot3D.constroi( drv, largura, altura );            
        muPlot3D.addRotacaoDesenhoGUIListener();
    }    

    @Override
    public SOMAplic getAplicCorrente() {
        return this;
    }    

    @Override
    public void configuraAplicCorrente(double[][] amostras) {
        gui.mostrarDesenhoComponent( DESENHO_COMP_NOME );
    }
    
    @Override
    public Grafico getGrafico() {
        return null;
    }

    @Override
    public void execIT(SOMEvent e) {
        super.setIteracaoInfo( e ); 
        if ( e.getIteracao() % 10 != 0 )
            return;         
                
        this.geraNosEGrafoRede();
        this.repintaGrafico();
        
        try {
            Thread.sleep( super.getSOMGUI().getDelay() );
        } catch (InterruptedException ex) {
            
        }
    }

    @Override
    public void arrastou(DesenhoUI ui) {
       operManagerThread.constroiERepinta( true ); 
    }

    @Override
    public void moveu(DesenhoUI ui) {
        
    }
    
    public static void main( String[] args ) {
        SOM3D som3d = new SOM3D();
        som3d.inicializa();
        som3d.getSOMGUI().setVisible( true );                 
    }
    
}
