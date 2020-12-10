package com.offcn.dycommon.enums;

/**
 * 项目状态
 */
public enum ProjectStatusEnume {

	STARTING("0","即将开始"),
	FUNDING("1", "众筹中"),
	SUCCESS("2","众筹成功"),
	FAIL("3","众筹失败");
	
	private String status;
	private String showName;

	private ProjectStatusEnume(String status, String showName) {
		this.status = status;
		this.showName = showName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

}
