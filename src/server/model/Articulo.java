package model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;

@Entity
public class Articulo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nombre;
    private String descripcion;
    private Float precio;
    private Float stock;
    private String unidadMedida;

    @Column(unique = true)
    private String codigo;

    public Articulo (){}

    public int getId() {
        return this.id;
     }
  
    public void setId(int id) {
        this.id = id;
     }

    public String getNombre() {
         return this.nombre;
     }

    public void setNombre(String nombre) {
         this.nombre = nombre;
     }

    public String getDescripcion(){
        return this.descripcion;
    }

    public void setDescripcion(String descripcion){
        this.descripcion = descripcion;
    }


    public String getCodigo(){
        return this.codigo;
    }

    public void setCodigo(String codigo){
        this.codigo = codigo;
    }
    
    public Float getPrecio(){
        return this.precio;
    }

    public void setPrecio(Float precio){
        this.precio = precio;
    }

    public Float getStock(){
        return this.stock;
    }

    public void setStock(Float stock){
        this.stock = stock;
    }

    public String getUnidadMedida(){
        return this.unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida){
        this.unidadMedida = unidadMedida;
    }
}