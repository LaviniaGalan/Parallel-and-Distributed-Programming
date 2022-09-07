package com.company;

import com.company.DSM.DSM;
import com.company.DSM.DSMObserver;
import mpi.MPI;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        MPI.Init(args);

        int me = MPI.COMM_WORLD.Rank();

        DSM dsm = new DSM();
        DSMObserver dsmObserver = new DSMObserver(dsm);
        Thread thread = new Thread(dsmObserver);
        thread.start();

        if(me == 0){
            dsm.subscribe("a");
            dsm.subscribe("b");
            dsm.subscribe("c");

            dsm.compareAndExchange("a", 0, 100);
            dsm.compareAndExchange("c", 0, 200);
            dsm.compareAndExchange("b", 0, 150);


            dsm.close();
        }
        else if(me == 1){
            dsm.subscribe("a");
            dsm.subscribe("b");
            dsm.subscribe("c");

        }
        else if(me == 2){
            dsm.subscribe("b");
            dsm.subscribe("c");
        }
        else if(me == 3){
            dsm.subscribe("b");
        }

        thread.join();
        MPI.Finalize();
    }
}
