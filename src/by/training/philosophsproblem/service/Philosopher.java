package by.training.philosophsproblem.service;


import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.training.philosophsproblem.entity.Fork;
import by.training.philosophsproblem.util.Util;


//Philosopher(философ) - класс,который определяет поведение философа(потока)
public class Philosopher implements Runnable {

	private static final Logger logger = LogManager.getRootLogger();
    private final Semaphore waiter;
    //официант(объект синхронизации), который определяет
    //может ли сейчас текущий философ(поток) сесть за стол(получить доступ к ресурсу) или 
    //ему нужно ожидать освобождения места за столом(ожидать осовобождения ресурса)
    private final AtomicInteger eaten;
    //поле, которое предназначено для хранения количества еды съеденной философом
    private final long eatTime;
    //время, в течение которого философ ест(поток использует ресурсы)
    private final long thinkTime;
    //время, в течение которого филосов думает(поток выполняет операции без использования ресурсов)
    private final Fork fork1;//правая вилка(первый захватываемый ресурс)
    private final Fork fork2;//левая вилка(второй захватываемый ресурс)
    private int count;//поле определяет сколько съел философ

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
    	//философ будет есть и думать,есть и думать и так пока его не попросят выйти из-за стола
    	//т.е. философ может есть много раз
    	//цикл выполняется пока поток не будет прерван т.е. пока не будет вызван метод interrupt
    	logger.debug("Философ "+Thread.currentThread().getName()+" подошёл к столу");
        while (!Thread.currentThread().isInterrupted()) {
            try {
                waiter.acquire();
                //философ просит разрешения сесть за стол у официанта
                //поток просит разрешения для доступа к ресурсу
                
                //если официант отказал философу,
                //то философ вынужден ожидать пока один из сидящих философов не выйдет из-за стола
                
                //если количество разрешений на доступ к ресурсу у семафора равен нулю,то
                //поток вынужден ожидать пока один из потоков получивших разрешение на ресурс
                //не освободят его
                
                logger.debug("Философ "+Thread.currentThread().getName()+" подошёл к столу");
                
                //если официант разрешил философу сесть за стол, то 
                //философ берёт правую вилку - take(fork1)
                //философ берёт левую вилку - take(fork1)
                
                //поток получает разрешение на доступ к ресурсам
                //поток блокирует объекты fork1 и fork2,
                //чтобы потом выполнять над ними те или иные действия 
                take(fork1);
                take(fork2);
                
                logger.debug("Философ "+Thread.currentThread().getName()+" кушает");
                
                //философ ест т.к. теперь у него есть для этого две вилки
                //поток выполняет над заблокированными ресурсами те или иные
                //действия - логику потока (в данном случае вызывается метод sleep)
                eat();
                //философ поел и теперь ложит на стол правую вилку put(fork1),
                //потом левую вилку put(fork2)
                
                //поток снимает блокировку с захваченных им объектов fork1 и fork2
                put(fork1);
                put(fork2);
                
                //филсоф сидит за столом и думает
                //поток выполняет те или действия согласно логике программы
                //(в данном случае sleep)
                think();
            } catch (InterruptedException e) {
                logger.error("Philosopher can't eat",e);
            } finally {
                waiter.release();//философ выходит из-за стола
                logger.debug("Философ "+Thread.currentThread().getName()+" вышел из-за стола");
                //поток освобождает разрешение на доступ к ресурсам
                think();
                //философ вышел из-за стола и думает,чтобы дать
                //другому философу возможность сесть за стол
                //поток выполняет какие-лбо действия;в этот момент 
                //другой поток может получить разрешение на доступ к ресурсу
            }
        }
        eaten.set(count);//позволяет потокобезопасно поместить количество еды eaten
    }
    //метод для взятия вилки философом
    //поток блокирует,захватывает объект fork
    private void take(Fork fork) {
        fork.onTake();
    }
    //метод позволяющий философу положить вилку на стол
    //поток снимает блокировку с захваченного объекта fork
    private void put(Fork fork) {
        fork.onPut();
    }
    //метод благодаря которому философ ест
    //метод определяющий логику потока
    //(в реальной программе данный метод по логике должен использовать захваченные ресурсы)
    private void eat() {
    	logger.debug("Философ "+Thread.currentThread().getName()+" кушает");
        count++;
        Util.waitMillis(eatTime);
    }
    //метод благодаря которому философ думает
    //метод определяющий действия выполняемые потокм
    //без использования ресурсов
    private void think() {
    	logger.debug("Философ "+Thread.currentThread().getName()+" думает");
        Util.waitMillis(thinkTime);
    }

}
