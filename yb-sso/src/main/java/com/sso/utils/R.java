package com.sso.utils;
import java.util.HashMap;
import java.util.Map;

public class R<T> extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;

	public R() {
		put("code", 200);
		put("msg", "操作成功");
	}

	public static R error() {
		return error(1, "操作失败");
	}

	public static R error(String msg) {
		return error(500, msg);
	}

	public static R error(int code, String msg) {
		R r = new R();
		r.put("code", code);
		r.put("msg", msg);
		return r;
	}

	public static R ok(String msg) {
		R r = new R();
		r.put("msg", msg);
		return r;
	}
	public static R ok(Object object,String msg) {
		R r = new R();
		r.put("msg", msg);
		r.put("data",object);
		return r;
	}

	public static R error(Map<String, Object> map,String msg) {
		R r = new R();
		r.put("code",500);
		r.put("msg",msg);
		r.putAll(map);
		return r;
	}

	public static R ok(Map<String, Object> map) {
		R r = new R();
		r.putAll(map);
		return r;
	}

	public static R ok() {
		return new R();
	}

	public static R ok(Object object) {
		R r = new R();
		r.put("data",object);
		r.put("code", 200);
		r.put("msg","操作成功");
		return  r;
	}



	@Override
	public R put(String key, Object value) {
		super.put(key, value);
		return this;
	}
}
