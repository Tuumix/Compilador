/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analise;

import Tools.Tabela;
import Tools.Variavel;
import java.util.ArrayList;

/**
 *
 * @author Aluno
 */
public class Semantica {

    private String erro = "";
    ArrayList<Variavel> var = new ArrayList<>();

    public Semantica() {
    }

    public String analise(ArrayList<Tabela> tab) {
        Tabela tabela; Tabela ant, atual;
        Boolean achou = false;
        erro = "";
        ArrayList<Tabela> aux = new ArrayList<>();
        var.clear();
        aux = tab;
        int pos;

        for (int i = 0; i < aux.size(); i++) {
            tabela = aux.get(i);
            if (tabela.getToken().equals("token_id")) {
                add_listaVariavel(tabela.getLexema());
                if (!verif_init(tab, tabela.getLexema())) {
                    erro += "Variável '" + tabela.getLexema() + "' não instanciada" + "\n";
                }
                if (tabela.getType().equals("int") && tabela.getValor().contains(".")) {
                    erro += "Erro ao atribuir um double em uma variável do tipo inteiro na linha : " + tabela.getLinha() + "\n";
                }
                if (tabela.getType().equals("double") && !tabela.getValor().contains(".")) {
                    erro += "Erro ao atribuir um inteiro em um double na linha : " + tabela.getLinha() + "\n";
                }
            }
        }

        for (int j = 0; j < var.size(); j++) {
            achou = false;
            for (int i = 1; i < tab.size(); i++) {
                ant = tab.get(i - 1);
                atual = tab.get(i);
                if ((ant.getLexema().equals("int") && atual.getLexema().equals(var.get(j).getVariavel())) || (ant.getLexema().equals("double") && atual.getLexema().equals(var.get(j).getVariavel()))) {
                    achou = true;
                }
            }
            if (var.get(j).getQtde() == 1 && achou) {
                erro += "Variável '" + var.get(j).getVariavel() + "' instânciada porém não utilizada" + "\n";
            }
        }
        return erro;
    }

    public Boolean verif_init(ArrayList<Tabela> tab, String variavel) {
        Boolean inicializou = false;
        Tabela ant, atual;
        Boolean achou = false;
        int cont = 0;

        for (int i = 1; i < tab.size(); i++) {
            ant = tab.get(i - 1);
            atual = tab.get(i);
            if ((ant.getLexema().equals("int") && atual.getLexema().equals(variavel)) || (ant.getLexema().equals("double") && atual.getLexema().equals(variavel))) {
                cont++;
                achou = true;
            }
        }

        if (cont >= 2) {
            //erro += "Variável '" + variavel + "' declarada mais de uma vez" + "\n";
        }

        if (achou) {
            return true;
        } else {
            return false;
        }
    }

    public int busca_variavel(String variavel) {
        int i = 0;
        while (i < var.size() && !variavel.equals(var.get(i).getVariavel())) {
            i++;
        }
        if (i < var.size()) {
            return i;
        } else {
            return -1;
        }
    }

    public void add_listaVariavel(String lexema) {
        int pos;
        if (var.isEmpty()) {
            var.add(new Variavel(1, lexema));
        } else {
            pos = busca_variavel(lexema);
            if (pos != -1) {
                var.get(pos).setQtde(var.get(pos).getQtde() + 1);
            } else {
                var.add(new Variavel(1, lexema));
            }
        }
    }
}
