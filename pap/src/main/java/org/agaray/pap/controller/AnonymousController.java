
package org.agaray.pap.controller;

import java.time.LocalDate;

import javax.servlet.http.HttpSession;

import org.agaray.pap.domain.Persona;
import org.agaray.pap.exception.DangerException;
import org.agaray.pap.helper.H;
import org.agaray.pap.helper.PRG;
import org.agaray.pap.repository.AficionRepository;
import org.agaray.pap.repository.PaisRepository;
import org.agaray.pap.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AnonymousController {

	@Autowired
	PersonaRepository repoPersona;

	@Autowired
	AficionRepository repoAficion;

	@Autowired
	PaisRepository repoPais;

	@GetMapping("/init")
	public String initGet(ModelMap m) throws DangerException {
		if (repoPersona.getByLoginname("admin") != null) {
			PRG.error("BD no vacía");
		}
		m.put("view", "/anonymous/init");
		return "/_t/frame";
	}

	@PostMapping("/init")
	public String initPost(@RequestParam("password") String password, ModelMap m) throws DangerException {
		if (repoPersona.getByLoginname("admin") != null) {
			PRG.error("Operación no válida. BD no vacía");
		}
		BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();
		if (!bpe.matches(password, bpe.encode("admin"))) { // Password harcoded
			PRG.error("Contraseña incorrecta","/init");
		}
		repoPersona.deleteAll();
		repoPais.deleteAll();
		repoAficion.deleteAll();
		repoPersona.save(new Persona("Administrador","admin","admin",170,LocalDate.now()));
		return "redirect:/";
	}

	@GetMapping("/info")
	public String info(HttpSession s, ModelMap m) {

		String mensaje = s.getAttribute("_mensaje") != null ? (String) s.getAttribute("_mensaje")
				: "Pulsa para volver a home";
		String severity = s.getAttribute("_severity") != null ? (String) s.getAttribute("_severity") : "info";
		String link = s.getAttribute("_link") != null ? (String) s.getAttribute("_link") : "/";

		s.removeAttribute("_mensaje");
		s.removeAttribute("_severity");
		s.removeAttribute("_link");

		m.put("mensaje", mensaje);
		m.put("severity", severity);
		m.put("link", link);

		m.put("view", "/_t/info");
		return "/_t/frame";
	}

	@GetMapping("/")
	public String home(ModelMap m) {
		m.put("view", "/anonymous/home");
		return "/_t/frame";
	}

	@GetMapping("/registro")
	public String registro(ModelMap m) {
		return "redirect:/persona/c";
	}

	@GetMapping("/login")
	public String loginGet(ModelMap m, HttpSession s) throws DangerException {
		H.isRolOK("anon", s);
		m.put("view", "/anonymous/login");
		return "/_t/frame";
	}

	@PostMapping("/login")
	public String loginPost(@RequestParam("loginname") String loginname, @RequestParam("password") String password,
			ModelMap m, HttpSession s) throws DangerException {
		H.isRolOK("anon", s);

		try {
			Persona persona = repoPersona.getByLoginname(loginname);
			if (!(new BCryptPasswordEncoder()).matches(password, persona.getPassword())) {
				throw new Exception();
			}
			s.setAttribute("persona", persona);
		} catch (Exception e) {
			PRG.error("Usuario o contraseña incorrecta", "/login");
		}

		return "redirect:/";
	}

	@GetMapping("/logout")
	public String logout(HttpSession s) throws DangerException {
		H.isRolOK("auth", s);

		s.invalidate();
		return "redirect:/";
	}
}