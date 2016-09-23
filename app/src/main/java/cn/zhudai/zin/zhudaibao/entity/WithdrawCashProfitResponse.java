package cn.zhudai.zin.zhudaibao.entity;

import java.util.List;

/**
 * Created by admin on 2016/6/5.
 */
public class WithdrawCashProfitResponse {
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
        private CtypeArray ctypeArray;

        private String prize_total;

        private List<Tixianmsg> tixianmsg ;

        private String register_prize_total;

        private String invite_prize_total;

        public void setCtypeArray(CtypeArray ctypeArray){
            this.ctypeArray = ctypeArray;
        }
        public CtypeArray getCtypeArray(){
            return this.ctypeArray;
        }
        public void setPrize_total(String prize_total){
            this.prize_total = prize_total;
        }
        public String getPrize_total(){
            return this.prize_total;
        }
        public void setTixianmsg(List<Tixianmsg> tixianmsg){
            this.tixianmsg = tixianmsg;
        }
        public List<Tixianmsg> getTixianmsg(){
            return this.tixianmsg;
        }
        public void setRegister_prize_total(String register_prize_total){
            this.register_prize_total = register_prize_total;
        }
        public String getRegister_prize_total(){
            return this.register_prize_total;
        }
        public void setInvite_prize_total(String invite_prize_total){
            this.invite_prize_total = invite_prize_total;
        }
        public String getInvite_prize_total(){
            return this.invite_prize_total;
        }
    }
    public class Tixianmsg {
        private String id;

        private String lid;

        private String uid;

        private String luid;

        private String lrealname;

        private String cname;

        private String ctype;

        private String cmoney;

        private String rebate_rate;

        private String prize_money;

        private String single_award;

        private String addtime;

        public void setId(String id){
            this.id = id;
        }
        public String getId(){
            return this.id;
        }
        public void setLid(String lid){
            this.lid = lid;
        }
        public String getLid(){
            return this.lid;
        }
        public void setUid(String uid){
            this.uid = uid;
        }
        public String getUid(){
            return this.uid;
        }
        public void setLuid(String luid){
            this.luid = luid;
        }
        public String getLuid(){
            return this.luid;
        }
        public void setLrealname(String lrealname){
            this.lrealname = lrealname;
        }
        public String getLrealname(){
            return this.lrealname;
        }
        public void setCname(String cname){
            this.cname = cname;
        }
        public String getCname(){
            return this.cname;
        }
        public void setCtype(String ctype){
            this.ctype = ctype;
        }
        public String getCtype(){
            return this.ctype;
        }
        public void setCmoney(String cmoney){
            this.cmoney = cmoney;
        }
        public String getCmoney(){
            return this.cmoney;
        }
        public void setRebate_rate(String rebate_rate){
            this.rebate_rate = rebate_rate;
        }
        public String getRebate_rate(){
            return this.rebate_rate;
        }
        public void setPrize_money(String prize_money){
            this.prize_money = prize_money;
        }
        public String getPrize_money(){
            return this.prize_money;
        }
        public void setSingle_award(String single_award){
            this.single_award = single_award;
        }
        public String getSingle_award(){
            return this.single_award;
        }
        public void setAddtime(String addtime){
            this.addtime = addtime;
        }
        public String getAddtime(){
            return this.addtime;
        }

    }
    public class CtypeArray {
        private String first_partner;

        private String second_partner;

        public void setFirst_partner(String first_partner){
            this.first_partner = first_partner;
        }
        public String getFirst_partner(){
            return this.first_partner;
        }
        public void setSecond_partner(String second_partner){
            this.second_partner = second_partner;
        }
        public String getSecond_partner(){
            return this.second_partner;
        }

    }
}
