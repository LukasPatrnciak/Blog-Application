package sk.lukaspatrnciak.blogapplication.repositories;

import sk.lukaspatrnciak.blogapplication.models.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
