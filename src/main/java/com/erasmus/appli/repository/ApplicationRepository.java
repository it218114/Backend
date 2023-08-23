package com.erasmus.appli.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erasmus.appli.entity.Application;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

	List<Application> findAllByUserIdAndStatusAndEnabled(long userId, String string, String enabled);
	
	List<Application> findAllByStatusAndEnabled(String string, String enabled);
	
	List<Application> findAllByStatusNotAndEnabled(String string, String enabled);
	
	List<Application> findAllByUserIdAndEnabledAndStatusNot(long userId, String enabled, String string);
	
	List<Application> findAllByUserId(long userId);

	List<Application> findAllByUserEnabledAndEnabled(String string, String enabled);

	Optional<Application> findByIdAndEnabled(long appId, String string);

	
}
