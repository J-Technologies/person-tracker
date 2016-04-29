package nl.ordina.personen.datatypes

import org.scalatest.{FunSuite, Matchers}

class BurgerservicenummerTest extends FunSuite with Matchers {

  test("burgerservicenummer voldoet aan elfproef") {
    val bsn = new Burgerservicenummer("064293464")
    bsn.toString shouldBe "064293464"
  }

  test("burgerservicenummer voldoet ook aan de elfproef") {
    new Burgerservicenummer("380101968").toString shouldBe "380101968"
  }

  test("burgerservicenummer voldoet *ook niet* aan elfproef") {
    the[AssertionError] thrownBy new Burgerservicenummer("123456789") should have message "assertion failed: 123456789 voldoet niet aan de elfproef"
  }

  test("burgerservicenummer voldoet *niet* aan elfproef") {
    the[AssertionError] thrownBy new Burgerservicenummer("987654321") should have message "assertion failed: 987654321 voldoet niet aan de elfproef"
  }

  test("test te kort burgerservicenummer") {
    the[AssertionError] thrownBy new Burgerservicenummer("123") should have message "assertion failed: Een burgerservicenummer bestaat uit negen cijfers"
  }

  test("test te lang burgerservicenummer") {
    the[AssertionError] thrownBy new Burgerservicenummer("1234567890") should have message "assertion failed: Een burgerservicenummer bestaat uit negen cijfers"
  }
}
