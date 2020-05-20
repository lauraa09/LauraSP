package org.agaray.pap.controller;

import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.agaray.pap.domain.Pais;
import org.agaray.pap.domain.Venta;
import org.agaray.pap.exception.DangerException;
import org.agaray.pap.exception.InfoException;
import org.agaray.pap.helper.H;
import org.agaray.pap.helper.PRG;
import org.agaray.pap.repository.PaisRepository;
import org.agaray.pap.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/venta")
public class VentaController {

	@Autowired
	private VentaRepository repoVenta;
	
	
	@GetMapping("r")
	public String read(ModelMap m, HttpSession s) throws DangerException {
		H.isRolOK("auth", s);
		List<Venta> ventas = repoVenta.findAll();
		m.put("ventas", ventas);

		m.put("view", "/venta/ventaR");
		return "/_t/frame";
	}
	

	@GetMapping("c")
	public String crearGet(ModelMap m, HttpSession s) throws DangerException {
		H.isRolOK("admin", s);
		m.put("view", "/venta/ventaC");
		return "/_t/frame";
	}

	@PostMapping("c")
	public void crearPost(
			@RequestParam(value = "fecha", required = false)
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaVenta,
			HttpSession s) throws DangerException, InfoException {
		H.isRolOK("admin", s);
		try {
			repoVenta.save(new Venta(fechaVenta));
		} catch (Exception e) {
			PRG.error("Venta " + fechaVenta + " duplicada", "/venta/c");
		}
		PRG.info("Venta " + fechaVenta + " creado correctamente", "/venta/r");
	}
	

}
