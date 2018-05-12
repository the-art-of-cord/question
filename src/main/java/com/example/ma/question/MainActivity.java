package com.example.ma.question;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String DB_PATH = "/data/data/com.example.ma.question/databases/";
        String DB_NAME = "question.db";
        //Ӧ������ʱ���ж����ݿ��Ƿ���ڣ�����������ǰ����õ����ݿ��ļ����Ƶ����ݿ�Ŀ¼��
        //���ݿ�Ŀ¼������ʱ���������ݿ�Ŀ¼
        if ((new File(DB_PATH + DB_NAME).exists()) == false) {
            File dir = new File(DB_PATH);
            if (!dir.exists()) {
                dir.mkdir();
            }
        }
//������������������ڸ����ļ�
        try {
            InputStream is = getBaseContext().getAssets().open(DB_NAME);
            OutputStream os = new FileOutputStream(DB_PATH + DB_NAME);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            //ˢ����������ر����������
            os.flush();
            os.close();
            is.close();


        } catch (IOException e) {
            e.printStackTrace();
        }


        Button btn = (Button)findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener()

        {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ExamActivity.class);
                startActivity(intent);


            }
        });


    }
}