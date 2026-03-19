package com.onboarding.documentRequirements.domain.outport;

import com.onboarding.documentRequirements.domain.model.DocumentsReq;

import java.util.List;
import java.util.Optional;

public interface DocumentsReqRepository {

    DocumentsReq save(DocumentsReq requirement);
    Optional<DocumentsReq> findById(Long id);
    List<DocumentsReq> findAll();
    List<DocumentsReq> findAllOrderedByDisplayOrder();
    List<DocumentsReq> findByIsRequired(Boolean isRequired);
    void deleteById(Long id);
}
