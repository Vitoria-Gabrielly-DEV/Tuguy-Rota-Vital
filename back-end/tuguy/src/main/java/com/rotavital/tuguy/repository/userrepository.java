package com.rotavital.tuguy.repository;

import com.rotavital.tuguy.model.user;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository 
public interface userrepository extends JpaRepository<user, Long>{

}
