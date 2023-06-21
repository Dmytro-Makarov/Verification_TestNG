import org.testng.annotations.*;
import uni.makarov.verification.Main;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

public class TestNGTests {

    private static Main repetitionExclusion;

    @BeforeSuite
    public static void setUpAll() {
        System.out.println("Running all tests...");
    }

    @BeforeGroups("exceptions")
    public void setUpExceptionGroup() {
        System.out.println("Running exception and special instances tests...");
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        repetitionExclusion = new Main();
    }

    @Test(groups = {"repeating"})
    public void RepeatingLetters_WordsRemoved() {
        String input = "marinara nori joker Sheffield Worcestershire mango Kuwait";
        ArrayList<String> expected = new ArrayList<>(List.of("nori joker mango Kuwait".split(" ")));
        ArrayList<String> actual = repetitionExclusion.removeLetterRepeatingWords(input);
        assertEquals(actual, expected);
    }

    @Test(groups = {"repeating"})
    public void RepeatingLettersWithSymbolsNewlines_WordsRemovedSymbolsIgnored() {
        String input = "MOOOOO!1!!!1! \n\n scar?y, is it ???? \t\t crazy. cow!";
        ArrayList<String> expected = new ArrayList<>(List.of("scary is it crazy cow".split(" ")));
        ArrayList<String> actual = repetitionExclusion.removeLetterRepeatingWords(input);
        assertEquals(actual, expected);
    }

    @Test(groups = {"repeating"})
    public void WordsWithHyphensAndDashes_HyphensAcceptDashIgnore() {
        String input = "na-do - ko-ko, kan-ryo-chi!";
        ArrayList<String> expected = new ArrayList<>(List.of("na-do kan-ryo-chi".split(" ")));
        ArrayList<String> actual = repetitionExclusion.removeLetterRepeatingWords(input);
        assertEquals(expected, actual);
    }

    @Test(groups = {"exceptions"})
    public void UnicodeLatinLetters_NormalizedToASCII() {
        //Note: Some unicode latin extensions are not supported
        //for decomposition, and are ignored as special symbols
        String input = "À ç ì ñ û ÿ : ł Ʈ Ǿ";
        ArrayList<String> expected = new ArrayList<>(List.of("A c i n u y".split(" ")));
        ArrayList<String> actual = repetitionExclusion.removeLetterRepeatingWords(input);
        assertEquals(expected, actual);
    }

    @Test(groups = {"exceptions"})
    public void UnicodeNonLatinLetters_IgnoredAsSpecialSymbols() {
        String input = "ぁ ヘ ᄚ ㄶ ㌇ ሕ Թ";
        assertTrue(repetitionExclusion.removeLetterRepeatingWords(input).isEmpty());
    }

    @Test(groups = {"exceptions"})
    public void EmptyText_ReturnsEmptyArray() {
        String text = "";
        assertTrue(repetitionExclusion.removeLetterRepeatingWords(text).isEmpty());
    }

    @Test(groups = {"exceptions"}, expectedExceptions = NullPointerException.class)
    public void NullText_ThrowsException() {
        String text = null;
        ArrayList<String> expected = new ArrayList<>(null);
        ArrayList<String> actual = repetitionExclusion.removeLetterRepeatingWords(text);
        assertEquals(expected, actual);
    }

    @DataProvider(name = "RepeatingLetterWordProvider")
    public Object[][] provideRepeatingLetterWords() {
        return new Object[][]{
                {"Termite, melting the Granite of Science", "melting the Granite of"},
                {"Tango Echo Sierra Tango", "Tango Echo"},
                {"ufo??? Flin-ging?!!!? \t alìen ufo mysti-1que much! 61 79 79 20 6c 6d 61 6f", "ufo alien mysti-que much c d f"}
        };
    }

    @Test(dataProvider = "RepeatingLetterWordProvider", groups = {"repeating"})
    public void ParameterizedTest(String actualText, String expectedText) {
        ArrayList<String> expected = new ArrayList<>(List.of(expectedText.split(" ")));
        ArrayList<String> actual = repetitionExclusion.removeLetterRepeatingWords(actualText);
        assertEquals(expected, actual);
    }

}
