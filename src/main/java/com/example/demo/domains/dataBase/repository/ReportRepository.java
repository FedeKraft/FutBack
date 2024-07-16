package com.example.demo.domains.dataBase.repository;

import com.example.demo.domains.dataBase.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ReportRepository extends JpaRepository<Report, Long> {

}
