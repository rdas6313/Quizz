package com.example.rdas6313.quizz.Models;

import android.support.v7.widget.RecyclerView;

/**
 * Created by rdas6313 on 26/4/18.
 */

public class Questiontype{
    private String id;
    private String questionsetname;

    public Questiontype(){

    }
    public Questiontype(String id,String type){
        this.id = id;
        questionsetname = type;
    }

    public String getId(){
        return id;
    }

    public String getQuestionType(){
        return questionsetname;
    }

}
