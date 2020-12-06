package cqupt.wss.wifieval;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cqupt.wss.util.TvThread;

public class MainActivity extends Activity implements OnClickListener {

	TextView tv;
	TextView tv_ts;
	EditText et;
	TvThread thread = null;
	Handler mHandler;
	String time;
	boolean is_start_press = false;
	ArrayList<String> times;
	private final static int REQUESTCODE = 1; // 返回的结果码
	@SuppressLint("SdCardPath")
	final String filePath="/sdcard/Test/";
	@SuppressLint("HandlerLeak")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tv = (TextView) findViewById(R.id.textview);
		tv_ts = (TextView) findViewById(R.id.times);
		et = (EditText) findViewById(R.id.square);
		times = new ArrayList<String>();
		Button button_start = (Button) findViewById(R.id.start);
		Button button_stop = (Button) findViewById(R.id.stop);
		Button button_eval = (Button) findViewById(R.id.eval);
		button_start.setOnClickListener(this);
		button_stop.setOnClickListener(this);
		button_eval.setOnClickListener(this);
		//生成文件夹
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdir();
		}
		mHandler = new Handler(){  
			public void handleMessage(Message msg) {  
				super.handleMessage(msg);  
				switch(msg.what){  
				case 1:  
					String resPerSec1 = msg.obj.toString();
					tv.append(resPerSec1);
					break;  
				case 2:  
					String resPerSec2 = msg.obj.toString();
					tv.setText(resPerSec2);
					break;  
				default:  
					break;  
				}
			}  
		};
	}

	@SuppressLint({ "SimpleDateFormat", "ShowToast" })
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.start:
			if(!is_start_press){
				//新建该开始按键对应的时间txt
				tv_ts.setText("这是第"+(times.size()+1)+"次采集：");
				tv.setText("");
				String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
				String fileName = filePath + time+".txt";
				File txtfile = new File(fileName);
				times.add(fileName);
				try {
					if (!txtfile.exists()) {
						txtfile.createNewFile();
						Toast.makeText(this, filePath + fileName+"创建", 0).show();
					}
				} catch (Exception e) {
					System.err.println("error:"+e);
				}
				thread = new TvThread(this,txtfile,mHandler);
				thread.start();
				is_start_press = !is_start_press;
			}else {
				Toast.makeText(this, "请先点击停止", 0).show();
			}
			break;
		case R.id.stop:
			if(thread==null)
				Toast.makeText(this, "请先点击开始", 0).show();
			else{
				thread.stopThread();
				is_start_press = !is_start_press;
				Toast.makeText(this, "已停止采集wifi", 0).show();}
			break;
		case R.id.eval:
			String squ = et.getText().toString();
			if(times.size()==0)
				Toast.makeText(this, "请先采集数据！", 0).show();
			else if(squ.length()==0)
				Toast.makeText(this, "请输入面积！！", 0).show();
			else{
				Intent intent = new Intent(MainActivity.this, SelectApActivity.class);
				intent.putStringArrayListExtra("times", times); 
				intent.putExtra("square", squ);
				startActivityForResult(intent, REQUESTCODE);}
			break;
		}
	}
	
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        times.clear();
        if (resultCode == 2) {
            if (requestCode == REQUESTCODE) {
                String res = data.getStringExtra("res");
                tv.setText(res);
            }
        }
    }
}
