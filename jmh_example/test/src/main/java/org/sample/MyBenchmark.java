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

package org.sample;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;

import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

public class MyBenchmark {

  /**
   * Example in README.md
   */
  @Benchmark
  public void testPrime() {
    int n = 1000000;
    SieveOfEratosthenes soe = new SieveOfEratosthenes(n);
    soe.getPrimes();
  }


  /**
   * State classes are used to send in parameters to the benchmark methods.
   */
  @State(Scope.Benchmark)
  public static abstract class MyBenchmarkState {

    // One benchmark is run for each value of n.
    @Param({"100", "1000", "10000000"})
    int n;

    // Performs the setup before each iteration
    @Setup(Level.Iteration)
    abstract public void setUp();
  }

  /**
   * Initializes a new sieve object every iteration (due to @Setup(Level.Iteration)).
   */
  public static class NaiveSieve extends MyBenchmarkState {

    public SieveOfEratosthenes soe;

    public void setUp() {
      soe = new SieveOfEratosthenes(n);
    }
  }


    // Declaring that this is a benchmark method
    @Benchmark
    // The test is run 1 time, and there is 1 warmup test.
    @Fork(value = 1, warmups = 1)
    // The warmup consists of two iterations
    @Warmup(iterations = 2)
    // There are 7 iterations per test.
    @Measurement(iterations = 7)
    // Get the average time
    @BenchmarkMode(Mode.AverageTime)
    // The output should be in nanoseconds
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    // Blackhole: Consumes the int[] primes array returned to avoid dead code elimination
    // NaiveSieve: Our state class that contains the SieveOfEratosthenes object
    public void testPrimes2(Blackhole blackhole, NaiveSieve state) {
        blackhole.consume(state.soe.getPrimes());
    }

}
