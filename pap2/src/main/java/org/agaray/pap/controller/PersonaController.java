package org.agaray.pap.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.agaray.pap.domain.Aficion;
import org.agaray.pap.domain.Pais;
import org.agaray.pap.domain.Persona;
import org.agaray.pap.exception.DangerException;
import org.agaray.pap.helper.H;
import org.agaray.pap.helper.PRG;
import org.agaray.pap.repository.AficionRepository;
import org.agaray.pap.repository.PaisRepository;
import org.agaray.pap.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/persona")
public class PersonaController {

	@Autowired
	private PersonaRepository repoPersona;

	@Autowired
	private PaisRepository repoPais;

	@Autowired
	private AficionRepository repoAficion;

	@PostMapping("d")
	public String borrarPost(@RequestParam("id") Long idPersona, HttpSession s) throws DangerException {
		H.isRolOK("admin", s);
		String nombrePersona = "----";
		try {
			Persona persona = repoPersona.getOne(idPersona);
			nombrePersona = persona.getNombre();
			repoPersona.delete(persona);
		} catch (Exception e) {
			PRG.error("Error al borrar la persona " + nombrePersona, "/persona/c");
		}
		return "redirect:/persona/r";
	}

	@GetMapping("c")
	public String crearGet(ModelMap m, HttpSession s) throws DangerException {
		H.isRolOK("admin", s);
		m.put("paises", repoPais.findAll());
		m.put("aficiones", repoAficion.findAll());
		m.put("view", "/persona/personaC");
		return "/_t/frame";
	}

	@PostMapping("c")
	public String crearPost(
			@RequestParam("nombre") String nombre,
			@RequestParam("loginname") String loginname, 
			@RequestParam("password") String password,
			@RequestParam(value="altura", required=false) Integer altura, 
			@RequestParam(value="fnac", required=false)
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
			LocalDate fnac,
			@RequestParam(value = "idPais", required = false) Long idPais,
			@RequestParam(value = "idAficionGusta[]", required = false) List<Long> idGustos,
			@RequestParam(value = "idAficionOdio[]", required = false) List<Long> idOdios,
			HttpSession s) throws DangerException {
		try {
			H.isRolOK("admin", s);
			Persona persona = new Persona(nombre, loginname, password, altura, fnac);
			if (idPais != null) {
				Pais paisNacimiento = repoPais.getOne(idPais);
				paisNacimiento.getNacidos().add(persona);
				persona.setNace(paisNacimiento);
			}

			idGustos = (idGustos == null ? new ArrayList<Long>() : idGustos);
			for (Long id : idGustos) {
				Aficion aficion = repoAficion.getOne(id);
				aficion.getGustosas().add(persona);
				persona.getGustos().add(aficion);
			}

			idOdios = (idOdios == null ? new ArrayList<Long>() : idOdios);
			for (Long id : idOdios) {
				Aficion aficion = repoAficion.getOne(id);
				aficion.getOdiosas().add(persona);
				persona.getOdios().add(aficion);
			}

			repoPersona.save(persona);

		} catch (Exception e) {
			PRG.error("Error al crear " + nombre, "/persona/r");
		}
		return "redirect:/persona/r";
	}

	@GetMapping("r")
	public String read(ModelMap m, @RequestParam(value = "f", required = false) String f, HttpSession s) throws DangerException {
		H.isRolOK("auth", s);

		f = (f == null) ? "" : f;
		m.put("f", f);
		/*
		 * List<Persona> personas = repoPersona
		 * .findDistinctByNombreIgnoreCaseContainingOrLoginnameIgnoreCaseContainingOrNaceNombreIgnoreCaseContainingOrGustosNombreIgnoreCaseContainingOrOdiosNombreIgnoreCaseContainingOrderByNaceNombreDesc(
		 * f, f, f, f, f);
		 */
		// List<Persona> personas = repoPersona.findAllByOrderByAlturaAsc();
		List<Persona> personas = repoPersona.findAllByOrderByFnacDesc();
		m.put("personas", personas);

		m.put("view", "/persona/personaR");
		return "/_t/frame";
	}

	@GetMapping("u")
	public String updateGet(ModelMap m, @RequestParam("id") Long id, HttpSession s) throws DangerException {
		H.isRolOK("admin", s);

		m.put("persona", repoPersona.getOne(id));
		m.put("paises", repoPais.findAll());
		m.put("aficiones", repoAficion.findAll());
		m.put("view", "/persona/personaU");
		return "/_t/frame";
	}

	@PostMapping("u")
	public void updatePost(@RequestParam("id") Long idPersona, 
			@RequestParam("nombre") String nombrePersona,
			@RequestParam("altura") Integer altura,
			@RequestParam("fnac") 
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
			LocalDate fnac,
			@RequestParam("idPais") Long idPais,
			@RequestParam(value = "idAficionGusta[]", required = false) List<Long> idGustos,
			@RequestParam(value = "idAficionOdio[]", required = false) List<Long> idOdios, HttpSession s)
			throws Exception {
		H.isRolOK("admin", s);

		try {

			Persona persona = repoPersona.getOne(idPersona);

			// ====================
			// ATRIBUTOS "NORMALES"
			// ====================
			persona.setNombre(nombrePersona);
			persona.setAltura(altura);
			persona.setFnac(fnac);
			// ====================


			// ====================
			// PAIS NACIMIENTO
			// ====================
			Pais paisNacimiento = repoPais.getOne(idPais);
			Pais paisNacimientoAnt = persona.getNace();
			// ====================

			paisNacimientoAnt.getNacidos().remove(persona);
			persona.setNace(null);

			paisNacimiento.getNacidos().add(persona);
			persona.setNace(paisNacimiento);

			
			// ====================
			// GUSTOS y ODIOS
			// ====================

			idGustos = (idGustos == null ? new ArrayList<Long>() : idGustos);
			idOdios = (idOdios == null ? new ArrayList<Long>() : idOdios);

			// Creo nueva colección de gustos nuevos y la sustituyo por la antigua
			List<Aficion> gustosNuevos = new ArrayList<Aficion>();
			for (Long id : idGustos) {
				Aficion aficion = repoAficion.getOne(id);
				aficion.getGustosas().add(persona);
				gustosNuevos.add(aficion);
			}
			persona.setGustos(gustosNuevos);

			// Creo nueva colección de odios nuevos y la sustituyo por la antigua
			List<Aficion> odiosNuevos = new ArrayList<Aficion>();
			for (Long id : idOdios) {
				Aficion aficion = repoAficion.getOne(id);
				aficion.getOdiosas().add(persona);
				odiosNuevos.add(aficion);
			}
			persona.setOdios(odiosNuevos);

			repoPersona.save(persona);

		}
		catch (Exception e) {
			PRG.error("Error al actualizar " + nombrePersona + " // "+e.getMessage(), "/persona/r");
		}

		PRG.info("Persona " + nombrePersona + " actualizada correctamente", "/persona/r");

	}

}
