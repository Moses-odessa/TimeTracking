package ua.moses.Integration;


import org.hamcrest.CoreMatchers;
import org.junit.Test;
import ua.moses.Controller.Main;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class IntegrationTest {
    private ConsoleMock console = new ConsoleMock();

    private void assertOut(String string) {
        assertEquals(string,
                console.getOut().trim().replaceAll("\r",""));
    }

    private void assertContains(String string) {
        assertThat(console.getOut().trim().replaceAll("\r",""),
                CoreMatchers.containsString(string));
    }

    private void assertNotContains(String string) {
        assertThat(console.getOut().trim().replaceAll("\r",""),
                CoreMatchers.not(CoreMatchers.containsString(string)));
    }



    @Test
    public void testConnectAndExit() {
        // given
        console.addIn("exit");
        // when
        Main.main(new String[0]);

        // then
        assertOut("Добро пожаловать!!!\n" +
                "Введите команду или help для вывода списка доступных команд:\n" +
                "До скорой встречи!");
    }

    @Test
    public void testHelpAndExit() {
        // given
        console.addIn("help");
        console.addIn("exit");
        // when
        Main.main(new String[0]);

        // then
        assertOut("Добро пожаловать!!!\n" +
                "Введите команду или help для вывода списка доступных команд:\n" +
                "Список доступных команд:\n" +
                "workers: выводит список сотрудников\n" +
                "add|fullname: добавляет сотрудника fullname в список сотрудников\n" +
                "remove|fullname_or_id: удаляет сотрудника по имени или id\n" +
                "checkin|fullname_or_id|time|date: отмечает приход на работу сотрудника fullname по имени или id\n" +
                "\tгде time - время в формате hh:mm, и date - дата в формате YYYY-MM-DD\n" +
                "\tесли дата и время опущены - используются текущие\n" +
                "checkout|fullname_or_id|time|date: отмечает приход на работу сотрудника fullname по имени или id\n" +
                "\tгде time - время в формате hh:mm, и date - дата в формате YYYY-MM-DD\n" +
                "\tесли дата и время опущены - используются текущие\n" +
                "show|fullname_or_id|datefrom|dateto: выводит отработанное время сотрудника fullname по имени или id\n" +
                "\tгде datefrom и dateto - дата в формате YYYY-MM-DD\n" +
                "\tесли даты опущены - используются весь период до текущей даты\n" +
                "showall|datefrom|dateto: выводит отработанное время всех сотрудников\n" +
                "\tгде datefrom и dateto - дата в формате YYYY-MM-DD\n" +
                "\tесли даты опущены - используются весь период до текущей даты\n" +
                "journal|fullname_or_id|datefrom|dateto: выводит журнал приходов/уходов сотрудника fullname по имени или id\n" +
                "\tгде datefrom и dateto - дата в формате YYYY-MM-DD\n" +
                "\tесли даты опущены - используются весь период до текущей даты\n" +
                "help: для вывода этой справки\n" +
                "exit: завершение программы\n" +
                "Введите команду или help для вывода списка доступных команд:\n" +
                "До скорой встречи!");
    }



    @Test
    public void testWorkerAddRemoveList() {
        String testName = "Test Test Test";
        testWorkerListAdd(testName);
        testWorkerListInList(testName);
        testWorkerListRemove(testName);
        testWorkerListOutOfList(testName);
    }

    private void testWorkerListOutOfList(String testName) {
        // given
        console.addIn("workers");
        console.addIn("exit");
        // when
        Main.main(new String[0]);

        // then
        assertNotContains(testName);
    }

    private void testWorkerListRemove(String testName) {
        // given
        console.addIn("remove|" + testName);
        console.addIn("exit");
        // when
        Main.main(new String[0]);

        // then
        assertContains(testName);
    }

    private void testWorkerListInList(String testName) {
        // given
        console.addIn("workers");
        console.addIn("exit");
        // when
        Main.main(new String[0]);

        // then
        assertContains(testName);
    }

    private void testWorkerListAdd(String testName) {
        // given
        console.addIn("add|" + testName);
        console.addIn("exit");
        // when
        Main.main(new String[0]);

        // then
        assertOut("Добро пожаловать!!!\n" +
                "Введите команду или help для вывода списка доступных команд:\n" +
                "Сотрудник " + testName + " добавлен в список.\n" +
                "Введите команду или help для вывода списка доступных команд:\n" +
                "До скорой встречи!");
    }


}
