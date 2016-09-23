package cn.zhudai.zin.zhudaibao.entity;

/**
 * Created by admin on 2016/6/26.
 */
public class UpdateInfoResponse {
    private int code;

    private Result result;

    public void setCode(int code){
        this.code = code;
    }
    public int getCode(){
        return this.code;
    }
    public void setResult(Result result){
        this.result = result;
    }
    public Result getResult(){
        return this.result;
    }
    public class Result{
        private int versionCode;

        private String versionName;

        private String description;

        private String download_url;

        public void setVersionCode(int versionCode){
            this.versionCode = versionCode;
        }
        public int getVersionCode(){
            return this.versionCode;
        }
        public void setVersionName(String versionName){
            this.versionName = versionName;
        }
        public String getVersionName(){
            return this.versionName;
        }
        public void setDescription(String description){
            this.description = description;
        }
        public String getDescription(){
            return this.description;
        }
        public void setDownload_url(String download_url){
            this.download_url = download_url;
        }
        public String getDownload_url(){
            return this.download_url;
        }

    }
}
