package org.example.basededatosjpa.demo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.basededatosjpa.model.Card;
import org.example.basededatosjpa.model.CardType;
import org.example.basededatosjpa.model.Course;
import org.example.basededatosjpa.model.Student;

public final class JpaDemo {
    private JpaDemo() {
    }

    public static void main(String[] args) {
        try (EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
             EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            Student firstStudent = new Student("Harry Potter", "12345678A", "600111222");
            Card firstCard = new Card("CARD-001", CardType.ESO);
            firstStudent.setCard(firstCard);

            Student secondStudent = new Student("Hermione Granger", "87654321B", "600333444");
            Card secondCard = new Card("CARD-002", CardType.BAC);
            secondStudent.setCard(secondCard);

            Course course = new Course("Defensa contra las artes oscuras");
            firstStudent.addCourse(course);
            secondStudent.addCourse(course);

            entityManager.getTransaction().begin();
            entityManager.persist(course);
            entityManager.persist(firstStudent);
            entityManager.persist(secondStudent);
            entityManager.getTransaction().commit();

            Long studentId = firstStudent.getId();
            Long cardId = firstCard.getId();
            Long courseId = course.getId();
            System.out.println("Guardados: " + firstStudent + ", " + firstCard + ", " + course);

            entityManager.getTransaction().begin();
            entityManager.remove(entityManager.find(Student.class, studentId));
            entityManager.getTransaction().commit();

            System.out.println("Tarjeta borrada junto al estudiante: "
                    + (entityManager.find(Card.class, cardId) == null));
            System.out.println("Curso compartido conservado: "
                    + (entityManager.find(Course.class, courseId) != null));
        }
    }
}
