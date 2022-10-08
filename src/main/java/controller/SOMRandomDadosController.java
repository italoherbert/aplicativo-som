package controller;

import aplic.SOMAplicContainer;
import java.text.ParseException;
import javax.swing.JOptionPane;
import som.SOMRandomDados;
import gui.random.SOMRandomDadosGUI;
import gui.random.SOMRandomDadosGUIListener;

public class SOMRandomDadosController implements SOMRandomDadosGUIListener {
    
    private SOMAplicContainer aplic;
    private SOMRandomDados randomDados = new SOMRandomDados();
    
    public SOMRandomDadosController( SOMAplicContainer aplic ) {
        this.aplic = aplic;
    }

    @Override
    public void geraRandomDadosBTAcionado(SOMRandomDadosGUI gui, Object itemSelecionado) {        
        aplic.getSOMGUI().setToWaitCursor();
        aplic.getSOMGUI().setProcessandoTexto( "Carregando..." ); 
        try {
            if ( itemSelecionado == SOMRandomDadosGUI.CBB_CENTROIDES2D ) {
                String[][] centroidesStrs = gui.getCentroides2D();
                double[][] centroides = new double[ centroidesStrs.length ][];
                try {
                    for( int i = 0; i < centroidesStrs.length; i++ ) {
                        int dim = centroidesStrs[i].length;
                        
                        double[] centroide = new double[ dim ];
                        for( int j = 0; j < dim; j++ )
                            centroide[j] = aplic.getDecimalFormat().parse( centroidesStrs[i][j] ).doubleValue();                
                        centroides[i] = centroide;
                    }
                } catch ( ParseException e ) {
                    throw new Exception( "Foram encontrados dados de centroides 2D inválidos.", e );
                }
                double raioDisp;
                try {
                    raioDisp = aplic.getDecimalFormat().parse( gui.getRaioDispersao2D() ).doubleValue();
                } catch ( ParseException e ) {
                    throw new Exception( "Raio de dispersão 2D inválido.", e );
                }     
                int quantDadosMult;
                try {
                    quantDadosMult = aplic.getDecimalFormat().parse( gui.getQuantDadosMult2D() ).intValue();
                } catch ( ParseException e ) {
                    throw new Exception( "Quantidade de dados (mult) 2D inválida.", e );
                } 
                double[][] dados = randomDados.geraRandomDados2D( quantDadosMult, raioDisp, centroides );
                aplic.carregaDados( dados ); 
                aplic.getSOMGUI().setInfoTexto( "Dados gerados com êxito." );
                gui.setVisible( false );
            } else {
                String[][] centroidesStrs = gui.getCentroides3D();
                double[][] centroides = new double[ centroidesStrs.length ][];
                try {
                    for( int i = 0; i < centroidesStrs.length; i++ ) {
                        int dim = centroidesStrs[i].length;
                        
                        double[] centroide = new double[ dim ];
                        for( int j = 0; j < dim; j++ )
                            centroide[j] = aplic.getDecimalFormat().parse( centroidesStrs[i][j] ).doubleValue();                
                        centroides[i] = centroide;
                    }
                } catch ( ParseException e ) {
                    throw new Exception( "Foram encontrados dados de centroides 3D inválidos.", e );
                }
                double raioDisp;
                try {
                    raioDisp = aplic.getDecimalFormat().parse( gui.getRaioDispersao3D() ).doubleValue();
                } catch ( ParseException e ) {
                    throw new Exception( "Raio de dispersão 2D inválido.", e );
                }     
                int quantDadosMult;
                try {
                    quantDadosMult = aplic.getDecimalFormat().parse( gui.getQuantDadosMult3D() ).intValue();
                } catch ( ParseException e ) {
                    throw new Exception( "Quantidade de dados (mult) 2D inválida.", e );
                } 
                double[][] dados = randomDados.geraRandomDados3D( quantDadosMult, raioDisp, centroides );
                aplic.carregaDados( dados );
                aplic.getSOMGUI().setInfoTexto( "Dados gerados com êxito." );
                gui.setVisible( false ); 
            }
        } catch ( Exception e ) {
            aplic.getSOMGUI().setInfoTexto( "Falha na geração dos dados." );
            JOptionPane.showMessageDialog( null, e.getMessage() );
        }

        aplic.getSOMGUI().setToDefaultCursor();        
    }
    
}
