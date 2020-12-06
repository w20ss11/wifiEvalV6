package cqupt.wss.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import Jama.Matrix;

public class AptxtUtil {
	public ArrayList<String> getApList(String filename) throws IOException{
		int n = 3;//随机读取n行的ap
		File file = new File(filename);
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);

		ArrayList<String> list = new ArrayList<String>();
		String s = "";
		while ((s = br.readLine()) != null) {
			list.add(s);
		}
		if(list.size()<3)
			n=list.size();
		br.close();
		fr.close();
		Set<String> aps=new HashSet<String>();
		for(int i=0;i<n;i++){
			int temp = (int) (Math.random() * (list.size()));
			String[] ss = list.get(temp).split("##");
			for(int j=0;j<ss.length;j++){
				String temp_ap = ss[j].split(",")[0].trim();
				if(temp_ap.length()!=0)
					aps.add(temp_ap);
			}
		}
		ArrayList<String> result = new ArrayList<String>(aps);
		return result;
	}
	public double[][] getDs(String filename,String[] selected_aps) throws IOException{
		//从file中读取 按照selected_aps获取矩阵
        FileInputStream inputStream = new FileInputStream(new File(filename));  //BufferedReader是可以按行读取文件  
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        
        ArrayList<String> list = new ArrayList<String>();
        String str = null;  
        while((str = bufferedReader.readLine()) != null)  
        {  
            list.add(str);
        }  
        inputStream.close();  
        bufferedReader.close(); 
        double[][] ds = new double[selected_aps.length][list.size()];
        for(int i=0;i<list.size();i++){
        	String[] per_sec = list.get(i).split("##");
        	for(int j=0;j<per_sec.length;j++){
        		String[] per_wifi = per_sec[j].split(",");
        		for(int m=0;m<selected_aps.length;m++){
        			if(per_wifi[0].equals(selected_aps[m])){
        				ds[m][i]=Double.parseDouble(per_wifi[2]);
        			}
        		}
        	}
        }
        //System.out.println("ds:"+Arrays.deepToString(ds));
		return ds;
	}
	
	public String getFinalRes(ArrayList<String> times,String[] selected_aps,double squ) throws IOException{
		String tiaoshi = "";//调试用 可3
		//times.size 个点 ， selected_aps.length 个AP
		double[][] pn_ds = new double[times.size()][selected_aps.length];
		double[][] py_ds = new double[times.size()][selected_aps.length];
		for(int m=0;m<times.size();m++){//多少个点 pn py中的行数
			double[][] temp_ds = getDs(times.get(m), selected_aps);
//			tiaoshi = tiaoshi+Arrays.deepToString(temp_ds);
			temp_ds = mathUtil.dbs2mw(temp_ds);
			for(int n=0;n<temp_ds.length;n++){//多个各ap pn py中的列数
				pn_ds[m][n] = mathUtil.getVariance(temp_ds[n]);
				py_ds[m][n] = mathUtil.getAverage(temp_ds[n]);
			}
		}
		Matrix pn_matrix = new Matrix(pn_ds);//times*aps
		Matrix py_matrix = new Matrix(py_ds);//times*aps
		Matrix px_matrix = py_matrix.minus(pn_matrix);//times*aps
//		tiaoshi = tiaoshi+"\n pn "+Arrays.deepToString(pn_ds)+"\n py"+Arrays.deepToString(py_ds);
//		tiaoshi =tiaoshi+"\n px" + Arrays.deepToString(px_matrix.getArray());
		Matrix ax_matrix = new Matrix(mathUtil.get2dSqrt_conv(px_matrix.transpose().getArray()));
		Matrix an_matrix = new Matrix(mathUtil.get2dSqrt_conv(pn_matrix.transpose().getArray()));
//		tiaoshi = tiaoshi+"\n ax: "+Arrays.deepToString(ax_matrix.getArray());
//		tiaoshi = tiaoshi+"\n an: "+Arrays.deepToString(an_matrix.getArray());
		double fenmu = (ax_matrix.plus(an_matrix)).det();
		double fenzi = an_matrix.det();
		tiaoshi = tiaoshi+"\n fenmu: "+fenmu+"\n fenzi: "+fenzi;
		double h = 0.5*Math.log(fenmu/fenzi)/Math.log(2.0);
		tiaoshi = tiaoshi+"\n h: "+h;
		double mean_error = Math.sqrt(squ/Math.PI/Math.pow(2, -h));
		return tiaoshi+"\n"+mean_error+"";
	}
}
