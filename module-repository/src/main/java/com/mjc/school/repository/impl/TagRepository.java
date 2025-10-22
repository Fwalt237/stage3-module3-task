package com.mjc.school.repository.impl;

import com.mjc.school.repository.BaseRepository;
import com.mjc.school.repository.model.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class TagRepository implements BaseRepository<Tag,Long> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Tag> readAll() {
        try{
            TypedQuery<Tag> query = entityManager.createQuery("SELECT t FROM Tag t", Tag.class);
            query.setHint("org.hibernate.cacheable", Boolean.TRUE);
            return query.getResultList();
        }catch(Exception e){
            throw new RuntimeException("Failed to fetch all tags", e);
        }
    }

    @Override
    public Optional<Tag> readById(Long id) {
        try{
            return Optional.ofNullable(entityManager.find(Tag.class, id));
        }catch(Exception e){
            throw new RuntimeException("Failed to fetch tag with id: "+id, e);
        }
    }

    @Override
    public Tag create(Tag entity) {
        try{
            entityManager.persist(entity);
            return entity;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create new tag", e);
        }
    }

    @Override
    public Tag update(Tag entity) {
        try{
            entityManager.merge(entity);
            return entity;
        }catch(Exception e){
            throw new RuntimeException("Failed to update tag", e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        try{
            Tag tag = entityManager.find(Tag.class, id);
            if(tag != null){
                entityManager.remove(tag);
                return true;
            }
            return false;
        }catch(Exception e){
            throw new RuntimeException("Failed to delete tag with id: "+id, e);
        }
    }

    public List<Tag> findTagsByNewsId(Long newsId) {
        String jpql = "SELECT t FROM News n JOIN n.tagsFromNews t WHERE n.newsId = :newsId";
        TypedQuery<Tag> query = entityManager.createQuery(jpql, Tag.class)
                .setParameter("newsId", newsId)
                .setHint("org.hibernate.cacheable", Boolean.TRUE);
        try{
            return query.getResultList();
        }catch(Exception e){
            throw new RuntimeException("Failed to fetch tags associated with newsId: "+newsId, e);
        }
    }
}
