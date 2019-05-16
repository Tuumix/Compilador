/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;

import java.util.ArrayList;

/**
 *
 * @author Aluno
 */
public class Semantica {

    public Semantica() {
    }
    
    public String analise(ArrayList<Tabela> tab) {
        Tabela tabela = new Tabela();
        String erro = "";
        ArrayList<Tabela> aux = new ArrayList<>();
        aux = tab;
        
        for (int i = 0; i < aux.size(); i++) {
            tabela = aux.get(i);
            if (tabela.getToken().equals("token_id")) {
                if(!verif_init(tab, tabela.getLexema()))
                    erro += "Variável '" + tabela.getLexema() + "' não instanciada"+ "\n";
                if (tabela.getType().equals("int") && tabela.getValor().contains(".")) {
                    erro += "Erro ao atribuir um double em uma variável do tipo inteiro na linha : "+tabela.getLinha() + "\n";
                }
                if(tabela.getType().equals("double") && !tabela.getValor().contains(".")){
                    erro += "Erro ao atribuir um inteiro em um double na linha : "+tabela.getLinha() + "\n";
                }
            }
        }
        return erro;
    }
    
    public Boolean verif_init(ArrayList<Tabela> tab, String variavel){
        Boolean inicializou = false;
        Tabela ant = new Tabela(), atual = new Tabela();
        
        for(int i = 1; i < tab.size();i++){
            ant = tab.get(i-1);
            atual = tab.get(i);
            if((ant.getLexema().equals("int") && atual.getLexema().equals(variavel)) || (ant.getLexema().equals("double") && atual.getLexema().equals(variavel)))
                return true;
        }
        return false;
    }
}
