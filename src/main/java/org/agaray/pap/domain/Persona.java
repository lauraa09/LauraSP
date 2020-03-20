package org.agaray.pap.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
public class Persona {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nombre;

	@Column(unique = true)
	private String loginname;

	private String password;
	
	private Integer altura;
	
	private LocalDate fnac;

	@ManyToOne
	private Pais nace;

	@ManyToMany
	private Collection<Aficion> gustos;

	@ManyToMany
	private Collection<Aficion> odios;
	
	@OneToMany(mappedBy = "persona")
	private Collection<Venta> ventas;
	
	@OneToOne(cascade = CascadeType.PERSIST)
	private Venta ventaencurso;
	

//======================

	



	public Venta getVentaencurso() {
		return ventaencurso;
	}





	public void setVentaencurso(Venta ventaencurso) {
		this.ventaencurso = ventaencurso;
	}





	public Collection<Venta> getVentas() {
		return ventas;
	}





	public void setVentas(Collection<Venta> ventas) {
		this.ventas = ventas;
	}





	public Integer getAltura() {
		return altura;
	}


	public void setAltura(Integer altura) {
		this.altura = altura;
	}


	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = (new BCryptPasswordEncoder()).encode(password);
	}

	public LocalDate getFnac() {
		return fnac;
	}

	public void setFnac(LocalDate fnac) {
		this.fnac = fnac;
	}

	public Persona() {
		this.gustos = new ArrayList<Aficion>();
		this.odios = new ArrayList<Aficion>();
		this.ventas = new ArrayList<Venta>();
		this.ventaencurso = new Venta();
		this.ventaencurso.setPersonaencurso(this);
	}
	
	public Persona(String nombre) {
		this.nombre = nombre;
		this.gustos = new ArrayList<Aficion>();
		this.odios = new ArrayList<Aficion>();
		this.ventas = new ArrayList<Venta>();
		this.ventaencurso = new Venta();
		this.ventaencurso.setPersonaencurso(this);
}


	
	public Persona(String nombrePersona, String loginnamePersona, String passwordPersona, Integer altura, LocalDate fnac) {
		this.nombre = nombrePersona;
		this.loginname = loginnamePersona;
		this.altura = altura;
		this.fnac=fnac;
		this.password = (new BCryptPasswordEncoder()).encode(passwordPersona);
		
		this.ventaencurso = new Venta();
		this.ventaencurso.setPersonaencurso(this);
		
		this.gustos = new ArrayList<Aficion>();
		this.odios = new ArrayList<Aficion>();
		this.ventas = new ArrayList<Venta>();
	}



	public Long getId() {
		return id;
	}

	public Collection<Aficion> getGustos() {
		return gustos;
	}

	public void setGustos(Collection<Aficion> gustos) {
		this.gustos = gustos;
	}

	public Collection<Aficion> getOdios() {
		return odios;
	}

	public void setOdios(Collection<Aficion> odios) {
		this.odios = odios;
	}

	public Pais getNace() {
		return nace;
	}

	public void setNace(Pais nace) {
		this.nace = nace;
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
