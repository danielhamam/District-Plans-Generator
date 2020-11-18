package com.cse416.backend.dao.repositories;

import com.cse416.backend.model.regions.Precinct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.*;
import java.lang.Integer;

@Repository
public interface PrecinctRepository extends JpaRepository<Precinct, Integer>{}