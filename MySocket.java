package com.example.medicalcare2;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
public class ma extends AppCompatActivity{
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mylayout);

        // 连接到服务器
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 假设服务器IP地址为192.168.1.2，端口为5173
                    socket = new Socket("192.168.1.2", 5173);
                    out = new PrintWriter(socket.getOutputStream(), true);
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    // 监听来自服务器的消息
                    listenForMessages();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void listenForMessages() {
        String message;
        try {
            while ((message = in.readLine()) != null) {
                // 处理收到的授权请求
                if (message.equals("AUTH_REQUEST")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 创建一个弹窗询问用户是否授权
                            AlertDialog.Builder builder = new AlertDialog.Builder(ma.this);
                            builder.setMessage("是否授权个人信息？")
                                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // 用户确认授权
                                            sendAuthorizationResult(true);
                                            Toast.makeText(ma.this, "授权完成", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            sendAuthorizationResult(false);
                                            // 不弹出Toast
                                        }
                                    });
                            // 显示弹窗
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



