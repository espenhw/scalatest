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
package org.scalatest

import org.scalactic.{Equality, Every, One, Many}
import org.scalactic.StringNormalizations._
import SharedHelpers._
import FailureMessages.decorateToStringValue
import Matchers._
import exceptions.TestFailedException
import exceptions.NotAllowedException

class EveryShouldContainOneElementOfSpec extends Spec {

  val upperCaseEquality =
    new Equality[String] {
      def areEqual(a: String, b: Any): Boolean = a.toUpperCase == b
    }

  //ADDITIONAL//

  object `an Every` {

    val fumList: Every[String] = Every("fum")
    val toList: Every[String] = Every("to")
    val fumfuList: Every[String] = Every("fum", "fu")

    object `when used with contain oneElementOf (...) syntax` {

      def `should do nothing if valid, else throw a TFE with an appropriate error message` {
        fumList should contain oneElementOf Seq("fee", "fie", "foe", "fum")
        val e1 = intercept[TestFailedException] {
          fumList should contain oneElementOf Seq("happy", "birthday", "to", "you")
        }
        e1.failedCodeFileName.get should be ("EveryShouldContainOneElementOfSpec.scala")
        e1.failedCodeLineNumber.get should be (thisLineNumber - 3)
        e1.message.get should be (FailureMessages.didNotContainOneElementOf(fumList, Seq("happy", "birthday", "to", "you")))
        // Here it contains two of, not one of
        val e2 = intercept[TestFailedException] {
          fumfuList should contain oneElementOf Seq("fee", "fum", "foe", "fu")
        }
        e2.failedCodeFileName.get should be ("EveryShouldContainOneElementOfSpec.scala")
        e2.failedCodeLineNumber.get should be (thisLineNumber - 3)
        e2.message.get should be (FailureMessages.didNotContainOneElementOf(fumfuList, Seq("fee", "fum", "foe", "fu")))
        // Contains duplicate elements in the right list
        fumList should contain oneElementOf Seq("fee", "fum", "foe", "fum")
      }
      def `should use the implicit Equality in scope` {
        implicit val ise = upperCaseEquality
        fumList should contain oneElementOf Seq("FEE", "FUM", "FOE", "FU")
        intercept[TestFailedException] {
          fumList should contain oneElementOf Seq("fee", "fum", "foe", "fu")
        }
      }
      def `should use an explicitly provided Equality` {
        (fumList should contain oneElementOf Seq("FEE", "FUM", "FOE", "FU")) (decided by upperCaseEquality)
        intercept[TestFailedException] {
          (fumList should contain oneElementOf Seq("fee", "fum", "foe", "fu")) (decided by upperCaseEquality)
        }
        intercept[TestFailedException] {
          fumList should contain oneElementOf Seq(" FEE ", " FIE ", " FOE ", " FUM ")
        }
        (fumList should contain oneElementOf Seq(" FEE ", " FIE ", " FOE ", " FUM ")) (after being lowerCased and trimmed)
      }
      def `should allow RHS to contain duplicated value` {
        fumList should contain oneElementOf Seq("fee", "fie", "foe", "fie", "fum")
      }
    }

    object `when used with (contain oneElementOf (...)) syntax` {

      def `should do nothing if valid, else throw a TFE with an appropriate error message` {

        fumList should (contain oneElementOf Seq("fee", "fie", "foe", "fum"))
        val e1 = intercept[TestFailedException] {
          fumList should (contain oneElementOf Seq("happy", "birthday", "to", "you"))
        }
        e1.failedCodeFileName.get should be ("EveryShouldContainOneElementOfSpec.scala")
        e1.failedCodeLineNumber.get should be (thisLineNumber - 3)
        e1.message.get should be (FailureMessages.didNotContainOneElementOf(fumList, Seq("happy", "birthday", "to", "you")))
      }
      def `should use the implicit Equality in scope` {
        implicit val ise = upperCaseEquality
        fumList should (contain oneElementOf Seq("FEE", "FUM", "FOE", "FU"))
        intercept[TestFailedException] {
          fumList should (contain oneElementOf Seq("fee", "fum", "foe", "fu"))
        }
      }
      def `should use an explicitly provided Equality` {
        (fumList should (contain oneElementOf Seq("FEE", "FUM", "FOE", "FU"))) (decided by upperCaseEquality)
        intercept[TestFailedException] {
          (fumList should (contain oneElementOf Seq("fee", "fum", "foe", "fu"))) (decided by upperCaseEquality)
        }
        intercept[TestFailedException] {
          fumList should (contain oneElementOf Seq(" FEE ", " FIE ", " FOE ", " FUM "))
        }
        (fumList should (contain oneElementOf Seq(" FEE ", " FIE ", " FOE ", " FUM "))) (after being lowerCased and trimmed)
      }
      def `should allow RHS to contain duplicated value` {
        fumList should (contain oneElementOf Seq("fee", "fie", "foe", "fie", "fum"))
      }
    }

