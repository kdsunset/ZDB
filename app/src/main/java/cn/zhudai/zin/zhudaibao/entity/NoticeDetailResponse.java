package cn.zhudai.zin.zhudaibao.entity;

/**
 * Created by admin on 2016/6/12.
 */
public class NoticeDetailResponse {
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
        private String id;

        private String title;

        private String content;

        private String author;

        private String addtime;

        public void setId(String id){
            this.id = id;
        }
        public String getId(){
            return this.id;
        }
        public void setTitle(String title){
            this.title = title;
        }
        public String getTitle(){
            return this.title;
        }
        public void setContent(String content){
            this.content = content;
        }
        public String getContent(){
            return this.content;
        }
        public void setAuthor(String author){
            this.author = author;
        }
        public String getAuthor(){
            return this.author;
        }
        public void setAddtime(String addtime){
            this.addtime = addtime;
        }
        public String getAddtime(){
            return this.addtime;
        }
    }
}
