package cqupt.wss.util;


public class mathUtil {
	/**
     * �����˫����������ֵ�ĺ�
     * 
     * @param inputData ������������
     * @return ������
     */
    public static double getSum(double[] inputData) {
        if (inputData == null || inputData.length == 0) {
            return -1;
        }
        int len = inputData.length;
        double sum = 0;
        for (int i = 0; i < len; i++) {
            sum = sum + inputData[i];
        }

        return sum;
    }
	
    /**
     * �����˫����������ֵ��ƽ����
     * 
     * @param inputData ������������
     * @return ������
     */
    public static double getSquareSum(double[] inputData) {
        if(inputData == null || inputData.length == 0) {
            return -1;
        }
        int len = inputData.length;
        double sqrsum = 0.0;
        for (int i = 0; i < len; i++) {
            sqrsum = sqrsum + inputData[i] * inputData[i];
        }


        return sqrsum;
    }
	/**
	 * �����˫����������ֵ��ƽ��ֵ
	 * 
	 * @param inputData ������������
	 * @return ������
	 */
	public static double getAverage(double[] inputData) {
		if (inputData == null || inputData.length == 0) {
			return -1;
		}
		int len = inputData.length;
		double result;
		result = getSum(inputData) / len;

		return result;
	}


	/**
	 * �����˫����������ֵ�ĸ��ź�ķ���
	 * 
	 * @param inputData ������������
	 * @return ������
	 */
	public static double getVariance(double[] inputData) {
		int len = inputData.length;
		double[] inputCopy = new double[len];
        for (int i = 0; i < len; i++) {
        	inputCopy[i] = Math.sqrt(inputData[i]);
        }
		double average = getAverage(inputCopy);
		for (int i = 0; i < len; i++) {
        	inputCopy[i] = inputCopy[i]-average;
        }
		double sqrsum = getSquareSum(inputCopy);

		return sqrsum/(len-1); 
	}

	/**
	 * �����˫����������ֵ�ĸ��ź�ķ���
	 * 
	 * @param inputData ������������
	 * @return ������
	 */
	public static double[][] get2dSqrt_conv(double[][] inputData) {//aps*times
		int len = inputData[0].length;//len times
		int aps = inputData.length;
		double[][] input_copy = new double[aps][len];
        for (int i=0; i < aps; i++) {
        	for(int j=0;j < len;j++){
        		input_copy[i][j] = Math.sqrt(inputData[i][j]);
        	}
        }
        double[][] res = new double[aps][aps];
        for(int i=0;i<aps;i++){
        	for(int j=0;j<aps;j++){
        		res[i][j] = getCovariance(input_copy[i],input_copy[j]);
        		//res[j][i] = res[i][j];
        	}
        }
		return res;
	}
	/**
	 * ���������˫���������Э����
	 * 
	 * @param ds1 ds2 ������������
	 * @return ������
	 */
	public static double getCovariance(double[] ds1, double[] ds2) {
		int len = ds1.length;
		double[] ds12 = new double[len];
		
		double aver1 = getAverage(ds1);
		double aver2 = getAverage(ds2);
		for(int i=0;i<len;i++){
			ds12[i] = (ds1[i] -aver1)*(ds2[i] -aver2);
		}
		return getSum(ds12)/(len-1);
	}
	
	public static double[][] dbs2mw(double[][] ds){
		for(int i=0;i<ds.length;i++)
			for(int j=0;j<ds[0].length;j++)
				ds[i][j] = Math.pow(10, ds[i][j]/10);
		return ds;
	}
}
