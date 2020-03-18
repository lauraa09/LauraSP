package org.agaray.pap.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.agaray.pap.domain.Aficion;
import org.agaray.pap.exception.DangerException;
import org.agaray.pap.exception.InfoException;
import org.agaray.pap.helper.H;
import org.agaray.pap.helper.PRG;
import org.agaray.pap.repository.AficionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/aficion")
public class AficionController {

	@Autowired
	private AficionRepository repoAficion;

	@PostMapping("d")
	public String borrarPost(@RequestParam("id") Long idAficion, HttpSession s) throws DangerException {
		H.isRolOK("admin", s);
		String nombreAficion = "----";
		try {
			Aficion aficion=repoAficion.getOne(idAficion);
			nombreAficion= aficion.getNombre();
			repoAficion.delete(aficion);
		} catch (Exception e) {
			PRG.error("Error al borrar la afición " + nombreAficion , "/aficion/c");
		}
		return "redirect:/aficion/r";
	}

	@GetMapping("r")
	public String read(ModelMap m, HttpSession s) throws DangerException {
		H.isRolOK("auth", s);
		List<Aficion> aficiones = repoAficion.findAll();
		m.put("aficiones", aficiones);
	
		m.put("view","/aficion/aficionR");
		return "/_t/frame";
	}

	@GetMapping("c")
	public String crearGet(ModelMap m, HttpSession s) throws DangerException {
		H.isRolOK("admin", s);
		m.put("view","/aficion/aficionC");
		return "/_t/frame";
	}

	@PostMapping("c")
	public String crearPost(@RequestParam("nombre") String nombreAficion, HttpSession s) throws DangerException {
		H.isRolOK("admin", s);
		try {
			repoAficion.save(new Aficion(nombreAficion));
		} catch (Exception e) {
			PRG.error("Afición " + nombreAficion + " duplicada", "/aficion/c");
		}
		return "redirect:/aficion/r";
	}
	
	@GetMapping("u")
	public String updateGet(@RequestParam("id") Long idAficion, ModelMap m, HttpSession s) throws DangerException {
		H.isRolOK("admin", s);
		m.put("aficion", repoAficion.getOne(idAficion));
		m.put("view", "/aficion/aficionU");
		return "/_t/frame";
	}

	@PostMapping("u")
	public void updatePost(@RequestParam("nombre") String nombreAficion, @RequestParam("id") Long idAficion, HttpSession s)
			throws DangerException, InfoException {
		H.isRolOK("admin", s);
		try {
			Aficion a = repoAficion.getOne(idAficion);
			a.setNombre(nombreAficion);
			repoAficion.save(a);
		} catch (Exception e) {
			PRG.error("Afición " + nombreAficion + " duplicada", "/aficion/r");
		}
		PRG.info("Afición " + nombreAficion + " actualizada correctamente", "/aficion/r");
	}


}
