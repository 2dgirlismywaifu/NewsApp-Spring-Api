package com.notmiyouji.newsapp.java.NewsAPI.NewsAPIModels.Category;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewsCategory {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("totalResult")
    @Expose
    private int totalResult;

    @SerializedName("articles")
    @Expose
    private List<ArticleCategory> article;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResult() {
        return totalResult;
    }

    public void setTotalResult(int totalResult) {
        this.totalResult = totalResult;
    }

    public List<ArticleCategory> getArticle() {
        return article;
    }

    public void setArticle(List<ArticleCategory> article) {
        this.article = article;
    }
}
