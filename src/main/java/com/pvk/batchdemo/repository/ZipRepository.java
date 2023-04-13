package com.pvk.batchdemo.repository;

import com.pvk.batchdemo.entity.Uszip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZipRepository extends JpaRepository<Uszip,Integer> {

}
