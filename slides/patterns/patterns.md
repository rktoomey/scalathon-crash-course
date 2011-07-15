!SLIDE

<p style="font-size: 60px; color: 16325a;">game, set, match</p>
<br/>
<br/>
<br/>
<br/>
<p style="font-size: 45px; color: 16325a; font-style: italic;">patterns all around</p>
<br/>
<br/>
<br/>
<br/>
[Rose Toomey](http://twitter.com/prasinous)
<br/>
Novus Partners
<br/>
<br/>
<br/>
15 July 2011 @ Scalathon

!SLIDE

# Pattern Matching

- pattern matching
- case classes
  - apply
  - unapply
- case objects
- enums

!SLIDE

# constant patterns
<br>
Here is a function that matches an incoming String against constant values.
<br>

    scala> def firstTry(x: String) = x match {
         | case "Hello" => "Hi!"
         | case "Goodbye" => "Bye!"
         | case _ => "Unsure what to say"
         | }
    firstTry: (x: String)java.lang.String

    scala> firstTry("Hello")
    res0: java.lang.String = Hi!

    scala> firstTry("Goodbye")
    res1: java.lang.String = Bye!

    scala> firstTry("Hey")
    res2: java.lang.String = Unsure what to say

    scala> firstTry("hello")
    res3: java.lang.String = Unsure what to say

!SLIDE

# what just happened?

- match is an _expression_ in Scala: it always results in a value
    - no match?  a `MatchError` will be thrown
- unlike switch, cases never "fall through" - there is no concept of `break`
    - you are required to specify a default case - in the example it was `_`
    - instead of using `_`, we could have captured the value and assigned it some name

!SLIDE

# variable patterns
<br>

- in the default case, all we have to do it use a name, like `greeting` to capture the value
- in a case where we are matching on a constant value, we use `hello @` sign to capture the input
"Hello"
<br>

```
    scala> def secondTry(x: String) = x match {
         | case hello @ "Hello" => hello.reverse
         | case greeting => "%s right back at you".format(greeting)
         | }
    secondTry: (x: String)String

    scala> secondTry("Hey")
    res5: String = Hey right back at you

    scala> secondTry("Hello")
    res6: String = olleH

    scala> secondTry("HI")
    res7: String = HI right back at you
```

<br>
<br>
don't worry, this information will be much more exciting when we cover case classes!

!SLIDE

# extracting a value from an `Option`
<br>
This is not an idiomatic way to work with `Option` in Scala!  But it's interesting to consider how
pattern matching can distinguish and extract the String `sunshine` here.

    scala> val x: Option[String] = Some("sunshine")
    x: Option[String] = Some(sunshine)

    scala> x match {
         | case Some(s) => s
         | case None => "only rain"
         | }
    res5: String = sunshine

<br>
<br>
A side note: do it this way instead, everyone will thank for you it...  :)
<br>

    scala> x.getOrElse("only rain")
    res7: String = sunshine


!SLIDE

# oh, how about `List`?
<br>
You might have considered that the behaviour of `Option` is sometimes like a monad, sometimes
like a collection.  Methods like `isEmpty`, `toList`, `map`, `flatMap`, `filter`, `filterNot`...
<br>
<br>
Try thinking of `Some` as a single entry list, and of `None` as an empty list.
<br>
<br>

    scala> List(1, 2, 3) match {
         | case List(1, _, _) => "found it"
         | case list => "not what I wanted: %s".format(list.mkString(","))
         | }
    res8: java.lang.String = found it

!SLIDE

# you can't get there from here
<br>
The Scala compiler will error out when you when your cases don't make sense!
<br>
<br>

    scala> def rococo(list: List[Int]) = list match {
         | case List(1, _, _) => "one is the first element of a three-element list"
         | case l @ List(1, _*) => "one is the first element of a %d-element long list"
            .format(l.size)
         | case list => "one is not the first element of this list: %s".format(list.mkString)
         | case Nil => "empty list"
         | }
    <console>:15: error: unreachable code
           case Nil => "empty list"

