package com.mjc.school.repository.model;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Tag implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagId;

    @Column(nullable = false, length = 15)
    private String name;

    @ManyToMany(mappedBy = "tagsFromNews")
    private Set<News> newsFromTag = new HashSet<>();

    @Override
    public Long getId() {return tagId;}

    @Override
    public void setId(Long id) {this.tagId = id;}
}
