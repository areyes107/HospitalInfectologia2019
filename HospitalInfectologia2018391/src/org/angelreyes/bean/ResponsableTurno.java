package org.angelreyes.bean;


public class ResponsableTurno {
    private int codigoResponsableTurno;
    private String nombreResponsable;
    private String ApellidosResponsable;
    private String telefonoPersonal;
    private int codigoArea;
    private int codigoCargo;

    public ResponsableTurno() {
    }

    public ResponsableTurno(int codigoResponsableTurno, String nombreResponsable, String ApellidosResponsable, String telefonoPersonal, int codigoArea, int codigoCargo) {
        this.codigoResponsableTurno = codigoResponsableTurno;
        this.nombreResponsable = nombreResponsable;
        this.ApellidosResponsable = ApellidosResponsable;
        this.telefonoPersonal = telefonoPersonal;
        this.codigoArea = codigoArea;
        this.codigoCargo = codigoCargo;
    }

    public int getCodigoResponsableTurno() {
        return codigoResponsableTurno;
    }

    public void setCodigoResponsableTurno(int codigoResponsableTurno) {
        this.codigoResponsableTurno = codigoResponsableTurno;
    }

    public String getNombreResponsable() {
        return nombreResponsable;
    }

    public void setNombreResponsable(String nombreResponsable) {
        this.nombreResponsable = nombreResponsable;
    }

    public String getApellidosResponsable() {
        return ApellidosResponsable;
    }

    public void setApellidosResponsable(String ApellidosResponsable) {
        this.ApellidosResponsable = ApellidosResponsable;
    }

    public String getTelefonoPersonal() {
        return telefonoPersonal;
    }

    public void setTelefonoPersonal(String telefonoPersonal) {
        this.telefonoPersonal = telefonoPersonal;
    }

    public int getCodigoArea() {
        return codigoArea;
    }

    public void setCodigoArea(int codigoArea) {
        this.codigoArea = codigoArea;
    }

    public int getCodigoCargo() {
        return codigoCargo;
    }

    public void setCodigoCargo(int codigoCargo) {
        this.codigoCargo = codigoCargo;
    }
    
    public String toString(){
        return getCodigoResponsableTurno() + "  |  " + getNombreResponsable() + ", " + getApellidosResponsable();
    }
}
