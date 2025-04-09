package com.quangcd.cinemaproject.repository;

import com.quangcd.cinemaproject.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query("""
            select m from Movie m where m.isDeleted=false and 
            (:title is null or lower(m.title) like concat('%',lower(:title),'%' ))
            and (:mainGenre is null or lower(m.mainGenre) like concat('%',lower(:mainGenre),'%' ))
            """)
    Page<Movie> searchByTitleAndGenreByIsDeleted(@Param("title") String title, @Param("mainGenre") String mainGenre, boolean deleted, Pageable pageable);

    Optional<Movie> findByIdAndIsDeleted(Long id, boolean isDeleted);

    Page<Movie> searchByCriteria(int pageNo, int pageSize, String sortBy, String[] search, Pageable pageable);
}
