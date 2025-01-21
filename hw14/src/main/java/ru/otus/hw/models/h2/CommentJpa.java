package ru.otus.hw.models.h2;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"book"})
@ToString(exclude = {"book"})
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class CommentJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private BookJpa book;

    @Column(name = "text")
    private String text;

    @Column(name = "comment_date")
    private LocalDate commentDate;

    @Column(name = "author")
    private String author;

    @PrePersist
    private void prePersist() {
        commentDate = LocalDate.now();
    }

}
