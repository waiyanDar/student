package com.example.student.register.dao;

import com.example.student.register.entity.Student;
import com.example.student.register.entity.User;
import com.example.student.register.entity.Course;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;

public class SpecificationUtil {
//	public static Specification<Role> withUserId(Optional<String> userId) {
//		if (userId.filter(StringUtils::hasLength).isPresent()) {
//			return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("userId")),
//					userId.get().toLowerCase().concat("%"));
//		}
//		return Specification.where(null);
//	}
//
//	public static Specification<User> witWord(Optional<String> word) {
//		if (word.filter(StringUtils::hasLength).isPresent()) {
//			return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("username")),
//					word.get().toLowerCase().concat("%"));
//		}
//		return Specification.where(null);
//	}

	public static Specification<User> userWithSearchTerm(String searchTerm) {
		if (!searchTerm.isEmpty()) {
			return (root, query, criteriaBuilder) -> {
				String likeSearchTerm = "%" +searchTerm.toLowerCase() + "%";

				return criteriaBuilder.or(
						criteriaBuilder.like(criteriaBuilder.lower(root.get("userId")), likeSearchTerm),
						criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), likeSearchTerm)
				);
			};
		}
		return Specification.where(null);
	}
	
	public static Specification<Student> studentWithSearchTerm(String searchTerm){
		if(!searchTerm.isEmpty()) {
			return (root, query, criteriaBuilder) -> {
				String likeSearchTerm = "%" + searchTerm.toLowerCase() + "%";
				
//				Join<Student, Course> studentCourse = root.join("course");
				Join<Student, Course> courseJoin = root.join("courses");
				
				return criteriaBuilder.or(
						criteriaBuilder.like(criteriaBuilder.lower(root.get("studentId")), likeSearchTerm),
						criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likeSearchTerm),
						criteriaBuilder.like(criteriaBuilder.lower(courseJoin.get("name")), likeSearchTerm)
						);
			};
			
		}
		return Specification.where(null);
	}

}
