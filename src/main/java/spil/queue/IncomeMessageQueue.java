package spil.queue;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by timestop on 13.03.17.
 */
public class IncomeMessageQueue implements Runnable {
    private Observable observable = new Observable();
    private ConcurrentLinkedQueue<Message> queue = new ConcurrentLinkedQueue<Message>();

    private boolean isRunned = false;
    private Object lock = new Object();
    private long delay = 100L;


    public IncomeMessageQueue(long delay) {
        this.delay = delay;
    }

    public void putMessage(Message message) {
        if (this.isStarted()) {
            queue.add(message);
        }
    }

    private Message getMessage() {
        if (queue.isEmpty()) {
            return null;
        }
        return queue.poll();
    }

    public void addObserver(IncomeMessageListener listener) {
        observable.addObserver(new QueueObserver(listener));
    }

    public void removeObserver(IncomeMessageListener listener) {
        observable.addObserver(new QueueObserver(listener));
    }

    public void start() {
        synchronized (lock) {
            isRunned = true;
        }
    }

    public void stop() {
        synchronized (lock) {
            isRunned = false;
        }
    }

    public boolean isStarted() {
        synchronized (lock) {
            return isRunned;
        }
    }

    public void run() {
        try {
            while (true) {
                if (this.isStarted()) {
                    Message message = this.getMessage();
                    if (message != null) {
                        observable.notifyObservers(message);
                    } else {
                        Thread.sleep(delay);
                    }
                } else {
                    Thread.sleep(delay);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class QueueObserver implements Observer {
        IncomeMessageListener listener;

        public QueueObserver(IncomeMessageListener listener) {
            this.listener = listener;
        }

        public void update(Observable observable, Object o) {
            listener.reciveMessage((Message) o);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            QueueObserver that = (QueueObserver) o;

            return listener != null ? listener.equals(that.listener) : that.listener == null;
        }

        @Override
        public int hashCode() {
            return listener != null ? listener.hashCode() : 0;
        }
    }
}