    /*
     I purposely don't want to support this syntax:

            fumList should contain (oneElementOf Seq("fee", "fie", "foe", "fum"))
            fumList should (contain (oneElementOf Seq("fee", "fie", "foe", "fum")))

     Reason is that I don't want people putting parentheses between contain and oneElementOf, etc. This will not compile.
    */
    object `when used with not contain oneElementOf (...) syntax` {

      def `should do nothing if valid, else throw a TFE with an appropriate error message` {
        toList should not contain oneElementOf (Seq("fee", "fie", "foe", "fum"))
        val e1 = intercept[TestFailedException] {
          toList should not contain oneElementOf (Seq("happy", "birthday", "to", "you"))
        }
        e1.failedCodeFileName.get should be ("EveryShouldContainOneElementOfSpec.scala")
        e1.failedCodeLineNumber.get should be (thisLineNumber - 3)
        e1.message.get should be (FailureMessages.containedOneElementOf(toList, Seq("happy", "birthday", "to", "you")))
      }
      def `should use the implicit Equality in scope` {
        implicit val ise = upperCaseEquality
        toList should not contain oneElementOf (Seq("happy", "birthday", "to", "you"))
        intercept[TestFailedException] {
          toList should not contain oneElementOf (Seq("HAPPY", "BIRTHDAY", "TO", "YOU"))
        }
      }
      def `should use an explicitly provided Equality` {
        (toList should not contain oneElementOf (Seq("happy", "birthday", "to", "you"))) (decided by upperCaseEquality)
        intercept[TestFailedException] {
          (toList should not contain oneElementOf (Seq("HAPPY", "BIRTHDAY", "TO", "YOU"))) (decided by upperCaseEquality)
        }
        toList should not contain oneElementOf (Seq(" HAPPY ", " BIRTHDAY ", " TO ", " YOU "))
        intercept[TestFailedException] {
          (toList should not contain oneElementOf (Seq(" HAPPY ", " BIRTHDAY ", " TO ", " YOU "))) (after being lowerCased and trimmed)
        }
      }
      def `should allow RHS to contain duplicated value` {
        toList should not contain oneElementOf (Seq("fee", "fie", "foe", "fie", "fum"))
      }
    }

    /*
    Interesting, of these three, the top one does happen to compile and run:

            toList should not contain (oneElementOf Seq("fee", "fie", "foe", "fum"))
            // toList should not (contain (oneElementOf Seq("fee", "fie", "foe", "fum")))
            // toList should (not (contain (oneElementOf Seq("fee", "fie", "foe", "fum"))))

    The bottom two don't, but still I don't want to support that in general.
    */
    object `when used with (not contain oneElementOf (...)) syntax` {

