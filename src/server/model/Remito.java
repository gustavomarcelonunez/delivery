package model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Remito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.DATE)
    private Date fechaArmado;

    private boolean entregado;

    @ManyToOne
    private DomicilioEntrega domicilioEntrega;

    public Remito() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFechaArmado() {
        return fechaArmado;
    }

    public void setFechaArmado(Date fechaArmado) {
        this.fechaArmado = fechaArmado;
    }

    public DomicilioEntrega getDomicilioEntrega() {
        return this.domicilioEntrega;
    }

    public void setDomicilioEntrega(DomicilioEntrega domicilioEntrega) {
        this.domicilioEntrega = domicilioEntrega;
    }

    public boolean getEntregado() {
        return this.entregado;
    }

    public void setEntregado(boolean entregado) {
        this.entregado = entregado;
    }

}
