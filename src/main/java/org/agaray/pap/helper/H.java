package org.agaray.pap.helper;

import javax.servlet.http.HttpSession;

import org.agaray.pap.domain.Persona;
import org.agaray.pap.exception.DangerException;

public class H {
	/**
	 * 
	 * @param rol Tres posibilidades "anon", "auth", "admin"
	 * @param s   la sesión activa
	 * @throws DangerException si el rol no coincide con el del usuario activo
	 */
	public static void isRolOK(String rol, HttpSession s) throws DangerException {
		Persona persona = null;

		if (s.getAttribute("persona") != null) {
			persona = (Persona) s.getAttribute("persona");
		}

		// Ya sé quién ha hecho login, y si alguien lo ha hecho
		
		if (persona == null) { // anon
			if (rol != "anon") {
				PRG.error("Rol inadecuado");
			}
		} else { // Auth o admin
			if (!persona.getLoginname().equals("admin") && rol.equals("admin")) { // anon
				PRG.error("Rol inadecuado");
			}
		}

	}
}
