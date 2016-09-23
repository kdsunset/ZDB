package cn.zhudai.zin.zhudaibao.entity;

/**
 * Created by admin on 2016/6/7.
 */
public class ShareInfoResponse {
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
        private String title;

        private String link;

        private String sharedesc;

        private String imgurl;

        private String QQimg;

        public void setTitle(String title){
            this.title = title;
        }
        public String getTitle(){
            return this.title;
        }
        public void setLink(String link){
            this.link = link;
        }
        public String getLink(){
            return this.link;
        }
        public void setSharedesc(String sharedesc){
            this.sharedesc = sharedesc;
        }
        public String getSharedesc(){
            return this.sharedesc;
        }
        public void setImgurl(String imgurl){
            this.imgurl = imgurl;
        }
        public String getImgurl(){
            return this.imgurl;
        }
        public void setQQimg(String QQimg){
            this.QQimg = QQimg;
        }
        public String getQQimg(){
            return this.QQimg;
        }
    }
}
