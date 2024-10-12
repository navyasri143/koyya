package in.koyya.krissaco.sleek.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.koyya.krissaco.sleek.dto.ProcessStagesDTO;
import in.koyya.krissaco.sleek.service.ProcessStagesService;

@RestController
@RequestMapping("/api/{sleekId}/commodities/process-stages")
public class ProcessStagesController {

    @Autowired
    private ProcessStagesService processStagesService;

    // Get current cashew nut process stages by sleekId
    @GetMapping
    public ProcessStagesDTO getProcessStages(@PathVariable String sleekId) {
        return processStagesService.getProcessStagesBySleekId(sleekId);
    }

    // Update cashew nut process stages by sleekId
    @PutMapping("/update")
    public ProcessStagesDTO updateProcessStages(@PathVariable String sleekId, @RequestBody ProcessStagesDTO updatedStages) {
        return processStagesService.updateProcessStages(sleekId, updatedStages);
    }

    // Reset cashew nut process stages to default values by sleekId
    @PutMapping("/reset")
    public ProcessStagesDTO resetProcessStages(@PathVariable String sleekId) {
        return processStagesService.resetProcessStages(sleekId);
    }
}
