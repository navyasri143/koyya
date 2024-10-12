package in.koyya.krissaco.sleek.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.koyya.krissaco.sleek.dto.ProcessStagesDTO;
import in.koyya.krissaco.sleek.entity.ProcessStages;
import in.koyya.krissaco.sleek.repository.ProcessStagesRepository;

@Service
public class ProcessStagesService {

    @Autowired
    private ProcessStagesRepository processStagesRepository;

    // Get the ProcessStages by sleekId
    public ProcessStagesDTO getProcessStagesBySleekId(String sleekId) {
        // Check if ProcessStages exist for the given sleekId
        Optional<ProcessStages> processStagesOptional = processStagesRepository.findBySleekId(sleekId);

        if (processStagesOptional.isEmpty()) {
            // If no stages exist for this sleekId, create default ProcessStages
            ProcessStages newStages = createDefaultProcessStages(sleekId);
            processStagesRepository.save(newStages);  // Save to the database
            return convertToDTO(newStages);
        }

        // Return existing stages if found
        return convertToDTO(processStagesOptional.get());
    }

    // Create default ProcessStages for a new sleekId with updated values
    private ProcessStages createDefaultProcessStages(String sleekId) {
        ProcessStages defaultStages = new ProcessStages();
        defaultStages.setSleekId(sleekId);
        defaultStages.setBoilingSteaming(5.0);
        defaultStages.setGrading(2.0);
        defaultStages.setCutting(1.5); 
        defaultStages.setPrimaryShelling(1.5);
        defaultStages.setSecondaryShelling(52.0);  // By-product
        defaultStages.setBormaDrying(5.0);
        defaultStages.setCooling(1.0);  // Renamed from chilling to cooling
        defaultStages.setPeeling(3.0);
        defaultStages.setSweating(2.0);
        defaultStages.setSorting(2.0);
        defaultStages.setPackaging(1.0);

        return defaultStages;
    }

    // Update ProcessStages for a given sleekId
    public ProcessStagesDTO updateProcessStages(String sleekId, ProcessStagesDTO updatedStagesDTO) {
        ProcessStages existingStages = processStagesRepository.findBySleekId(sleekId)
                .orElseThrow(() -> new IllegalArgumentException("No ProcessStages found for sleekId: " + sleekId));

        // Update the existing stages
        existingStages.setBoilingSteaming(updatedStagesDTO.getBoilingSteaming());
        existingStages.setGrading(updatedStagesDTO.getGrading());
        existingStages.setCutting(updatedStagesDTO.getCutting()); 
        existingStages.setPrimaryShelling(updatedStagesDTO.getPrimaryShelling());
        existingStages.setSecondaryShelling(updatedStagesDTO.getSecondaryShelling());
        existingStages.setBormaDrying(updatedStagesDTO.getBormaDrying());
        existingStages.setCooling(updatedStagesDTO.getCooling());  // Updated to cooling
        existingStages.setPeeling(updatedStagesDTO.getPeeling());
        existingStages.setSweating(updatedStagesDTO.getSweating());
        existingStages.setSorting(updatedStagesDTO.getSorting());
        existingStages.setPackaging(updatedStagesDTO.getPackaging());

        ProcessStages updatedStages = processStagesRepository.save(existingStages);
        return convertToDTO(updatedStages);
    }

    // Reset ProcessStages to the default values for a given sleekId
    public ProcessStagesDTO resetProcessStages(String sleekId) {
        ProcessStages existingStages = processStagesRepository.findBySleekId(sleekId)
                .orElseThrow(() -> new IllegalArgumentException("No ProcessStages found for sleekId: " + sleekId));

        // Reset all stage values to updated defaults
        existingStages.setBoilingSteaming(5.0);
        existingStages.setGrading(2.0);
        existingStages.setCutting(1.5);
        existingStages.setPrimaryShelling(1.5);
        existingStages.setSecondaryShelling(52.0);  // By-product
        existingStages.setBormaDrying(5.0);
        existingStages.setCooling(1.0);  // Updated to cooling
        existingStages.setPeeling(3.0);
        existingStages.setSweating(2.0);
        existingStages.setSorting(2.0);
        existingStages.setPackaging(1.0);

        return convertToDTO(processStagesRepository.save(existingStages));
    }

    // Convert ProcessStages entity to DTO
    private ProcessStagesDTO convertToDTO(ProcessStages processStages) {
        return new ProcessStagesDTO(
                processStages.getBoilingSteaming(),
                processStages.getGrading(),
                processStages.getCutting(), 
                processStages.getPrimaryShelling(),
                processStages.getSecondaryShelling(),
                processStages.getBormaDrying(),
                processStages.getCooling(),  // Updated to cooling
                processStages.getPeeling(),
                processStages.getSweating(),
                processStages.getSorting(),
                processStages.getPackaging()
        );
    }
}
