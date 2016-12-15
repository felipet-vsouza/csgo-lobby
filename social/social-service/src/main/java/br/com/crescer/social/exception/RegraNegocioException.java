package br.com.crescer.social.exception;

public class RegraNegocioException extends Exception {

    public RegraNegocioException(String msg) {
        super(msg);
    }

    public RegraNegocioException(String msg, String cl) {
        this(msg);
        this.cl = cl;
    }

    private String cl;

    public String getCl() {
        return cl;
    }
}
