package netSec.utils;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import netSec.boundaries.UserBoundary;
import netSec.data.UserEntity;
import netSec.logic.BadRequestException;

@Component
public class Converter {
	private ObjectMapper jackson;

	public Converter() {
		this.jackson = new ObjectMapper();
	}

	public UserEntity toEntity(UserBoundary boundary) {
		UserEntity entity = new UserEntity();

		entity.setId(boundary.getId());

		if (boundary.getUserName() == null || boundary.getUserName().isBlank()) {
			throw new BadRequestException("Username cannot be null");
		} else {
			entity.setUserName(boundary.getUserName());
		}

		if (boundary.getPassword() == null || boundary.getPassword().isBlank()) {
			throw new BadRequestException("Password cannot be null");
		} else {
			entity.setPassword(boundary.getPassword());
		}
		if (boundary.getEmail() == null || boundary.getEmail().isBlank()) {
			throw new BadRequestException("Email is not in valid format");
		} else {
			entity.setEmail(boundary.getEmail());
		}

		return entity;
	}

	public String toEntity(Map<String, Object> data) {
		try {
			return this.jackson.writeValueAsString(data);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public UserBoundary toBoundary(UserEntity entity) {
		UserBoundary boundary = new UserBoundary();

		boundary.setId(entity.getId());
		boundary.setUserName(entity.getUserName());
		boundary.setPassword(entity.getPassword());
		boundary.setEmail(entity.getEmail());
		return boundary;
	}
}
