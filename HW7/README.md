# Disclaimer

В домашних заданиях вы можете менять сигнатуры методов/интерфейс, если на написано обратного. Если в задании нужно
реализовать определенную функцию, которая уже объявлена за вас, в таких случаях ее сигнатуру менять нельзя. То же
касается и интерфейсов/адт: если задана четкая структура с полями трейтов/классов, то ее можно только дополнять для
удобства вашего решения, но не менять.

## Важно: автоматические 0 баллов за работу, если:

* Сдано после дедлайна
* Красный CI (даже если падает только scalafmt)
* Если CI проходит, но было выполнено 0 тестов
* Есть правки в условии, которые не разрешены в условии
* Если есть действия после дедлайна, которые меняют код

# Сериализуемся

### Encoder / Decoder (easy lvl)

В данном задании вам нужно научиться выполнять сериализацию и десериализацию вашего case class, для начала попробуем
написать свой `Encoder` и `Decoder` для `Json` руками. (реализуйте `companyEmployeeEncoder/companyEmployeeDecoder`
и `employeeEncoder/employeeDecoder` в компаньоне классов `CompanyEmployee` и `Employee` соответственно). Ожидаемое
поведение - можете подсмотреть в тестах.

### Encoder (medium lvl)

В продолжении предыдущего задания: вы наверное заметили, что писать руками для каждого поля описание того как его надо
сериализовать и десериализовать - монотонно, однотипно и избыточно, т.к. везде всё одинаково и повторяется, а ещё легко
допустить ошибку. Для того чтобы не дублировать однотипный код в scala есть возможность деривации нужного вам типа. Есть
разные механизмы, например явное выведение через implicit, или использование макросов (они есть!). В этом задании вам
предлагается написать implicit для выведения `Encoder`'а для произвольного case class. (реализуйте
метод `def autoDerive[A]: Encoder[A]` в компаньоне `Encoder`)

Для реализации задания используйте библиотеку `shapless` и гетерогенные списки (`HList`) из неё.

### Note

Данные 2 задания предлагается выполнять последовательно, но если есть желаение или вы хорошо разбираетесь, можно сразу
написать `Encoder` для любого case class и использовать его в первом задании.

Писать тесты не обязательно, только если из-за этого не проходят доп проверки на coverage.

### Примитивный пример получения полей классов

```scala worksheet
import data.Employee
import shapeless.labelled.FieldType
import shapeless.{LabelledGeneric, Lazy, Witness}
import unmarshal.encoder.Encoder
import unmarshal.model.Json
import unmarshal.model.Json.{JsonNum, JsonNull, JsonObject, JsonString}

val gen = LabelledGeneric[Employee]

val employee = gen.to(Employee("John", 45, 7, None))

// simple type encoder (your must create encoders for all simple types)
implicit def intEncoder[K <: Symbol]: Encoder[Int] =
  (value: Int) => JsonNum(value)

// simple type encoder with field name (don't use this example,
// because it's not correct - you shouldn't create encoders which result
// is ```JsonObject```, when it's not actually new json object).
implicit def stringFieldEncoder[K <: Symbol](implicit
                                             witness: Witness.Aux[K]
                                            ): Encoder[FieldType[K, String]] = {
  val name = witness.value.name

  (value: FieldType[K, String]) => JsonObject(Map(name -> JsonString(value)))
}

// encoder for option, which use nestedEncoder. Don't use this example (same things as before)
implicit def optionFieldEncoder[K <: Symbol, V](implicit
                                                witness: Witness.Aux[K],
                                                nestedEncoder: Lazy[Encoder[V]]
                                               ): Encoder[FieldType[K, Option[V]]] = {
  val name = witness.value.name

  (value: FieldType[K, Option[V]]) =>
    (value: Option[V]) match {
      case Some(v) => JsonObject(Map(name -> nestedEncoder.value.toJson(v)))
      case None => JsonObject(Map(name -> JsonNull))
    }

}

// helpers, for correct types (you don't need it)
def foo[K <: Symbol](value: FieldType[K, String])(implicit
                                                  witness: Witness.Aux[K]
): Json = stringFieldEncoder[K].toJson(value)

def fooOpt[K <: Symbol](value: FieldType[K, Option[Int]])(implicit
                                                          witness: Witness.Aux[K]
): Json = optionFieldEncoder[K, Int].toJson(value)

foo(employee.head)
fooOpt(employee.tail.tail.tail.head)

```

## Ссылки:

* [Getting started with shapeless](https://jto.github.io/articles/getting-started-with-shapeless/)
* [Shapeless: Introduction and HLists](https://scalerablog.wordpress.com/2015/11/23/shapeless-introduction-and-hlists-part-1/)
* [Shapeless](https://github.com/milessabin/shapeless)
* [Derivation example (scala 2)](https://github.com/milessabin/shapeless/blob/main/examples/src/main/scala/shapeless/examples/derivation.scala)
* [Derivation example (scala 2, play)](https://polyglot.jamie.ly/software/2018/09/01/implementing_play_json_writers_using_scalas_shapeless)
* [Shapeless guide (3.2 Deriving instances for products)](https://books.underscore.io/shapeless-guide/shapeless-guide.pdf)

### Для развития:

* [Derivation (scala 3)](https://docs.scala-lang.org/scala3/reference/contextual/derivation.html)

### Code Style:

Мы последовательно вводим список запрещенных механик, которыми нельзя пользоваться при написании кода, и рекомендаций по
code style. За нарушения мы оставляем за собой право **снижать оценку**.

* Переменные и функции должны иметь осмысленные названия;
* Тест классы именуются `<ClassName>Spec`, где `<ClassName>` - класс к которому пишутся тесты;
* Тест классы находятся в том же пакете, что и класс к которому пишутся тесты (например, класс `Fibonacci` находится в
  пакете `fibonacci` в директории `src/main/scala/fibonacci`, значит его тест класс `FibonacciSpec` должен быть в том же
  пакете в директории `src/test/scala/fibonacci`);
* Каждый тест должен быть в отдельном test suite;
* Использовать java коллекции запрещается (Используйте `Scala` коллекции);
* Использовать `mutable` коллекции запрещается;
* Использовать `var` запрещается;
* Использование `this` запрещается (используйте `self` если требуется);
* Использование `return` запрещается;
* Использование `System.exit` запрещается;
* Касты или проверки на типы с помощью методов из Java вроде `asInstanceOf` запрещаются;
* Использовать `throw` запрещено;
* Использование циклов запрещается (используйте `for comprehension`, `tailRec`, методы `Monad`, `fold`);
* Использование не безопасных вызовов разрешено только в тестах (например `.get` у `Option`);
* Использование взятия и освобождения примитивов синхронизации: semaphore, mutex - из разных потоков запрещено;
* Использование require для ошибок запрещается
