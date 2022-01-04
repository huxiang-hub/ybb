package com.vim.chatapi.utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperUtil {

	private static final ObjectMapper mapper = new ObjectMapper();
	/**
	 * 把对象转换为JSON串
	 * @param target
	 * @return
	 */
	public static String toJSON(Object target) {
		String result = null;
		try {
			result = mapper.writeValueAsString(target);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}
	/**
	 * 把JSON串转换为对象
	 * @param <T>
	 * @param json
	 * @param targetClass
	 * @return
	 */
	public static <T>T toObject(String json, Class<T> targetClass) {
		T object = null;
		try {
			object = mapper.readValue(json, targetClass);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return object;
	}
}
