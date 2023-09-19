package exemplos.somNd;

import javax.swing.JFrame;
import italo.iplot.plot3d.g3d.Objeto3D;
import italo.iplot.plot3d.g3d.SuperficieObjeto3D;
import som.SOM;
import som.SOMCFG;
import som.SOMEvent;
import som.SOMMatrizU;
import som.SOMMatrizUSuperficie;
import aplic.SOMAplicContainer;
import grafico.PlotaSOMNDGrafico;
import gui.desenho.Grafico;
import italo.iplot.plot3d.planocartesiano.PlanoCartesianoPlot3D;
import italo.iplot.plot3d.planocartesiano.PlanoCartesianoPlot3DDriver;
import italo.iplot.plot3d.planocartesiano.g3d.PlanoCartesianoObjeto3D;
import javax.swing.SwingUtilities;

public class SOMNDAplic implements SOMAplic2 {

    private final Grafico grafico = new PlotaSOMNDGrafico();
    
    private final SOMAplicContainer aplicContainer;
    
    public SOMNDAplic( SOMAplicContainer manager ) {
        this.aplicContainer = manager;
    }

    @Override
    public void inicializaSOM( String aplicNome ) {                    
        aplicContainer.getSOMGUI().getDesenhoPNL().repintaGrafico( aplicContainer.getSOMCFG() ); 
    }

    @Override
    public void primeiraVezGrafoGerado() {
        
    }

    @Override
    public void geraNosEGrafoRede() {
        
    }     
    
    @Override
    public void aposTreinoConcluido() {                
        SOM som = aplicContainer.getSOM();
        SOMCFG cfg = aplicContainer.getSOMCFG();
        
        if ( som.getIteracao() < cfg.getQuantGrupoIteracoes() )
            return;                                       
        
        PlanoCartesianoPlot3D muPlot3D = new PlanoCartesianoPlot3D();

        SwingUtilities.invokeLater( () -> {         
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
                pc.setAltura2D( 2.0d ); 
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
    public void repintaGraficoSOM() {
        
    }   

    @Override
    public void execIT(SOMEvent e) {
        aplicContainer.setIteracaoInfo( e );
    }
    
    @Override
    public Grafico getGrafico() {
        return grafico;
    }
    
}
