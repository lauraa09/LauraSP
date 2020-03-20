package org.agaray.pap.domain;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Aficion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String nombre;

	@ManyToMany(mappedBy = "gustos")
	private Collection<Persona> gustosas;

	@ManyToMany(mappedBy = "odios")
	private Collection<Persona> odiosas;

	// ======================
	
	public Aficion() {
		this.gustosas = new ArrayList<Persona>();
		this.odiosas= new ArrayList<Persona>();
	}

	public Aficion(String nombre) {
		this.nombre=nombre;
		this.gustosas = new ArrayList<Persona>();
		this.odiosas= new ArrayList<Persona>();
	}

	public Long getId() {
		return id;
	}

	public Collection<Persona> getGustosas() {
		return gustosas;
	}

	public void setGustosas(Collection<Persona> gustosas) {
		this.gustosas = gustosas;
	}

	public Collection<Persona> getOdiosas() {
		return odiosas;
	}

	public void setOdiosas(Collection<Persona> odiosas) {
		this.odiosas = odiosas;
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

}
