package fr.soat;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public final class MyBenchmarkLauncher {

	private MyBenchmarkLauncher() {
	}

	public static void main(final String[] _args) throws RunnerException {
		Options opt = new OptionsBuilder()
				.include("fr.soat.MyBenchmark")
				.build();

		new Runner(opt).run();
	}
}