package cn.zhudai.zin.zhudaibao.entity;

/**
 * Created by admin on 2016/5/27.
 */
public class MainMenuData {
    private String title;
    private String detail;
    private int imageResId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public MainMenuData() {
    }

    public MainMenuData(String title, String detail, int imageResId) {

        this.title = title;
        this.detail = detail;
        this.imageResId = imageResId;
    }
}
