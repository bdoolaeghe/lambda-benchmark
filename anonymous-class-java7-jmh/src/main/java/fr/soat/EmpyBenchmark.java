package fr.soat;



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

import com.github.javafaker.Faker;


////@BenchmarkMode( Mode.SampleTime)
//@BenchmarkMode( Mode.Throughput)
////@OutputTimeUnit(TimeUnit.MILLISECONDS)
////@Warmup(iterations = 5, time = 3, timeUnit = TimeUnit.SECONDS)
////@Measurement(iterations = 20, time = 3, timeUnit = TimeUnit.SECONDS)
////@Fork(1)
////@State(Scope.Benchmark)
////@BenchmarkMode(Mode.SampleTime)
//@OutputTimeUnit(TimeUnit.MILLISECONDS)
//@Warmup(iterations = 10)//, time = 1, timeUnit = TimeUnit.NANOSECONDS)
////@Warmup(time = 200, timeUnit = TimeUnit.MILLISECONDS)
//@Measurement(iterations = 30)//, time = 100, timeUnit = TimeUnit.MILLISECONDS)
////@Measurement(time = 200, timeUnit = TimeUnit.MILLISECONDS)
//@Fork(5)
//@State(Scope.Thread) // all fields of object will be local to threads of benchmark
//@Threads(Threads.MAX)
//public class SimplePersonneSorter7Benchmark {
//
//	// benchmarked service
////	private PersonneSorter8 sortBean = new PersonneSorter8();
//
//	// iterations for each measure
////	@Param({ "200", "400", "800", "1600" })
////	@Param({ "10", "100", "1000" })
////	public int nbPersons;
//
//	// array to sort
//	private Personne[] persons;
//
//	@Setup
//	public void init() {
//		// init the array of personne
//		persons = new Personne[] {
//			new Personne("Jen", "Barber"),
//			new Personne("Roy", "Trenneman"),
//			new Personne("Maurice", "Moss"),
//			new Personne("Deynholm", "Reynholm"),
//			new Personne("Richmond", "Avenal"),
//			new Personne("Douglas" , "Reynholm")			
//		};
//	}
//
//	@Benchmark
//    public void benchmarkSort() {
//		Arrays.sort(persons, new Comparator<Personne>() {
//			public int compare(Personne p1, Personne p2) {
//				return p1.getNom().compareTo(p2.getNom());
//			}
//		});
//    }
//	
//}
//

//@BenchmarkMode(Mode.SampleTime)
//@OutputTimeUnit(TimeUnit.MICROSECONDS)
//@Warmup(iterations = 10)
//@Measurement(iterations = 8)
//@Fork(1)
//@State(Scope.Thread)
//@Threads(1)
//public class AnonymousClassBenchmark {
//
//	// array to sort
//	private Personne[] persons;
//
//	@Setup(Level.Iteration)
//	public void init() {
//		// init the array of personne
//		persons = new Personne[] { new Personne("Jen", "Barber"),
//				new Personne("Roy", "Trenneman"),
//				new Personne("Maurice", "Moss"),
//				new Personne("Deynholm", "Reynholm"),
//				new Personne("Richmond", "Avenal"),
//				new Personne("Douglas", "Reynholm") };
//	}
//
//	@Benchmark
//	public void benchmarkSort() {
//		// Here is my benchmark code (sort array)
//		// Personne[] persons = new Personne[] {
//		// new Personne("Jen", "Barber"),
//		// new Personne("Roy", "Trenneman"),
//		// new Personne("Maurice", "Moss"),
//		// new Personne("Deynholm", "Reynholm"),
//		// new Personne("Richmond", "Avenal"),
//		// new Personne("Douglas" , "Reynholm")};
//
//		// use an anonymous class to immplement a Comparator
//		Arrays.sort(persons, new Comparator<Personne>() {
//			public int compare(Personne p1, Personne p2) {
//				return p1.getNom().compareTo(p2.getNom());
//			}
//		});
//	}
//}

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.MILLISECONDS)
@Fork(1)
@State(Scope.Thread)
@Threads(1)
public class EmpyBenchmark {

//	@Param({"128", "256", "512", "1024", "2048", "4092", "8184", "16368" })
//	public int nbPersons;
//
//	// array to sort
//	private Personne[] persons;

	@Setup(Level.Invocation)
	public void init() {
		System.out.println("init in invokation " + i);
	}

	private int i = 0;
	
	@Benchmark
	public void benchmarkSort(EmpyBenchmark b) {
		// Here is my benchmark code (sort array)
		// use an anonymous class to immplement a Comparator
		System.out.println(Thread.currentThread().getName() + " run benchmark " + b.i++);
	}
}