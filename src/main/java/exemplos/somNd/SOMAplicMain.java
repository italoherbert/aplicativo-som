package exemplos.somNd;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import aplic.SOMAplic;
import aplic.SOMAplicContainer;

public class SOMAplicMain extends SOMAplicContainer {
        
    private final static String SOM_2D = "SOM2D";
    private final static String SOM_3D = "SOM3D";
    private final static String SOM_ND = "SOMND";
    
    private final Map<Object, SOMAplic2> aplics = new HashMap();
    private SOMAplic aplicCorrente;
    
    public SOMAplicMain() { 
        super( "SOM" );      
        aplics.put( SOM_2D, new SOM2DAplic( this ) );
        aplics.put( SOM_3D, new SOM3DAplic( this ) );
        aplics.put( SOM_ND, new SOMNDAplic( this ) );
        aplicCorrente = aplics.get( SOM_ND );
    }
        
    @Override
    public void inicializaSOM() {    
        Set<Object> keys = aplics.keySet();
        for( Object key : keys )
            aplics.get( key ).inicializaSOM( String.valueOf( key ) );                
    }      

    @Override
    public void primeiraVezGrafoGerado() {
        Set<Object> keys = aplics.keySet();
        for( Object key : keys )
            aplics.get( key ).primeiraVezGrafoGerado();
    }
    
    @Override
    public void configuraAplicCorrente(double[][] dados) {
        if ( dados.length > 0 ) {
            switch (dados[0].length) {
                case 2:
                    aplicCorrente = aplics.get( SOM_2D );
                    gui.mostrarDesenhoComponent( SOM_2D );
                    break;
                case 3:
                    aplicCorrente = aplics.get( SOM_3D );
                    gui.mostrarDesenhoComponent( SOM_3D );
                    break;            
                default:
                    aplicCorrente = aplics.get( SOM_ND );
                    gui.mostrarDesenhoPNL();
                    break;
            }
        }
    }

    @Override
    public SOMAplic getAplicCorrente() {
        return aplicCorrente;
    }
        
    public static void main( String[] args ) {
        SOMAplicMain somNd = new SOMAplicMain();
        somNd.inicializa();
        somNd.getSOMGUI().setVisible( true ); 
    }
    
}
