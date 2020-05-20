package org.agaray.pap.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.agaray.pap.domain.Categoria;

import org.agaray.pap.exception.DangerException;
import org.agaray.pap.exception.InfoException;
import org.agaray.pap.helper.H;
import org.agaray.pap.helper.PRG;
import org.agaray.pap.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/categoria")
public class CategoriaController  {

	@Autowired
	private CategoriaRepository repoCategoria;

	@GetMapping("r")
	public String read(ModelMap m, HttpSession s) throws DangerException {
		H.isRolOK("auth", s);
		List<Categoria> categorias = repoCategoria.findAll();
		m.put("categorias", categorias);

		m.put("view", "/categoria/categoriaR");
		return "/_t/frame";
	}

	@GetMapping("c")
	public String crearGet(ModelMap m, HttpSession s) throws DangerException {
		H.isRolOK("admin", s);
		m.put("view", "/categoria/categoriaC");
		return "/_t/frame";
	}

	@PostMapping("c")
	public void crearPost(@RequestParam("nombre") String nombreCategoria, HttpSession s) throws DangerException, InfoException {
		H.isRolOK("admin", s);
		try {
			repoCategoria.save(new Categoria(nombreCategoria));
		} catch (Exception e) {
			PRG.error("Categoria " + nombreCategoria + " duplicada", "/categoria/c");
		}
		PRG.info("Categoria " + nombreCategoria + " creada correctamente", "/categoria/r");
	}

	@PostMapping("d")
	public String borrarPost(@RequestParam("id") Long idCategoria, HttpSession s) throws DangerException {
		H.isRolOK("admin", s);
		String nombreCategoria = "----";
		try {
			Categoria categoria = repoCategoria.getOne(idCategoria);
			nombreCategoria = categoria.getNombre();
			repoCategoria.delete(categoria);
		} catch (Exception e) {
			PRG.error("Error al borrar la categoria " + nombreCategoria, "/categoria/c");
		}
		return "redirect:/categoria/r";
	}

	@GetMapping("u")
	public String updateGet(@RequestParam("id") Long idCategoria, ModelMap m, HttpSession s) throws DangerException {
		H.isRolOK("admin", s);
		m.put("categoria", repoCategoria.getOne(idCategoria));
		m.put("view", "/categoria/categoriaU");
		return "/_t/frame";
	}

	@PostMapping("u")
	public void updatePost(@RequestParam("nombre") String nombreCategoria, @RequestParam("id") Long idCategoria, HttpSession s)
			throws DangerException, InfoException {
		H.isRolOK("admin", s);
		try {
			Categoria categoria = repoCategoria.getOne(idCategoria);
			categoria.setNombre(nombreCategoria);
			repoCategoria.save(categoria);
		} catch (Exception e) {
			PRG.error("Categoria " + nombreCategoria + " duplicada", "/categoria/r");
		}
		PRG.info("Categoria " + nombreCategoria + " actualizada correctamente", "/categoria/r");
	}

}
