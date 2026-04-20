package com.bmt.GrupoBM.controller;

import com.bmt.GrupoBM.dto.ServicioDTO;
import com.bmt.GrupoBM.models.Cliente;
import com.bmt.GrupoBM.models.Galeria;
import com.bmt.GrupoBM.models.Servicio;
import com.bmt.GrupoBM.service.ServicioService;
import com.bmt.GrupoBM.service.ClienteService;
import com.bmt.GrupoBM.service.ContenidoService;
import com.bmt.GrupoBM.service.GaleriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    private ServicioService servicioService;

    @Autowired
    private GaleriaService galeriaService;  // ← Agregado
    
    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ContenidoService contenidoService;

    @GetMapping("/")
    public String home(Model model) {
        List<Servicio> serviciosActivos = servicioService.listarActivos();
        model.addAttribute("servicios", serviciosActivos);

        Map<String, String> contenidos = contenidoService.getMapaContenido();
        // Asegurar valores por defecto si no existen
        contenidos.putIfAbsent("hero_titulo", "Cortinas de acero para<br>locales comerciales, bodegas y accesos de seguridad.");
        contenidos.putIfAbsent("hero_subtitulo", "Fabricamos y suministramos soluciones integrales para cortinas de acero, ofreciendo todo lo necesario en materiales, componentes y procesos, con calidad, experiencia y servicio profesional.");
        contenidos.putIfAbsent("nosotros_titulo", "Nosotros");
        contenidos.putIfAbsent("nosotros_parrafo1", "Somos una empresa con <strong class=\"text-[#8B0000]\">más de 25 años de experiencia</strong> en cortinas de acero.");
        contenidos.putIfAbsent("nosotros_parrafo2", "Nos dedicamos a la <strong class=\"text-[#8B0000]\">fabricación de tira para cortina</strong>, así como al suministro de material y cortinas listas para su instalación.");
        contenidos.putIfAbsent("nosotros_parrafo3", "Hoy seguimos evolucionando e incorporamos <strong class=\"text-[#8B0000]\">corte metálico CNC</strong>, ampliando nuestras capacidades para brindar soluciones más completas.");
        model.addAttribute("contenidos", contenidos);

        return "index";
    }

    @GetMapping("/api/servicios/{id}")
    @ResponseBody
    public ServicioDTO obtenerServicio(@PathVariable int id) {
        Servicio servicio = servicioService.buscarPorId(id);
        return servicioService.toDTO(servicio);
    }

    @GetMapping("/api/galeria")
    @ResponseBody
    public List<Galeria> obtenerGaleria() {
        return galeriaService.listarActivos();  // ← Ahora funciona
    }
    
    @GetMapping("/api/clientes")
    @ResponseBody
    public List<Cliente> obtenerClientes() {
        return clienteService.listarActivos();
    }
    
    
}

