/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.porfoliofedenadal.backendSpringBoot.Controller;

import com.porfoliofedenadal.backendSpringBoot.Dto.dtoEducacion;
import com.porfoliofedenadal.backendSpringBoot.Model.Educacion;
import com.porfoliofedenadal.backendSpringBoot.Security.Controller.Mensaje;
import com.porfoliofedenadal.backendSpringBoot.Service.EducacionService;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("educ")
@CrossOrigin(origins = "https://portfoliofedenadal.web.app")

public class EducacionController {
    @Autowired
    EducacionService educacionService;

    @GetMapping("/lista")
    public ResponseEntity<List<Educacion>> list() {
        List<Educacion> list = educacionService.list();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @PreAuthorize ("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody dtoEducacion dtoedu) {
        if (StringUtils.isBlank(dtoedu.getNombreEd())) {
            return new ResponseEntity(new Mensaje("por favor ingrese el nombre"), HttpStatus.BAD_REQUEST);
        }
        if (educacionService.existsByNombreEd(dtoedu.getNombreEd())) {
            return new ResponseEntity(new Mensaje("ese elemento ya fue registrado"), HttpStatus.BAD_REQUEST);
        }

        Educacion educacion = new Educacion (dtoedu.getNombreEd(), dtoedu.getDescripcionEd(), dtoedu.getAreaEd());
        educacionService.save(educacion);

        return new ResponseEntity(new Mensaje("item agregado"), HttpStatus.OK);
    }
@PreAuthorize ("hasRole('ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<?> update(@PathVariable("id") int id, @RequestBody dtoEducacion dtoedu) {
        if (!educacionService.existsById(id)) {
            return new ResponseEntity(new Mensaje("El id no existe"), HttpStatus.BAD_REQUEST);
        }

        if (educacionService.existsByNombreEd(dtoedu.getNombreEd()) && educacionService.getByNombreEd(dtoedu.getNombreEd()).get().getId() != id) {
            return new ResponseEntity(new Mensaje("Item ya registrado"), HttpStatus.BAD_REQUEST);
        }

        if (StringUtils.isBlank(dtoedu.getNombreEd())) {
            return new ResponseEntity(new Mensaje("por favor ingrese el nombre"), HttpStatus.BAD_REQUEST);
        }
        Educacion educacion = educacionService.getOne(id).get();
        educacion.setNombreEd(dtoedu.getNombreEd());
        educacion.setDescripcionEd(dtoedu.getDescripcionEd());
        educacion.setAreaEd(dtoedu.getAreaEd());


        educacionService.save(educacion);
        return new ResponseEntity(new Mensaje("Item actualizado"), HttpStatus.OK);
    }
@PreAuthorize ("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable("id") int id) {

        if (!educacionService.existsById(id)) {
            return new ResponseEntity(new Mensaje("El id no existe"), HttpStatus.BAD_REQUEST);
        }
        educacionService.delete(id);
        return new ResponseEntity(new Mensaje("Item eliminado"), HttpStatus.OK);
    }

}
