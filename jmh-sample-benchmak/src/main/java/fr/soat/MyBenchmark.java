/*
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

import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;


@BenchmarkMode(Mode.SampleTime)
@Warmup(iterations = 8, time = 200, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 20, time = 200, timeUnit = TimeUnit.MILLISECONDS)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(1)
public class MyBenchmark {

	@State(Scope.Benchmark)
	public static class DataContainer {
		@Param({"1", "10", "100", "1000", "1000000", "1000000000" })
		int x;
	}

	@Benchmark
	public double benchmarkLogarithmJdk(DataContainer data) {
		// Math.log10() from jdk
		double logN = java.lang.Math.log10(data.x);
		return logN;
	}

	@Benchmark
	public double benchmarkLogarithmGuava(DataContainer data) {
		// IntMath.log10() from google guava
		double logN = com.google.common.math.IntMath.log10(data.x, RoundingMode.UNNECESSARY);
		return logN;
	}
	
	@Benchmark
	public double benchmarkLogarithmApacheCommon(DataContainer data) {
		// MathUtils.log() from apache commons
		double logN = org.apache.commons.math.util.MathUtils.log(10, data.x);
		return logN;
	}


}
