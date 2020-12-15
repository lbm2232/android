package com.leb.doduge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.concurrent.ThreadPoolExecutor;

public class MainActivity extends AppCompatActivity {
    // 두더지 9마리 find해오기
    ImageView[] doduge  = new ImageView[9];
    dodoThread[] dodoThreads = new dodoThread[9]; // 쓰레드 9개 공간 생성
    int score = 0;
    TextView tv_cnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_cnt = findViewById(R.id.tv_cnt);
        new timeThread().start(); // 30초 세기 시작!

        // 버튼 25개 find했던 코드 찾아서 두더지 9마리 찾아주세요!

        // 문자열 (imageView1~9)까지로 뷰의 id찾기
        for(int i = 0; i<9; i++){
            int id = getResources().getIdentifier("imageView" + (i+1), "id",
                    getPackageName());
            doduge[i] = findViewById(id);
            // 잘 찾아와졌는지 확인하기 위해서 두더지이미지를 off으로 변경
            doduge[i].setImageResource(R.drawable.off); // 이미지 지정
            doduge[i].setTag(R.drawable.off); // 태그 지정
            dodoThreads[i] = new dodoThread(doduge[i], new dodoHandler());
            dodoThreads[i].start();

            doduge[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 지금 올라온 두더지? 내려간 두더지?
                    // tag를 달아서 구분해주자!
                    if (v.getTag().toString().equals(R.drawable.on+"")){
                        // 올라온 두더지였다면
                        Toast.makeText(getApplicationContext(), "잡았다 요놈!", Toast.LENGTH_SHORT).show();
                        score++;
                    }else{
                        Toast.makeText(getApplicationContext(), "...",Toast.LENGTH_SHORT).show();
                        score--;
                    }

                    tv_cnt.setText(score + "");

                }
            });

        }
    }

    public class dodoThread extends Thread{
        ImageView doduge;
        Handler handler;

        public dodoThread(ImageView doduge, Handler handler){
            this.doduge = doduge;
            this.handler = handler;
        }

        @Override
        public void run() {
            while(true){ // 무제한으로 움직이는 두더지
                // 숨어있는 시간
                int offTime = new Random().nextInt(5000) + 500; // 0.5 ~ 5.5초 사이

                // offTime만큼 쉽시다
                try {
                    Thread.sleep(offTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 이제 튀어나옵시다!
                // 그런데 subThread에서는 이미지 바꾸기가 안되니
                // Handler한테 요청합시다!

                Message msg = new Message();
                // 내가 바꿔야할 두더지
                msg.obj = doduge;
                // 두더지 이미지
                msg.arg1 = R.drawable.on;
                handler.sendMessage(msg);

                // 쉬었다 내려가게 해봅시다
                int onTime = new Random().nextInt(1000) + 500; // 0.5 ~ 1.5초
                try {
                    Thread.sleep(onTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 주의사항!!! 한번 보낸 메세지는 재활용 할 수가 없어요!!
                Message msg2 = new Message();
                msg2.obj = doduge;
                msg2.arg1 = R.drawable.off;
                handler.sendMessage(msg2);

            }
        }
    }

    class timeThread extends Thread{
        @Override
        public void run() {
            for(int i = 0; i<3; i++){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } // for문이 다 돌면 30초가 지났다는 소리! -> 게임 끝!

            // score => 두더지 잡은 개수

            setResult(RESULT_OK,
                    new Intent().putExtra("score", score)); // 다시 돌아가쟈!
            // 나를 실행시킨 Activity의 onActivityResult 메소드 호출
            finish(); // 끝내자!
        }
    }

    class dodoHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            //요청한 메세지를 처리하는 메소드
            ((ImageView)msg.obj).setImageResource(msg.arg1);
            ((ImageView)msg.obj).setTag(msg.arg1); // 현재 이미지뷰의 ResourseID를 태그로 달아줍니다!
        }
    }
}








