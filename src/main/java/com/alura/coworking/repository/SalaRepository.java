package com.alura.coworking.repository;

import com.alura.coworking.domain.Sala;
import org.springframework.data.jpa.repository.JpaRepository;



public interface SalaRepository extends JpaRepository<Sala, String> {
}
