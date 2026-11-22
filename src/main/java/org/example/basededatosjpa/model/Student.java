package org.example.basededatosjpa.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_sequence_generator")
    @SequenceGenerator(name = "student_sequence_generator", sequenceName = "student_sequence", allocationSize = 1)
    @Column(name = "idstudent")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "identification")
    private String dni;

    @Column(name = "phone")
    private String phone;

    @ManyToMany
    @JoinTable(
            name = "student_courses",
            joinColumns = @JoinColumn(name = "idstudent"),
            inverseJoinColumns = @JoinColumn(name = "idcourse")
    )
    private Set<Course> courses = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "idcard", unique = true)
    private Card card;

    protected Student() {
    }

    public Student(String name, String dni, String phone) {
        this.name = name;
        this.dni = dni;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void addCourse(Course course) {
        if (courses.add(Objects.requireNonNull(course, "course"))) {
            course.addStudentInternal(this);
        }
    }

    public void removeCourse(Course course) {
        if (courses.remove(course)) {
            course.removeStudentInternal(this);
        }
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        if (this.card == card) {
            return;
        }
        if (this.card != null) {
            this.card.setStudentInternal(null);
        }
        this.card = card;
        if (card != null) {
            Student previousStudent = card.getStudent();
            if (previousStudent != null && previousStudent != this) {
                previousStudent.setCard(null);
            }
            card.setStudentInternal(this);
        }
    }

    @Override
    public String toString() {
        return "Student{id=" + id + ", name='" + name + "', dni='" + dni + "', phone='" + phone + "'}";
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Student)) {
            return false;
        }
        Student student = (Student) other;
        return id != null && id.equals(student.id);
    }

    @Override
    public int hashCode() {
        return Student.class.hashCode();
    }
}
