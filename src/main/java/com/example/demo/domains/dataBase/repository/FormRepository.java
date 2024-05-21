package com.example.demo.domains.dataBase.repository;

import com.example.demo.domains.dataBase.model.Form;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormRepository extends JpaRepository<Form, Long> {
}
