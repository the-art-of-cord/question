package com.example.ma.question;

/**
 * Created by maqia on 2018/5/12.
 */
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;


//�������ݿ⣬�������ݿ��л�ȡ��������
public class DBService {
    private SQLiteDatabase db;
    //�ڹ��캯���д�ָ�����ݿ⣬������������ָ��db
    public DBService(){
        db=SQLiteDatabase.openDatabase("/data/data/com.example.ma.question/databases/question.db" +
                "",null,SQLiteDatabase.OPEN_READWRITE);
    }
    //��ȡ���ݿ��е�����
    public List<Question> getQuestion(){
        List<Question> list=new ArrayList<Question>();
  /*
    Cursor�ǽ�����α꣬���ڶԽ���������������,��ʵCursor��JDBC�е�ResultSet���ú����ơ�
    rawQuery()�����ĵ�һ������Ϊselect��䣻�ڶ�������Ϊselect�����ռλ��������ֵ�����select���û��ʹ��ռλ�����ò�����������Ϊnull��*/
        Cursor cursor =db.rawQuery("select * from question",null);
        if(cursor.getCount()>0){
            cursor.moveToFirst();//��cursor�ƶ�����һ�������
            int count=cursor.getCount();
            //��cursor�е�ÿһ����¼����һ��question���󣬲�����question������ӵ�list��
            for(int i=0;i<count;i++){
                cursor.moveToPosition(i);
                Question question =new Question();
                question.ID=cursor.getInt(cursor.getColumnIndex("Field1"));
                question.question=cursor.getString(cursor.getColumnIndex("Field2"));
                question.answerA=cursor.getString(cursor.getColumnIndex("Field3"));
                question.answerB=cursor.getString(cursor.getColumnIndex("Field4"));
                question.answerC=cursor.getString(cursor.getColumnIndex("Field5"));
                question.answerD=cursor.getString(cursor.getColumnIndex("Field6"));
                question.answer=cursor.getInt(cursor.getColumnIndex("Field7"));

                question.explaination = cursor.getString(cursor.getColumnIndex("Field8"));
                //��ʾû��ѡ���κ�ѡ��
                question.selectedAnswer=-1;
                list.add(question);
            }
        }
        return list;
    }

}
