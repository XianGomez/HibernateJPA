package org.example.basededatosjpa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "card_sequence_generator")
    @SequenceGenerator(name = "card_sequence_generator", sequenceName = "card_sequence", allocationSize = 1)
    @Column(name = "idcard")
    private Long id;

    @Column(name = "code")
    private String code;

    @Transient
    private CardType type;

    @OneToOne(mappedBy = "card")
    private Student student;

    protected Card() {
    }

    public Card(String code, CardType type) {
        this.code = code;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public Student getStudent() {
        return student;
    }

    void setStudentInternal(Student student) {
        this.student = student;
    }

    @Override
    public String toString() {
        return "Card{id=" + id + ", code='" + code + "', type=" + type + "}";
    }
}
