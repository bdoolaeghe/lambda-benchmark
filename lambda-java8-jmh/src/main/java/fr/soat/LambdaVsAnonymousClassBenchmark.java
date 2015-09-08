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
@Warmup(iterations = 5000)
@Measurement(iterations = 5000)
//@Fork(value = 20, jvmArgsPrepend = { "-server", "-Xmx2g", "-XX:+TieredCompilation" })
@Fork(1)//value = 1, jvmArgsPrepend = { "-server", "-Xmx2g", "-XX:+TieredCompilation" })
@Threads(1)
public class LambdaVsAnonymousClassBenchmark {

    @State(Scope.Benchmark)
    public static class PersonnesContainer {

        // measure for a set of dataset...
        @Param({ 
//        	"100", "200", "400", "800", "1600", "3200", 
        	"6400", 
//        	"12800"
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
        Arrays.sort(c.personneToSortArray, new Comparator<Personne>() {
            public int compare(Personne p1, Personne p2) {
                int nomCompaison = p1.getNom().compareTo(p2.getNom());
                return (nomCompaison != 0) ? nomCompaison : p1.getPrenom().compareTo(p2.getPrenom()) ;
            }
        });
        return c.personneToSortArray;
    }
    
    @Benchmark
    public Object[] lambda(PersonnesContainer c) {
        Arrays.sort(c.personneToSortArray,
                (p1, p2) -> {
	                	int nomCompaison = p1.getNom().compareTo(p2.getNom());
	                	return (nomCompaison != 0) ? nomCompaison : p1.getPrenom().compareTo(p2.getPrenom()) ;
                });        
        return c.personneToSortArray;
    }
    
    
    @Benchmark
    public Object[] lambda_then(PersonnesContainer c) {
    	Comparator<Personne> comparatorNom = (p1, p2) -> p1.getNom().compareTo(p2.getNom()); 
    	Comparator<Personne> comparatorPrenom = (p1, p2) -> p1.getPrenom().compareTo(p2.getPrenom());

    	Arrays.sort(c.personneToSortArray,
                comparatorNom.thenComparing(comparatorPrenom));        
        return c.personneToSortArray;
    }
    
    @Benchmark
    public Object[] lambda_comparator(PersonnesContainer c) {
        Arrays.sort(c.personneToSortArray,
                Comparator
                .comparing((Personne p) -> p.getNom())
                .thenComparing((Personne p) -> p.getPrenom()));
        
        return c.personneToSortArray;
    }
    
    @Benchmark
    public Object[] methodFreference_comparator(PersonnesContainer c) {
        Arrays.sort(c.personneToSortArray,
                Comparator
                .comparing(Personne::getNom)
                .thenComparing(Personne::getPrenom));
        return c.personneToSortArray;
    }

    @Benchmark
    public Object[] stream_lambda(PersonnesContainer c) {
		return Arrays.stream(c.personneToSortArray)
			  .sorted((p1, p2) -> {
				  		int nomCompaison = p1.getNom().compareTo(p2.getNom());
				  		return (nomCompaison != 0) ? nomCompaison : p1.getPrenom().compareTo(p2.getPrenom());
			  		 })
			  .toArray();
    }

    @Benchmark
    public Object[] stream_lambda_then(PersonnesContainer c) {
    	Comparator<Personne> comparatorNom = (p1, p2) -> p1.getNom().compareTo(p2.getNom()); 
    	Comparator<Personne> comparatorPrenom = (p1, p2) -> p1.getPrenom().compareTo(p2.getPrenom());

    	return Arrays.stream(c.personneToSortArray)
			  .sorted(comparatorNom.thenComparing(comparatorPrenom))
			  .toArray();
    }
    
    @Benchmark
    public Object[] stream_lambda_comparator(PersonnesContainer c) {
		return Arrays.stream(c.personneToSortArray)
			  .sorted(Comparator
		                .comparing((Personne p) -> p.getNom())
		                .thenComparing((Personne p) -> p.getPrenom()))
			  .toArray();
    }

    @Benchmark
    public Object[] stream_method_reference_comparator(PersonnesContainer c) {
		return Arrays.stream(c.personneToSortArray)
			  .sorted(Comparator
		                .comparing(Personne::getNom)
		                .thenComparing(Personne::getPrenom))
			  .toArray();
    }

    /* parallel sorts */
    
    @Benchmark
    public Object[] parallel_anonymous_class(PersonnesContainer c) {
        Arrays.parallelSort(c.personneToSortArray, new Comparator<Personne>() {
            public int compare(Personne p1, Personne p2) {
                int nomCompaison = p1.getNom().compareTo(p2.getNom());
                return (nomCompaison != 0) ? nomCompaison : p1.getPrenom().compareTo(p2.getPrenom()) ;
            }
        });
        return c.personneToSortArray;
    }
    
    @Benchmark
    public Object[] parallel_lambda(PersonnesContainer c) {
        Arrays.parallelSort(c.personneToSortArray,
                (p1, p2) -> {
	                	int nomCompaison = p1.getNom().compareTo(p2.getNom());
	                	return (nomCompaison != 0) ? nomCompaison : p1.getPrenom().compareTo(p2.getPrenom()) ;
                });        
        return c.personneToSortArray;
    }
    
    
    @Benchmark
    public Object[] parallel_lambda_then(PersonnesContainer c) {
    	Comparator<Personne> comparatorNom = (p1, p2) -> p1.getNom().compareTo(p2.getNom()); 
    	Comparator<Personne> comparatorPrenom = (p1, p2) -> p1.getPrenom().compareTo(p2.getPrenom());

    	Arrays.parallelSort(c.personneToSortArray,
                comparatorNom.thenComparing(comparatorPrenom));        
        return c.personneToSortArray;
    }
    
    @Benchmark
    public Object[] parallel_lambda_comparator(PersonnesContainer c) {
        Arrays.parallelSort(c.personneToSortArray,
                Comparator
                .comparing((Personne p) -> p.getNom())
                .thenComparing((Personne p) -> p.getPrenom()));
        
        return c.personneToSortArray;
    }
    
    @Benchmark
    public Object[] parallel_methodFreference_comparator(PersonnesContainer c) {
        Arrays.parallelSort(c.personneToSortArray,
                Comparator
                .comparing(Personne::getNom)
                .thenComparing(Personne::getPrenom));
        return c.personneToSortArray;
    }

    
    @Benchmark
    public Object[] parallel_stream_lambda(PersonnesContainer c) {
		return Arrays.stream(c.personneToSortArray)
		      .parallel()
			  .sorted((p1, p2) -> {
				  		int nomCompaison = p1.getNom().compareTo(p2.getNom());
				  		return (nomCompaison != 0) ? nomCompaison : p1.getPrenom().compareTo(p2.getPrenom());
			  		 })
			  .toArray();
    }

    @Benchmark
    public Object[] parallel_stream_lambda_then(PersonnesContainer c) {
    	Comparator<Personne> comparatorNom = (p1, p2) -> p1.getNom().compareTo(p2.getNom()); 
    	Comparator<Personne> comparatorPrenom = (p1, p2) -> p1.getPrenom().compareTo(p2.getPrenom());

    	return Arrays.stream(c.personneToSortArray)
  		      .parallel()
    		  .sorted(comparatorNom.thenComparing(comparatorPrenom))
			  .toArray();
    }
    
    @Benchmark
    public Object[] parallel_stream_lambda_comparator(PersonnesContainer c) {
		return Arrays.stream(c.personneToSortArray)
			  .parallel()
			  .sorted(Comparator
		                .comparing((Personne p) -> p.getNom())
		                .thenComparing((Personne p) -> p.getPrenom()))
			  .toArray();
    }

    @Benchmark
    public Object[] parallel_stream_method_reference_comparator(PersonnesContainer c) {
		return Arrays.stream(c.personneToSortArray)
			  .parallel()
			  .sorted(Comparator
		                .comparing(Personne::getNom)
		                .thenComparing(Personne::getPrenom))
			  .toArray();
    }
    
//    @Benchmark
//    public Object[] anonymous_class_for_comparator_in_parallel(PersonnesContainer c) {
//        Arrays.parallelSort(c.personneToSortArray, new Comparator<Personne>() {
//            public int compare(Personne p1, Personne p2) {
//                int nomCompaison = p1.getNom().compareTo(p2.getNom());
//                return (nomCompaison != 0) ? nomCompaison : p1.getPrenom().compareTo(p2.getPrenom()) ;
//            }
//        });
//        return c.personneToSortArray;
//    }
//
//    @Benchmark
//    public Object[] lambda_as_compartor_in_parallel(PersonnesContainer c) {
//        Arrays.parallelSort(c.personneToSortArray,
//                (p1, p2) -> {
//	                	int nomCompaison = p1.getNom().compareTo(p2.getNom());
//	                	return (nomCompaison != 0) ? nomCompaison : p1.getPrenom().compareTo(p2.getPrenom()) ;
//                });        
//        return c.personneToSortArray;
//    }
//    
//    @Benchmark
//    public Object[] lambda_comparing_in_parallel(PersonnesContainer c) {
//        Arrays.parallelSort(c.personneToSortArray,
//                Comparator
//                .comparing((Personne p) -> p.getNom())
//                .thenComparing((Personne p) -> p.getPrenom()));
//        
//        return c.personneToSortArray;
//    }
//    
//    @Benchmark
//    public Object[] methodFreference_comparing_in_parallel(PersonnesContainer c) {
//        Arrays.parallelSort(c.personneToSortArray,
//                Comparator
//                .comparing(Personne::getNom)
//                .thenComparing(Personne::getPrenom));
//        return c.personneToSortArray;
//    }
//    
//    @Benchmark
//    public Object[] lambda_with_stream_in_parallel(PersonnesContainer c) {
//		return  Arrays.stream(c.personneToSortArray)
//			  .parallel()
//			  .sorted((p1, p2) -> {
//				  		int nomCompaison = p1.getNom().compareTo(p2.getNom());
//				  		return (nomCompaison != 0) ? nomCompaison : p1.getPrenom().compareTo(p2.getPrenom());
//			  		 })
//			  .toArray();
//    }
//
//
//    @Benchmark
//    public Object[] lambda_comparator_with_stream_in_parallel(PersonnesContainer c) {
//		return Arrays.stream(c.personneToSortArray)
//			  .parallel()
//			  .sorted(Comparator
//		                .comparing((Personne p) -> p.getNom())
//		                .thenComparing((Personne p) -> p.getPrenom()))
//			  .toArray();
//    }
//
//    @Benchmark
//    public Object[] method_reference_comparator_with_stream_in_parallel(PersonnesContainer c) {
//		return (Personne[]) Arrays.stream(c.personneToSortArray)
//			  .parallel()
//			  .sorted(Comparator
//		                .comparing(Personne::getNom)
//		                .thenComparing(Personne::getPrenom))
//			  .toArray();
//    }

	
}
