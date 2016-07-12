package by.training.philosophsproblem.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import by.training.philosophsproblem.entity.Fork;
import by.training.philosophsproblem.util.Util;


public class SemaphoreEngine {
	private final static Logger logger = LogManager.getRootLogger();
	
    static final int SECONDS = 2;

    final List<Philosopher> philosophers = new ArrayList<>();
    //������ ���������
    private final int n;
    //���������� ���������(���������� �������)
    private final long thinkTime;
    //�����, � ������� �������� ������� ������
    private final long eatTime;
    //�����,� ������� �������� ������� ���

    public SemaphoreEngine(int n, long eatTime, long thinkTime) {
        this.n = n;
        this.eatTime = eatTime;
        this.thinkTime = thinkTime;        
    }

    public void newRun() {
    	//�������� ���������, ������� ����� �������, �����
    	//�� ������ ������������ ���� n-1 ���������(n - ����� ���������� ���������)
    	
    	//�������� ������ ������������� ������� 
    	//������� �������� �������� ������ � ������� (n-1)
        Semaphore waiter = new Semaphore(n - 1);
        //������ ��� �������� �� ������� �������
        //���������� ��� ������� �� ����
        AtomicInteger[] eaten = new AtomicInteger[n];
        //�������� ������ �����(����������� ��������)
        List<Fork> forks = new ArrayList<>();
        //� ������ ����������� ������� ����� ������� ���������
        logger.debug("���������������� �������� ����� ������� �����.");
        for (int i = 0; i < n; i++) {
            forks.add(new Fork());
            logger.debug((i+1)+" "+"-�� ����� �������");
        }
        logger.debug("��� ����� �������");
        //� ������ ����� ������� �������� ����������� �����, ������� �� ����� �����
        //(��������� ��������� � ����� � ����)
        //0 - (0,1)
        //1 - (1,2)
        //2 - (2,3)
        //3 - (3,4)
        //4 - (4,0)
        for (int i = 0; i < n; i++) {
        	//�������������� ���������� ��������� ��� ����������
            eaten[i] = new AtomicInteger(0);
            //�� ������ ����� �������� ����� � ������ ����� ��� ���������
            Fork left = forks.get(i);
            Fork right = forks.get((i + 1) % n);
            //������ �������� (1,2,3,4,5,6)
            //1 - ��������(�������) � ���� ��������� ���� � ��� ��
            //2 - ���������� ��� �������� ������ ���, ������� ���� �������
            //3 - �����, � ������� �������� ������� ���(� ���� ��������� ����������)
            //4 - �����, � ������� �������� ������� ������(� ���� ��������� ����������)
            //5 - ����� ����� ��������
            //6 - ������ ����� ��������
            //� ��������� �������� � ������ ���������
            philosophers.add(new Philosopher(waiter, eaten[i], eatTime, thinkTime, left, right));
            logger.debug("������ "+(i+1)+"-�� �������");
        }
        logger.debug("��� �������� ������� � ������ ������.");
        //�������� ������ �������
        List<Thread> philosopherThreads = new ArrayList<>();
        for (int i = 0; i < n; i++) {
        	//�������� ������
            Thread philosopherThread = new Thread(philosophers.get(i));
            //���������� ������ � ������ �������
            philosopherThreads.add(philosopherThread);
            //������ ������
            philosopherThread.start();
            
        }
        //���������������� ���������� �������� ������
        Util.waitMillis(10 * SECONDS);
        //����� ������������ �������� ������
        //���������������� �������� ������
        logger.debug("���� ����������� ����");
        logger.debug("������� ���� ��������� ��������� ��-�� �����");
        philosopherThreads.stream().forEach(Thread::interrupt);
        logger.debug("���� ��������");
        //������ ����� � ���� �������
        Util.waitMillis(1000);
        //������� ���������� ������ �������
        Util.printResult(eaten);
    }

}
