package nl.ordina.personen.domein.datatype

import org.scalatest.{FunSuite, Matchers}

class BurgerservicenummerTest extends FunSuite with Matchers {

	val geldigeNummers =
		"""881011320 985083906 162470320 703626917 249913987 709548795 936636427 131529572 114845074 363090800 465797118
    955761189 637527951 203162419 709183033 852719048 689311965 227599251 850829318 624760698 640922284 749089325
    634858889 694499432 186293902 704157147 865563366 696513055 499179456 762876943 582212315 769436201 321999411
    495855960 355744922 269313060 274972438 728516615 725101180 722206653 393114302 715830144 531943343 793797469
    175775060 187168441 751373485 377508196 360417425 192162159 722508931 921420262 707544671 164238864 864868054
    205241311 147998785 399610340 626530283 258919516 191561642 712497468 645095060 318988331 302886795 836368903
    333255409 207091031 547905191 202839175 623782972 157177105 300661903 821380102 637639133 978141234 878308684
    545001110 550732196 442831018 854055241 883843833 135251941 125304407 432845458 709605237 931314884 258118428
    506263356 495763287 643702659 627176355 635592125 104005580 960895309 458770139 772284349 947512317 436889791
    876327614 813550245 171691246 703974294 779281718 919129055 300975181 569218652 773550458 679005389 498346456
    371475727 480900486 998679872 532698071 378144698""".stripMargin.split("\\s+")

	test("geldige burgerservicenummers") {
		geldigeNummers.foreach(n => Burgerservicenummer(n).value == n)
	}

	test("geldige burgerseervicenummers als integer") {
		geldigeNummers.foreach(n => Burgerservicenummer(n.toInt).value == n)
	}

	test("ongeldige burgerservicenummers") {
		geldigeNummers.map(n => (n.toInt + 7).toString).foreach { n =>
			the[AssertionError] thrownBy Burgerservicenummer(n) should have message s"assertion failed: $n is geen geldig " +
				s"burgerservicenummer"
		}
	}

	test("ongeldige burgerservicenummers als integer") {
		geldigeNummers.map(n => n.toInt + 7).foreach { n =>
			the[AssertionError] thrownBy Burgerservicenummer(n) should have message s"assertion failed: $n is geen geldig " +
				s"burgerservicenummer"
		}
	}

	test("te kort burgerservicenummer") {
		the[AssertionError] thrownBy Burgerservicenummer("123") should have message "assertion failed: Een " +
			"burgerservicenummer heeft negen cijfers; 123 heeft er 3"
	}

	test("te lang burgerservicenummer") {
		the[AssertionError] thrownBy Burgerservicenummer("1234567890") should have message "assertion failed: Een " +
			"burgerservicenummer heeft negen cijfers; 1234567890 heeft er 10"
	}

	test("te kort burgerservicenummer als integer") {
		the[AssertionError] thrownBy Burgerservicenummer(123) should have message "assertion failed: 000000123 is geen " +
			"geldig burgerservicenummer"
	}

	test("te lang burgerservicenummer als integer") {
		the[AssertionError] thrownBy Burgerservicenummer(1234567890) should have message "assertion failed: Een " +
			"burgerservicenummer heeft negen cijfers; 1234567890 heeft er 10"
	}
}
