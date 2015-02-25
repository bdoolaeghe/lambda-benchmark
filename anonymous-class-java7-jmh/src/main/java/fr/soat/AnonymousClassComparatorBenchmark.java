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

import cern.colt.Sorting;
import fr.soat.AnonymousClassComparatorBenchmark.PersonnesContainer;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Fork(value = 1, jvmArgsPrepend = {"-server", "-Xmx512m"} )
@Threads(1)
//@Fork(value = 0)
public class AnonymousClassComparatorBenchmark {

    @State(Scope.Benchmark)
    public static class PersonnesContainer {

        Personne p1 = new Personne("Jean-claude", "DUSS");
        
        Personne p2 = new Personne("Miguel", "DUSS");
        
    }
    
    static final Comparator<Personne> comparator = new Comparator<Personne>() {
        public int compare(Personne p1, Personne p2) {
            int nameComparaison = p1.getNom().compareTo(p2.getNom());
            if (nameComparaison != 0) {
                // noms are different
                return nameComparaison;
            } else {
                // noms are same, we need to comapre prenoms
                return p1.getPrenom().compareTo(p2.getPrenom());
            }
        }
    };



    @Warmup(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
    @Measurement(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
    @Benchmark
    public int benchmarkComparatorAnonymousClassInvokeOnly(PersonnesContainer c) {
        int compared = comparator.compare(c.p1, c.p2);
        return compared;
    }
    
    @Warmup(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
    @Measurement(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
    @Benchmark
    public Comparator<Personne> benchmarkComparatorAnonymousClassCreateOnly(PersonnesContainer c) {
        // Here is my benchmark code (sort array)
        Comparator<Personne> comparator = new Comparator<Personne>() {
            public int compare(Personne p1, Personne p2) {
                int nameComparaison = p1.getNom().compareTo(p2.getNom());
                if (nameComparaison != 0) {
                    // noms are different
                    return nameComparaison;
                } else {
                    // noms are same, we need to comapre prenoms
                    return p1.getPrenom().compareTo(p2.getPrenom());
                }
            }
        };

        return comparator;
    }
    
    @Warmup(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
    @Measurement(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
    @Benchmark
    public int benchmarkComparatorAnonymousClass(PersonnesContainer c) {
        // Here is my benchmark code (sort array)
        Comparator<Personne> comparator = new Comparator<Personne>() {
            public int compare(Personne p1, Personne p2) {
                int nameComparaison = p1.getNom().compareTo(p2.getNom());
                if (nameComparaison != 0) {
                    // noms are different
                    return nameComparaison;
                } else {
                    // noms are same, we need to comapre prenoms
                    return p1.getPrenom().compareTo(p2.getPrenom());
                }
            }
        };
        
        int compared = comparator.compare(c.p1, c.p2);
        return compared;
    }
}