      def `should do nothing if valid, else throw a TFE with an appropriate error message` {
        toList should (not contain oneElementOf (Seq("fee", "fie", "foe", "fum")))
        val e1 = intercept[TestFailedException] {
          toList should (not contain oneElementOf (Seq("happy", "birthday", "to", "you")))
        }
        e1.failedCodeFileName.get should be ("EveryShouldContainOneElementOfSpec.scala")
        e1.failedCodeLineNumber.get should be (thisLineNumber - 3)
        e1.message.get should be (FailureMessages.containedOneElementOf(toList, Seq("happy", "birthday", "to", "you")))
      }
      def `should use the implicit Equality in scope` {
        implicit val ise = upperCaseEquality
        toList should (not contain oneElementOf (Seq("happy", "birthday", "to", "you")))
        intercept[TestFailedException] {
          toList should (not contain oneElementOf (Seq("HAPPY", "BIRTHDAY", "TO", "YOU")))
        }
      }
      def `should use an explicitly provided Equality` {
        (toList should (not contain oneElementOf (Seq("happy", "birthday", "to", "you")))) (decided by upperCaseEquality)
        intercept[TestFailedException] {
          (toList should (not contain oneElementOf (Seq("HAPPY", "BIRTHDAY", "TO", "YOU")))) (decided by upperCaseEquality)
        }
        toList should (not contain oneElementOf (Seq(" HAPPY ", " BIRTHDAY ", " TO ", " YOU ")))
        intercept[TestFailedException] {
          (toList should (not contain oneElementOf (Seq(" HAPPY ", " BIRTHDAY ", " TO ", " YOU ")))) (after being lowerCased and trimmed)
        }
      }
      def `should allow RHS to contain duplicated value` {
        toList should (not contain oneElementOf (Seq("fee", "fie", "foe", "fie", "fum")))
      }
    }

    object `when used with shouldNot contain oneElementOf (...) syntax` {

      def `should do nothing if valid, else throw a TFE with an appropriate error message` {
        toList shouldNot contain oneElementOf Seq("fee", "fie", "foe", "fum")
        val e1 = intercept[TestFailedException] {
          toList shouldNot contain oneElementOf Seq("happy", "birthday", "to", "you")
        }
        e1.failedCodeFileName.get should be ("EveryShouldContainOneElementOfSpec.scala")
        e1.failedCodeLineNumber.get should be (thisLineNumber - 3)
        e1.message.get should be (FailureMessages.containedOneElementOf(toList, Seq("happy", "birthday", "to", "you")))
      }
      def `should use the implicit Equality in scope` {
        implicit val ise = upperCaseEquality
        toList shouldNot contain oneElementOf Seq("happy", "birthday", "to", "you")
        intercept[TestFailedException] {
          toList shouldNot contain oneElementOf Seq("HAPPY", "BIRTHDAY", "TO", "YOU")
        }
      }
      def `should use an explicitly provided Equality` {
        (toList shouldNot contain oneElementOf Seq("happy", "birthday", "to", "you")) (decided by upperCaseEquality)
        intercept[TestFailedException] {
          (toList shouldNot contain oneElementOf Seq("HAPPY", "BIRTHDAY", "TO", "YOU")) (decided by upperCaseEquality)
        }
        toList shouldNot contain oneElementOf Seq(" HAPPY ", " BIRTHDAY ", " TO ", " YOU ")
        intercept[TestFailedException] {
          (toList shouldNot contain oneElementOf Seq(" HAPPY ", " BIRTHDAY ", " TO ", " YOU ")) (after being lowerCased and trimmed)
        }
      }
      def `should allow RHS to contain duplicated value` {
        toList shouldNot contain oneElementOf Seq("fee", "fie", "foe", "fie", "fum")
      }
    }

    object `when used with shouldNot (contain oneElementOf (...)) syntax` {

      def `should do nothing if valid, else throw a TFE with an appropriate error message` {
        toList shouldNot (contain oneElementOf Seq("fee", "fie", "foe", "fum"))
        val e1 = intercept[TestFailedException] {
          toList shouldNot (contain oneElementOf Seq("happy", "birthday", "to", "you"))
        }
        e1.failedCodeFileName.get should be ("EveryShouldContainOneElementOfSpec.scala")
        e1.failedCodeLineNumber.get should be (thisLineNumber - 3)
        e1.message.get should be (FailureMessages.containedOneElementOf(toList, Seq("happy", "birthday", "to", "you")))
      }
      def `should use the implicit Equality in scope` {
        implicit val ise = upperCaseEquality
        toList shouldNot (contain oneElementOf Seq("happy", "birthday", "to", "you"))
        intercept[TestFailedException] {
          toList shouldNot (contain oneElementOf Seq("HAPPY", "BIRTHDAY", "TO", "YOU"))
        }
      }
      def `should use an explicitly provided Equality` {
        (toList shouldNot (contain oneElementOf Seq("happy", "birthday", "to", "you"))) (decided by upperCaseEquality)
        intercept[TestFailedException] {
          (toList shouldNot (contain oneElementOf Seq("HAPPY", "BIRTHDAY", "TO", "YOU"))) (decided by upperCaseEquality)
        }
        toList shouldNot (contain oneElementOf Seq(" HAPPY ", " BIRTHDAY ", " TO ", " YOU "))
        intercept[TestFailedException] {
          (toList shouldNot (contain oneElementOf Seq(" HAPPY ", " BIRTHDAY ", " TO ", " YOU "))) (after being lowerCased and trimmed)
        }
      }
      def `should allow RHS to contain duplicated value` {
        toList shouldNot (contain oneElementOf Seq("fee", "fie", "foe", "fie", "fum"))
      }
    }
  }

