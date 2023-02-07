package model;

import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
// Agrega los tipos de datos para almacenar las fechas
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({
        @NamedQuery(name = "Pedido.findPedidosCliente", query = "SELECT p FROM Pedido p WHERE p.cliente.id = :id"), })
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private int id;

    @Temporal(TemporalType.DATE)
    private Date date;

    private String observaciones;

    @ManyToOne
    private Cliente cliente;

    @ManyToOne
    private DomicilioEntrega domicilioEntrega;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Collection<ArticulosPedido> articulosPedido;

    public Pedido() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getObservaciones() {
        return this.observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Collection<ArticulosPedido> getArticulosPedido() {
        return this.articulosPedido;
    }

    public void setArticulosPedido(Collection<ArticulosPedido> articulosPedido) {
        this.articulosPedido = articulosPedido;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public DomicilioEntrega getDomicilioEntrega() {
        return this.domicilioEntrega;
    }

    public void setDomicilioEntrega(DomicilioEntrega domicilioEntrega) {
        this.domicilioEntrega = domicilioEntrega;
    }

    public double total() {
        double total = 0;
        for (ArticulosPedido art : articulosPedido) {
            total += art.getPrecio();
        }
        total = Math.round(total * 100d) / 100d;
        return total;
    }
}
