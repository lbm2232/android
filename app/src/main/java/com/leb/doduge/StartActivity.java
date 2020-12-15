package com.leb.doduge;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class StartActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // SharedPreferences 사용처 => 어플 껐다 켜도 상태 유지할때
        // 1. 어플 최초 실행인지?
        // 2. 환경설정값 저장할때
        // 3. 아이디/비밀번호 저장해둘때
        // 4. 로그인 유지할때

        // MODE_PRIVATE => doduge라는 이름의 값이 있으면 가져오기, 없으면 생성하기
        SharedPreferences spf = getSharedPreferences("doduge", MODE_PRIVATE);
        int score = spf.getInt("score", 0);

        TextView tv_score = findViewById(R.id.tv_score);
        tv_score.setText("best Score : " + score);

        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                startActivityForResult(intent, 100); // requestCode값 : 요청코드값
                // 대신! intent에 값을 담아주지 않을 때만 가능
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 갔다 왔을 때 호출되는 메소드
        // 1. 요청코드값, 2. 결과 코드값, 3. 갔다 왔을때 가져온 데이터


        if (requestCode == 100 && resultCode == RESULT_OK){
            int score = data.getIntExtra("score", -999);
            // getIntExtra는 매개변수가 2개입니다.
            // 1. 데이터의 키값, 2. Default값 (키값에 해당하는 데이터가 없을 때 대체할 값)
            TextView tv_score = findViewById(R.id.tv_score);
            tv_score.setText("your Score : " + score);

            // 데이터저장, 쿠키와 세션
            // 데이터를 임시로 저장합니다.
            // MODE_PRIVATE => doduge라는 이름의 프리퍼런스는 오로지 한개만 만들어주겠다.
            // 혹시 doduge라는 이름의 프리퍼런스가 없다면 새로 생성하겠다.
            SharedPreferences spf = getSharedPreferences("doduge", MODE_PRIVATE);
            SharedPreferences.Editor edit = spf.edit(); // 수정할 수 있는 객체 가져오기
            if (score > spf.getInt("score", 0)){ // spf에 저장되어 있는 score보다
                                                            // 지금 달성한 점수가 더 높았을 때
                edit.putInt("score", score); // 값 저장!
                edit.commit(); // commit! 값 반영!!!!
            }

        }
    }
}






