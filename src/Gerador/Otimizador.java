/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Gerador;

import java.util.ArrayList;

/**
 *
 * @author hiroshi
 */
public class Otimizador {

    private String copia = "";

    public String gera_otimizacao(String lin) {
        copia = lin;
        String receiver = "", attribute = "";
        ArrayList<String> otimizacao = new ArrayList<>();
        String[] line = lin.split("\n"); //linha
        String[] divisoria, variaveis, c2, c1, c3;
        String tudo = "", prox = "", atribuicao = "";
        String[] atual_lin;
        Boolean achou_var = false;
        int ocorrencia = 0;
        String var = "", busca = "";
        String aux = "", aux1 = "";
        String[] var_atri;

        for (int i = 0; i < line.length; i++) {
            c1 = line[i].split("=");
            //variaveis = null;
            //atual_lin = line[i].split("="); //split na determinada pos
            //var = atual_lin[0]; //variavel
            //atribuicao = atual_lin[1]; //atribuidor
            //atual_lin[1] = atual_lin[1].replace("+", "").replace("-", "").replace("*", "").replace(";", "");
            //variaveis = atual_lin[1].replace(" ", " ").split(" ");//variaveis
            for (int j = i + 1; j < line.length - 1; j++) {
                c2 = line[j].split("=");
                aux = line[j];
                c1[1] = c1[1].replace(";", "");
                c2[1] = c2[1].replace(";", "");

                if (c2[1].trim().equals(c1[1].trim())) {
                    copia = copia.replace(aux, "");
                    for (int k = j + 1; k < line.length; k++) {
                        c3 = line[k].split("=");
                        var = c3[1];
                        if (c2[0].trim().equals(c3[1].trim())) {
                            System.out.println("1");
                            aux = var.replace(c3[1], c1[0]);
                            copia = copia.replace(var, aux);
                        }
                        if (c3[1].trim().contains(c2[0].trim())) {
                            aux = var.replace(c2[0], c1[0]);
                            copia = copia.replace(var, aux);
                        }
                    }
                }

                aux = c1[1];
                var_atri = aux.split(" ");
                for (int p = 0; p < var_atri.length; p++) {
                    if (c2[0].trim().equals(var_atri[p])) {
                        achou_var = true;
                    }
                }

                /*if (!achou_var) {
                    busca = aux;
                    busca(lin, aux);
                }*/
                /*divisoria = line[j].split("="); //divide
                auxiliar = divisoria[1];
                teste = auxiliar.split(" ");
                //variaveis = atual_lin[1].split(" ");
                for(int p = 0; p < teste.length;p++){
                    System.out.println(""+teste[p]);
                    if(var.equals(teste[p]))
                        System.out.println("Achou Aqui");
                }
                if (variaveis[0] == divisoria[0] || atual_lin[1].charAt(0) == divisoria[0].charAt(1)) {
                    achou_var = true;
                }*/
 /*if (atribuicao.equals(auxiliar)) {
                    otimizacao.add("T" + ocorrencia + " = " + auxiliar);
                    for (int k = 0; k < variaveis.length; k++) {
                        if (!variaveis[k].equals("")) {
                            otimizacao.add(variaveis[k] + " = " + "T" + ocorrencia + "\n");
                        }
                    }
                }*/
            }
            //propagacao(lin, line[i]);
        }
        for (int i = 0; i < otimizacao.size(); i++) {
            tudo = tudo.concat(otimizacao.get(i));
        }
        //System.out.println(""+copia);
        return copia;
    }

    public void propagacao(String lin, String instrucao) {
        String[] c1 = lin.split("\n"), c2;
        String[] var = instrucao.split("=");
        Boolean achou = false;
        String aux = "", aux1 = "";

        for (int i = 0; i < c1.length; i++) {
            c2 = c1[i].split("=");
            aux = c1[i];
            if (c2[0].trim().equals(var[0].trim())) {
                System.out.println("" + c2[0] + var[0]);
                achou = true;
            }
            if (c2[1].trim().equals(var[0].trim()) && !achou) {
                System.out.println("");
                aux1 = aux.replace(var[0], var[1]);
                copia = copia.replace(aux, aux1);
            }
        }
    }

    public void busca(String lin, String expressao) {
        String[] c1 = lin.split("\n"), c2;
        ArrayList<String> aux = new ArrayList<>();
        String aux1;
        int cont = 0, pos = 0;
        for (int i = 0; i < c1.length; i++) {
            aux1 = c1[i];
            c2 = c1[i].split("=");
            if (c2[1].trim().contains(expressao)) {
                cont++;
                aux.add(c1[i]);
            }
        }

        if (cont > 2) {
            for (int j = 0; j < aux.size(); j++) {
                copia = copia.replace("T1", "");
                pos = j;
            }
            System.out.println("" + copia);
            copia += "T1 " + "=" + expressao + "\n";
            //copia = copia.replace(aux.get(pos), "");
        }
    }
}
