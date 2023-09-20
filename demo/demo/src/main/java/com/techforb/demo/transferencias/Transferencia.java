package com.techforb.demo.transferencias;

public class Transferencia {
    private Long idOrigen;
    private Long idDestino;
    private Float cantidad;

    public Transferencia(Long idOrigen, Long idDestino, Float cantidad) {
        this.idOrigen = idOrigen;
        this.idDestino = idDestino;
        this.cantidad = cantidad;
    }

    public Long getIdOrigen() {
        return idOrigen;
    }

    public void setIdOrigen(Long idOrigen) {
        this.idOrigen = idOrigen;
    }

    public Long getIdDestino() {
        return idDestino;
    }

    public void setIdDestino(Long idDestino) {
        this.idDestino = idDestino;
    }

    public Float getCantidad() {
        return cantidad;
    }

    public void setCantidad(Float cantidad) {
        this.cantidad = cantidad;
    }
}