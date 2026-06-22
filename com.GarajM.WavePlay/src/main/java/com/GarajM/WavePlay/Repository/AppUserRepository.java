package com.GarajM.WavePlay.Repository;

import com.GarajM.WavePlay.entity.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Boolean existsByEmail(String email);

    Optional<AppUser> findByEmail(String email);

    Optional<AppUser> findByRefreshToken(String refreshToken);

    Page<AppUser> findAll(Pageable pageable);
}
