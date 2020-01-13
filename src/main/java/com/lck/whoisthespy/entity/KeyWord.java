package com.lck.whoisthespy.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "key_word")
public class KeyWord {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "ordinary_word")
    private String ordinaryWord;

    @Column(name = "spy_word")
    private String spyWord;

}
