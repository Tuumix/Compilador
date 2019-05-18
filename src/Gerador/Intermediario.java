/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Gerador;

/**
 *
 * @author hiroshi
 */
public class Intermediario { // a = a + b + ( b * c);

    public String gera_intermediario(String lin) {
        String[] aux;
        aux = lin.split(" ");
        String var = "", prev = "", prox = "", add = "";
        Boolean flag = false;
        int temp = 1, cnt = 0;
        char c1, c2;
        String intermed = "";
        
        try {
            if (aux[1].equals("=")) {
                String div[] = lin.split("=");
                add = div[0];
                lin = div[1];
                flag = true;
            }

            for (int i = 0; i < aux.length; i++) {
                //System.out.println("entrou");
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
                        prev = "T" + (cnt - 1);
                    }

                    prox = "";
                    for (int j = i + 1; j < aux.length; j++) {
                        if (aux[j].equals("+") || aux[j].equals("-") || aux[j].equals("*") || aux[j].equals(";")) {
                            break;
                        }
                        prox += aux[j];
                    }
                    System.out.println("T" + cnt + " = " + prev + " " + aux[i] + " " + prox);
                    intermed += "T" + cnt + " = " + prev + " " + aux[i] + " " + prox + "\n";;
                    cnt++;
                }
            }
            if (flag) {
                System.out.println(add + " = " + "T" + (cnt - 1));
                intermed += add + " = " + "T" + (cnt - 1);
            }
        } catch (Exception e) {
            System.out.println("" + e);
        }
        return intermed;
    }
}
