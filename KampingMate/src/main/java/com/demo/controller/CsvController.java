package com.demo.controller;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.dto.CampingItem;
import com.demo.service.GoCampingAPI;

@RestController
@RequestMapping("/api")
public class CsvController {

    @Autowired
    private GoCampingAPI goCampingAPI;

    @GetMapping("/campingSites/csv")
    public ResponseEntity<byte[]> getCampingSitesAsCSV() {
        try {
            List<CampingItem> campingItems = goCampingAPI.getAllCampingSites();
            ByteArrayOutputStream outputStream = goCampingAPI.convertToCSV(campingItems);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            headers.setContentDispositionFormData("attachment", "campingSites.csv");

            return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/campingSites/xml")
    public ResponseEntity<byte[]> getCampingSitesAsXML() {
        try {
            List<CampingItem> campingItems = goCampingAPI.getAllCampingSites();
            ByteArrayOutputStream outputStream = goCampingAPI.convertToXML(campingItems);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_XML);
            headers.setContentDispositionFormData("attachment", "campingSites.xml");

            return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
