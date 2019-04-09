package Tools;

/**
 *
 * @author hiroshi
 */
public class Comandos {
    private String comando;
    private String tipo;
    private String token;

    public Comandos()
    {}

    public Comandos(String comando, String tipo, String token) {
        this.comando = comando;
        this.tipo = tipo;
        this.token = token;
    }

    public String getComando() {
        return comando;
    }

    public void setComando(String comando) {
        this.comando = comando;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
    
    
}
