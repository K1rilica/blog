package com.example.blog.repository;

import com.example.blog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByTitleContainingIgnoreCase(String query, Pageable pageable);
}
