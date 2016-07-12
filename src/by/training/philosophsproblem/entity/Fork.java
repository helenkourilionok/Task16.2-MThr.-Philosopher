package by.training.philosophsproblem.entity;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//������� ������ Fork(�����) - ��� �������, ������� ����� ����������� ������(��������)
public class Fork {

    private final Lock lock = new ReentrantLock();
    //lock ������ ��� ���������� ������� ������
    //������ lock �������� � ��������� fair = false
    //��� ������, ��� �� �� ������������ ���������
    //�������� �����, ������� ����� ������������� ������� ����� ��� ���������
    
    //����� onTake - ������������ ��� ����, ����� ������������� ������(������� ���� ����� �� �����)
    public void onTake() {
        lock.lock();
    }

    //����� onPut - ������������ ��� ����, ����� �������������� ������(������� ����� ����� �� ����)
    public void onPut() {
        lock.unlock();
    }
}
