package org.agaray.pap.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class LineaDeVenta {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Integer cantidad;

	@ManyToOne
	private Producto producto;

	@ManyToOne
	private Venta venta;

	// =================================

	public LineaDeVenta() {
	}

	public LineaDeVenta(Integer cantidad) {
		super();
		this.cantidad = cantidad;
	}

	public Long getId() {
		return id;
	}

	public Venta getVenta() {
		return venta;
	}

	public void setVenta(Venta venta) {
		this.venta = venta;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	
	public double getTotal() {
		return cantidad * this.producto.getPrecio();
	}

}
