package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.commands.EmployeeCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.FindEmployeeCommand;
import seedu.address.logic.commands.FindPotentialCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.ListEmployeeCommand;
import seedu.address.logic.commands.ListPotentialCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.PersonUtil;

public class AddressBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_add() throws Exception {
        Person person = new PersonBuilder().buildEmployee();
        EmployeeCommand command = (EmployeeCommand) parser.parseCommand(PersonUtil.getEmployeeCommand(person));
        assertEquals(new EmployeeCommand(person), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Person person = new PersonBuilder().buildEmployee();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + PersonUtil.getEditPersonDescriptorDetails(descriptor));
        assertEquals(new EditCommand(INDEX_FIRST_PERSON, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(keywords)), command);

        FindEmployeeCommand employeeCommand = (FindEmployeeCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + FindEmployeeCommand.ARGUMENT_WORD + " "
                        + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindEmployeeCommand(new NameContainsKeywordsPredicate(keywords)), employeeCommand);

        FindPotentialCommand potentialCommand = (FindPotentialCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + FindPotentialCommand.ARGUMENT_WORD + " "
                        + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindPotentialCommand(new NameContainsKeywordsPredicate(keywords)), potentialCommand);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        //list command
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);

        //list employee command
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " e") instanceof ListEmployeeCommand);

        //list potential hire command
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " ph") instanceof ListPotentialCommand);

        //wrong parameter (is not e or ph)
        assertThrows(ParseException.class, () -> parser.parseCommand(ListCommand.COMMAND_WORD + " 3"));

        //additional text behind parameter
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " e 3") instanceof ListEmployeeCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " ph 3") instanceof ListPotentialCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}
