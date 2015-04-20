/*
 * Copyright 2001-2013 Artima, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.scalactic

import org.scalatest._
import scala.collection.GenSeq
import scala.collection.GenMap
import scala.collection.GenSet
import scala.collection.GenIterable
import scala.collection.GenTraversable
import scala.collection.GenTraversableOnce
import scala.collection.{mutable,immutable}

class TypeCheckedMapEqualityConstraintsSpec extends FunSpec with NonImplicitAssertions with TypeCheckedTripleEquals with MapEqualityConstraints {

  case class Super(size: Int)
  class Sub(sz: Int) extends Super(sz)

  val super1: Super = new Super(1)
  val sub1: Sub = new Sub(1)
  val super2: Super = new Super(2)
  val sub2: Sub = new Sub(2)
  val nullSuper: Super = null

  case class Fruit(name: String)
  class Apple extends Fruit("apple")
  class Orange extends Fruit("orange")

  describe("the MapEqualityConstraints trait") {

    it("should allow any Map to be compared with any other Map, so long as the element types of the two Maps adhere to the equality constraint in force for those types") {
      assert(mutable.HashMap('a' -> 1, 'b' -> 2, 'c' -> 3) === immutable.HashMap('a' -> 1, 'b' -> 2, 'c' -> 3))
      assertTypeError("mutable.HashMap('a' -> 1, 'b' -> 2, 'c' -> 3) === immutable.HashMap('a' -> 1L, 'b' -> 2L, 'c' -> 3L)")
      assertTypeError("mutable.HashMap('a' -> 1L, 'b' -> 2L, 'c' -> 3L) === immutable.HashMap('a' -> 1, 'b' -> 2, 'c' -> 3)")
      assertTypeError("immutable.HashMap('a' -> 1, 'b' -> 2, 'c' -> 3) === mutable.HashMap('a' -> 1L, 'b' -> 2L, 'c' -> 3L)")
      assertTypeError("immutable.HashMap('a' -> 1L, 'b' -> 2L, 'c' -> 3L) === mutable.HashMap('a' -> 1, 'b' -> 2, 'c' -> 3)")
      assert(mutable.HashMap('a' -> new Apple, 'b' -> new Apple) === immutable.HashMap('a' -> new Fruit("apple"), 'b' -> new Fruit("apple")))
      assert(immutable.HashMap('a' -> new Fruit("apple"), 'b' -> new Fruit("apple")) === mutable.HashMap('a' -> new Apple, 'b' -> new Apple))
      assertTypeError("mutable.HashMap('a' -> new Apple, 'b' -> new Apple) === immutable.HashMap('a' -> new Orange, 'b' -> new Orange)")
      assertTypeError("immutable.HashMap('a' -> new Apple, 'b' -> new Apple) === mutable.HashMap('a' -> new Orange, 'b' -> new Orange)")
      assertTypeError("immutable.HashMap('a' -> new Orange, 'b' -> new Orange) === mutable.HashMap('a' -> new Apple, 'b' -> new Apple)")
      assertTypeError("mutable.HashMap('a' -> new Orange, 'b' -> new Orange) === immutable.HashMap('a' -> new Apple, 'b' -> new Apple)")
    }
  }
}
