package com.start.question;

public class Q1 {

	public static void main(String[] args) {
//		Q1 q=new Q1();
//		q.next("321");
		System.out.println(0xffff);
	}
	
	//123-132-213-231-312-321
	//1234-1243
	private void next(String str) {
		int[] num=new int[str.length()];
		for(int i=0;i<str.length();i++){
			num[i]=Integer.parseInt(str.substring(i,i+1));
		}
		int maxIndex=maxIndex(num);
		if(maxIndex==-1){
			System.out.println(arrayByString(num)+" is Max");
		}else{
			for(int i=maxIndex-1;i>=0;i--){
				if(num[maxIndex]>num[i]){
					boolean flag=false;
					int tmp=num[maxIndex];
					for(int j=maxIndex;j>0;j--){
						num[j]=num[j-1];
						if(tmp>num[j]){
							flag=true;
						}
					}
					num[i]=tmp;
					if(flag){
						sort(num,maxIndex);
					}
					System.out.println(arrayByString(num));
					break;
				}
			}
		}
	}
	
	private void sort(int[] num,int start){
		for(int i=start;i<num.length;i++){
			num[i]=getMin(num, i);
		}
	}
	
	private int getMin(int[] num,int start){
		int min=num[start];
		for(int i=start+1;i<num.length;i++){
			if(min>num[i]){
				int tmp=num[i];
				num[i]=min;
				min=tmp;
			}
		}
		return min;
	}

	private int maxIndex(int[] num) {
		for(int i=num.length-1;i>0;i--){
			for(int j=i-1;j>=0;j--){
				if(num[i]>num[j]){
					return i;
				}
			}
		}
		return -1;
	}

	private String arrayByString(int[] num) {
		StringBuilder str=new StringBuilder();
		for(int i=0;i<num.length;i++){
			str.append(num[i]);
		}
		return str.toString();
	}
	
}
