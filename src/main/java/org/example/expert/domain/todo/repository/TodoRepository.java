package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    // N+1 문제의 가장 일반적인 해결 방법은 fetch join
    // fetch join은 기본 설정을 lazy로 걸고 inner join 수행. left를 붙여 outer join을 수행

    // EntityGraph는 Spring Data JPA에서 제공하는 것으로 연관된 엔티티들을 SQL 한번에 조회하는 방법
    // EntityGraph는 기본 설정을 eager로 변환하고 left outer join으로 연관된 데이터를 끌어온다.
    @EntityGraph(attributePaths = "user")

    Page<Todo> findAllByOrderByModifiedAtDesc(Pageable pageable);

    @Query("SELECT t FROM Todo t " +
            "LEFT JOIN FETCH t.user " +
            "WHERE t.id = :todoId")
    Optional<Todo> findByIdWithUser(@Param("todoId") Long todoId);

    int countById(Long todoId);
}
