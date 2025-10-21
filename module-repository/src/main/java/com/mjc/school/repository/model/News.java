package com.mjc.school.repository.model;

import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@Cache(usage= CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class News implements BaseEntity<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long newsId;

    @Column(nullable = false, length = 30)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createDate;

    @Column(nullable = false)
    @LastModifiedDate
    private LocalDateTime lastUpdateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="author_id")
    private Author author;

    @ManyToMany
    @JoinTable(name="NEWS_TAG",
    joinColumns = @JoinColumn(name="NEWS_ID"),
    inverseJoinColumns = @JoinColumn(name="TAG_ID"))
    @Cache(usage= CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Tag> tagsFromNews = new HashSet<>();

    @Override
    public Long getId() {return newsId;}

    @Override
    public void setId(Long id) {this.newsId = id;}
}
