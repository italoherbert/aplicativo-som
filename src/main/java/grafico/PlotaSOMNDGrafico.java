package grafico;

import java.awt.Graphics;
import som.SOMCFG;
import gui.desenho.Grafico;
import gui.desenho.PainelGrafico;

public class PlotaSOMNDGrafico extends AbstractGrafico implements Grafico<SOMCFG> {
 
    private String mensagem = "Treinando...";
            
    @Override
    public void desenha(Graphics g, SOMCFG gc, PainelGrafico painel) {
        
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
    
}
