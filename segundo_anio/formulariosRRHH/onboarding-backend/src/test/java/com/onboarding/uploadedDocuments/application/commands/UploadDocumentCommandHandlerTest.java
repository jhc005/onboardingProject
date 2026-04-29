package com.onboarding.uploadedDocuments.application.commands;

import com.onboarding.documentRequirements.domain.model.DocumentsReq;
import com.onboarding.documentRequirements.domain.outport.DocumentsReqRepository;
import com.onboarding.uploadedDocuments.application.dto.UploadedDocsDto;
import com.onboarding.uploadedDocuments.domain.model.UploadedDocs;
import com.onboarding.uploadedDocuments.domain.outport.UploadedDocsRepositoryPort;
import com.onboarding.uploadedDocuments.mapper.UploadedDocsMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.nio.file.Path;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UploadDocumentCommandHandlerTest {

    @Mock
    private UploadedDocsRepositoryPort uploadedDocsPort;

    @Mock
    private DocumentsReqRepository documentsReqPort;

    @Mock
    private UploadedDocsMapper mapper;

    @InjectMocks
    private UploadDocumentCommandHandler handler;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(handler, "storagePath", tempDir.toString());
    }


    @Test
    void handle_withValidFile_savesDocumentWithPendingStatus() throws Exception {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("[DNI] Garcia Lopez, Juan.pdf");
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[]{1, 2, 3}));
        when(file.getContentType()).thenReturn("application/pdf");
        when(file.getSize()).thenReturn(2048L);

        when(documentsReqPort.findById(1L)).thenReturn(Optional.of(requirement("PDF, DOCX")));
        when(uploadedDocsPort.findByOnboardingProcessIdAndDocumentRequirementId(any(), any()))
                .thenReturn(Optional.empty());

        UploadedDocs saved = new UploadedDocs();
        saved.setId(10L);
        when(uploadedDocsPort.save(any())).thenReturn(saved);

        UploadedDocsDto.Response expectedResponse = UploadedDocsDto.Response.builder()
                .id(10L)
                .validationStatus("PENDING_VALIDATION")
                .build();
        when(mapper.toResponse(saved)).thenReturn(expectedResponse);

        UploadedDocsDto.Response result =
                handler.handle(new UploadDocumentCommand("proceso-uuid-abc", 1L, file));

        assertThat(result.getId()).isEqualTo(10L);
        assertThat(result.getValidationStatus()).isEqualTo("PENDING_VALIDATION");
    }

    @Test
    void handle_withDisallowedExtension_throwsIllegalArgument() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("[DNI] Garcia Lopez, Juan.exe");

        when(documentsReqPort.findById(1L)).thenReturn(Optional.of(requirement("PDF, DOCX")));

        assertThatThrownBy(() -> handler.handle(new UploadDocumentCommand("proceso-uuid-abc", 1L, file)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Formato no permitido");
    }

    @Test
    void handle_withInvalidFilenameFormat_throwsIllegalArgument() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("mi_dni_escaneado.pdf");

        when(documentsReqPort.findById(1L)).thenReturn(Optional.of(requirement("PDF")));

        assertThatThrownBy(() -> handler.handle(new UploadDocumentCommand("proceso-uuid-abc", 1L, file)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("formato");
    }

    @Test
    void handle_withBlankFilename_throwsIllegalArgument() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("");

        when(documentsReqPort.findById(1L)).thenReturn(Optional.of(requirement("PDF")));

        assertThatThrownBy(() -> handler.handle(new UploadDocumentCommand("proceso-uuid-abc", 1L, file)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El nombre del archivo no puede estar vacío.");
    }

    @Test
    void handle_withNonExistentRequirement_throwsIllegalArgument() {
        MultipartFile file = mock(MultipartFile.class);

        when(documentsReqPort.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> handler.handle(new UploadDocumentCommand("proceso-uuid-abc", 99L, file)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Requisito de documento no encontrado.");
    }

    private DocumentsReq requirement(String allowedFormats) {
        DocumentsReq req = new DocumentsReq();
        req.setId(1L);
        req.setName("DNI");
        req.setAllowedFormats(allowedFormats);
        return req;
    }
}
