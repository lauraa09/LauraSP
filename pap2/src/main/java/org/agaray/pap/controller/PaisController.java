package org.agaray.pap.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.agaray.pap.domain.Pais;
import org.agaray.pap.exception.DangerException;
import org.agaray.pap.exception.InfoException;
import org.agaray.pap.helper.H;
import org.agaray.pap.helper.PRG;
import org.agaray.pap.repository.PaisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/pais")
public class PaisController {

	@Autowired
	private PaisRepository repoPais;

	@GetMapping("r")
	public String read(ModelMap m, HttpSession s) throws DangerException {
		H.isRolOK("auth", s);
		List<Pais> paises = repoPais.findAll();
		m.put("paises", paises);

		m.put("view", "/pais/paisR");
		return "/_t/frame";
	}

	@GetMapping("c")
	public String crearGet(ModelMap m, HttpSession s) throws DangerException {
		H.isRolOK("admin", s);
		m.put("view", "/pais/paisC");
		return "/_t/frame";
	}

	@PostMapping("c")
	public void crearPost(@RequestParam("nombre") String nombrePais, HttpSession s) throws DangerException, InfoException {
		H.isRolOK("admin", s);
		try {
			repoPais.save(new Pais(nombrePais));
		} catch (Exception e) {
			PRG.error("País " + nombrePais + " duplicado", "/pais/c");
		}
		PRG.info("País " + nombrePais + " creado correctamente", "/pais/r");
	}

	@PostMapping("d")
	public String borrarPost(@RequestParam("id") Long idPais, HttpSession s) throws DangerException {
		H.isRolOK("admin", s);
		String nombrePais = "----";
		try {
			Pais pais = repoPais.getOne(idPais);
			nombrePais = pais.getNombre();
			repoPais.delete(pais);
		} catch (Exception e) {
			PRG.error("Error al borrar el país " + nombrePais, "/pais/c");
		}
		return "redirect:/pais/r";
	}

	@GetMapping("u")
	public String updateGet(@RequestParam("id") Long idPais, ModelMap m, HttpSession s) throws DangerException {
		H.isRolOK("admin", s);
		m.put("pais", repoPais.getOne(idPais));
		m.put("view", "/pais/paisU");
		return "/_t/frame";
	}

	@PostMapping("u")
	public void updatePost(@RequestParam("nombre") String nombrePais, @RequestParam("id") Long idPais, HttpSession s)
			throws DangerException, InfoException {
		H.isRolOK("admin", s);
		try {
			Pais p = repoPais.getOne(idPais);
			p.setNombre(nombrePais);
			repoPais.save(p);
		} catch (Exception e) {
			PRG.error("País " + nombrePais + " duplicado", "/pais/r");
		}
		PRG.info("País " + nombrePais + " actualizado correctamente", "/pais/r");
	}

}
