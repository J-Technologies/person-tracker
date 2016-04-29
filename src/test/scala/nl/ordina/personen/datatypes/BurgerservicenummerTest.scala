package nl.ordina.personen.datatypes

import org.scalatest.{FunSuite, Matchers}

class BurgerservicenummerTest extends FunSuite with Matchers {

  test("burgerservicenummer voldoet aan elfproef") {
    Burgerservicenummer("064293464").value shouldBe "064293464"
  }

  test("burgerservicenummer als integer voldoet aan elfproef") {
    Burgerservicenummer(64293464).value shouldBe "064293464"
  }

  test("burgerservicenummer voldoet ook aan de elfproef") {
    Burgerservicenummer("380101968").value shouldBe "380101968"
  }

  test("burgerservicenummer als integer voldoet ook aan de elfproef") {
    Burgerservicenummer(380101968).value shouldBe "380101968"
  }

  test("burgerservicenummer als integer voldoet *ook niet* aan elfproef") {
    the[AssertionError] thrownBy Burgerservicenummer(123456789) should have message "assertion failed: 123456789 is geen geldig burgerservicenummer"
  }

  test("burgerservicenummer voldoet *niet* aan elfproef") {
    the[AssertionError] thrownBy Burgerservicenummer("987654321") should have message "assertion failed: 987654321 is geen geldig burgerservicenummer"
  }

  test("burgerservicenummer als integer voldoet *niet* aan elfproef") {
    the[AssertionError] thrownBy Burgerservicenummer(987654321) should have message "assertion failed: 987654321 is geen geldig burgerservicenummer"
  }

  test("test te kort burgerservicenummer") {
    the[AssertionError] thrownBy Burgerservicenummer("123") should have message "assertion failed: Een burgerservicenummer heeft negen cijfers"
  }

  test("test te lang burgerservicenummer") {
    the[AssertionError] thrownBy Burgerservicenummer("1234567890") should have message "assertion failed: Een burgerservicenummer heeft negen cijfers"
  }

  test("test te kort burgerservicenummer als integer") {
    the[AssertionError] thrownBy Burgerservicenummer(123) should have message "assertion failed: 000000123 is geen geldig burgerservicenummer"
  }

  test("test te lang burgerservicenummer als integer") {
    the[AssertionError] thrownBy Burgerservicenummer(1234567890) should have message "assertion failed: Een burgerservicenummer heeft negen cijfers"
  }
}
