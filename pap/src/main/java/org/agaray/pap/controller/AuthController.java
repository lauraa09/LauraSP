package org.agaray.pap.controller;

import java.time.LocalDate;

import javax.servlet.http.HttpSession;

import org.agaray.pap.domain.LineaDeVenta;
import org.agaray.pap.domain.Persona;
import org.agaray.pap.domain.Producto;
import org.agaray.pap.domain.Venta;
import org.agaray.pap.exception.DangerException;
import org.agaray.pap.exception.InfoException;
import org.agaray.pap.helper.H;
import org.agaray.pap.helper.PRG;
import org.agaray.pap.repository.LdvRepository;
import org.agaray.pap.repository.PersonaRepository;
import org.agaray.pap.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

	@Autowired
	private PersonaRepository repoPersona;
	
	@Autowired
	private ProductoRepository repoProducto;

	@Autowired
	private LdvRepository repoLdv;

	@GetMapping("/carrito/add")
	public String carritoAddGet(ModelMap m, HttpSession s) throws DangerException {
		H.isRolOK("auth", s);
		m.put("productos", repoProducto.findAll());
		m.put("view", "auth/carritoAdd");
		return "_t/frame";
	}

	@PostMapping("/carrito/add")
	public String carritoAddPost(
			@RequestParam("idProducto") Long id, 
			@RequestParam("cantidad") Integer cantidad,
			HttpSession s) throws DangerException {

		try {
			Producto producto = repoProducto.getOne(id);
			if (cantidad > producto.getStock() ) {
				throw new Exception("Stock insuficiente");
			}
			
			Persona persona = (Persona) s.getAttribute("persona");
			Venta vec = persona.getVentaencurso();
			LineaDeVenta ldv = new LineaDeVenta(cantidad);

			
			ldv.setVenta(vec);
			vec.getLdvs().add(ldv);

			ldv.setProducto(producto);
			producto.getLdvs().add(ldv);
			producto.setStock(producto.getStock()-cantidad);

			repoLdv.save(ldv);
		} catch (Exception e) {
			PRG.error("Error al añadir producto al carrito: "+e.getMessage(),"/carrito/r");
		}
		return "redirect:/carrito/r";
	}

	@GetMapping("/carrito/r")
	public String carritoR(ModelMap m, HttpSession s) throws DangerException {
		H.isRolOK("auth", s);
		Persona persona = (Persona)s.getAttribute("persona");
		m.put("venta", persona.getVentaencurso());
		m.put("view", "auth/carritoR");
		return "_t/frame";
	}
	
	@PostMapping("/carrito/comprar")
	public void comprar(
			HttpSession s
			) throws DangerException, InfoException {
		try {
			// Hacer la compra
			Persona persona = (Persona)s.getAttribute("persona");
			Venta vec = persona.getVentaencurso();
			
			persona.getVentas().add(vec);
			vec.setPersona(persona);
			vec.setFecha(LocalDate.now());
			vec.setPersonaencurso(null);
			
			Venta nuevaVec = new Venta();
			persona.setVentaencurso(nuevaVec);
			nuevaVec.setPersonaencurso(persona);
			repoPersona.save(persona);
			
		}
		catch (Exception e) {
			PRG.error("Error al realizar la compra","/producto/r");
		}
		PRG.info("Compra realizada","/producto/r");
	}

}