<br>
Why is `Nil` after `list` unreachable?

!SLIDE

# you might not get there from here
<br>
Remember `MatchError`?   The Scala compiler will _warn_ but not stop you if your cases don't
cover all the possibilities.
<br>
<br>

    scala> def rococo(list: List[Int]) = list match {
         | case List(1, _, _) => "one is the first element of a three-element list"
         | case l @ List(1, _*) => "one is the first element of a %d-element long list"
            .format(l.size)
         | }
    <console>:11: warning: match is not exhaustive!
    missing combination            Nil

           def rococo(list: List[Int]) = list match {
                                                 ^
    rococo: (list: List[Int])java.lang.String

!SLIDE

# list matching: as fiddly as you want it to be

    scala> def rococo(list: List[Int]) = list match {
         | case List(1, _, _) => "one is the first element of a three-element list"
         | case l @ List(1, _*) => "one is the first element of a %d-element long list"
            .format(l.size)
         | case Nil => "empty list"
         | case list => "one is not the first element of this list: %s".format(list.mkString(", "))
         | }
    rococo: (list: List[Int])java.lang.String

    scala> rococo(List(1, 2, 3))
    res10: java.lang.String = one is the first element of a three-element list

    scala> rococo(List(1, 2, 3, 4))
    res11: java.lang.String = one is the first element of a 4-element long list

    scala> rococo(List(4, 5, 6))
    res12: java.lang.String = one is not the first element of this list: 4, 5, 6

    scala> rococo(Nil)
    res13: java.lang.String = empty list

!SLIDE

# case classes

- how is a case class different to a regular class
- how is `Product` useful?

!SLIDE

# all this and mode

When you declare a class with the `case` modifier in Scala, you get a lot of built-in functionality for free!
<br>
<br>

Case classes are Scala's way to allow matching on objects without boilerplate code, but you also get:

- every field in the constructor is a public `val`
- a product iterator
  - built-in "natural" equals, hashCode and toString methods
- `apply`
- `unapply`
- `copy`

!SLIDE

# what does `Product` do?

    case class Alpha(a: Double, b: Long)
    case class Bravo(x: String, y: Int, z: Option[Alpha])

<br>
Scala's `Product` trait makes a case class behave like an ordered list of fields.  This capability
drives `equals`, `hashCode`, `toString` and more.

## arity
A list has a size, a case class has a number of fields up to 21.

- arity is zero-indexed.
- you can use the arity to get the Nth element of the product.

## product iterator
A case class has a product iterator, which gives you an ordered iteration of the fields - just like
iterating through a list.

## does this sound familiar?
`Pair` and `Tuple1`...`Tuple22` are subclasses of `Product`.

!SLIDE

# demonstration of what `Product` does
<br>

    val alpha = Alpha(a = 1.4, b = 99L)
    val bravo = Bravo(x = "bravo", y = 2, z = Some(alpha))

<br>
<br>

    class: 'prasinous.exercises.Bravo'
    arity: 3
    prefix: 'Bravo'
    [0]	java.lang.String		baker
    [1]	java.lang.Integer		2
    [2]	scala.Some		Some(Alpha(1.4,99))

    toString: Bravo(baker,2,Some(Alpha(1.4,99)))

!SLIDE

# how does the compiler show this?

    target/scala_2.9.0-1/classes/prasinous/exercises$ scalap -cp . "Bravo"

    package prasinous.exercises

    case class Bravo(x : scala.Predef.String, y : scala.Int, z : scala.Option[prasinous.exercises.Alpha])
        extends java.lang.Object with scala.ScalaObject with scala.Product with scala.Serializable {
      val x : scala.Predef.String = { /* compiled code */ }
      val y : scala.Int = { /* compiled code */ }
      val z : scala.Option[prasinous.exercises.Alpha] = { /* compiled code */ }
      def copy(x : scala.Predef.String, y : scala.Int,
        z : scala.Option[prasinous.exercises.Alpha]) : prasinous.exercises.Bravo = { /* compiled code */ }
      override def hashCode() : scala.Int = { /* compiled code */ }
      override def toString() : scala.Predef.String = { /* compiled code */ }
      override def equals(x$1 : scala.Any) : scala.Boolean = { /* compiled code */ }
      override def productPrefix : java.lang.String = { /* compiled code */ }
      override def productArity : scala.Int = { /* compiled code */ }
      override def productElement(x$1 : scala.Int) : scala.Any = { /* compiled code */ }
      override def canEqual(x$1 : scala.Any) : scala.Boolean = { /* compiled code */ }
    }

!SLIDE

# what about the companion object?

    target/scala_2.9.0-1/classes/prasinous/exercises$ scalap -cp . "Bravo$"

    package prasinous.exercises;

    final class Bravo$ extends scala.runtime.AbstractFunction3 with scala.Serializable
      with scala.ScalaObject {

      def this(): scala.Unit;
      def apply(scala.Any, scala.Any, scala.Any): scala.Any;
      def readResolve(): scala.Any;
      def apply(java.lang.String, scala.Int, scala.Option): prasinous.exercises.Bravo;
      def unapply(prasinous.exercises.Bravo): scala.Option;
      def toString(): java.lang.String;
    }
    object Bravo$ {
      final val MODULE$: prasinous.exercises.Bravo$;
    }

!SLIDE

# three ways to create a case class
<br>
The most idiomatic way to create a case class is just to say `Alpha(3.0, 4L)`
<br>
<br>

    scala> import prasinous.exercises._
    import prasinous.exercises._

    scala> val a = Alpha(3.0, 4L)
    a: prasinous.exercises.Alpha = Alpha(3.0,4)

    scala> val a_* = Alpha.apply(3.0, 4L)
    a_*: prasinous.exercises.Alpha = Alpha(3.0,4)

    scala> val a_** = new Alpha(3.0, 4L)
    a_**: prasinous.exercises.Alpha = Alpha(3.0,4)

    scala> a == a_* && a == a_** && a_* == a_**
    res0: Boolean = true


!SLIDE

# so what does `unapply` do?
<br>
The `unapply` method converts `Alpha` to an `Option` with a `Tuple2` typed to the order of the
fields in `Alpha`.  But why `Option`?
<br>
<br>

    scala> Alpha.unapply(a)
    res1: Option[(Double, Long)] = Some((3.0,4))

## but why does `unapply` return an `Option`?

Can anyone think why that might be useful?

!SLIDE

# and we circle back to pattern matching!
<br>

    scala> def guess(a: Alpha) = a match {
         | case Alpha(3.0, 4L) => "Princess defeats Runarstiltskin again.  Curses!"
         | case wrongOne => "Keep guessing, you haven't got my name yet... %s".format(wrongOne)
         | }
    guess: (a: prasinous.exercises.Alpha)java.lang.String

    scala> guess(a)
    res2: java.lang.String = Princess defeats Runarstiltskin again.  Curses!

    scala> guess(Alpha(7.0, 2L))
    res3: java.lang.String = Keep guessing, you haven't got it yet... Alpha(7.0,2)

!SLIDE

# copy: the good
<br>
The ease of `copy` with case classes puts doddering, inconvenient `java.lang.Cloneable` to shame:
<br>
<br>

    scala> a
    res18: prasinous.exercises.Alpha = Alpha(3.0,4)

    scala> a.copy(a = 3.14)
    res19: prasinous.exercises.Alpha = Alpha(3.14,4)


!SLIDE

# copy: the wtf

There's only one catch: for some reason that has never been explained to my satisfaction, the Scala compiler
does not _enforce_ `copy` on the case class.  So you can depend on `copy` being present:

- within the case class itself
- on an instance of the case class

<br>
<br>
But nothing guarantees that `copy` will actually be present.  Notice that we you can't `override` the
`copy` method as you might expect.

    scala> case class Foo(x: String) {
         | override def copy(x: String) = throw new Error("LOL")
         | }
    <console>:11: error: method copy overrides nothing
           override def copy(x: String) = throw new Error("LOL")

!SLIDE

# where we are so far
<br>
So far, we've seen examples of:

- pattern matching using a constant value
- pattern matching using a case class

<br>
<br>

## what else can we do with pattern matching?
- pattern matching on type
- guards
- case object and enums

!SLIDE

# typed patterns

    scala> def matchOnType[A <: AnyRef](a: A) = a match {
         | case s: String => "String: %s".format(s)
         | case d: java.util.Date => "Date: %s".format(d)
         | case m: Map[_, _] => "Map: %s".format(m.mkString)
         | case x => "??? %s with value %s".format(x.getClass.getName, x)
         | }
    matchOnType: [A <: AnyRef](a: A)String

    scala> matchOnType("moon")
    res14: String = String: moon

    scala> matchOnType(a)
    res16: String = ??? prasinous.exercises.Alpha with value Alpha(3.0,4)

!SLIDE

# the cruelty of type erasure

    scala> def isIntMap(m: Any) = m match {
         | case Map[Int, Int] => true
         | case _ => false
         | }
    <console>:11: error: type Map of type Map does not take type parameters.
           case Map[Int, Int] => true

<br>
<br>
If you chose to run Scala `unchecked`, it will cheerfully act like this:

    scala> isIntMap(Map(1 -> 1))
    res19: Boolean = true
    scala> isIntMap(Map("abc" -> "abc"))
    res20: Boolean = true

!SLIDE

# pattern guards

    scala> def guard(a: Alpha) = a match {
         | case Alpha(a, b) if a == b.doubleValue => "jinx!"
         | case Alpha(a, b) => "%s != %s".format(a, b)
         | }
    guard: (a: prasinous.exercises.Alpha)java.lang.String

    scala> guard(Alpha(4.0,4L))
    res27: java.lang.String = jinx!

    scala> guard(Alpha(4.0,5L))
    res28: java.lang.String = 4.0 != 5

!SLIDE

# why case objects?

The `case` modifier applies to `object` as well: compare the definition of
`Some` against `None`: there can be many instances of `Some` but there only needs to
be a single `None`.  Similarly, `List` and `Nil`.
<br>

    final case class Some[+A](x: A) extends Option[A] {
      def isEmpty = false
      def get = x
    }

    case object None extends Option[Nothing] {
      def isEmpty = true
      def get = throw new NoSuchElementException("None.get")
    }

!SLIDE

# extra bonus: be warned of non-exhaustive matches

    sealed abstract class Fish(val scales: Int)
    case object RedFish extends Fish(scales = 55)
    case object BlueFish extends Fish(scales = 102)

<br>
Now the compiler can warn you is the match is non-exhaustive:
<br>

    scala> def fishy(f: Fish) = f match {
         | case rf @ RedFish => rf.scales
         | }
    <console>:12: warning: match is not exhaustive!
    missing combination       BlueFish

           def fishy(f: Fish) = f match {
                                ^
    fishy: (f: Fish)Int

!SLIDE

# enums: probably not what you wanted

    scala> object Color extends Enumeration {
         | val Red, Green, Blue = Value
         | }
    defined module Color

    scala> def color(c: Color.Value) = c match {
         | case Color.Red => "rot"
         | case Color.Blue => "blau"
         | case Color.Green => "gruen"
         | }
    color: (c: Color.Value)java.lang.String

    scala> def color(c: Color.Value) = c match {
         | case Color.Red => "rot"
         | case Color.Blue => "blau"
         | }
    color: (c: Color.Value)java.lang.String

    scala> color(Color.Green)
    scala.MatchError: Green (of class scala.Enumeration$Val)

<img src="/patterns/novus-logo.png" />