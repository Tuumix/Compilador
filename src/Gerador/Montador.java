/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Gerador;

import Tools.Tabela;
import java.util.ArrayList;

/**
 *
 * @author hiroshi
 */
public class Montador {

    private String[] reg = {"R0", "R1", "R2", "R3", "R4", "R5", "R6", "R7", "R7", "R8", "R9"};
    private ArrayList<Montador> mont = new ArrayList<>();
    private String rg;
    private String val;
    private String lexema;

    public Montador() {

    }

    public Montador(String rg, String val, String lexema) {
        this.rg = rg;
        this.val = val;
        this.lexema = lexema;
    }

    public String[] getReg() {
        return reg;
    }

    public void setReg(String[] reg) {
        this.reg = reg;
    }

    public ArrayList<Montador> getMont() {
        return mont;
    }

    public void setMont(ArrayList<Montador> mont) {
        this.mont = mont;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getLexema() {
        return lexema;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    public String gera_machineCode(ArrayList<Tabela> lin) {
        String machine_code = "";
        int pos_reg = 0;
        Tabela ant, atual, prox;
        Montador monte = new Montador();
        String loop = "";
        String opr = "", var1 = "", var2 = "";

        try {
            for (int i = 1; i < lin.size() - 1; i++) {
                ant = lin.get(i - 1);
                atual = lin.get(i);
                prox = lin.get(i + 1);

                if (atual.getLexema().equals("=") && prox.getLexema().contains(";")) {
                    machine_code += "load " + reg[pos_reg] + ", " + lin.get(i + 2).getLexema() + "\n";
                }

                if (atual.getLexema().equals("+")) {
                    machine_code += "load " + "RF, " + lin.get(i + 1).getLexema() + " " + "\n";
                    machine_code += "load " + "RE, " + lin.get(i - 1).getLexema() + " " + "\n";

                    loop = "Loop : addi  $1, 1\n"
                            + "	  addi RF, -1\n"
                            + "	  jmpEQ 'RF' = 0, LF\n"
                            + "	  jmp Loop\n"
                            + "LF: 	  move [0], R0\n";
                    loop = loop.replace("$1", "RE");
                    machine_code += loop;
                }

                if (atual.getLexema().equals("*")) {
                    machine_code += "load " + "RF, " + lin.get(i + 1).getLexema() + " " + "\n";
                    machine_code += "load " + "RE, " + lin.get(i - 1).getLexema() + " " + "\n";

                    loop = "Loop : addi  $1, $2\n"
                            + "	  addi RF, -1\n"
                            + "	  jmpEQ 'RF' = 0, LF\n"
                            + "	  jmp Loop\n"
                            + "LF: 	  move [0], R0\n";
                    loop = loop.replace("$1", "RE").replace("$2", "RE");
                    loop = loop.replace("$4", opr);
                    loop = loop.replace("$3", var2);
                    machine_code += loop;
                }

                if (atual.getLexema().equals("while")) {
                    int j = i + 1;
                    var1 = lin.get(j + 1).getLexema();
                    opr = lin.get(j + 2).getLexema();
                    var2 = lin.get(j + 3).getLexema();
                    System.out.println("" + var1 + " " + opr + " " + var2);
                    while (!lin.get(j).equals("}")) {
                        System.out.println("entrando");
                        atual = lin.get(j);

                        if (atual.getLexema().equals("+")) {
                            machine_code += "load " + "RF, " + lin.get(j + 1).getLexema() + " " + "\n";
                            machine_code += "load " + "RE, " + lin.get(j - 1).getLexema() + " " + "\n";

                            loop = "Loop : addi  $1, 1\n"
                                    + "	  addi RF, -1\n"
                                    + "	  jmpEQ RF $4 $3, LF\n"
                                    + "	  jmp Loop\n"
                                    + "LF: 	  move [0], R0\n";
                            loop = loop.replace("$1", "RE");
                            loop = loop.replace("$4", opr);
                            loop = loop.replace("$3", var2);
                            machine_code += loop;
                        }

                        if (atual.getLexema().equals("*")) {
                            machine_code += "load " + "RF, " + lin.get(j + 1).getLexema() + " " + "\n";
                            machine_code += "load " + "RE, " + lin.get(j - 1).getLexema() + " " + "\n";

                            loop = "Loop : addi  $1, $2\n"
                                    + "	  addi RF, -1\n"
                                    + "	  jmpEQ 'RF' = 0, LF\n"
                                    + "	  jmp Loop\n"
                                    + "LF: 	  move [0], R0\n";
                            loop = loop.replace("$1", "RE");
                            loop = loop.replace("$4", opr);
                            loop = loop.replace("$3", var2);
                            machine_code += loop;
                        }
                        j++;
                    }
                }
                if (atual.getLexema().equals("if")) {
                    int j = i + 1;
                    var1 = lin.get(j + 1).getLexema();
                    opr = lin.get(j + 2).getLexema();
                    var2 = lin.get(j + 3).getLexema();
                    System.out.println("" + var1 + " " + opr + " " + var2);
                    while (!lin.get(j).equals("}")) {
                        System.out.println("entrando");
                        atual = lin.get(j);

                        if (atual.getLexema().equals("+")) {
                            machine_code += "load " + "RF, " + lin.get(j + 1).getLexema() + " " + "\n";
                            machine_code += "load " + "RE, " + lin.get(j - 1).getLexema() + " " + "\n";

                            loop = "Loop : addi  $1, 1\n"
                                    + "	  addi RF, -1\n"
                                    + "	  jmpEQ RF $4 $3, LF\n"
                                    + "	  jmp Loop\n"
                                    + "LF: 	  move [0], R0\n";
                            loop = loop.replace("$1", "RE");
                            loop = loop.replace("$4", opr);
                            loop = loop.replace("$3", var2);
                            machine_code += loop;
                        }

                        if (atual.getLexema().equals("*")) {
                            machine_code += "load " + "RF, " + lin.get(j + 1).getLexema() + " " + "\n";
                            machine_code += "load " + "RE, " + lin.get(j - 1).getLexema() + " " + "\n";

                            loop = "Loop : addi  $1, $2\n"
                                    + "	  addi RF, -1\n"
                                    + "	  jmpEQ 'RF' = 0, LF\n"
                                    + "	  jmp Loop\n"
                                    + "LF: 	  move [0], R0\n";
                            loop = loop.replace("$1", "RE");
                            loop = loop.replace("$4", opr);
                            loop = loop.replace("$3", var2);
                            machine_code += loop;
                        }
                        j++;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("" + e);
        }
        return machine_code;
    }
}
