package com.mjc.school.repository.model;

import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@Cache(usage= CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Author implements BaseEntity<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authorId;

    @Column(nullable = false, length = 15)
    private String name;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createDate;

    @Column(nullable = false)
    @LastModifiedDate
    private LocalDateTime lastUpdateTime;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    @Cache(usage= CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<News> newsFromAuthor = new ArrayList<>();

    @Override
    public Long getId() {return authorId;}

    @Override
    public void setId(Long id) {this.authorId = id;}
}
