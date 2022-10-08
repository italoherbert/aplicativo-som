package grafico;

import gui.desenho.PainelGrafico;

public abstract class AbstractGrafico {
    
    protected int borda = 20;
        
    protected int getX( PainelGrafico painel ) {
        return borda;
    }
    
    protected int getY( PainelGrafico painel ) {
        return borda;
    }
    
    protected int getWidth( PainelGrafico painel ) {
        return painel.getWidth() - 2*borda;
    }
    
    protected int getHeight( PainelGrafico painel ) {
        return painel.getHeight() - 2*borda;
    }

    public int getBorda() {
        return borda;
    }

    public void setBorda(int borda) {
        this.borda = borda;
    }        
    
}
