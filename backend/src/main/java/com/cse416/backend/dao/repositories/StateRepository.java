package com.cse416.backend.dao.repositories;

import com.cse416.backend.model.regions.state.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.*;
import java.lang.Integer;

@Repository
public interface StateRepository extends JpaRepository<State, String>{}