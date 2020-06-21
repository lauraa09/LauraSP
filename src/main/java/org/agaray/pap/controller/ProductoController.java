package org.agaray.pap.controller;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.agaray.pap.domain.Categoria;
import org.agaray.pap.domain.Pais;
import org.agaray.pap.domain.Producto;
import org.agaray.pap.exception.DangerException;
import org.agaray.pap.exception.InfoException;
import org.agaray.pap.helper.H;
import org.agaray.pap.helper.PRG;
import org.agaray.pap.repository.CategoriaRepository;
import org.agaray.pap.repository.ProductoRepository;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper.WarningHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping(value = "/producto")
public class ProductoController {

	@Autowired
	private ProductoRepository repoProducto;
	
	@Autowired
	private CategoriaRepository repoCategoria;
	
	@Value("${app.uploadFolder}")
	private String UPLOAD_FOLDER;
	
	
	//===AJAX===//
	@ResponseBody
	@PostMapping(value = "ajaxProdExistente", produces = "text/plain")
		public String ajaxProdExistente (
				@RequestParam String nombreP, HttpSession s) throws JsonProcessingException, DangerException{
				H.isRolOK("admin", s);
				
				
				HashMap<String, Integer> nombreProducto= new HashMap<>();
				
				if (repoProducto.getByNombre(nombreP) != null) {
					nombreProducto.put("coincide", 1);
				}
				else {
					nombreProducto.put("coincide", 0);
				}
						
			return new ObjectMapper().writeValueAsString(nombreProducto);
					
	}
	
	@PostMapping("d")
	public String borrarPost(@RequestParam("id") Long idProducto, ModelMap m, HttpSession s) throws DangerException {
		H.isRolOK("admin", s);
		try {
			String nombreProducto ="----";
			Producto producto = repoProducto.getOne(idProducto);
			nombreProducto = producto.getNombre();
			repoProducto.delete(producto);
		} catch (Exception e) {
			PRG.error("Error al borrar el producto", "producto/c");
		}
		return "redirect:/producto/r";

	}
	
	@GetMapping("c")
	public String crearGet(ModelMap m, HttpSession s) throws DangerException {
		H.isRolOK("admin", s);
		m.put("categorias", repoCategoria.findAll());
		m.put("view", "producto/productoC");
		return "_t/frame";
	}

	@PostMapping("c")
	public String crearPost(
			@RequestParam("nombre") String nombre, 
			@RequestParam("stock") Long stock,
			@RequestParam("precio") Double precio, 
			@RequestParam("foto") MultipartFile imgFile,
			@RequestParam(value = "idCategoria", required = false) Long idCategoria,
			HttpSession s) throws Exception, InfoException{
		
			if (idCategoria != null) {
				try {
					H.isRolOK("admin", s);
					
					String extensionFoto = null;
					extensionFoto = imgFile.getOriginalFilename().split("\\.")[1];
					
					Producto producto = new Producto(nombre, stock, precio, extensionFoto);
					
				
					Categoria categoriaProducto = repoCategoria.getOne(idCategoria);
					categoriaProducto.getCategorias().add(producto);
					producto.setCategoria(categoriaProducto);
					
					repoProducto.save(producto);
					
					byte[] contenido = imgFile.getBytes();
					Path path = Paths.get(UPLOAD_FOLDER + "producto-" + producto.getId()+ "." + producto.getFoto());
					Files.write(path, contenido);
				
				} catch (Exception e) {
					PRG.error("Error al crear " + nombre, "producto/r");
				}
			}
			else {
				PRG.info("Debes asociar un producto a una categoría", "/");
			}

		
		return "redirect:/producto/r";
	}
	

	@GetMapping("r")
	public String read(HttpSession s, ModelMap m) throws DangerException {
		H.isRolOK("admin", s);
		m.put("productos", repoProducto.findAllByOrderByCategoriaNombreAscNombreAsc());
		//m.put("productos", repoProducto.findAll());
		m.put("view", "producto/productoR");
		return "_t/frame";
	}
	
	@GetMapping("u")
	public String updateGet(@RequestParam("id") Long idProducto, ModelMap m, HttpSession s) throws DangerException {
		H.isRolOK("admin", s);
		m.put("producto", repoProducto.getOne(idProducto));
		m.put("view", "/producto/productoU");
		return "_t/frame";
	}
	
	@PostMapping("u")
	public void updatePost(
			@RequestParam("id") Long idProducto,
			@RequestParam("nombre") String nombre, 
			@RequestParam("stock") Long stock,
			@RequestParam("precio") Double precio, 
			HttpSession s) throws DangerException, InfoException {
		H.isRolOK("admin", s);
		try {
			Producto producto = repoProducto.getOne(idProducto);
			//======================
			// ATRIBUTOS "NORMALES"
			//======================
			producto.setNombre(nombre);
			producto.setStock(stock);
			producto.setPrecio(precio);
			
			repoProducto.save(producto);
			
		} catch (Exception e) {
			PRG.error("Error al actualizar, producto " + nombre + "duplicada ", "/producto/r");
		}
		PRG.info("Producto " + nombre + " actualizada correctamente", "/producto/r");
	}

	
	
}
