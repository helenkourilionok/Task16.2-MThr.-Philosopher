package by.training.philosophsproblem.util;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

//��������� �����, ��������������� ��� ���������� �� ����������� � �������� ������ ��������
public class Util {
	//������ ����� ������� �� ����� �����:
	//1)����� � �������, �������� ������� ���
	//2)����� � �������, �������� ������� ������
	//3)������� ������� ���� ������ �������
	//4)���������� �����������,������� ��������� �������� 
	//������������� � ��������� ���������(�������),
	//� ������������� ����� ����� ����������(�������� ����� ��������)
	//��� ������ ������ ������� �� ��� � �� "�������" ��������(���������� �������� �������)
    public static void printResult(String s, AtomicInteger[] a){
        int sum = 0;
        int[] answer = new int[a.length];
        for (int i = 0; i < answer.length; i++) {
            answer[i] = a[i].get();
            sum += answer[i];
        }
        String[] percentage = new String[answer.length];
        for (int i = 0; i < answer.length; i++) {
            double percents =  10000.0 * answer[i] / sum;
            long round = Math.round(percents);
            percentage[i] = "" + round / 100 + "."
                    + (round % 100 < 10 ? "0" : "") + round % 100 + "%";
        }

        System.out.println(s + " " + sum + ": " + Arrays.toString(answer));
        System.out.println("Percentage: " + Arrays.toString(percentage));
        System.out.println();
        waitMillis(1000);
    }

    public static void printResult(AtomicInteger[] a){
        printResult("Eaten", a);
    }
    
    //����� waitMillis ���������������� ���������� �������� ������
    //�� ��������� � millis �����
    
    //����� ������ ����� ���������� ��������� ��������, ����� �� ��� � ������
    //�.�. �� ������ ���������� ������� ��� � ������,
    //� �� ������ ��������� �����(�������) ���������������� ��� ���������� �� �������� �����
    public static void waitMillis(long millis) {
        if (millis <= 0) {
            return;
        }
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
