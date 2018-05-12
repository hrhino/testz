/*
 * Copyright (c) 2018, Edmund Noble
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package testz.benchmarks

import scalaz.\/
import scalaz.concurrent.Task
import scalaz.std.anyVal._
import scalaz.syntax.all._

import testz._
import testz.runner._
import testz.z._

import scala.concurrent.ExecutionContext.Implicits.global

object Main {
  def testPureSuite = new PureSuite {
    def test[T](test: Harness[Function0, T]): T =
      test.section("some tests")(
        test("this test fails")(() =>
          assertEqualNoShow(1, 2)
        ),
        test("this test wins")(() =>
          assertEqualNoShow(1, 1)
        )
      )
  }

  def testTaskSuite = new TaskSuite {
    def test[T](test: Harness[Task, Task[T]]): Task[T] = {
      def doMoreTests(ts: Task[T], i: Int): Task[T] = {
        if (i == 0) ts
        else doMoreTests(
          test.section(s"test section $i")(
            ts,
            test(s"this is test $i-1")(
              assertEqualNoShow(1, 1).pure[Task]
            ),
            test(s"this is test $i-2")(
              assertEqualNoShow(1, 4).pure[Task]
            )
          ), i - 1
        )
      }
      doMoreTests(
        test.section("root tests")(
          test("this test wins")(
            assertEqualNoShow(1, 1).pure[Task]
          )
        ), 1
      )
    }
  }

  def run: Task[Unit] = {
    Task.async { cb =>
      Runner((() => testPureSuite) :: List.fill(2)(() => testTaskSuite))
        .onComplete(t => cb(\/.fromEither(t.toEither)))
    }
  }
}