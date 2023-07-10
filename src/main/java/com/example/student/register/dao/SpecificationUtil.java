package com.example.student.register.dao;

import com.example.student.register.entity.Course;
import com.example.student.register.entity.Student;
import com.example.student.register.entity.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.Optional;

import javax.persistence.criteria.Join;

public class SpecificationUtil {
	public static Specification<User> withUserId(Optional<String> userId) {

		if (userId.filter(StringUtils::hasLength).isPresent()) {
			return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("userId")),
					userId.get().toLowerCase().concat("%"));
		}
		return Specification.where(null);
	}

	public static Specification<User> withName(Optional<String> name) {
		if (name.filter(StringUtils::hasLength).isPresent()) {
			return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("username")),
					name.get().toLowerCase().concat("%"));
		}
		return Specification.where(null);
	}

	public static Specification<Student> withStudentId(Optional<String> studentId) {
		if (studentId.filter(StringUtils::hasLength).isPresent()) {
			return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("studentId")),
					studentId.get().toLowerCase().concat("%"));
		}
		return Specification.where(null);
	}

	public static Specification<Student> withStudentName(Optional<String> name) {
		if (name.filter(StringUtils::hasLength).isPresent()) {
			return (root, query, cb) -> cb.like(cb.lower(root.get("name")), name.get().toLowerCase().concat("%"));
		}

		return Specification.where(null);
	}

	public static Specification<Student> withCourse(Optional<String> course) {
		if (course.filter(StringUtils::hasLength).isPresent()) {
//			return (root, query, cb) -> cb.like(cb.lower(root.get("courses.name")), course.get().toLowerCase().concat("%"));
			return (root, query, cb) -> {
				Join<Student, Course> courseJoin = root.join("courses");
				return cb.like(cb.lower(courseJoin.get("name")), course.get().toLowerCase().concat("%"));
			};
		}

		return Specification.where(null);
	}
}
