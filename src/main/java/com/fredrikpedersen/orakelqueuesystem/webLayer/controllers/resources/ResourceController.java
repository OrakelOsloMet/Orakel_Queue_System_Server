package com.fredrikpedersen.orakelqueuesystem.webLayer.controllers.resources;

import com.fredrikpedersen.orakelqueuesystem.serviceLayer.resources.QueueEntityExportService;
import com.fredrikpedersen.orakelqueuesystem.utilities.constants.URLs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author Fredrik Pedersen
 * @since 20/09/2020 at 21:41
 */

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(URLs.RESOURCES_BASE_URL)
public class ResourceController {

    private QueueEntityExportService queueEntityExportService;

    public ResourceController(final QueueEntityExportService queueEntityExportService) {
        this.queueEntityExportService = queueEntityExportService;
    }

    @GetMapping(value = URLs.USER_GUIDE_URL, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getUserGuideAndDocumentation() throws IOException {

        ClassPathResource resource = new ClassPathResource("/documentation/UserGuideAndDocumentation.pdf");

        return ResponseEntity
                .ok()
                .contentLength(resource.contentLength())
                .body(new InputStreamResource(resource.getInputStream()));
    }
    
    @GetMapping(value = URLs.QUEUE_DATA_EXPORT_URL)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Resource> getExportedQueueData() {
        final String filename = "QueueData.csv";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(queueEntityExportService.generateCsvStreamResourceFromEntities());
    }
}
