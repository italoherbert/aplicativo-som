package aplic;

import som.SOMEvent;
import gui.desenho.Grafico;

public class SOMAplicManager implements SOMAplic {
    
    private final SOMAplicContainer aplicContainer;

    public SOMAplicManager(SOMAplicContainer aplicContainer) {
        this.aplicContainer = aplicContainer;
    }

    @Override
    public void execIT(SOMEvent e) {
        if ( aplicContainer.getAplicCorrente() != null )
            aplicContainer.getAplicCorrente().execIT( e );
    }
        
    @Override
    public void geraNosEGrafoRede() {
        if ( aplicContainer.getAplicCorrente() != null )
            aplicContainer.getAplicCorrente().geraNosEGrafoRede();
    }

    @Override
    public void aposTreinoConcluido() {        
        if ( aplicContainer.getAplicCorrente() != null )
            aplicContainer.getAplicCorrente().aposTreinoConcluido();
    }

    @Override
    public void repintaGraficoSOM() {
        if ( aplicContainer.getAplicCorrente() != null ) {
            aplicContainer.getAplicCorrente().repintaGraficoSOM();
        }
    }

    @Override
    public Grafico getGrafico() {
        if ( aplicContainer.getAplicCorrente() != null )
            return aplicContainer.getAplicCorrente().getGrafico();
        return null;
    }
       
}
