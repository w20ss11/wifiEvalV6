package cqupt.wss.wifieval;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import cqupt.wss.util.AptxtUtil;

public class SelectApActivity extends Activity implements OnClickListener{

	ListView lv;
	ArrayList<String> list;
	AptxtUtil aptxt;
	String[] selected_aps;
	ArrayList<String> times;
	double squ;
	@SuppressLint("ShowToast")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_ap);
		Intent intent = getIntent();
		times = new ArrayList<String>();
		times = intent.getStringArrayListExtra("times");
		squ = Double.parseDouble(intent.getStringExtra("square"));
		Toast.makeText(this, times.toString(), 0).show();
		Button button_done = (Button) findViewById(R.id.done);
		button_done.setOnClickListener(this);
		lv = (ListView) findViewById(R.id.lv);
		
		list = new ArrayList<String>();
		aptxt = new AptxtUtil();
		try {
			list = aptxt.getApList(times.get(0));//��txt�л�ȡ���е�ap��
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice, list);
        lv.setAdapter(adapter);//������ap����ʾ��listview��
	}
	
	public void getSelectAps() {//��ȡѡ�е�ap�����ַ���������
        // long[] authorsId = radioButtonList.getCheckItemIds();
        long[] apsId = getListSelectededItemIds(lv);
        selected_aps = new String[apsId.length];
        String message = null;
        if (apsId.length > 0) {
            for (int i = 0; i < apsId.length; i++) {
            	selected_aps[i] = list.get((int)apsId[i]);
            	message = Arrays.toString(selected_aps);
            }
        } else {
            message = "������ѡ��һ��AP��";
        }
        Toast.makeText(SelectApActivity.this, message, Toast.LENGTH_LONG).show();
    }
	
	 // ����ʹ��getCheckItemIds()����
    public long[] getListSelectededItemIds(ListView listView) {
         
        long[] ids = new long[listView.getCount()];//getCount()����ȡ��ListView��������item�ܸ���
        //�����û�ѡ��Item���ܸ���
        int checkedTotal = 0;
        for (int i = 0; i < listView.getCount(); i++) {
            //������Item�Ǳ�ѡ�е�
            if (listView.isItemChecked(i)) {
                ids[checkedTotal++] = i;
            }
        }
 
        if (checkedTotal < listView.getCount()) {
            //����ѡ�е�Item��ID����
            final long[] selectedIds = new long[checkedTotal];
            //���鸴�� ids
            System.arraycopy(ids, 0, selectedIds, 0, checkedTotal);
            return selectedIds;
        } else {
            //�û������е�Item��ѡ��
            return ids;
        }
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.done:
			getSelectAps();
			String res = "no result";
			try {
				res = aptxt.getFinalRes(times, selected_aps, squ);//��ѡ�е�ap�����ɾ���
			} catch (IOException e) {
				e.printStackTrace();
			}
			
            Intent intent = new Intent();
            // ��ȡ�û������Ľ��
            intent.putExtra("res", res); //�������ֵ�ش���ȥ
            setResult(2, intent);
            finish(); //������ǰ��activity����������
			break;
		}
	}
}
