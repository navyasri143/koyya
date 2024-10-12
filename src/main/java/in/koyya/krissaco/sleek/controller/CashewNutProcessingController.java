package in.koyya.krissaco.sleek.controller;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import in.koyya.krissaco.sleek.entity.CashewNutProcessing;
import in.koyya.krissaco.sleek.service.CashewNutProcessingService;

@RestController
@RequestMapping("/api/{sleekId}/processing")
public class CashewNutProcessingController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CashewNutProcessingController.class);
    @Autowired
    private CashewNutProcessingService service;

    // Endpoint to create or update processing data
    @PostMapping
    public CashewNutProcessing createOrUpdateProcessingData(
            @PathVariable String sleekId,
            @RequestBody CashewNutProcessing processingData) {
        LOGGER.info("Creating or updaing processing data for sleek id : '{}'", sleekId);
        processingData.setSleekId(sleekId); // Attach sleekId to the data
        return service.createOrUpdateProcessingData(processingData);
    }

    // Endpoint to get all processing data or filter by date
    @GetMapping
    public List<CashewNutProcessing> getAllProcessingData(
            @PathVariable String sleekId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
        if (date != null) {
            return service.getProcessingDataByDate(sleekId, date);
        }
        return service.getAllProcessingData(sleekId);
    }

    @GetMapping("/reports")
    public List<CashewNutProcessing> getProcessingDataInRange(
            @PathVariable String sleekId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {

        if (startDate == null || endDate == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Both startDate and endDate must be provided.");
        }

        return service.getProcessingDataInRange(sleekId, startDate, endDate);
    }
}
