package edu.coursera.concurrent;

import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import edu.rice.pcdp.Actor;

import static edu.rice.pcdp.PCDP.finish;
/**
 * An actor-based implementation of the Sieve of Eratosthenes.
 *
 * TODO Fill in the empty SieveActorActor actor class below and use it from
 * countPrimes to determin the number of primes <= limit.
 */
public final class SieveActor extends Sieve {
    public static void main(String[] args) {
        int limit = 20_000;
        int totalPrimes = new SieveActor().countPrimes(limit);
        System.out.println("Total primes: " + totalPrimes);
    }

    /**
     * {@inheritDoc}
     *
     * TODO Use the SieveActorActor class to calculate the number of primes <= limit
     * in parallel. You might consider how you can model the Sieve of Eratosthenes
     * as a pipeline of actors, each corresponding to a single prime number.
     */
    @Override
    public int countPrimes(final int limit) {
        // throw new UnsupportedOperationException();
        SieveActorActor sieveActor = new SieveActorActor(2);

        finish(() -> {
            for (int i = 3; i <= limit; i+=2) {
                sieveActor.send(i);
            }
            sieveActor.send(0);
        });

        int numPrimes = 0;
        SieveActorActor loopActor = sieveActor;
        while (loopActor != null) {
            System.out.println( loopActor.getActorPrimeId() );

            numPrimes += loopActor.getNumLocalPrimes();
            loopActor = loopActor.nextActor;
        }
        return numPrimes;
    }

    /**
     * An actor class that helps implement the Sieve of Eratosthenes in parallel.
     */
    public static final class SieveActorActor extends Actor {
        // local states
        private static final int MAX_LOCAL_PRIMES = 1000;
        private SieveActorActor nextActor = null;
        private Set<Integer> localPrimes = new HashSet<>(MAX_LOCAL_PRIMES);
        private int actorPrimeId;

        public SieveActorActor(int localPrime) {
            this.localPrimes.add(localPrime);
            this.actorPrimeId = localPrime;
        }

        public int getActorPrimeId() {
            return actorPrimeId;
        }

        /**
         * Process a single message sent to this actor.
         *
         * TODO complete this method.
         *
         * @param msg Received message
         */
        @Override
        public void process(final Object msg) {
            final int candidate = (Integer) msg;
            if (candidate <= 0) {
                if (nextActor != null) {
                    nextActor.send(msg);
                }
                return;
            } else {
                final boolean locallyPrime = isLocalPrime(candidate);

                if (locallyPrime) {
                    if (getNumLocalPrimes() < MAX_LOCAL_PRIMES) {
                        localPrimes.add(candidate);
                    } else {
                        if (nextActor == null) {
                            // System.out.println(" create new Actor for: "+ candidate + "\t, at: " +  Instant.now().toEpochMilli());
                            nextActor = new SieveActorActor(candidate);
                        }
                        nextActor.send(msg);
                    }
                }
            }
        }

        boolean isLocalPrime(int candidate) {
            for (int prime : localPrimes) {
                if (candidate % prime == 0) {
                    return false;
                }
            }
            return true;
        }

        public int getNumLocalPrimes() {
            return localPrimes.size();
        }
    }
}
