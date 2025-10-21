package com.mjc.school.repository.impl;

import com.mjc.school.repository.BaseRepository;
import com.mjc.school.repository.model.Author;
import com.mjc.school.repository.model.News;
import com.mjc.school.repository.model.Tag;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class NewsRepository implements BaseRepository<News,Long> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public List<News> findAll() {
        try{
            TypedQuery<News> query = entityManager.createQuery("SELECT n FROM News n", News.class);
            query.setHint("org.hibernate.cacheable", Boolean.TRUE);
            return query.getResultList();
        }catch(Exception e){
            throw new RuntimeException("Failed to fetch all News", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<News> findById(Long id) {
        try{
            return Optional.ofNullable(entityManager.find(News.class, id));
        }catch(Exception e){
            throw new RuntimeException("Failed to fetch News with id: "+id, e);
        }
    }

    @Override
    @Transactional
    public News create(News entity) {
        try{
            entityManager.persist(entity);
            return entity;
        }catch(Exception e){
            throw new RuntimeException("Failed to create News", e);
        }
    }

    @Override
    @Transactional
    public News update(News entity) {
        try{
            entityManager.merge(entity);
            return entity;
        }catch(Exception e){
            throw new RuntimeException("Failed to update News", e);
        }
    }

    @Override
    @Transactional
    public boolean deleteById(Long id) {
        try{
            News news = entityManager.find(News.class, id);
            if(news != null){
                entityManager.remove(news);
                return true;
            }
            return false;
        }catch(Exception e){
            throw new RuntimeException("Failed to delete News with id: "+id, e);
        }
    }

    @Transactional(readOnly = true)
    public List<News> findNewsByParams(List<String> tagNames, List<Long> tagIds,
                                       String authorName, String title, String content) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<News> cq = cb.createQuery(News.class);
        Root<News> root = cq.from(News.class);
        Join<News, Author> authorJoin = root.join("author", JoinType.LEFT);
        Join<News, Tag> tagJoin = root.join("tagsFromNews", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        if(tagNames != null && !tagNames.isEmpty()){
            predicates.add(tagJoin.get("name").in(tagNames));
        }

        if(tagIds != null && !tagIds.isEmpty()){
            predicates.add(tagJoin.get("tagId").in(tagIds));
        }

        if(StringUtils.hasText(authorName)){
            predicates.add(cb.like(cb.lower(authorJoin.get("name")), "%"+authorName.toLowerCase()+"%"));
        }

        if(StringUtils.hasText(title)){
            predicates.add(cb.like(cb.lower(root.get("title")), "%"+title.toLowerCase()+"%"));
        }

        if(StringUtils.hasText(content)){
            predicates.add(cb.like(cb.lower(root.get("content")), "%"+content.toLowerCase()+"%"));
        }

        if(!predicates.isEmpty()){
            cq.where(cb.or(predicates.toArray(new Predicate[0])));
        }

        cq.select(root).distinct(true);

        TypedQuery<News> query = entityManager.createQuery(cq).setHint("org.hibernate.cacheable", Boolean.TRUE);
        return query.getResultList();
    }

}
