package runtime;

import static java.lang.System.gc;

public class Main  {

    public static void main(String[] args) {
        System.out.println("Hello World!");

        Main main = new Main();

        main.memoryStuff();
        System.out.println("------");
        main.fillMemory();
        main.memoryStuff();
        System.out.println("------");

        gc();

        main.memoryStuff();
    }

    public void memoryStuff() {
        Runtime run = Runtime.getRuntime();

        System.out.println("a: Available processors: " + run.availableProcessors());
        System.out.println("b: Max available memory: " + run.totalMemory());
        System.out.println("c: Allocated memory: " + run.maxMemory());
        System.out.println("d: Amount of allocated free memory: " + run.freeMemory());
        System.out.println("e: Amount of used memory: " + (run.totalMemory() - run.freeMemory()));
    }

    public void fillMemory() {
        String s;
        for(int i=0; i<100000; i++) {
            s = "Hello" + i;
        }

    }
}



/*
https://stackoverflow.com/questions/12807797/java-get-available-memory
a.	het aantal beschikbare processoren
b.	de totale hoeveelheid geheugen die het proces momenteel beschikbaar heeft
c.	de hoeveelheid geheugen die maximaal beschikbaar gesteld kan worden aan het proces
d.	de hoeveelheid vrij geheugen die het proces momenteel heeft
e.	de hoeveelheid geheugen die het proces momenteel gebruikt
(dat is het verschil tussen het momenteel beschikbare geheugen en het vrije geheugen)
*/