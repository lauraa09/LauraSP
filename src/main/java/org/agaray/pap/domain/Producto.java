package org.agaray.pap.domain;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Producto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nombre;
	
	private Long stock;
	
	private Double precio;

	private String foto;
	
	@OneToMany(mappedBy = "producto")
	private Collection<LineaDeVenta> ldvs;
	
	@ManyToOne
	private Categoria categoria;

	// ===========================

	
	public Producto() {
		this.ldvs = new ArrayList<LineaDeVenta>();
	}

	public Producto(String nombre, Long stock, Double precio, String foto) {
		super();
		this.nombre = nombre;
		this.stock = stock;
		this.precio = precio;
		this.foto = foto;
		this.ldvs = new ArrayList<LineaDeVenta>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Long getStock() {
		return stock;
	}

	public void setStock(Long stock) {
		this.stock = stock;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public Collection<LineaDeVenta> getLdvs() {
		return ldvs;
	}

	public void setLdvs(Collection<LineaDeVenta> ldvs) {
		this.ldvs = ldvs;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	

	// ========================

}