  object `an every of Everys` {

    val list1s: Every[Every[Int]] = Every(Every(1), Every(1), Every(1))
    val lists: Every[Every[Int]] = Every(Every(1), Every(1), Every(2))
    val hiLists: Every[Every[String]] = Every(Every("hi"), Every("hi"), Every("hi"))
    val toLists: Every[Every[String]] = Every(Every("to"), Every("to"), Every("to"))

    object `when used with contain oneElementOf (...) syntax` {

      def `should do nothing if valid, else throw a TFE with an appropriate error message` {
        all (list1s) should contain oneElementOf Seq(1, 3, 4)
        atLeast (2, lists) should contain oneElementOf Seq(1, 3, 4)
        atMost (2, lists) should contain oneElementOf Seq(2, 3, 4)
        no (lists) should contain oneElementOf Seq(3, 4, 5)

        val e1 = intercept[TestFailedException] {
          all (lists) should contain oneElementOf Seq(1, 3, 4)
        }
        e1.failedCodeFileName.get should be ("EveryShouldContainOneElementOfSpec.scala")
        e1.failedCodeLineNumber.get should be (thisLineNumber - 3)
        e1.message should be (Some("'all' inspection failed, because: \n" +
          "  at index 2, " + FailureMessages.didNotContainOneElementOf(lists(2), Seq(1, 3, 4)) + " (EveryShouldContainOneElementOfSpec.scala:" + (thisLineNumber - 5) + ") \n" +
          "in " + decorateToStringValue(lists)))
      }

      def `should use the implicit Equality in scope` {
        all (hiLists) should contain oneElementOf Seq("hi", "he")
        intercept[TestFailedException] {
          all (hiLists) should contain oneElementOf Seq("ho", "he")
        }
        implicit val ise = upperCaseEquality
        all (hiLists) should contain oneElementOf Seq("HI", "HE")
        intercept[TestFailedException] {
          all (hiLists) should contain oneElementOf Seq("hi", "he")
        }
      }
      def `should use an explicitly provided Equality` {
        (all (hiLists) should contain oneElementOf Seq("HI", "HE")) (decided by upperCaseEquality)
        intercept[TestFailedException] {
          (all (hiLists) should contain oneElementOf Seq("hi", "he")) (decided by upperCaseEquality)
        }
        implicit val ise = upperCaseEquality
        (all (hiLists) should contain oneElementOf Seq("hi", "he")) (decided by defaultEquality[String])
        intercept[TestFailedException] {
          (all (hiLists) should contain oneElementOf Seq("ho", "he")) (decided by defaultEquality[String])
        }
      }
      def `should allow RHS to contain duplicated value` {
        all (list1s) should contain oneElementOf Seq(1, 2, 2, 3)
      }
    }

    object `when used with (contain oneElementOf (...)) syntax` {

