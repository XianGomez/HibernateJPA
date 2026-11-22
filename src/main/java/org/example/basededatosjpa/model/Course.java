package org.example.basededatosjpa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcourse")
    private Long id;

    @Column(name = "description", length = 100)
    private String description;

    @Column(name = "begindate", nullable = false)
    private LocalDate beginDate = LocalDate.now();

    @Column(name = "enddate")
    private LocalDate endDate;

    @ManyToMany(mappedBy = "courses")
    private Set<Student> students = new HashSet<>();

    protected Course() {
    }

    public Course(String description) {
        setDescription(description);
    }

    public Course(String description, LocalDate beginDate, LocalDate endDate) {
        this(description);
        this.beginDate = Objects.requireNonNull(beginDate, "beginDate");
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description != null && description.length() > 100) {
            throw new IllegalArgumentException("La descripción no puede superar los 100 caracteres");
        }
        this.description = description;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = Objects.requireNonNull(beginDate, "beginDate");
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Set<Student> getStudents() {
        return students;
    }

    void addStudentInternal(Student student) {
        students.add(student);
    }

    void removeStudentInternal(Student student) {
        students.remove(student);
    }

    @Override
    public String toString() {
        return "Course{id=" + id + ", description='" + description + "', beginDate=" + beginDate
                + ", endDate=" + endDate + "}";
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Course)) {
            return false;
        }
        Course course = (Course) other;
        return id != null && id.equals(course.id);
    }

    @Override
    public int hashCode() {
        return Course.class.hashCode();
    }
}
