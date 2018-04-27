package com.example.rdas6313.quizz.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rdas6313 on 27/4/18.
 */

public class Questions {
    private String id;
    private String question;
    private int ans;
    private String type;
    private List<String> options = new ArrayList<>();

    public Questions(){}

    public Questions(String question,int ans,List<String>options,String type,String id){
        this.id = id;
        this.question = question;
        this.type = type;
        this.ans = ans;
        this.options = options;
    }

    public void setId(String key){
        this.id = key;
    }

    public String getQuestion(){
        return question;
    }

    public int getAns(){
        return ans;
    }

    public String getAnsName(){
        if(options != null)
            return options.get(ans);
        return "";
    }


    public List<String> getOptions(){
        return options;
    }

    public String getType(){
        return type;
    }

    public String getId(){
        return id;
    }
}
