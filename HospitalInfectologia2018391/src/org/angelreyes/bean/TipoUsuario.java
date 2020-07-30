package org.angelreyes.bean;


public class TipoUsuario {
    private int codigoTipoUsuario;
    private String descripcion;

    public TipoUsuario() {
    }

    public TipoUsuario(int codigoTipoUsuario, String descripcion) {
        this.codigoTipoUsuario = codigoTipoUsuario;
        this.descripcion = descripcion;
    }

    public int getCodigoTipoUsuario() {
        return codigoTipoUsuario;
    }

    public void setCodigoTipoUsuario(int codigoTipoUsuario) {
        this.codigoTipoUsuario = codigoTipoUsuario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String toString(){
        return  "  |  " + getDescripcion() + "  |  " ;
    }
    
}
