package com.example.hhvanet;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AssessAll extends Activity{
	private MyApplication myApplication;
	private ProgressBar mProgressBar1,mProgressBar2,mProgressBar3;
	private TextView score1 = null;
	private TextView score2;
	private TextView score3;
	
	private double stdv = 21.169;
	private double stda = 1.977;
	private double stdw1 = 13.1802;
	private double stdw2 = 6.5901;
	private double stdw3 = 3.2951;
	private double stdw4 = 1.977;
	private double stdr1 = 6.5901;
	private double stdr2 = 3.2951;
	private double stdr3 = 1.977;
	private double stdTPI = 21.17;
	private double stdTPId = 13.18;
	/**
	 * ͬ����ֻ��1�����������ĵ�·�����е�·ΪÿСʱ������50����
	 */
	private int Vertical_max = 50;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.assessall);
		mProgressBar1 = (ProgressBar)findViewById(R.id.my_progress1);
		mProgressBar2 = (ProgressBar)findViewById(R.id.my_progress2);
		mProgressBar3 = (ProgressBar)findViewById(R.id.my_progress3);
		score1 = (TextView)findViewById(R.id.score1);
		score2 = (TextView)findViewById(R.id.score2);
		score3 = (TextView)findViewById(R.id.score3);
		myApplication = (MyApplication)getApplication();
		List<Float> vertical = myApplication.getVertical();
		/**
		 * ���ٶ�������
		 *kw1:����ת��Ĵ���
		 */
		int kw1 = myApplication.getSlowTurn();
		/**
		 * ���ٶ�������
		 * kw2:����ת��Ĵ���
		 */
		int kw2 = myApplication.getNormTurn();
		/**
		 * ���ٶ�������
		 * kw3:����ת��Ĵ���
		 */
		int kw3 = myApplication.getQuickTurn();
		/**
		 * ���ٶ�������
		 * kw4:����ת��Ĵ���
		 */
		int kw4 = myApplication.getEmgTurn();
		/**
		 * ������������
		 * kr1:�޲෭����΢�෭���մ���
		 */
		int kr1 = myApplication.getNoTurnOver();
		/**
		 * ������������
		 * kr2:�еȲ෭���մ���
		 */
		int kr2 = myApplication.getMidTurnOver();
		/**
		 * ������������
		 * kr3:�ߵȲ෭���մ���
		 */
		int kr3 = myApplication.getTurnOver();	
		
		float accelerate = myApplication.getMyCarInfo().acceleration_X;
		/**
		 * ������˲ʱ�ٶ�
		 */
		float Vertical = myApplication.getSpeed();
		/**
		 * �����ſ���
		 */
		double tpi = myApplication.getTpi();
		/**
		 * �ͺ�
		 */
		double oil = myApplication.getOil();
		/**
		 * ǰһʱ�̵Ľ����ſ���
		 */
		double PreTpi = myApplication.getPreOil();
		/**
		 *�����ٶ�
		 */
		float speed = myApplication.getMyCarInfo().speed;
		int[] score = new int[3];
		score[0] =	(int)(SafetyAssess(Vertical,vertical,accelerate,kw1, kw2, kw3,kw4,kr1, kr2, kr3)*100);
		mProgressBar1.setProgress(score[0]);
		score1.setText(score[0] + "��");
		score[1] = (int)(EconomicalAssess(Vertical,tpi,PreTpi,oil)*100);
		mProgressBar2.setProgress(score[1]);
		score2.setText(score[1] + "��");
		score[2] = (int)( assessAll(speed,vertical,accelerate,kw1, kw2, kw3,kw4,kr1, kr2, kr3,tpi,PreTpi,oil)*100);
		mProgressBar3.setProgress(score[2]);
		score3.setText(score[2]+ "��");
		TextView textView = (TextView)findViewById(R.id.suggest);
		if (score[0] < 85 || score[1] <85) {
			textView.setText("���ļ�ʻ��Ϊ������Ϊ���������˵�����ȫ�뼰ʱ�������ļ�ʻ״̬��ϰ�ߣ���ȫ��ʻ");
		}
		else {
			textView.setText("���ļ�ʻϰ�����ã����������ϣ�");
		}
		Button button = (Button)findViewById(R.id.back);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(AssessAll.this,MainActivity.class));
			}
		});
	}
	   /**
			 * ��ȡ10�������ٶȵľ�ֵ
			 * @param vertical
			 * @return
			 */
			private float Average(List<Float> vertical){
				float averge  = 0;
				for(int i = 0; i < vertical.size();i++)
					averge = averge + vertical.get(i).floatValue();
				averge = averge/vertical.size();
				return averge;
			}
			/**
			 * �ٶȺ����������Ⱥ���
			 * @param speed ������˲ʱ�ٶ�
			 * @param vertical 10�����ڳ������ٶȼ���
			 * @return
			 */
			private double VerticalReasonable(float speed,List<Float> vertical){
				double result = 0;
				if (speed < 3) {
					result = 1;
				}else {
					if (speed <= 0.8*Average(vertical)) {
						result = Math.pow(Math.E, -Math.pow((speed-0.8*Average(vertical))/stdv, 2));
					}
					else {
						if (speed <= 1.2*Average(vertical)) {
							result = 1;
						}else {
							if (speed <= Vertical_max) {
								result = Math.pow(Math.E, -Math.pow((speed-1.2*Average(vertical))/stdv, 2));
							}
							else {
								result = 0;
							}
						}
					}
				}
				return result;
				
			}
			/**
			 * �ٶ��ȶ���������
			 * @param Accelerate ������˲ʱ���ٶ�
			 * @return
			 */
			private  double Vertical_Stable(float Accelerate){
				double result = 0;
				if(Math.abs(Accelerate) <=1){
					result =1;
				}else {
					result = Math.pow(Math.E,-Math.pow((Math.abs(Accelerate)-1)/stda,2));
				}
				return result;
			}
			/**
			 * ���ٶ�������
			 * @return
			 */
			private double Angular_Speed(int kw1,int kw2,int kw3,int kw4 ){
				double result = 0;
				result = 0.1*Math.pow(Math.E, -Math.pow(kw1/stdw1, 2))+0.2*Math.pow(Math.E, -Math.pow(kw2/stdw2, 2))
						+0.3*Math.pow(Math.E, -Math.pow(kw3/stdw3, 2))+0.4*Math.pow(Math.E, -Math.pow(kw4/stdw4, 2));
				return result;
				
			}
			/**
			 * ������������
			 * @return
			 */
			private double Roll(int kr1,int kr2,int kr3){
				double result = 0;
				result = 0.2*Math.pow(Math.E, -Math.pow(kr1/stdr1, 2))+0.3*Math.pow(Math.E, -Math.pow(kr2/stdr2, 2))
						+0.5*Math.pow(Math.E, -Math.pow(kr3/stdr3, 2));
				return result;
			}
			/**
			 * ��ȫ��ʻ����
			 * @return
			 */
			private double SafetyAssess(float speed,List<Float> vertical,float Accelerate,int kw1,int kw2,int kw3,int kw4,int kr1,int kr2,int kr3){
				double result = 0;
				result = 0.5396*(0.5*VerticalReasonable(speed,vertical)+0.5*Vertical_Stable(Accelerate))+0.2969*Angular_Speed(kw1,kw2,kw3,kw4)+0.1634*Roll(kr1,kr2,kr3);
				return result;
			}
			/**
			 * �����ſ��Ⱥ����Ժ���
			 * @param tpi ʵʱ�Ľ���������
			 * @return
			 */
			private double OpenReasonable(float speed,double tpi){
				double result = 0;
				//��ǰ�ٶ��µ���������ź���
				double Tpi = 0.507*speed + 28.66;
				if (tpi < 0.8*Tpi) {
					result = Math.pow(Math.E, -Math.pow((tpi-0.8*Tpi)/stdTPI, 2));
//					result = Math.pow(Math.E, -Math.pow((30.98-0.8*Tpi)/stdTPI, 2));
				}else {
					if ((0.8*Tpi<=tpi)&&(tpi<=1.2*Tpi)) {
						result = 1;
					}else {
						result = Math.pow(Math.E, -Math.pow((tpi-1.2*Tpi)/stdTPI, 2));
//						result = Math.pow(Math.E, -Math.pow((30.98-1.2*Tpi)/stdTPI, 2));
					}
				}
				return result;
			}
			/**
			 * �����ſ����ȶ��������Ⱥ����������ͺı仯��������
			 * @param tpi
			 * @return
			 */
			private double OpenStable(double oil,double preTpi){
				double result = 0;
				result = Math.pow(Math.E, -Math.pow(Math.abs(oil-preTpi)/stdTPId, 2));
//				result = Math.pow(Math.E, -Math.pow(Math.abs(1.35)/stdTPId, 2));
				return result;
			}
			private double  EconomicalAssess(float speed,double tpi,double preTpi,double oil){
				double result = 0;
				result = 0.6*OpenReasonable(speed, tpi) + 0.4*OpenStable(oil,preTpi);
				return result;
			}
			private double assessAll(float speed,List<Float> vertical,float Accelerate,int kw1,int kw2,int kw3,int kw4,int kr1,int kr2,int kr3,double tpi,double preTpi,double oil){
				double result = 0;
				result = 0.6*SafetyAssess(speed,vertical,Accelerate,kw1,kw2,kw3,kw4,kr1,kr2,kr3)+0.4*EconomicalAssess(speed,tpi,preTpi,oil);
				return result;
			}

}