      def `should do nothing if valid, else throw a TFE with an appropriate error message` {
        all (list1s) should (contain oneElementOf Seq(1, 3, 4))
        atLeast (2, lists) should (contain oneElementOf Seq(1, 3, 4))
        atMost (2, lists) should (contain oneElementOf Seq(2, 3, 4))
        no (lists) should (contain oneElementOf Seq(3, 4, 5))

        val e1 = intercept[TestFailedException] {
          all (lists) should (contain oneElementOf Seq(1, 3, 4))
        }
        e1.failedCodeFileName.get should be ("EveryShouldContainOneElementOfSpec.scala")
        e1.failedCodeLineNumber.get should be (thisLineNumber - 3)
        e1.message should be (Some("'all' inspection failed, because: \n" +
          "  at index 2, " + FailureMessages.didNotContainOneElementOf(lists(2), Seq(1, 3, 4)) + " (EveryShouldContainOneElementOfSpec.scala:" + (thisLineNumber - 5) + ") \n" +
          "in " + decorateToStringValue(lists)))
      }

      def `should use the implicit Equality in scope` {
        all (hiLists) should (contain oneElementOf Seq("hi", "he"))
        intercept[TestFailedException] {
          all (hiLists) should (contain oneElementOf Seq("ho", "he"))
        }
        implicit val ise = upperCaseEquality
        all (hiLists) should (contain oneElementOf Seq("HI", "HE"))
        intercept[TestFailedException] {
          all (hiLists) should (contain oneElementOf Seq("hi", "he"))
        }
      }
      def `should use an explicitly provided Equality` {
        (all (hiLists) should (contain oneElementOf Seq("HI", "HE"))) (decided by upperCaseEquality)
        intercept[TestFailedException] {
          (all (hiLists) should (contain oneElementOf Seq("hi", "he"))) (decided by upperCaseEquality)
        }
        implicit val ise = upperCaseEquality
        (all (hiLists) should (contain oneElementOf Seq("hi", "he"))) (decided by defaultEquality[String])
        intercept[TestFailedException] {
          (all (hiLists) should (contain oneElementOf Seq("ho", "he"))) (decided by defaultEquality[String])
        }
      }
      def `should allow RHS to contain duplicated value` {
        all (list1s) should (contain oneElementOf Seq(1, 2, 2, 3))
      }
    }

    /*
     I purposely don't want to support this syntax:

    scala> all (list1s) should contain (oneElementOf Seq(1, 3, 4))
    <console>:15: error: org.scalatest.words.NewContainWord does not take parameters
                  all (list1s) should contain (oneElementOf Seq(1, 3, 4))
                                                 ^

    scala> all (list1s) should (contain (oneElementOf Seq(1, 3, 4)))
    <console>:15: error: org.scalatest.words.NewContainWord does not take parameters
                  all (list1s) should (contain (oneElementOf Seq(1, 3, 4)))
                                                  ^

     Reason is that I don't want people putting parentheses between contain and oneElementOf, etc. This will not compile.
    */
    object `when used with not contain oneElementOf (...) syntax` {

      def `should do nothing if valid, else throw a TFE with an appropriate error message` {
        all (toLists) should not contain oneElementOf (Seq("fee", "fie", "foe", "fum"))
        val e1 = intercept[TestFailedException] {
          all (toLists) should not contain oneElementOf (Seq("happy", "birthday", "to", "you"))
        }
        e1.failedCodeFileName.get should be ("EveryShouldContainOneElementOfSpec.scala")
        e1.failedCodeLineNumber.get should be (thisLineNumber - 3)
        e1.message should be (Some("'all' inspection failed, because: \n" +
          "  at index 0, " + FailureMessages.containedOneElementOf(toLists(0), Seq("happy", "birthday", "to", "you")) + " (EveryShouldContainOneElementOfSpec.scala:" + (thisLineNumber - 5) + ") \n" +
          "in " + decorateToStringValue(toLists)))
      }
      def `should use the implicit Equality in scope` {
        implicit val ise = upperCaseEquality
        all (toLists) should not contain oneElementOf (Seq("happy", "birthday", "to", "you"))
        intercept[TestFailedException] {
          all (toLists) should not contain oneElementOf (Seq("HAPPY", "BIRTHDAY", "TO", "YOU"))
        }
      }
      def `should use an explicitly provided Equality` {
        (all (toLists) should not contain oneElementOf (Seq("happy", "birthday", "to", "you"))) (decided by upperCaseEquality)
        intercept[TestFailedException] {
          (all (toLists) should not contain oneElementOf (Seq("HAPPY", "BIRTHDAY", "TO", "YOU"))) (decided by upperCaseEquality)
        }
        all (toLists) should not contain oneElementOf (Seq(" HAPPY ", " BIRTHDAY ", " TO ", " YOU "))
        intercept[TestFailedException] {
          (all (toLists) should not contain oneElementOf (Seq(" HAPPY ", " BIRTHDAY ", " TO ", " YOU "))) (after being lowerCased and trimmed)
        }
      }
      def `should allow RHS to contain duplicated value` {
        all (toLists) should not contain oneElementOf (Seq("fee", "fie", "foe", "fie", "fum"))
      }
    }

