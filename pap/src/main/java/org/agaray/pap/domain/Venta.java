package org.agaray.pap.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Venta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDate fecha;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "venta")
	private Collection<LineaDeVenta> ldvs;
	
	@ManyToOne
	private Persona persona;
	
	@OneToOne(mappedBy = "ventaencurso")
	private Persona personaencurso;

	// ==========================
	public Venta() {
		this.ldvs = new ArrayList<LineaDeVenta>();
	}

	public Persona getPersonaencurso() {
		return personaencurso;
	}

	public void setPersonaencurso(Persona personaencurso) {
		this.personaencurso = personaencurso;
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public Venta(LocalDate fecha) {
		super();
		this.fecha = fecha;
		this.ldvs = new ArrayList<LineaDeVenta>();
	}

	public Long getId() {
		return id;
	}

	public Collection<LineaDeVenta> getLdvs() {
		return ldvs;
	}

	public void setLdvs(Collection<LineaDeVenta> ldvs) {
		this.ldvs = ldvs;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public double getTotal() {
		double total = 0;
		for (LineaDeVenta ldv:this.ldvs) {
			total += ldv.getTotal();
		}
		return total;
	}
}
