package exemplos;

import som.SOMEvent;
import aplic.SOMAplic;
import aplic.SOMAplicContainer;
import grafico.RGBCMat;
import grafico.RetangularMatCoresGrafico;
import gui.SOMGUI;
import gui.desenho.Grafico;

public class OrdenaCoresEX extends SOMAplicContainer implements SOMAplic {

    private final double[][] amostras = {
        {255,0,0},{0,128,0},{0,0,255},{0,100,0},{0,0,139},{255,255,0},{255,165,0},{128,0,128}
    };
    
    private int[][][] cores = null;
    
    private Grafico grafico = new RetangularMatCoresGrafico();
    
    public OrdenaCoresEX() {
        super( "Ordena cores (SOM)" );

        gui.setCarregarBTVisivel( false ); 
    }
            
    @Override
    public void inicializaSOM() {                        
        for( int i = 0; i < amostras.length; i++ ) 
            for( int j = 0; j < 3; j++ )
                amostras[i][j] /= 255.0d;                                                               
        
    }   

    @Override
    public void primeiraVezGrafoGerado() {
        
    }

    @Override
    public void geraDadosBTAcionado( SOMGUI gui ) {
        super.carregaDados( amostras );
    }
         
    @Override
    public void repintaGraficoSOM() {
        if ( cfg.getNeuronios().length == 0 )
            return;
        
        int gradeQNH = cfg.getGradeQuantNeuroniosHorizontal();
        int gradeQNV = cfg.getGradeQuantNeuroniosVertical();                
        
        if ( cores == null )        
            cores = new int[ gradeQNV ][ gradeQNH ][3];
        
        for( int i = 0; i < gradeQNV; i++ ) {
            for( int j = 0; j < gradeQNH; j++ ) {                    
                double[] peso = cfg.getNeuronios()[i][j].getPeso();
                cores[i][j][0] = (int)Math.round( peso[0] * 255 );
                cores[i][j][1] = (int)Math.round( peso[1] * 255 );
                cores[i][j][2] = (int)Math.round( peso[2] * 255 );                
            }
        }

        gui.getDesenhoPNL().repintaGrafico( new RGBCMat( cores ) ); 
    }
    
    @Override
    public void execIT(SOMEvent e) {
        gui.setIteracaoInfo( decimalFormat, e.getIteracao(), e.getTaxaAprendizado(), e.getRaioVizinhanca() ); 
        if ( e.getIteracao() % 10 == 0 ) {
            this.repintaGrafico();
            try {
                Thread.sleep( gui.getDelay() );
            } catch ( InterruptedException ex ) {

            }
        }
    }

    @Override
    public void configuraAplicCorrente(double[][] amostras) {
        
    }

    @Override
    public void geraNosEGrafoRede() {
        
    }
    
    @Override
    public void aposTreinoConcluido() {
        
    }

    @Override
    public SOMAplic getAplicCorrente() {
        return this;
    }

    @Override
    public Grafico getGrafico() {
        return grafico;
    }
        
    public static void main( String[] args ) {                                
        OrdenaCoresEX exemplo = new OrdenaCoresEX();
        exemplo.inicializa();
        exemplo.getSOMGUI().setVisible( true ); 
        /*
        int[][][] cores = new int[ gradeQNV ][ gradeQNH ][3];
        for( int i = 0; i < gradeQNV; i++ ) {
            for( int j = 0; j < gradeQNH; j++ ) {
                double[] peso = neuronios[i][j].getPeso();

                double minD = Double.MAX_VALUE;
                double[] cor = amostras[0];
                for ( double[] amostra : amostras ) {
                    double d = math.distancia( peso, amostra, 3 );
                    if ( d < minD ) {
                        cor = amostra;
                        minD = d;
                    }
                }

                cores[i][j] = new int[]{
                    (int)(cor[0] * 255),
                    (int)(cor[1] * 255),
                    (int)(cor[2] * 255)
                } ;
            } 
        }

        // GUI
        
        gui.repintaGrafico( new RGBCMat( cores ) );
        */
    }
    
}
