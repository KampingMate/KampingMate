package com.demo.domain;

import java.util.Date;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Table(name = "SearchHistory", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"contentId", "no_data"})
})
public class SearchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "searchHistory_generator")
    @SequenceGenerator(name="searchHistory_generator", sequenceName = "HISTORY_SEQ", allocationSize = 1)
    private int history_id;

    @ManyToOne
    @JoinColumn(name = "contentId")
    private GoCamping gocamping;

    @ManyToOne
    @JoinColumn(name = "no_data")
    private MemberData member;
    
    @ColumnDefault("sysdate")
    private Date recommendDate;
}
