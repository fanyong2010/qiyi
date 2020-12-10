package com.offcn.dycommon.response;

import com.offcn.dycommon.enums.ResponseCodeEnume;

/**
 * 应用统一返回结果数据封装类
 * @author Administrator
 * @param <T> 返回结果数据类型
 */
public class AppResponse<T> {

	private Integer code;
	private String msg;

	// 按照之前东易买Result结果类的思路
	// 我们又 在响应结果类 中增加了 数据这个属性
	// 增强后的结果类：可以同时返回状态码、字符串提示、页面需要使用的数据如List<>
	private T data;


	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	/**
	 * 快速响应成功
	 * @param data
	 * @return
	 */
	public static <T> AppResponse<T> ok(T data){
		AppResponse<T> resp = new AppResponse<T>();

		// 操作成功：设置关于成功的一些提示信息
		resp.setCode(ResponseCodeEnume.SUCCESS.getCode());
		resp.setMsg(ResponseCodeEnume.SUCCESS.getMsg());
		resp.setData(data);

		return resp;
	}

	/**
	 * 快速响应失败
	 */
	public static <T> AppResponse<T> fail(T data){
		AppResponse<T> resp = new AppResponse<T>();

		resp.setCode(ResponseCodeEnume.FAIL.getCode());
		resp.setMsg(ResponseCodeEnume.FAIL.getMsg());
		resp.setData(data);

		return resp;
	}

}
