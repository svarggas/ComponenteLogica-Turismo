package edu.turismo.service;

import edu.turismo.model.Ciudad;
import edu.turismo.model.LugarTuristico;
import edu.turismo.model.Pais;

import javax.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TurismoLugares {
	private static Conector ch = new Conector();

	public List<LugarTuristico> getLugaresTuristicos(String nombrePais) {
		Pais pais;
		List<Ciudad> ciudades = new ArrayList<>();
		List<LugarTuristico> lugares = new ArrayList<>();

		try {
			ch.startEntityManagerFactory();
			String query = "SELECT p FROM Pais p WHERE p.nombre = :nombrePais";
			TypedQuery<Pais> tq = ch.getEm().createQuery(query, Pais.class);
			tq.setParameter("nombrePais", nombrePais);
			pais = tq.getSingleResult();

			if (pais != null) {
				query = "SELECT c FROM Ciudad c WHERE c.pais = :pais";
				TypedQuery<Ciudad> tq2 = ch.getEm().createQuery(query, Ciudad.class);
				tq2.setParameter("pais", pais);
				ciudades = tq2.getResultList();

				for (Ciudad c : ciudades) {
					query = "SELECT l FROM LugarTuristico l WHERE l.ciudad= :ciudad";
					TypedQuery<LugarTuristico> tq3 = ch.getEm().createQuery(query, LugarTuristico.class);
					tq3.setParameter("ciudad", c);
					lugares.addAll(tq3.getResultList());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ch.stopEntityManagerFactory();
		}

		return lugares.isEmpty() ? null : lugares;
	}

	public LugarTuristico agregarlugares(String nombrePais, String nombreCiudad, String nombreTuris,
			String cordenadas) {
		LugarTuristico lugarTuristico = null;

		try {
			ch.startEntityManagerFactory();
			Pais pais = new Pais();
			pais.setNombre(nombrePais);
			pais.setCiudades(new HashSet<Ciudad>());

			Ciudad ciudad = new Ciudad();
			ciudad.setNombre(nombreCiudad);
			ciudad.setPais(pais);
			ciudad.setLugares(new HashSet<LugarTuristico>());

			lugarTuristico = new LugarTuristico();
			lugarTuristico.setCiudad(ciudad);
			lugarTuristico.setNombre(nombreTuris);
			lugarTuristico.setGeoCorde(cordenadas);

			pais.getCiudades().add(ciudad);

			ciudad.getLugares().add(lugarTuristico);

			ch.getEm().getTransaction().begin();
			ch.getEm().persist(pais);
			ch.getEm().persist(ciudad);
			ch.getEm().flush();
			ch.getEm().getTransaction().commit();
			ch.stopEntityManagerFactory();
			System.out.println("Finalizo");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lugarTuristico;

	}

	public LugarTuristico agregarLugarConPais(int idPais, String nombreCiudad, String nombreTuris, String cordenadas) {
		LugarTuristico lugarTuristico = null;
		try {
			ch.startEntityManagerFactory();
			Pais pais = new Pais();
			pais = ch.getEm().find(Pais.class, idPais);

			Ciudad ciudad = new Ciudad();
			ciudad.setNombre(nombreCiudad);
			ciudad.setPais(pais);
			ciudad.setLugares(new HashSet<LugarTuristico>());

			lugarTuristico = new LugarTuristico();
			lugarTuristico.setCiudad(ciudad);
			lugarTuristico.setNombre(nombreTuris);
			lugarTuristico.setGeoCorde(cordenadas);

			pais.getCiudades().add(ciudad);

			ciudad.getLugares().add(lugarTuristico);

			ch.getEm().getTransaction().begin();
			ch.getEm().merge(pais);
			ch.getEm().getTransaction().commit();
			ch.stopEntityManagerFactory();
			System.out.println("Finalizo");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return lugarTuristico;
	}

	public LugarTuristico agregarLugarConCiudad(int idCiudad, String nombreTuris, String cordenadas) {
		LugarTuristico lugarTuristico = null;
		try {
			ch.startEntityManagerFactory();
			Ciudad ciudad = new Ciudad();
			ciudad = ch.getEm().find(Ciudad.class, idCiudad);

			lugarTuristico = new LugarTuristico();
			lugarTuristico.setCiudad(ciudad);
			lugarTuristico.setNombre(nombreTuris);
			lugarTuristico.setGeoCorde(cordenadas);

			ciudad.getLugares().add(lugarTuristico);

			ch.getEm().getTransaction().begin();
			ch.getEm().merge(ciudad);
			ch.getEm().getTransaction().commit();
			ch.stopEntityManagerFactory();
			System.out.println("Finalizo");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return lugarTuristico;
	}

	public void borrarLugarTuristico(int id) {
		LugarTuristico lt = null;
		try {
			ch.startEntityManagerFactory();
			ch.getEm().getTransaction().begin();
			lt = ch.getEm().find(LugarTuristico.class, id);

			ch.getEm().remove(lt);
			ch.getEm().flush();
			ch.getEm().getTransaction().commit();
			ch.stopEntityManagerFactory();
			System.out.println("Finalizo");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void modificarLugarTuristico(int id, String nombre) {
		LugarTuristico lt = null;
		try {
			ch.startEntityManagerFactory();
			ch.getEm().getTransaction().begin();
			lt = ch.getEm().find(LugarTuristico.class, id);
			lt.setNombre(nombre);

			ch.getEm().persist(lt);
			ch.getEm().flush();
			ch.getEm().getTransaction().commit();

			ch.stopEntityManagerFactory();
			System.out.println("Finalizo");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
