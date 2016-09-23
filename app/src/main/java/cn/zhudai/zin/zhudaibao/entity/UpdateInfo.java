package cn.zhudai.zin.zhudaibao.entity;

public class UpdateInfo {

	public String version; // 服务器上的版本号
	public String description; // 升级信息
	public String download_url; // apk下载地址

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getApkurl() {
		return download_url;
	}

	public void setApkurl(String download_url) {
		this.download_url = download_url;
	}

}
