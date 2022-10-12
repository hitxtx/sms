package com.example.ms.module.system.repository;

import com.example.ms.module.system.model.bo.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long> {

}
