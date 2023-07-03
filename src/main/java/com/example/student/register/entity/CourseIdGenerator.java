package com.example.student.register.entity;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class CourseIdGenerator implements IdentifierGenerator {

	private static int lastCourseId =0;

	@Override
	public Serializable generate(SharedSessionContractImplementor arg0, Object arg1) throws HibernateException {
		String prefix= "COU";
		String formattedId= String.format("%03d", lastCourseId + 1);
		lastCourseId ++;
		return prefix + formattedId;
	}
}
