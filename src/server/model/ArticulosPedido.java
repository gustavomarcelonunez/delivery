package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
        @NamedQuery(name = "ArticulosPedido.findSinRemitoHasta", query = "SELECT ap FROM ArticulosPedido ap WHERE (ap.pedido.date <= :date AND ap.remito is NULL) ORDER BY ap.pedido.domicilioEntrega"),
        @NamedQuery(name = "ArticulosPedido.findSinFacturar", query = "SELECT ap FROM ArticulosPedido ap WHERE (ap.remito is NOT NULL AND ap.factura is NULL AND ap.remito.entregado = TRUE) ORDER BY ap.pedido.cliente"), 
        @NamedQuery(name = "ArticulosPedido.findSameRemito", query = "SELECT ap FROM ArticulosPedido ap WHERE ap.remito.id = :id"),
        @NamedQuery(name = "ArticulosPedido.findSameFactura", query = "SELECT ap FROM ArticulosPedido ap WHERE ap.factura.id = :id"),
        @NamedQuery(name = "ArticulosPedido.findSameCliente", query = "SELECT ap FROM ArticulosPedido ap WHERE ap.pedido.cliente.cuit = :cuit"),
    })
public class ArticulosPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Float cantidad;
    private Float precioUnitario;
    private Float precio;

    @ManyToOne
    private Articulo articulo;

    @ManyToOne
    private Pedido pedido;

    @ManyToOne
    private Remito remito;

    @ManyToOne
    private Factura factura;

    public ArticulosPedido() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Float getCantidad() {
        return this.cantidad;
    }

    public void setCantidad(Float cantidad) {
        this.cantidad = cantidad;
    }

    public Float getPrecioUnitario() {
        return this.precioUnitario;
    }

    public void setPrecioUnitario(Float precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Articulo getArticulo() {
        return this.articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }

    public Pedido getPedido() {
        return this.pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Remito getRemito() {
        return this.remito;
    }

    public void setRemito(Remito remito) {
        this.remito = remito;
    }

    public Factura getFactura() {
        return this.factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public Float getPrecio() {
        return this.precio;
        
    }

    public void setPrecio(Float precio){
        this.precio = precio;
    }
}
