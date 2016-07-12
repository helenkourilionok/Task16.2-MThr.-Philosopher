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
    //список философов
    private final int n;
    //количество философов(количество потоков)
    private final long thinkTime;
    //время, в течение которого философ думает
    private final long eatTime;
    //время,в течение которого философ ест

    public SemaphoreEngine(int n, long eatTime, long thinkTime) {
        this.n = n;
        this.eatTime = eatTime;
        this.thinkTime = thinkTime;        
    }

    public void newRun() {
    	//создание официанта, который будет следить, чтобы
    	//за столом одновременно было n-1 философов(n - общее количество философов)
    	
    	//создаётся объект синхронизации семафор 
    	//который позволит получить доступ к ресурсу (n-1)
        Semaphore waiter = new Semaphore(n - 1);
        //массив для хранения по каждому филоофу
        //количества еды которой он съел
        AtomicInteger[] eaten = new AtomicInteger[n];
        //создаётся список вилок(разделяемых ресурсов)
        List<Fork> forks = new ArrayList<>();
        //в список добавляется столько вилок сколько философов
        logger.debug("Подготовительные действия перед началом обеда.");
        for (int i = 0; i < n; i++) {
            forks.add(new Fork());
            logger.debug((i+1)+" "+"-ая вилка создана");
        }
        logger.debug("Все вилки созданы");
        //в данном цикле каждому философу назначаются вилки, которые он может брать
        //(нумерация философов и вилок с нуля)
        //0 - (0,1)
        //1 - (1,2)
        //2 - (2,3)
        //3 - (3,4)
        //4 - (4,0)
        for (int i = 0; i < n; i++) {
        	//инициализируем количество съеденной еды философами
            eaten[i] = new AtomicInteger(0);
            //из списка вилок получаем левую и правую вилки для философов
            Fork left = forks.get(i);
            Fork right = forks.get((i + 1) % n);
            //создаём философа (1,2,3,4,5,6)
            //1 - официант(семафор) у всех философов один и тот же
            //2 - переменная для хранения объёма еды, который съел философ
            //3 - время, в течение которого философ ест(у всех философов одинаковое)
            //4 - время, в течение которого философ думает(у всех философов одинаковое)
            //5 - левая вилка философа
            //6 - правая вилка философа
            //и добавляем философа в список философов
            philosophers.add(new Philosopher(waiter, eaten[i], eatTime, thinkTime, left, right));
            logger.debug("Создан "+(i+1)+"-ый философ");
        }
        logger.debug("Все философы созданы и готовы кушать.");
        //создание списка потоков
        List<Thread> philosopherThreads = new ArrayList<>();
        for (int i = 0; i < n; i++) {
        	//создание потока
            Thread philosopherThread = new Thread(philosophers.get(i));
            //добавление потока в список потоков
            philosopherThreads.add(philosopherThread);
            //запуск потока
            philosopherThread.start();
            
        }
        //приостанавливаем выполнение текущего потока
        Util.waitMillis(10 * SECONDS);
        //после приостановки главного потока
        //приостанавливаем дочерние потоки
        logger.debug("Пора заканчивать обед");
        logger.debug("Просьба всем философам удалиться из-за стола");
        philosopherThreads.stream().forEach(Thread::interrupt);
        logger.debug("Обед завершён");
        //делаем паузу в одну секунду
        Util.waitMillis(1000);
        //выводим результаты работы потоков
        Util.printResult(eaten);
    }

}
