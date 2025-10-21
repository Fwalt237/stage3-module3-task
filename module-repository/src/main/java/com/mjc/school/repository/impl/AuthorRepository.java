package com.mjc.school.repository.impl;

import com.mjc.school.repository.BaseRepository;
import com.mjc.school.repository.model.Author;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public class AuthorRepository implements BaseRepository<Author,Long> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public List<Author> findAll() {
        try{
            TypedQuery<Author> query = entityManager.createQuery("SELECT a FROM Author a", Author.class);
            query.setHint("org.hibernate.cacheable", Boolean.TRUE);
            return query.getResultList();
        }catch(Exception e){
            throw new RuntimeException("Failed to fetch all authors", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Author> findById(Long id) {
        try{
            return Optional.ofNullable(entityManager.find(Author.class, id));
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch author with id: "+id, e);
        }
    }

    @Override
    @Transactional
    public Author create(Author entity) {
        try{
            entityManager.persist(entity);
            return entity;
        }catch(Exception e){
            throw new RuntimeException("Failed to create author", e);
        }
    }

    @Override
    @Transactional
    public Author update(Author entity) {
        try{
            entityManager.merge(entity);
            return entity;
        }catch(Exception e){
            throw new RuntimeException("Failed to update author", e);
        }
    }

    @Override
    @Transactional
    public boolean deleteById(Long id) {
        try {
            Author author = entityManager.find(Author.class, id);
            if (author != null) {
                entityManager.remove(author);
                return true;
            }
            return false;
        }catch(Exception e){
            throw new RuntimeException("Failed to delete author with id: "+id, e);
        }
    }

    @Transactional(readOnly = true)
    public Author findAuthorByNewsId(Long newsId) {
        String jpql = "SELECT n.author FROM News n WHERE n.newsId = :newsId";
        TypedQuery<Author> query = entityManager.createQuery(jpql, Author.class)
                .setParameter("newsId", newsId)
                .setHint("org.hibernate.cacheable", Boolean.TRUE);
        try{
            return query.getSingleResult();
        }catch(Exception e){
            throw new RuntimeException("Failed to fetch author associated with newsId: "+newsId, e);
        }
    }

}
