package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class DomicilioEntrega {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String calle;
    private int altura;
    private String pisoDpto;

    @ManyToOne
    private Localidad localidad;

    public DomicilioEntrega() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCalle() {
        return this.calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public int getAltura() {
        return this.altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public String getPisoDpto() {
        return this.pisoDpto;
    }

    public void setPisoDpto(String pisoDpto) {
        this.pisoDpto = pisoDpto;
    }

    public Localidad getLocalidad() {
        return this.localidad;
    }

    public void setLocalidad(Localidad localidad) {
        this.localidad = localidad;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final DomicilioEntrega other = (DomicilioEntrega) obj;
        if (id != other.id)
            return false;
        if (calle == null) {
            if (other.calle != null)
                return false;
        } else if (!calle.equals(other.calle))
            return false;
        if (altura != other.altura)
            return false;

        return true;
    }
}
