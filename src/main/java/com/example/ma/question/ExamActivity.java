package com.example.ma.question;
        import android.app.Activity;
        import android.app.AlertDialog;
        import android.content.DialogInterface;
        import android.os.Bundle;
        //import android.support.v7.app.AppCompatActivity;
        import android.view.View;
        import android.widget.Button;
        import android.widget.RadioButton;
        import android.widget.RadioGroup;
        import android.widget.TextView;

        import java.util.ArrayList;
        import java.util.List;



public class ExamActivity extends Activity {

    private int count;
    private int current;
    private boolean wrongMode;//标志变量，判断是否进入错题模式
   private  TextView answer_explanation;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        DBService dbService = new DBService();
        final List<Question> list = dbService.getQuestion();

        count = list.size();
        current = 0;
        wrongMode=false;//默认情况

        final TextView set_question = (TextView) findViewById(R.id.set_question);
        final RadioButton[] radioButtons = new RadioButton[4];
        radioButtons[0] = (RadioButton)findViewById(R.id.Button_answerA);
        radioButtons[1] = (RadioButton)findViewById(R.id.Button_answerB);
        radioButtons[2] = (RadioButton)findViewById(R.id.Button_answerC);
        radioButtons[3] =(RadioButton) findViewById(R.id.Button_answerD);
        answer_explanation = (TextView) findViewById(R.id.answer_explanation);
        Button btn_previous = (Button)findViewById(R.id.btn_previous);
        Button btn_next =(Button) findViewById(R.id.btn_next);

        final RadioGroup radioGroup =(RadioGroup) findViewById(R.id.mRadioGroup);
        //为控件赋值
        Question q = list.get(0);
        set_question.setText(q.question);

        radioButtons[0].setText(q.answerA);
        radioButtons[1].setText(q.answerB);
        radioButtons[2].setText(q.answerC);
        radioButtons[3].setText(q.answerD);
        answer_explanation.setText(q.explanation);
        btn_next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (current < count - 1) {//若当前题目不为最后一题，点击next按钮跳转到下一题；否则不响应
                    current++;
                    //更新题目
                    Question q = list.get(current);
                    set_question.setText(q.question);
                    radioButtons[0].setText(q.answerA);
                    radioButtons[1].setText(q.answerB);
                    radioButtons[2].setText(q.answerC);
                    radioButtons[3].setText(q.answerD);
                    answer_explanation.setText(q.explanation);


                    //若之前已经选择过，则应记录选择
                    radioGroup.clearCheck();
                    if (q.selectedAnswer != -1) {
                        radioButtons[q.selectedAnswer].setChecked(true);
                    }

                }
                //错题模式的最后一题
                else if(current==count-1&& wrongMode==true){
                    new AlertDialog.Builder(ExamActivity.this)
                            .setTitle("提示")
                            .setMessage("已经到达最后一题，是否退出？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ExamActivity.this.finish();
                                }
                            })
                            .setNegativeButton("取消",null)
                            .show();

                }
                else{
                    //当前题目为最后一题时，告知用户作答正确的数量和作答错误的数量，并询问用户是否要查看错题
                    final List<Integer> wrongList=checkAnswer(list);
                    //作对所有题目
                    if(wrongList.size()==0){
                        new AlertDialog.Builder(ExamActivity.this)
                                .setTitle("提示")
                                .setMessage("恭喜你全部回答正确！")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        ExamActivity.this.finish();
                                    }
                                }).show();

                    }
                    else
                        new AlertDialog.Builder(ExamActivity.this)
                                .setTitle("提示")
                                .setMessage("您答对了"+(list.size()-wrongList.size())+
                                        "道题目；答错了"+wrongList.size()+"道题目。是否查看错题？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int which) {

                                        //判断进入错题模式
                                        wrongMode=true;
                                        List<Question> newList=new ArrayList<Question>();
                                        //将错误题目复制到newList中
                                        for(int i=0;i< wrongList.size();i++){
                                            newList.add(list.get(wrongList.get(i)));
                                        }
                                        //将原来的list清空
                                        list.clear();
                                        //将错误题目添加到原来的list中
                                        for(int i=0;i<newList.size();i++){
                                            list.add(newList.get(i));
                                        }
                                        current=0;
                                        count=list.size();
                                        //更新显示时的内容
                                        Question q = list.get(current);
                                        set_question.setText(q.question);
                                        radioButtons[0].setText(q.answerA);
                                        radioButtons[1].setText(q.answerB);
                                        radioButtons[2].setText(q.answerC);
                                        radioButtons[3].setText(q.answerD);
                                        answer_explanation.setText(q.explanation);
                                        //显示结果
                                        answer_explanation.setVisibility(View.VISIBLE);
                                        //显示解析
                                        answer_explanation.setText(q.explanation);
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int which) {
//点击取消时，关闭当前activity
                                        ExamActivity.this.finish();
                                    }
                                }).show();
                }
            }
        });
        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (current > 0)//若当前题目不为第一题，点击previous按钮跳转到上一题；否则不响应
                {
                    current--;
                    Question q = list.get(current);
                    set_question.setText(q.question);
                    radioButtons[0].setText(q.answerA);
                    radioButtons[1].setText(q.answerB);
                    radioButtons[2].setText(q.answerC);
                    radioButtons[3].setText(q.answerD);
                    answer_explanation.setText(q.explanation);


                    //若之前已经选择过，则应记录选择
                    radioGroup.clearCheck();
                    if (q.selectedAnswer != -1) {
                        radioButtons[q.selectedAnswer].setChecked(true);
                    }

                }

            }
        });
        //选择选项时更新选择
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                for (int i = 0; i < 4; i++) {
                    if (radioButtons[i].isChecked() == true) {
                        list.get(current).selectedAnswer = i;
                        break;
                    }
                }

            }
        });
    }

    /*
   判断用户作答是否正确，并将作答错误题目的下标生成list,返回给调用者
    */
    private List<Integer> checkAnswer(List<Question> list) {
        List<Integer> wrongList = new ArrayList<Integer>();
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).answer!=list.get(i).selectedAnswer){
                wrongList.add(i);
            }
        }
        return wrongList;
    }
}