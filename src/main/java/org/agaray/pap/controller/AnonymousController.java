
package org.agaray.pap.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.agaray.pap.domain.Aficion;
import org.agaray.pap.domain.Pais;
import org.agaray.pap.domain.Persona;
import org.agaray.pap.exception.DangerException;
import org.agaray.pap.exception.InfoException;
import org.agaray.pap.helper.H;
import org.agaray.pap.helper.PRG;
import org.agaray.pap.repository.AficionRepository;
import org.agaray.pap.repository.PaisRepository;
import org.agaray.pap.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class AnonymousController {

	@Autowired
	PersonaRepository repoPersona;

	@Autowired
	AficionRepository repoAficion;

	@Autowired
	PaisRepository repoPais;
	
	@Value("${app.uploadFolder}")
	private String UPLOAD_FOLDER;

	@GetMapping("/init")
	public String initGet(ModelMap m) throws DangerException { 
//		if (repoPersona.getByLoginname("admin") != null) {
//			//throw new ResponseStatusException(HttpStatus.NOT_FOUND);
//			PRG.error("BD no vacía");
//		}
		m.put("view", "/anonymous/init");
		return "/_t/frame";
	}

	@PostMapping("/init")
	public String initPost(@RequestParam("password") String password, ModelMap m) throws DangerException {
//		if (repoPersona.getByLoginname("admin") != null) {
//			//throw new ResponseStatusException(HttpStatus.NOT_FOUND);
//			//PRG.error("Operación no válida. BD no vacía");
//		}
		BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();
		
		if (!bpe.matches(password, bpe.encode("admin"))) { // Password harcoded
			//throw new ResponseStatusException(HttpStatus.NOT_FOUND);
			PRG.error("Contraseña incorrecta","/init");
		}
		repoPersona.deleteAll();
		repoPais.deleteAll();
		repoAficion.deleteAll();
		repoPersona.save(new Persona("Administrador","admin","admin",170,LocalDate.now()));
		
		m.put("view", "/anonymous/recrearBD");
		return "/_t/frame";
	
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
	//==========================//
	
	@GetMapping("/registro")
	public String registroGet(ModelMap m, HttpSession s) throws DangerException {
		m.put("view", "/anonymous/registro");
		return "/_t/frame";
	}

	
	@PostMapping("/registro")
	public void registroPost(
			@RequestParam("foto") MultipartFile imgFile,
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
			ModelMap m, HttpSession s) throws Exception {
			
			try {
				H.isRolOK("anon", s);
				String extensionFoto = null;
				extensionFoto = imgFile.getOriginalFilename().split("\\.")[1];
				
				Persona persona = new Persona(nombre, extensionFoto, loginname, password, altura, fnac);
				
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
				
				byte[] contenido = imgFile.getBytes();
				Path path = Paths.get(UPLOAD_FOLDER + "persona-" + persona.getId()+ "." + persona.getFoto());
				Files.write(path, contenido);
				
				
                PRG.info("Usuario " + loginname + " creado correctamente ", "/persona/r");	
												
			} catch (Exception e) {
			
				PRG.info(e.getMessage(), "/persona/r");
			}
				return;
		
	}
//===========================
	
	@GetMapping("/login")
	public String loginGet(ModelMap m, HttpSession s) throws DangerException {
		H.isRolOK("anon", s);
		m.put("view", "/anonymous/login");
		return "/_t/frame";
	}

	@PostMapping("/login")
	public String loginPost(
			@RequestParam("loginname") String loginname, 
			@RequestParam("password") String password,
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