package exemplos;

import javax.swing.JFrame;
import italo.iplot.gui.DesenhoUI;
import som.SOMEvent;
import som.SOMMatrizU;
import aplic.SOMAplic;
import aplic.SOMAplicContainer;
import grafico.PlotaMatrizUReduzida2DGrafico;
import grafico.PlotaSOM2DGrafico;
import gui.desenho.DesenhoPNL;
import gui.desenho.Grafico;

public class SOM2D extends SOMAplicContainer implements SOMAplic {
              
    private Grafico grafico = new PlotaSOM2DGrafico();        

    public SOM2D() {
        super( "SOM 2D");
    }
    
    @Override
    public void inicializaSOM() {                       
        
    }

    @Override
    public void configuraAplicCorrente(double[][] amostras) {
        
    }
        
    @Override
    public void geraNosEGrafoRede() {
        
    }   

    @Override
    public void primeiraVezGrafoGerado() {
        
    }
    
    @Override
    public void repintaGraficoSOM() {
        gui.getDesenhoPNL().repintaGrafico( cfg ); 
    }
    
    @Override
    public void execIT(SOMEvent e) {
        super.setIteracaoInfo( e );
        
        if ( e.getIteracao() % 5 == 0 ) {        
            gui.getDesenhoPNL().repintaGrafico( cfg );                 
            try { 
                Thread.sleep( gui.getDelay() );
            } catch (InterruptedException ex) {

            }                
        }
    }
    
    @Override
    public void aposTreinoConcluido() {                
        if ( som.getIteracao() < cfg.getQuantIteracoes() )
            return;
        
        DesenhoPNL matUDesenhoPNL = new DesenhoPNL();

        JFrame matUFrame = new JFrame();
        matUFrame.setTitle( "Treinamento de mapa 2D" ); 
        matUFrame.setContentPane( matUDesenhoPNL );
        matUFrame.setSize( 500, 500 );
        matUFrame.setDefaultCloseOperation( JFrame.HIDE_ON_CLOSE );
        matUFrame.setLocationRelativeTo( matUFrame );                

        PlotaMatrizUReduzida2DGrafico grafico = new PlotaMatrizUReduzida2DGrafico();
        grafico.setMostrarGrade( false );

        SOMMatrizU mu = som.geraMatrizU( cfg );
        matUDesenhoPNL.setGrafico( grafico );

        matUFrame.setVisible( true );                 

        matUDesenhoPNL.repintaGrafico( mu );
    }

    @Override
    public SOMAplic getAplicCorrente() {
        return this;
    }
    
    @Override
    public Grafico getGrafico() {
        return grafico;
    }
    
    public static void main(String[] args) {         
        SOM2D exemplo = new SOM2D();
        exemplo.inicializa();                  
        exemplo.getSOMGUI().setVisible( true );
    }
        
}
