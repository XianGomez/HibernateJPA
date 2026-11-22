package org.example.basededatosjpa.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StudentCourseCardTest {
    @Test
    void persistsAssociationsAndDeletesOnlyTheStudentCard() {
        try (EntityManagerFactory factory = Persistence.createEntityManagerFactory("default");
             EntityManager entityManager = factory.createEntityManager()) {
            Course course = new Course("Pociones");
            Student first = new Student("Harry Potter", "12345678A", "600111222");
            Student second = new Student("Hermione Granger", "87654321B", "600333444");
            Card firstCard = new Card("CARD-001", CardType.ESO);
            Card secondCard = new Card("CARD-002", CardType.BAC);
            first.setCard(firstCard);
            second.setCard(secondCard);
            first.addCourse(course);
            second.addCourse(course);

            entityManager.getTransaction().begin();
            entityManager.persist(course);
            entityManager.persist(first);
            entityManager.persist(second);
            entityManager.getTransaction().commit();

            Long firstId = first.getId();
            Long firstCardId = firstCard.getId();
            Long secondId = second.getId();
            Long secondCardId = secondCard.getId();
            Long courseId = course.getId();

            entityManager.clear();
            Student persistedFirst = entityManager.find(Student.class, firstId);
            assertNotNull(persistedFirst.getCard());
            assertEquals(1, persistedFirst.getCourses().size());

            entityManager.getTransaction().begin();
            entityManager.remove(persistedFirst);
            entityManager.getTransaction().commit();
            entityManager.clear();

            assertNull(entityManager.find(Student.class, firstId));
            assertNull(entityManager.find(Card.class, firstCardId));
            assertNotNull(entityManager.find(Student.class, secondId));
            assertNotNull(entityManager.find(Card.class, secondCardId));
            assertNull(entityManager.find(Card.class, secondCardId).getType());
            assertNotNull(entityManager.find(Course.class, courseId));
            assertEquals(1, entityManager.find(Course.class, courseId).getStudents().size());
        }
    }

    @Test
    void appliesCourseDefaultsAndDescriptionLimit() {
        Course course = new Course("Runas");

        assertEquals(java.time.LocalDate.now(), course.getBeginDate());
        assertNull(course.getEndDate());
        assertThrows(IllegalArgumentException.class, () -> course.setDescription(repeat('x', 101)));

        course.setDescription(repeat('x', 100));
        assertEquals(100, course.getDescription().length());
    }

    @Test
    void transientStudentsAreNotEqualAndCardTypeIsNotPersisted() {
        Student first = new Student("Harry", "12345678A", "600111222");
        Student second = new Student("Harry", "12345678A", "600111222");
        assertFalse(first.equals(second));

        Card card = new Card("CARD-001", CardType.FP);
        assertEquals("fp", card.getType().toString());
    }

    private String repeat(char value, int length) {
        char[] chars = new char[length];
        java.util.Arrays.fill(chars, value);
        return new String(chars);
    }
}
