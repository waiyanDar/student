package com.example.student.register.dao;

import com.example.student.register.entity.Role;
import com.example.student.register.entity.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;

public class SpecificationUtil {
	public static Specification<Role> withUserId(Optional<String> userId) {
//		System.out.println("userid....." + userId);
		if (userId.filter(StringUtils::hasLength).isPresent()) {
			return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("userId")),
					userId.get().toLowerCase().concat("%"));
		}
		return Specification.where(null);
	}

	public static Specification<User> witWord(Optional<String> word) {
		if (word.filter(StringUtils::hasLength).isPresent()) {
			return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("username")),
					word.get().toLowerCase().concat("%"));
		}
		return Specification.where(null);
	}

	public static Specification<User> withSearchTerm(String searchTerm) {
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

}
