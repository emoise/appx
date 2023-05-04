package com.principal33.appx.repostitory;

import com.principal33.appx.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {}
