/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;

/**
 *
 * @author hiroshi
 */
public class Variavel {
    private int qtde;
    private String variavel;
    
    public Variavel(){
        
    }

    public Variavel(int qtde, String variavel) {
        this.qtde = qtde;
        this.variavel = variavel;
    }

    public int getQtde() {
        return qtde;
    }

    public void setQtde(int qtde) {
        this.qtde = qtde;
    }

    public String getVariavel() {
        return variavel;
    }

    public void setVariavel(String variavel) {
        this.variavel = variavel;
    }
}
