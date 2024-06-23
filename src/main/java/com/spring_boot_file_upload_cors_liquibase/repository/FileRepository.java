package com.spring_boot_file_upload_cors_liquibase.repository;

import com.spring_boot_file_upload_cors_liquibase.model.FileModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileModel, Long> {
}