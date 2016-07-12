package by.training.philosophsproblem.entity;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//объекты класса Fork(вилка) - это ресурсы, которые будут захватывать потоки(философы)
public class Fork {

    private final Lock lock = new ReentrantLock();
    //lock объёкт для блокировки ресурса потока
    //объект lock создаётся с парметром fair = false
    //это значит, что ОС по определённому алгоритму
    //выбирает поток, который после разблокировки ресурса может его захватить
    
    //метод onTake - используется для того, чтобы заблокировать ресурс(философ берёт вилку со стола)
    public void onTake() {
        lock.lock();
    }

    //метод onPut - используется для того, чтобы разблокировать ресурс(философ ложит вилку на стол)
    public void onPut() {
        lock.unlock();
    }
}
