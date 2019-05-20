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
public class Intermediario { // a = a + b + ( b * c);
    //private int cnt = 0;

    public String gera_intermediario(String lin, int p) {
        String[] aux;
        aux = lin.replace("    ", "").split(" ");
        String var = "", prev = "", prox = "", add = "";
        Boolean flag = false, achou = false;
        int temp = 1, cnt = 0;
        char c1, c2;
        String intermed = "";
        String goTo = "L";
        int pos = 1;
        String high_low = "";

        try {
            if (aux[1].equals("=") && !aux[2].contains(";")) {
                String div[] = lin.split("=");
                add = div[0];
                lin = div[1];
                flag = true;
            }

            for (int i = 0; i < aux.length; i++) {
                /*if (lin.contains("int") || lin.contains("double")) {
                    intermed += lin;
                    break;
                }*/
                if(aux[i].matches("^[a-zA-Z]+$") && i == 0 && aux[i+2].contains(";")){
                    intermed += aux[i] + " = " + aux[i+2]+ "\n";
                }
                if (aux[i].equals("while") || aux[i].equals("if")) {
                    System.out.println("hehe");
                    intermed += goTo + pos + " : " + " if " + aux[i + 2] + " " + aux[i + 3] + " " + aux[i + 4] + " " + " goto " + goTo + (pos + 1) + " \n";
                }
                if (aux[i].equals("}")) {
                    intermed = "goto " + goTo + pos + " \n";
                    pos++;
                }
                if (aux[i].equals("+=") || aux[i].equals("-=")) {
                    high_low = aux[i].replace("=", "");
                    intermed += "T" + p + " = " + aux[i - 1] + " " + high_low + " " + aux[i + 1] + " \n";
                    intermed += aux[i - 1] + " = " + "T" + p + " \n";
                    p++;
                }
                if (aux[i].matches("^[a-zA-Z0-9]+$") && aux[i + 2].matches("^[a-zA-Z0-9]+$") && aux[i + 2].indexOf(";") != -1) {
                    intermed += aux[i] + " = " + aux[i + 2] + "\n";
                    System.out.println("MEU DEUS");
                    break;
                }
                if (aux[i].equals("+") || aux[i].equals("-") || aux[i].equals("*")) {
                    if (temp == 1) {
                        prev = "";
                        temp += 1;
                        for (int j = i - 1; j >= 0; j--) {
                            if (aux[j].equals("+") || aux[j].equals("-") || aux[j].equals("*") || aux[j].equals("=")) {
                                break;
                            }
                            prev += aux[j];
                        }
                    } else {
                        prev = "T" + (p - 1);
                    }

                    prox = "";
                    for (int j = i + 1; j < aux.length; j++) {
                        if (aux[j].equals("+") || aux[j].equals("-") || aux[j].equals("*") || aux[j].equals(";")) {
                            break;
                        }
                        prox += aux[j];
                    }
                    intermed += "T" + p + " = " + prev + " " + aux[i] + " " + prox + "\n";
                    //System.out.println(""+"T" + cnt + " = " + prev + " " + aux[i] + " " + prox + "\n");
                    p++;
                }
            }
            if (flag) {
                intermed += add + " = " + "T" + (p - 1) + "\n";;
            }
            intermed = intermed.replace("	", "");
            intermed = intermed.replace("    ", "");
        } catch (Exception e) {
            System.out.println("" + e);
        }
        return intermed;
    }
}
