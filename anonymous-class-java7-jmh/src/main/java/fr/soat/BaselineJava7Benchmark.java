package fr.soat;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 1000)
@Measurement(iterations = 1000)
//@Fork(value = 20, jvmArgsPrepend = { "-server", "-Xmx2g", "-XX:+TieredCompilation" })
@Fork(1)//value = 1, jvmArgsPrepend = { "-server", "-Xmx2g", "-XX:+TieredCompilation" })
@Threads(1)
public class BaselineJava7Benchmark {

    @State(Scope.Benchmark)
    public static class PersonnesContainer {

        Comparator<Personne> comparatorAsAnonymousClass = new Comparator<Personne>() {
            public int compare(Personne p1, Personne p2) {
                int nomCompaison = p1.getNom().compareTo(p2.getNom());
                return (nomCompaison != 0) ? nomCompaison : p1.getPrenom().compareTo(p2.getPrenom()) ;
            }
        };

        
        // measure for a set of dataset...
        @Param({ 
        	"100", "200", "400", "800", "1600", "3200", 
        	"6400", 
        	"12800",
//        	"25600",
        	"51200",
//        	"102400",
        	"204800",
//        	"409600",
        	"819200"        	
        	})
        int nbPersons;

        // the array of peronne to sort during invokation work
        private Personne[] personneToSortArray;

        // the unsorted "read only" array of personne
        private Personne[] unsortedPersonneArray;

        @Setup(Level.Trial)
        public void readPersonnesFromFile() throws IOException {
            // one time at benchark init, read list of personnes
            unsortedPersonneArray = PersonneProvider.load("../data/personnes.txt", nbPersons);
        }

        @Setup(Level.Iteration)
        public void reshuflePersonnesArray() throws IOException {
            // before each sort invocation,
            // restore unsorted array of personnes before sorting
            personneToSortArray = Arrays.copyOf(unsortedPersonneArray, unsortedPersonneArray.length);
        }

    }

    @Benchmark
    public Object[] anonymous_class(PersonnesContainer c) {
        Arrays.sort(c.personneToSortArray, c.comparatorAsAnonymousClass);
        return c.personneToSortArray;
    }
    

}