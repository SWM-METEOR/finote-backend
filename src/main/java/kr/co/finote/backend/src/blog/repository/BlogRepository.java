package kr.co.finote.backend.src.blog.repository;

import kr.co.finote.backend.src.blog.domain.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Blog, Integer> {

}
