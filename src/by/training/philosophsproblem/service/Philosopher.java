package by.training.philosophsproblem.service;


import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.training.philosophsproblem.entity.Fork;
import by.training.philosophsproblem.util.Util;


//Philosopher(�������) - �����,������� ���������� ��������� ��������(������)
public class Philosopher implements Runnable {

	private static final Logger logger = LogManager.getRootLogger();
    private final Semaphore waiter;
    //��������(������ �������������), ������� ����������
    //����� �� ������ ������� �������(�����) ����� �� ����(�������� ������ � �������) ��� 
    //��� ����� ������� ������������ ����� �� ������(������� ������������� �������)
    private final AtomicInteger eaten;
    //����, ������� ������������� ��� �������� ���������� ��� ��������� ���������
    private final long eatTime;
    //�����, � ������� �������� ������� ���(����� ���������� �������)
    private final long thinkTime;
    //�����, � ������� �������� ������� ������(����� ��������� �������� ��� ������������� ��������)
    private final Fork fork1;//������ �����(������ ������������� ������)
    private final Fork fork2;//����� �����(������ ������������� ������)
    private int count;//���� ���������� ������� ���� �������

    public Philosopher(Semaphore waiter, AtomicInteger eaten,
                       long eatTime, long thinkTime,
                       Fork fork1, Fork fork2
    ) {
        this.waiter = waiter;
        this.eaten = eaten;
        this.eatTime = eatTime;
        this.thinkTime = thinkTime;
        this.fork1 = fork1;
        this.fork2 = fork2;
    }

    @Override
    public void run() {
    	//������� ����� ���� � ������,���� � ������ � ��� ���� ��� �� �������� ����� ��-�� �����
    	//�.�. ������� ����� ���� ����� ���
    	//���� ����������� ���� ����� �� ����� ������� �.�. ���� �� ����� ������ ����� interrupt
    	logger.debug("������� "+Thread.currentThread().getName()+" ������� � �����");
        while (!Thread.currentThread().isInterrupted()) {
            try {
                waiter.acquire();
                //������� ������ ���������� ����� �� ���� � ���������
                //����� ������ ���������� ��� ������� � �������
                
                //���� �������� ������� ��������,
                //�� ������� �������� ������� ���� ���� �� ������� ��������� �� ������ ��-�� �����
                
                //���� ���������� ���������� �� ������ � ������� � �������� ����� ����,��
                //����� �������� ������� ���� ���� �� ������� ���������� ���������� �� ������
                //�� ��������� ���
                
                logger.debug("������� "+Thread.currentThread().getName()+" ������� � �����");
                
                //���� �������� �������� �������� ����� �� ����, �� 
                //������� ���� ������ ����� - take(fork1)
                //������� ���� ����� ����� - take(fork1)
                
                //����� �������� ���������� �� ������ � ��������
                //����� ��������� ������� fork1 � fork2,
                //����� ����� ��������� ��� ���� �� ��� ���� �������� 
                take(fork1);
                take(fork2);
                
                logger.debug("������� "+Thread.currentThread().getName()+" ������");
                
                //������� ��� �.�. ������ � ���� ���� ��� ����� ��� �����
                //����� ��������� ��� ���������������� ��������� �� ��� ����
                //�������� - ������ ������ (� ������ ������ ���������� ����� sleep)
                eat();
                //������� ���� � ������ ����� �� ���� ������ ����� put(fork1),
                //����� ����� ����� put(fork2)
                
                //����� ������� ���������� � ����������� �� �������� fork1 � fork2
                put(fork1);
                put(fork2);
                
                //������ ����� �� ������ � ������
                //����� ��������� �� ��� �������� �������� ������ ���������
                //(� ������ ������ sleep)
                think();
            } catch (InterruptedException e) {
                logger.error("Philosopher can't eat",e);
            } finally {
                waiter.release();//������� ������� ��-�� �����
                logger.debug("������� "+Thread.currentThread().getName()+" ����� ��-�� �����");
                //����� ����������� ���������� �� ������ � ��������
                think();
                //������� ����� ��-�� ����� � ������,����� ����
                //������� �������� ����������� ����� �� ����
                //����� ��������� �����-��� ��������;� ���� ������ 
                //������ ����� ����� �������� ���������� �� ������ � �������
            }
        }
        eaten.set(count);//��������� ��������������� ��������� ���������� ��� eaten
    }
    //����� ��� ������ ����� ���������
    //����� ���������,����������� ������ fork
    private void take(Fork fork) {
        fork.onTake();
    }
    //����� ����������� �������� �������� ����� �� ����
    //����� ������� ���������� � ������������ ������� fork
    private void put(Fork fork) {
        fork.onPut();
    }
    //����� ��������� �������� ������� ���
    //����� ������������ ������ ������
    //(� �������� ��������� ������ ����� �� ������ ������ ������������ ����������� �������)
    private void eat() {
    	logger.debug("������� "+Thread.currentThread().getName()+" ������");
        count++;
        Util.waitMillis(eatTime);
    }
    //����� ��������� �������� ������� ������
    //����� ������������ �������� ����������� ������
    //��� ������������� ��������
    private void think() {
    	logger.debug("������� "+Thread.currentThread().getName()+" ������");
        Util.waitMillis(thinkTime);
    }

}
