package cn.zhudai.zin.zhudaibao.entity;

import java.util.List;

/**
 * Created by admin on 2016/6/5.
 */
public class ManagerCommentResponse {
    private int code;

    private List<Result> result ;

    public void setCode(int code){
        this.code = code;
    }
    public int getCode(){
        return this.code;
    }
    public void setResult(List<Result> result){
        this.result = result;
    }
    public List<Result> getResult(){
        return this.result;
    }
    public class Result{
        private String id;

        private String truename;

        private String managerid;

        private String time;

        private String addtime;

        private String mname;

        private String mphone;

        private String content;

        private String grade;

        private int is_review;

        public void setId(String id){
            this.id = id;
        }
        public String getId(){
            return this.id;
        }
        public void setTruename(String truename){
            this.truename = truename;
        }
        public String getTruename(){
            return this.truename;
        }
        public void setManagerid(String managerid){
            this.managerid = managerid;
        }
        public String getManagerid(){
            return this.managerid;
        }
        public void setTime(String time){
            this.time = time;
        }
        public String getTime(){
            return this.time;
        }
        public void setAddtime(String addtime){
            this.addtime = addtime;
        }
        public String getAddtime(){
            return this.addtime;
        }
        public void setMname(String mname){
            this.mname = mname;
        }
        public String getMname(){
            return this.mname;
        }
        public void setMphone(String mphone){
            this.mphone = mphone;
        }
        public String getMphone(){
            return this.mphone;
        }
        public void setContent(String content){
            this.content = content;
        }
        public String getContent(){
            return this.content;
        }
        public void setGrade(String grade){
            this.grade = grade;
        }
        public String getGrade(){
            return this.grade;
        }
        public void setIs_review(int is_review){
            this.is_review = is_review;
        }
        public int getIs_review(){
            return this.is_review;
        }
    }
}
