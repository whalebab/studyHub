package com.mkc.studyHub.domain.verification.repository;

import com.mkc.studyHub.domain.verification.vo.AuthToken;
import org.springframework.data.repository.CrudRepository;

public interface AuthTokenRepository extends CrudRepository<AuthToken, String> {
}
