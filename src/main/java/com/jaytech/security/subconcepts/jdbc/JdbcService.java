package com.jaytech.security.subconcepts.jdbc;

import com.jaytech.security.subconcepts.model.PocUser;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class JdbcService {

    private final EntityManager entityManager;

    private final JdbcTemplate jdbcTemplate;

    public List<PocUser> findAll() {

        return jdbcTemplate.query(" select * from poc_user ", new BeanPropertyRowMapper<PocUser>(PocUser.class));
    }

    public PocUser findById(long id) {
        return jdbcTemplate.queryForObject(" select * from poc_user where id=? ", new Object[]{id}, new BeanPropertyRowMapper<PocUser>(PocUser.class));

    }

    public long deleteById(long id) {
        return jdbcTemplate.update(" delete from poc_user where id=? ", new Object[]{id});

    }

    public long insertUser(PocUser user) {
        return jdbcTemplate.update(" insert into poc_user (id, name) values (?,?) ", new Object[]{user.getId(), user.getName()});
    }

    public long updateUser(PocUser user) {
        return jdbcTemplate.update(" update poc_user set name =? where id=? ", new Object[]{user.getName()}, user.getId());
    }


    public PocUser findByIdUsingEntityManager(Long id) {
        return entityManager.find(PocUser.class, id);
    }

    public String createPocUserUsingEntityManager() {
        PocUser pocUser1 = new PocUser(0L, "using entity manager persist method to save");
        entityManager.persist(pocUser1);
        entityManager.detach(pocUser1); //now the changes of the pocUSer1 will not be tracked by entity manger
        entityManager.clear(); //another way of not tracking the changes
        pocUser1.setName("changed this and this will be persisted as we have transactional annotation the em object will keep track of the object changed");
        entityManager.flush();// the changes that were done till here will be persisted in the database

        PocUser pocUser2 = new PocUser(0L, "using entity manager persist method to save 1");
        entityManager.persist(pocUser2);
        pocUser2.setName("changed this and this will be persisted as we have transactional annotation the em object will keep track of the object changed 1");
        entityManager.flush();// the changes that were done till here will be persisted in the database

        entityManager.refresh(pocUser1); // refresh will load the entity from the database
        return "data persisted successfully";

    }

    public void findByIdUsingSimpleQuery(){

    }
    public void findByIdUsingTypedQuery(){

    }


}
