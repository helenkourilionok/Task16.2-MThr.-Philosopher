package by.training.philosophsproblem.util;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

//утилитный класс, предназначенный для выполнения не относящихся к основной задаче действий
public class Util {
	//данный метод выводит на экран время:
	//1)время в течение, которого философ ест
	//2)время в течение, которого философ думает
	//3)выводит сколько съел каждый философ
	//4)процентное соотношение,которое позволяет получить 
	//представление о голодании философов(потоков),
	//о распределении вилок между философами(ресурсов между потоками)
	//при разных затрах времени на еду и на "думание" философа(выполнение действий потоком)
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
    
    //метод waitMillis приостанавливает выполнение текущего потока
    //на указанное в millis время
    
    //также данный метод определяет поведение философа, когда он ест и думает
    //т.е. на уровне абстракции философ ест и думает,
    //а на уровне программы поток(философ) приостанавливает своё выполнение на заданное время
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
