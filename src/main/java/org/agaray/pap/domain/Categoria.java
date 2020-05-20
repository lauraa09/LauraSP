package org.agaray.pap.domain;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Categoria {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String nombre;
	
	@OneToMany(mappedBy = "categoria")
	private Collection<Producto> categorias;

	//=====================
	
	
	public Categoria(String nombre) {
		this.nombre = nombre;
		this.categorias = new ArrayList<Producto>();
	}

	public Categoria() {
		this.categorias = new ArrayList<Producto>();
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

	public Collection<Producto> getCategorias() {
		return categorias;
	}

	public void setCategorias(Collection<Producto> categorias) {
		this.categorias = categorias;
	}	
	
	

}