    /*
    Interesting, of these three, the last one does happen to compile and run:

    scala> all (toLists) should (not (contain (oneElementOf Seq("fee", "fie", "foe", "fum"))))
    <console>:15: error: org.scalatest.words.NewContainWord does not take parameters
                  all (toLists) should (not (contain (oneElementOf Seq("fee", "fie", "foe", "fum"))))
                                                        ^

    scala> all (toLists) should not (contain (oneElementOf Seq("fee", "fie", "foe", "fum")))
    <console>:15: error: org.scalatest.words.NewContainWord does not take parameters
                  all (toLists) should not (contain (oneElementOf Seq("fee", "fie", "foe", "fum")))
                                                       ^

    scala> all (toLists) should not contain (oneElementOf Seq("fee", "fie", "foe", "fum"))

    The top two don't, but still I don't want to support that in general.
    */
    object `when used with (not contain oneElementOf (...)) syntax` {

      def `should do nothing if valid, else throw a TFE with an appropriate error message` {
        all (toLists) should (not contain oneElementOf (Seq("fee", "fie", "foe", "fum")))
        val e1 = intercept[TestFailedException] {
          all (toLists) should (not contain oneElementOf (Seq("happy", "birthday", "to", "you")))
        }
        e1.failedCodeFileName.get should be ("EveryShouldContainOneElementOfSpec.scala")
        e1.failedCodeLineNumber.get should be (thisLineNumber - 3)
        e1.message should be (Some("'all' inspection failed, because: \n" +
          "  at index 0, " + FailureMessages.containedOneElementOf(toLists(0), Seq("happy", "birthday", "to", "you")) + " (EveryShouldContainOneElementOfSpec.scala:" + (thisLineNumber - 5) + ") \n" +
          "in " + decorateToStringValue(toLists)))
      }
      def `should use the implicit Equality in scope` {
        implicit val ise = upperCaseEquality
        all (toLists) should (not contain oneElementOf (Seq("happy", "birthday", "to", "you")))
        intercept[TestFailedException] {
          all (toLists) should (not contain oneElementOf (Seq("HAPPY", "BIRTHDAY", "TO", "YOU")))
        }
      }
      def `should use an explicitly provided Equality` {
        (all (toLists) should (not contain oneElementOf (Seq("happy", "birthday", "to", "you")))) (decided by upperCaseEquality)
        intercept[TestFailedException] {
          (all (toLists) should (not contain oneElementOf (Seq("HAPPY", "BIRTHDAY", "TO", "YOU")))) (decided by upperCaseEquality)
        }
        all (toLists) should (not contain oneElementOf (Seq(" HAPPY ", " BIRTHDAY ", " TO ", " YOU ")))
        intercept[TestFailedException] {
          (all (toLists) should (not contain oneElementOf (Seq(" HAPPY ", " BIRTHDAY ", " TO ", " YOU ")))) (after being lowerCased and trimmed)
        }
      }
      def `should allow RHS to contain duplicated value` {
        all (toLists) should (not contain oneElementOf (Seq("fee", "fie", "foe", "fie", "fum")))
      }
    }

    object `when used with shouldNot contain oneElementOf (...) syntax` {

