/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

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

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 20, time = 2000, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 20, time = 2000, timeUnit = TimeUnit.MILLISECONDS)
@Fork(value = 3, jvmArgsPrepend = { "-server", "-Xmx2g", "-XX:+TieredCompilation" })
@Threads(2)
public class LambdaTimSortBenchmark {

    @State(Scope.Thread)
    public static class PersonnesContainer {

        // measure for a set of dataset...
        @Param({ "5000", "10000", "50000", "100000", "200000", "400000" , "800000" /*, "1200000"*/ })
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

        @Setup(Level.Invocation)
        public void reshuflePersonnesArray() throws IOException {
            // before each sort invocation,
            // restore unsorted array of personnes before sorting
            personneToSortArray = Arrays.copyOf(unsortedPersonneArray, unsortedPersonneArray.length);
        }

    }

    @Benchmark
    public Personne[] lambda_as_compartor(PersonnesContainer c) {
        // Here is my benchmark code (sort array)
        // use a lambda to define the Comparator
        Arrays.sort(c.personneToSortArray,
                (p1, p2) -> {
                    int nomCompaison = p1.getNom().compareTo(p2.getNom());
                    return (nomCompaison == 0) ? p1.getPrenom().compareTo(p2.getPrenom()) : nomCompaison;
                });        
        return c.personneToSortArray;
    }
    
    @Benchmark
    public Personne[] lambda_comparing(PersonnesContainer c) {
        // Here is my benchmark code (sort array)
        // use a lambda and comparing method define the comparator
        Arrays.sort(c.personneToSortArray,
                Comparator
                .comparing((Personne p) -> p.getNom())
                .thenComparing((Personne p) -> p.getPrenom()));
        
        return c.personneToSortArray;
    }
    
    @Benchmark
    public Personne[] methodFreference_comparing(PersonnesContainer c) {
        // Here is my benchmark code (sort array)
        // use a method reference and comparing method
        Arrays.sort(c.personneToSortArray,
                Comparator
                .comparing(Personne::getNom)
                .thenComparing(Personne::getPrenom));

        
        return c.personneToSortArray;
    }
  
    
    
}
