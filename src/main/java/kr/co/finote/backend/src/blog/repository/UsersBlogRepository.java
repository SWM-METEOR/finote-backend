package kr.co.finote.backend.src.blog.repository;

import kr.co.finote.backend.src.blog.domain.UsersBlog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersBlogRepository extends JpaRepository<UsersBlog, Integer> {}