      def `should do nothing if valid, else throw a TFE with an appropriate error message` {
        all (toLists) shouldNot contain oneElementOf Seq("fee", "fie", "foe", "fum")
        val e1 = intercept[TestFailedException] {
          all (toLists) shouldNot contain oneElementOf Seq("happy", "birthday", "to", "you")
        }
        e1.failedCodeFileName.get should be ("EveryShouldContainOneElementOfSpec.scala")
        e1.failedCodeLineNumber.get should be (thisLineNumber - 3)
        e1.message should be (Some("'all' inspection failed, because: \n" +
          "  at index 0, " + FailureMessages.containedOneElementOf(toLists(0), Seq("happy", "birthday", "to", "you")) + " (EveryShouldContainOneElementOfSpec.scala:" + (thisLineNumber - 5) + ") \n" +
          "in " + decorateToStringValue(toLists)))
      }
      def `should use the implicit Equality in scope` {
        implicit val ise = upperCaseEquality
        all (toLists) shouldNot contain oneElementOf Seq("happy", "birthday", "to", "you")
        intercept[TestFailedException] {
          all (toLists) shouldNot contain oneElementOf Seq("HAPPY", "BIRTHDAY", "TO", "YOU")
        }
      }
      def `should use an explicitly provided Equality` {
        (all (toLists) shouldNot contain oneElementOf Seq("happy", "birthday", "to", "you")) (decided by upperCaseEquality)
        intercept[TestFailedException] {
          (all (toLists) shouldNot contain oneElementOf Seq("HAPPY", "BIRTHDAY", "TO", "YOU")) (decided by upperCaseEquality)
        }
        all (toLists) shouldNot contain oneElementOf Seq(" HAPPY ", " BIRTHDAY ", " TO ", " YOU ")
        intercept[TestFailedException] {
          (all (toLists) shouldNot contain oneElementOf Seq(" HAPPY ", " BIRTHDAY ", " TO ", " YOU ")) (after being lowerCased and trimmed)
        }
      }
      def `should allow RHS to contain duplicated value` {
        all (toLists) shouldNot contain oneElementOf Seq("fee", "fie", "foe", "fie", "fum")
      }
    }

    object `when used with shouldNot (contain oneElementOf (...)) syntax` {

      def `should do nothing if valid, else throw a TFE with an appropriate error message` {
        all (toLists) shouldNot (contain oneElementOf Seq("fee", "fie", "foe", "fum"))
        val e1 = intercept[TestFailedException] {
          all (toLists) shouldNot (contain oneElementOf Seq("happy", "birthday", "to", "you"))
        }
        e1.failedCodeFileName.get should be ("EveryShouldContainOneElementOfSpec.scala")
        e1.failedCodeLineNumber.get should be (thisLineNumber - 3)
        e1.message should be (Some("'all' inspection failed, because: \n" +
          "  at index 0, " + FailureMessages.containedOneElementOf(toLists(0), Seq("happy", "birthday", "to", "you")) + " (EveryShouldContainOneElementOfSpec.scala:" + (thisLineNumber - 5) + ") \n" +
          "in " + decorateToStringValue(toLists)))
      }
      def `should use the implicit Equality in scope` {
        implicit val ise = upperCaseEquality
        all (toLists) shouldNot (contain oneElementOf Seq("happy", "birthday", "to", "you"))
        intercept[TestFailedException] {
          all (toLists) shouldNot (contain oneElementOf Seq("HAPPY", "BIRTHDAY", "TO", "YOU"))
        }
      }
      def `should use an explicitly provided Equality` {
        (all (toLists) shouldNot (contain oneElementOf Seq("happy", "birthday", "to", "you"))) (decided by upperCaseEquality)
        intercept[TestFailedException] {
          (all (toLists) shouldNot (contain oneElementOf Seq("HAPPY", "BIRTHDAY", "TO", "YOU"))) (decided by upperCaseEquality)
        }
        all (toLists) shouldNot (contain oneElementOf Seq(" HAPPY ", " BIRTHDAY ", " TO ", " YOU "))
        intercept[TestFailedException] {
          (all (toLists) shouldNot (contain oneElementOf Seq(" HAPPY ", " BIRTHDAY ", " TO ", " YOU "))) (after being lowerCased and trimmed)
        }
      }
      def `should allow RHS to contain duplicated value` {
        all (toLists) shouldNot (contain oneElementOf Seq("fee", "fie", "foe", "fie", "fum"))
      }
    }
  }
}