package cn.zhudai.zin.zhudaibao.entity;

/**
 * Created by admin on 2016/6/2.
 */
public class QRCodeResponse {
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
        private String qrimg;

        private String shareimg;

        private String sharelink;

        private int uid;

        public void setQrimg(String qrimg){
            this.qrimg = qrimg;
        }
        public String getQrimg(){
            return this.qrimg;
        }
        public void setShareimg(String shareimg){
            this.shareimg = shareimg;
        }
        public String getShareimg(){
            return this.shareimg;
        }
        public void setSharelink(String sharelink){
            this.sharelink = sharelink;
        }
        public String getSharelink(){
            return this.sharelink;
        }
        public void setUid(int uid){
            this.uid = uid;
        }
        public int getUid(){
            return this.uid;
        }

    }
}
