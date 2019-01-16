package com.hk3t.repository;

import com.hk3t.model.Blog;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BlogRepository extends PagingAndSortingRepository <Blog, Long> {
}
