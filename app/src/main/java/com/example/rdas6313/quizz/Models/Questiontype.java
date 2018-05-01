package com.example.rdas6313.quizz.Models;

import android.support.v7.widget.RecyclerView;

/**
 * Created by rdas6313 on 26/4/18.
 */

public class Questiontype{
    private String id;
    private String questionsetname;
    boolean alreadSelected;

    public Questiontype(){

    }
    public Questiontype(String id,String type_name,boolean alreadSelected){
        this.id = id;
        questionsetname = type_name;
        this.alreadSelected = alreadSelected;
    }

    public String getId(){
        return id;
    }

    public String getQuestionType(){
        return questionsetname;
    }

    public boolean isAlreadSelected(){
        return alreadSelected;
    }

}
