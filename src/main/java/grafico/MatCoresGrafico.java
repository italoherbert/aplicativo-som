package grafico;

import java.awt.Color;

public class MatCoresGrafico extends AbstractGrafico {
        
    protected boolean mostrarGrade = true;
    protected Color gradeCor = Color.BLACK;    

    public boolean isMostrarGrade() {
        return mostrarGrade;
    }

    public void setMostrarGrade(boolean mostrarGrade) {
        this.mostrarGrade = mostrarGrade;
    }

    public Color getGradeCor() {
        return gradeCor;
    }

    public void setGradeCor(Color gradeCor) {
        this.gradeCor = gradeCor;
    }
    
}
