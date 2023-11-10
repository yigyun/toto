package com.main.toto.repository;

import com.main.toto.domain.member.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String>{

    @EntityGraph(attributePaths = "roleSet")
    Optional<Member> findByEmail(String email);

    @EntityGraph(attributePaths = "roleSet")
    @Query("select m from Member m where m.mid = :mid and m.social = false")
    Optional<Member> getWithRoles(@Param("mid") String mid);

    @Modifying
    @Transactional
    @Query("update Member m set m.mpassword =:mpassword where m.mid = :mid ")
    void updatePassword(@Param("mpassword") String password, @Param("mid") String mid);
}
