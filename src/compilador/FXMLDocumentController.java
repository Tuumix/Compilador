/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import Tools.Comandos;
import Analise.Semantica;
import Gerador.Intermediario;
import Tools.Tabela;

import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.Subscription;

/**
 *
 * @author hiroshi
 */
public class FXMLDocumentController implements Initializable {

    private Label label;
    @FXML
    private CodeArea cdArea;
    private ArrayList<Comandos> comand_list = new ArrayList<Comandos>();
    private static final String[] Comandos = new String[]{
        "while", "if", "else"
    };

    private static final String comandos_padrao = String.join("|", Comandos);
    private static final String operacao_padrao = "\\<|\\>|\\<=|\\>=|\\=|\\==";
    private static final String parenteses_padrao = "\\(|\\)";
    private static final String chave_padrao = "\\{|\\}";
    private static final String colchete_padrao = "\\[|\\]";
    private static final String DOT_PATTERN = "\\.(?!\\.)";
    private static final String string_padrao = "\\${1}.*\\${1}";
    private static final String line_pattern = "\".*[0-9].*\"";
    private static final String declaracao = "\\bint\\b|\\bdouble\\b|\\<=|\\>=|\\=";
    private static final String comentario = "\\${1}.*\\${1}";
    private static final String inicio_fim = "\\bbegin\\b|\\bend\\b";

    private static final String group_tecla = "KEYWORD";
    private static final String group_parentese = "PAREN";
    private static final String group_chave = "BRACE";
    private static final String group_colchete = "BRACKET";
    private static final String GROUP_DOT = "DOT";
    private static final String group_string = "STRING";
    private static final String GROUP_COMMENT = "COMMENT";
    private static final String group_comando = "COMMAND";
    private static final String group_dec = "declaracao";
    private static final String group_inifim = "inicio_fim";

    private static final Pattern HIGHLIGHT_PATTERN = Pattern.compile(
            "(?<" + group_tecla + ">" + comandos_padrao + ")"
            + "|(?<" + group_parentese + ">" + parenteses_padrao + ")"
            + "|(?<" + group_chave + ">" + chave_padrao + ")"
            + "|(?<" + group_colchete + ">" + colchete_padrao + ")"
            + "|(?<" + GROUP_DOT + ">" + DOT_PATTERN + ")"
            + "|(?<" + group_string + ">" + string_padrao + ")"
            + "|(?<" + group_comando + ">" + operacao_padrao + ")"
            + "|(?<" + GROUP_COMMENT + ">" + comentario + ")"
            + "|(?<" + group_dec + ">" + declaracao + ")"
    );

    @FXML
    private TableView<Tabela> tabela;
    @FXML
    private TextArea txt_sintatica;
    @FXML
    private TextArea txt_semantica;

    private ArrayList<Tabela> lista_tabela = new ArrayList<>();
    private Tabela tab = new Tabela();
    private Semantica semantica = new Semantica();
    private Intermediario intermediario = new Intermediario();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cdArea.setParagraphGraphicFactory(LineNumberFactory.get(cdArea));

        TableColumn token = new TableColumn("Token");
        token.setCellValueFactory(new PropertyValueFactory<>("token"));

        TableColumn lexema = new TableColumn("Lexema");
        lexema.setCellValueFactory(new PropertyValueFactory<>("lexema"));

        TableColumn linha = new TableColumn("Linha");
        linha.setCellValueFactory(new PropertyValueFactory<>("linha"));

        TableColumn tipo = new TableColumn("Categoria");
        tipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        TableColumn valor = new TableColumn("Valor");
        valor.setCellValueFactory(new PropertyValueFactory<>("valor"));

        TableColumn type = new TableColumn("Tipo");
        type.setCellValueFactory(new PropertyValueFactory<>("type"));

        tabela.getColumns().addAll(token, lexema, linha, tipo, valor, type);

        token.setMaxWidth(150);
        lexema.setMaxWidth(150);
        linha.setMaxWidth(150);
        tipo.setMaxWidth(150);
        type.setMaxWidth(150);

        insercao_tokens();
        InicializaTextColor();

