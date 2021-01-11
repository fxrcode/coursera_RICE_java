package edu.coursera.tutorial;

/**
 * The SimpleThreads Example
 * https://docs.oracle.com/javase/tutorial/essential/concurrency/simple.html
 */
public class SimpleThread {
    static void threadMsg(String message) {
        String threadName = Thread.currentThread().getName();
        System.out.format("%s: %s%n", threadName, message);
    }

    private static class MessageLoop implements Runnable {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            String importantInfo[] = {
                "Mares eat oats",
                "Does eat oats",
                "Little lambs eat ivy",
                "A kid will eat ivy too"
            };

            try {
                for (int i = 0; i < importantInfo.length; ++i) {
                    // pause for 4 sec
                    Thread.sleep(4000);
                    // print a message
                    threadMsg(importantInfo[i]);
                }
            } catch (InterruptedException exception) {
                threadMsg("I wasn't done yet!");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // delay before we interrupt messageloop thread
        long patience = 1000 * 6;

        threadMsg("Starting MessageLoop thread");
        long startTime = System.currentTimeMillis();
        Thread t = new Thread(new MessageLoop());
        t.start();

        threadMsg("Waiting for MessageLoop Thread to finish");
        // loop until messageLoop thread exits
        while (t.isAlive()) {
            threadMsg("Still waiting...");
            // wait maximum of 1 sec for messageLoop thread to finish
            t.join(1000);
            if ( ((System.currentTimeMillis() - startTime) > patience)
                    && t.isAlive() ) {
                        threadMsg("Tired of waiting!");
                        t.interrupt();
                        // shouildn't be long now
                        // -- wait indefinitely
                        t.join();
                    }
        }
        threadMsg("Finally");
    }
}

/*-
main: Starting MessageLoop thread
main: Waiting for MessageLoop Thread to finish
main: Still waiting...
main: Still waiting...
main: Still waiting...
main: Still waiting...
Thread-0: Mares eat oats
main: Still waiting...
main: Still waiting...
main: Tired of waiting!
Thread-0: I wasn't done yet!
main: Finally
*/
