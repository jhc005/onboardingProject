-- ============================================
-- BASE DE DATOS FORMS_DB – FORMULARIO DINÁMICO
-- ============================================
CREATE DATABASE IF NOT EXISTS forms_db;
USE forms_db;

-- ============================================
-- 1. ADMINISTRADORES (RRHH)
-- ============================================
CREATE TABLE admin_users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(150) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- 2. CANDIDATOS
-- ============================================
CREATE TABLE candidates (
    id CHAR(36) PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- 3. PROCESOS DE ONBOARDING
-- ============================================
CREATE TABLE onboarding_processes (
    id CHAR(36) PRIMARY KEY,
    candidate_id CHAR(36) NOT NULL,
    status ENUM('PENDING_ACCESS','IN_PROGRESS','SUBMITTED','VALIDATED','REJECTED') DEFAULT 'PENDING_ACCESS',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    submitted_at TIMESTAMP NULL,
    validated_at TIMESTAMP NULL,
    validated_by BIGINT NULL,
    FOREIGN KEY (candidate_id) REFERENCES candidates(id),
    FOREIGN KEY (validated_by) REFERENCES admin_users(id)
);

-- ============================================
-- 4. CAMPOS DINÁMICOS DEL FORMULARIO
-- ============================================
CREATE TABLE data_fields (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    label VARCHAR(255) NOT NULL,
    field_type ENUM('TEXT','NUMBER','SELECT','TEXTAREA') NOT NULL,
    validation_regex VARCHAR(255),
    min_length INT NULL,
    max_length INT NULL,
    placeholder VARCHAR(255),
    is_required BOOLEAN DEFAULT TRUE,
    display_order INT NOT NULL
);

CREATE TABLE field_options (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    data_field_id BIGINT NOT NULL,
    option_label VARCHAR(100) NOT NULL,
    option_value VARCHAR(100) NOT NULL,
    display_order INT NOT NULL,
    FOREIGN KEY (data_field_id) REFERENCES data_fields(id) ON DELETE CASCADE
);

CREATE TABLE data_answers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    onboarding_process_id CHAR(36) NOT NULL,
    data_field_id BIGINT NOT NULL,
    answer_value TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (onboarding_process_id) REFERENCES onboarding_processes(id),
    FOREIGN KEY (data_field_id) REFERENCES data_fields(id)
    );

-- ============================================
-- 5. DOCUMENTOS REQUERIDOS
-- ============================================
CREATE TABLE document_requirements (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    allowed_formats VARCHAR(255) NOT NULL,
    is_required BOOLEAN DEFAULT TRUE,
    display_order INT NOT NULL,
    template_file_path VARCHAR(512),
    template_original_filename VARCHAR(255)
);

CREATE TABLE uploaded_documents (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    onboarding_process_id CHAR(36) NOT NULL,
    document_requirement_id BIGINT NOT NULL,
    file_path VARCHAR(512) NOT NULL,
    original_filename VARCHAR(255) NOT NULL,
    mime_type VARCHAR(100) NOT NULL,
    file_size_bytes BIGINT NOT NULL,
    validation_status ENUM('PENDING_VALIDATION','APPROVED','REJECTED') DEFAULT 'PENDING_VALIDATION',
    validation_notes TEXT,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (onboarding_process_id) REFERENCES onboarding_processes(id),
    FOREIGN KEY (document_requirement_id) REFERENCES document_requirements(id)
);

-- ============================================
-- 6. TOKENS DE ACCESO (para candidatos sin contraseña)
-- ============================================
CREATE TABLE access_tokens (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    onboarding_process_id CHAR(36) NOT NULL,
    token_hash VARCHAR(255) UNIQUE NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    used_at TIMESTAMP NULL,
    is_used BOOLEAN DEFAULT FALSE,
    attempts INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (onboarding_process_id) REFERENCES onboarding_processes(id) ON DELETE CASCADE
);

-- ============================================
-- 7. NOTIFICACIONES
-- ============================================
CREATE TABLE notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    onboarding_process_id CHAR(36) NOT NULL,
    type ENUM('ACCESS_LINK','FORM_SUBMITTED','VALIDATED','REJECTED','DOCUMENT_REJECTED') NOT NULL,
    recipient_email VARCHAR(255) NOT NULL,
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('SENT','FAILED') DEFAULT 'SENT',
    error_message VARCHAR(500) NULL,
    FOREIGN KEY (onboarding_process_id) REFERENCES onboarding_processes(id)
);