        cdArea.replaceText("begin\n"
                + "{\n"
                + "    int i = 0; int b = 0;\n"
                + "\n"
                + "    while ( i <= 10 )\n"
                + "    {\n"
                + "        i += 1;\n"
                + "    }\n"
                + "}");
    }

    public void InicializaTextColor() {
        try {
            Subscription limpar = cdArea.plainTextChanges().
                    successionEnds(Duration.ofMillis(500)).
                    subscribe(ignore -> cdArea.setStyleSpans(0, paint()));
        } catch (Exception e) {
            System.out.println("" + e);
        }
    }

    public StyleSpans<Collection<String>> paint() {
        int lastMatchEnd = 0, cont = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        Matcher matcher = HIGHLIGHT_PATTERN.matcher(cdArea.getText());
        while (matcher.find()) {
            String styleClass
                    = matcher.group(group_tecla) != null ? "comando" //checado
                    : matcher.group(group_parentese) != null ? "parentese" //checado
                    : matcher.group(group_chave) != null ? "brace"
                    : matcher.group(group_colchete) != null ? "colchete"
                    : matcher.group(GROUP_DOT) != null ? "dot"
                    : matcher.group(group_string) != null ? "string"
                    : matcher.group(GROUP_COMMENT) != null ? "comentario"
                    : matcher.group(group_dec) != null ? "declaracao"
                    //: matcher.group(group_inifim) != null ? "ini_fim"
                    : null;
            spansBuilder.add(new ArrayList<String>(), matcher.start() - lastMatchEnd);
            spansBuilder.add(new ArrayList<String>(Arrays.asList(styleClass)), matcher.end() - matcher.start());
            lastMatchEnd = matcher.end();
        }

        spansBuilder.add(new ArrayList<String>(), cdArea.getText().length() - lastMatchEnd);
        return spansBuilder.create();
    }

    public void insercao_tokens() {
        comand_list.add(new Comandos("int", "declaracao", "token_decl"));
        comand_list.add(new Comandos("double", "declaracao", "token_decl"));
        comand_list.add(new Comandos("<=", "operação comparação", "token_comp"));
        comand_list.add(new Comandos(">=", "operação comparação", "token_comp"));
        comand_list.add(new Comandos("!=", "operação comparação", "token_comp"));
        comand_list.add(new Comandos("+", "operação comparação", "token_opr"));
        comand_list.add(new Comandos("-", "operação comparação", "token_opr"));
        comand_list.add(new Comandos("+=", "operação incremento", "token_opr"));
        comand_list.add(new Comandos("==", "operação comparação", "token_comp"));
        comand_list.add(new Comandos("=", "operação atribuição", "token_comp"));
        comand_list.add(new Comandos("-=", "operação decremento", "token_opr"));
        comand_list.add(new Comandos("<", "operação comparação", "token_comp"));
        comand_list.add(new Comandos(">", "operação comparação", "token_comp"));
        comand_list.add(new Comandos("while", "loop", "token_loop"));
        comand_list.add(new Comandos("if", "operação comparação", "token_if"));
        comand_list.add(new Comandos("begin", "Inicio", "token_inic"));
        comand_list.add(new Comandos("end", "Fim", "token_fim"));
        comand_list.add(new Comandos("(", "parenteses", "token_paren"));
        comand_list.add(new Comandos(")", "parenteses", "token_paren"));
        comand_list.add(new Comandos("{", "chave", "token_chave"));
        comand_list.add(new Comandos("}", "chave", "token_chave"));
    }

    @FXML
    private void btn_openfile(ActionEvent event) {
        /*JFileChooser file = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
        String codigo_txt = "";
        String pt;
        file.setFileFilter(filter);
        int i = file.showSaveDialog(null);
        try {
            File arquivo = file.getSelectedFile();
            BufferedReader br = new BufferedReader(new FileReader(arquivo.getPath()));

            while ((pt = br.readLine()) != null) {
                codigo_txt += pt + "\n";
            }
            cdArea.replaceText(codigo_txt);
        } catch (Exception e) {
            System.out.println("" + e);
        }*/
        //insere_tabela();
    }

    public void insere_tabela() {
        String codigo = cdArea.getText(), erro = "";
        codigo = codigo.replaceAll("\\${1}.*\\${1}", "");
        String valor = "-", aux;
        String[] linha = codigo.split("\n");
        String[] coluna;
        Boolean entrou = false;
        int lin;
        //System.out.println(""+intermediario.gera_intermediario(codigo));

        try {
            for (int i = 0; i < linha.length; i++) {
                txt_sintatica.setText(intermediario.gera_intermediario(linha[i]));
                coluna = linha[i].split((" "));
                lin = i + 1;
                for (int j = 0; j < coluna.length; j++) {
                    entrou = false;

                    for (int k = 0; k < comand_list.size(); k++) {
                        if (coluna[j].contentEquals(comand_list.get(k).getComando())) {
                            tab = new Tabela(comand_list.get(k).getToken(), coluna[j], lin, comand_list.get(k).getTipo(), "-", "-");
                            lista_tabela.add(tab);
                            tabela.getItems().add(tab);
                            entrou = true;
                        }
                    }

                    if (!entrou && coluna[j].matches("^[a-zA-Z0-9]+$")) {

                        if (coluna[j + 1].equals("=")) {
                            if (coluna[j - 1].equals("double")) {
                                tab = new Tabela("token_id", coluna[j], lin, "variavel", coluna[j + 2].replace(";", ""), "double");
                                lista_tabela.add(tab);
                                tabela.getItems().add(tab);
                            } else {
                                tab = new Tabela("token_id", coluna[j], lin, "variavel", coluna[j + 2].replace(";", ""), "int");
                                lista_tabela.add(tab);
                                tabela.getItems().add(tab);
                            }
                        } else {
                            if (coluna[j].matches("\\d+")) {
                                tab = new Tabela("token_num", coluna[j], lin, "numero", "-", "-");
                                lista_tabela.add(tab);
                                tabela.getItems().add(tab);
                            }
                            if (coluna[j].matches("[a-zA-Z]+")) {
                                tab = new Tabela("token_id", coluna[j], lin, "variavel", "-", "-");
                                lista_tabela.add(tab);
                                tabela.getItems().add(tab);
                            }
                        }
                    }

                    if (coluna[j].contains(";")) {
                        aux = coluna[j].replace(";", "");
                        if (coluna[j].contains(".")) {
                            tab = new Tabela("token_id_float", aux, lin, "valor decimal", "-", "-");
                            lista_tabela.add(tab);
                            tabela.getItems().add(tab);
                            tab = new Tabela("token_fimlinha", ";", lin, "ponto e virgula", "-", "-");
                            lista_tabela.add(tab);
                            tabela.getItems().add(tab);
                        } else {
                            tab = new Tabela("token_num", aux, lin, "valor real", "-", "valor");
                            lista_tabela.add(tab);
                            tabela.getItems().add(tab);
                            tab = new Tabela("token_fimlinha", ";", lin, "ponto e virgula", "-", "-");
                            lista_tabela.add(tab);
                            tabela.getItems().add(tab);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("" + e);
        }
    }

    public String enquanto(String[] lin/*int pos, String erro*/) {
        String erro = "", ant, prox;
        //Queue<String> fila = new LinkedList<>();
        for (int i = 0; i < lin.length - 1; i++) {
            /*if (lin[i] && !lin[i + 1].equals("(")) {

            }*/

        }
        /*String ant = tabela.getItems().get(pos).getLexema();
        String prox = tabela.getItems().get(pos + 1).getLexema();

        if (ant.equals("(") && prox.matches("^[a-zA-Z]+$") || prox.matches("\\d+")) {

        }
        if (ant.equals("(")) {
            if (prox.matches("^[a-zA-Z]+$") || prox.matches("\\d+")) {
                ant = tabela.getItems().get(pos + 2).getLexema();
                prox = tabela.getItems().get(pos + 3).getLexema();
                if (ant.matches("\\<|\\>|\\<=|\\>=|\\==")) {
                    if (prox.matches("^[a-zA-Z]+$") || prox.matches("\\d+")) {
                        prox = tabela.getItems().get(pos + 4).getLexema();
                        if (!prox.equals(")")) {
                            erro += "Parenteses ')' inexistente" + tabela.getItems().get(pos).getLinha() + "\n";
                        }
                    } else {
                        erro += "Variável inexistente para comparação : AQUI" + tabela.getItems().get(pos).getLinha() + "\n";
                    }
                } else {
                    erro += "Operador Inexistente na linha : " + tabela.getItems().get(pos).getLinha() + "\n";
                }
            } else {
                erro += "Variável inexistente para comparação : " + tabela.getItems().get(pos).getLinha() + "\n";
            }
        } else {
            erro += "Falta de parenteses na linha : " + tabela.getItems().get(pos).getLinha() + "\n";
        }*/
        return "";
    }

    public String analise_sintatica(String linha) {
        String[] col;
        String ant, prox;
        col = linha.split(" ");
        Queue<String> fila = new LinkedList<>();
        for (int i = 0; i < fila.size(); i++) {
            fila.add(col[i]);
        }

        while (!fila.isEmpty()) {
            ant = fila.remove(); //remove o primeiro 
            prox = fila.remove(); //remove o segundo

            if (ant.equals("token_loop")) {

            }
        }
        return "";
    }

    private void verif_begend() {

    }

    @FXML
    private void btnCompilar(ActionEvent event) {
        tabela.getItems().clear();
        lista_tabela.clear();
        insere_tabela();
        txt_semantica.setText(semantica.analise(lista_tabela));
    }
}
