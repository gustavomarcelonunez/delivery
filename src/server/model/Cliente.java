package model;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@NamedQueries({
        @NamedQuery(name = "Cliente.findClienteByCuit", query = "SELECT c FROM Cliente c WHERE c.cuit = :cuit"), })
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String cuit;

    private String razonSocial;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Collection<DomicilioEntrega> domiciliosEntrega;

    public Cliente() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCuit() {
        return this.cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public String getRazonSocial() {
        return this.razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public Collection<DomicilioEntrega> getDomiciliosEntrega() {
        return this.domiciliosEntrega;
    }

    public void setDomiciliosEntrega(Collection<DomicilioEntrega> domiciliosEntrega) {
        this.domiciliosEntrega = domiciliosEntrega;
    }

    // Aun no se utiliza
    public void agregarDomicilio(DomicilioEntrega domicilioEntrega) {
        this.domiciliosEntrega.add(domicilioEntrega);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Cliente other = (Cliente) obj;
        if (id != other.id)
            return false;
        if (cuit == null) {
            if (other.cuit != null)
                return false;
        } else if (!cuit.equals(other.cuit))
            return false;

        return true;
    }
}
